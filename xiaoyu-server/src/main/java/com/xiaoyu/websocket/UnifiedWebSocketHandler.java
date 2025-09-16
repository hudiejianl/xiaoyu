package com.xiaoyu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            
            // 只处理基础的连接管理消息，业务逻辑通过MQ处理
            switch (messageType) {
                case "ping":
                    handlePingMessage(userId);
                    break;
                case "heartbeat":
                    handleHeartbeatMessage(userId);
                    break;
                default:
                    // 对于业务消息，返回提示信息
                    Map<String, Object> errorMessage = Map.of(
                        "type", "error",
                        "message", "业务操作请通过API接口处理",
                        "timestamp", System.currentTimeMillis()
                    );
                    sessionManager.forwardMessageToUser(userId, errorMessage);
                    log.warn("用户 {} 尝试通过WebSocket处理业务消息: {}", userId, messageType);
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
}
