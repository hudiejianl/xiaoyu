package com.xiaoyu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyu.context.BaseContext;
import com.xiaoyu.dto.message.MessageCreateDTO;
import com.xiaoyu.service.MessageService;
import com.xiaoyu.vo.message.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;

/**
 * 统一WebSocket处理器
 * 只负责消息转发，不处理业务逻辑
 * 所有业务逻辑都通过MQ处理
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class UnifiedWebSocketHandler implements WebSocketHandler {
    
    @Autowired
    private WebSocketSessionManager sessionManager;
    
    @Autowired
    private UserOnlineEventHandler userOnlineEventHandler;
    
    @Autowired
    private MessageService messageService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            // 添加用户会话
            sessionManager.addUserSession(userId, session);
            
            // 处理用户上线事件，推送离线消息
            userOnlineEventHandler.handleUserOnline(userId);
            
            // 发送连接成功确认
            Map<String, Object> confirmMessage = Map.of(
                "type", "connection",
                "status", "connected",
                "message", "连接已建立",
                "timestamp", System.currentTimeMillis()
            );
            sessionManager.forwardMessageToUser(userId, confirmMessage);
            
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
                    sessionManager.forwardMessageToUser(userId, errorMessage);
                    log.warn("用户 {} 发送了不支持的消息类型: {}", userId, messageType);
            }
            
        } catch (Exception e) {
            log.error("处理WebSocket消息失败: userId={}, error={}", userId, e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "消息处理失败");
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
            sessionManager.removeUserSession(userId);
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
        sessionManager.forwardMessageToUser(userId, pongMessage);
    }
    
    /**
     * 处理心跳消息
     */
    private void handleHeartbeatMessage(Long userId) {
        Map<String, Object> heartbeatResponse = Map.of(
            "type", "heartbeat_ack",
            "timestamp", System.currentTimeMillis()
        );
        sessionManager.forwardMessageToUser(userId, heartbeatResponse);
    }
    
    /**
     * 处理发送私信消息 - 直接调用现有MessageService，复用所有业务逻辑
     */
    private void handleSendMessage(Long userId, Map<String, Object> messageData) {
        try {
            // 设置当前用户上下文
            BaseContext.setCurrentId(userId);
            
            // 1. 立即响应客户端确认收到请求
            String tempMessageId = "temp_" + System.currentTimeMillis();
            Map<String, Object> sendingResponse = Map.of(
                "type", "message_sending",
                "temp_id", tempMessageId,
                "status", "processing",
                "timestamp", System.currentTimeMillis()
            );
            sessionManager.forwardMessageToUser(userId, sendingResponse);
            
            // 2. 构建消息DTO
            MessageCreateDTO messageDTO = new MessageCreateDTO();
            messageDTO.setToId(Long.valueOf(messageData.get("to_id").toString()));
            messageDTO.setContent((String) messageData.get("content"));
            messageDTO.setMessageType((String) messageData.getOrDefault("message_type", "TEXT"));
            
            // 3. 直接调用现有业务逻辑：好友验证、数据库保存、Redis缓存、MQ推送
            MessageVO messageVO = messageService.sendMessage(userId, messageDTO);
            
            // 4. 立即返回成功结果给发送者
            Map<String, Object> successResponse = Map.of(
                "type", "message_sent",
                "temp_id", tempMessageId,
                "message", messageVO,
                "status", "success",
                "timestamp", System.currentTimeMillis()
            );
            sessionManager.forwardMessageToUser(userId, successResponse);
            
            log.info("WebSocket私信发送成功: fromUserId={}, toUserId={}, messageId={}", 
                    userId, messageDTO.getToId(), messageVO.getId());
            
        } catch (Exception e) {
            log.error("WebSocket处理发送消息失败: userId={}, error={}", userId, e.getMessage(), e);
            
            // 发送错误响应
            Map<String, Object> errorResponse = Map.of(
                "type", "message_error",
                "temp_id", messageData.getOrDefault("temp_id", "unknown"),
                "error", e.getMessage(),
                "status", "failed",
                "timestamp", System.currentTimeMillis()
            );
            sessionManager.forwardMessageToUser(userId, errorResponse);
        } finally {
            // 清理上下文
            BaseContext.setCurrentId(null);
        }
    }
}
