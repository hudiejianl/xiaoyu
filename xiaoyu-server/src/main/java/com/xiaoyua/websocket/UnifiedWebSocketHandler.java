package com.xiaoyua.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyua.common.context.ContextManager;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import com.xiaoyua.dto.message.MessageCreateDTO;
import com.xiaoyua.service.MessageService;
import com.xiaoyua.vo.message.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一WebSocket处理器
 * 只负责消息转发，不处理业务逻辑
 * 所有业务逻辑都通过MQ处理
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class UnifiedWebSocketHandler extends TextWebSocketHandler {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private UserOnlineEventHandler userOnlineEventHandler;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // 存储用户ID与WebSocket会话的映射
    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            // 添加用户会话
            USER_SESSIONS.put(userId, session);
            // 在Redis中标记用户在线
            redisTemplate.opsForValue().set("user:online:" + userId, "1");
            
            // 处理用户上线事件，推送离线消息
            userOnlineEventHandler.handleUserOnline(userId);
            
            // 发送连接成功确认
            Map<String, Object> confirmMessage = Map.of(
                "type", "connection",
                "status", "connected",
                "message", "连接已建立",
                "timestamp", System.currentTimeMillis()
            );
            sendMessageToUser(userId, confirmMessage);
            
            log.info("用户 {} WebSocket连接建立", userId);
        }
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            return;
        }
        
        try {
            // 解析客户端消息
            String payload = message.getPayload().toString();
            @SuppressWarnings("unchecked")
            Map<String, Object> messageData = objectMapper.readValue(payload, Map.class);
            String messageType = (String) messageData.get("type");
            
            log.debug("收到用户 {} 的WebSocket消息: type={}", userId, messageType);
            
            // 处理连接管理消息和私信消息
            switch (messageType) {
                case "ping":
                    handlePingMessage(userId);
                    break;
                case "heartbeat":
                    handleHeartbeatMessage(userId);
                    break;
                case "send_message":
                    handleSendMessage(userId, messageData);
                    break;
                default:
                    // 对于其他业务消息，返回提示信息
                    Map<String, Object> errorMessage = Map.of(
                        "type", "error",
                        "message", "不支持的消息类型: " + messageType,
                        "timestamp", System.currentTimeMillis()
                    );
                    sendMessageToUser(userId, errorMessage);
                    log.warn("用户 {} 发送了不支持的消息类型: {}", userId, messageType);
            }
            
        } catch (Exception e) {
            log.error("处理WebSocket消息失败: userId={}, error={}", userId, e.getMessage(), e);
            sendErrorMessage(session, "消息处理失败");
        }
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        log.error("用户 {} WebSocket传输错误: {}", userId, exception.getMessage());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            USER_SESSIONS.remove(userId);
            // 从Redis中移除在线状态
            redisTemplate.delete("user:online:" + userId);
            log.info("用户 {} WebSocket连接关闭: {}", userId, closeStatus);
        }
    }
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 处理心跳消息
     */
    private void handlePingMessage(Long userId) {
        Map<String, Object> pongMessage = Map.of(
            "type", "pong",
            "timestamp", System.currentTimeMillis()
        );
        sendMessageToUser(userId, pongMessage);
    }
    
    /**
     * 处理心跳消息
     */
    private void handleHeartbeatMessage(Long userId) {
        Map<String, Object> heartbeatResponse = Map.of(
            "type", "heartbeat_ack",
            "timestamp", System.currentTimeMillis()
        );
        sendMessageToUser(userId, heartbeatResponse);
    }
    
    /**
     * 处理发送私信消息
     */
    private void handleSendMessage(Long userId, Map<String, Object> messageData) {
        // 使用栈式自动清理上下文
        try (ContextManager ignored = ContextManager.withUser(userId)) {
            // 1. 优先使用客户端传来的temp_id
            String tempId = (String) messageData.getOrDefault("temp_id", "srv_" + System.currentTimeMillis());

            // 2. 构建消息DTO
            MessageCreateDTO messageDTO = new MessageCreateDTO();
            messageDTO.setToId(Long.valueOf(messageData.get("to_id").toString()));
            messageDTO.setContent((String) messageData.get("content"));
            messageDTO.setMessageType((String) messageData.getOrDefault("message_type", "TEXT"));
            
            // 3. 直接调用现有业务逻辑：好友验证、数据库保存、Redis缓存、MQ推送
            MessageVO messageVO = messageService.sendMessage(userId, messageDTO);
            
            // 4. 幂等推送给接收者（避免MQ消费者重复推送）
            String dedupKey = "ws:push:" + messageVO.getId() + ":" + messageVO.getToId();
            Boolean isFirst = redisTemplate.opsForValue().setIfAbsent(dedupKey, "1", Duration.ofSeconds(5));
            if (Boolean.TRUE.equals(isFirst)) {
                //这里直接推送给接收者，MessageVO没有发送者详细信息
                Map<String, Object> receivedMessage = Map.of(
                    "type", "private_message",
                    "message_id", messageVO.getId(),
                    "from_user_id", messageVO.getFromId(),
                    "content", messageVO.getContent(),
                    "message_type", messageVO.getMessageType(),
                    "created_at", messageVO.getCreatedAt().format(DATE_TIME_FORMATTER),
                    "timestamp", System.currentTimeMillis()
                );
                sendMessageToUser(messageVO.getToId(), receivedMessage);
            }
            
            // 5. 发送成功确认给发送者（不包含消息内容，避免重复）
            Map<String, Object> successResponse = Map.of(
                "type", "message_sent",
                "temp_id", tempId,
                "message_id", messageVO.getId(),
                "status", "success",
                "timestamp", System.currentTimeMillis()
            );
            sendMessageToUser(userId, successResponse);
            
            log.info("WebSocket私信发送成功: fromUserId={}, toUserId={}, messageId={}, contentLen={}, messageType={}", 
                    userId, messageDTO.getToId(), messageVO.getId(), 
                    messageDTO.getContent().length(), messageDTO.getMessageType());
            
        } catch (Exception e) {
            log.error("WebSocket处理发送消息失败: userId={}, error={}", userId, e.getMessage(), e);
            
            // 发送错误响应（脱敏处理）
            String tempId = (String) messageData.getOrDefault("temp_id", "unknown");
            Map<String, Object> errorResponse = Map.of(
                "type", "message_error",
                "temp_id", tempId,
                "error", "SEND_FAILED",
                "status", "failed",
                "timestamp", System.currentTimeMillis()
            );
            sendMessageToUser(userId, errorResponse);
        }
    }
    
    /**
     * 向指定用户发送消息（线程安全的try-send模式）
     */
    private boolean sendMessageToUser(Long userId, Object payload) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        if (session == null) return false;
        
        try {
            String messageJson = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(messageJson));
            return true;
        } catch (Exception e) {
            log.warn("WebSocket发送失败: userId={}, error={}", userId, e.getMessage());
            return false;
        }
    }
    
    /**
     * 发送错误消息
     */
    private void sendErrorMessage(WebSocketSession session, String errorMsg) {
        try {
            String errorJson = objectMapper.writeValueAsString(Map.of(
                "type", "error",
                "message", errorMsg,
                "timestamp", System.currentTimeMillis()
            ));
            session.sendMessage(new TextMessage(errorJson));
        } catch (IOException e) {
            log.error("发送错误消息失败: {}", e.getMessage());
        }
    }
    
    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        return USER_SESSIONS.containsKey(userId) && 
               Boolean.TRUE.equals(redisTemplate.hasKey("user:online:" + userId));
    }
    
    /**
     * 获取用户会话
     */
    public WebSocketSession getUserSession(Long userId) {
        return USER_SESSIONS.get(userId);
    }
}
