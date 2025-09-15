package com.xiaoyu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyu.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;

/**
 * 通知WebSocket处理器
 * 专门处理实时通知推送
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class NotificationWebSocketHandler implements WebSocketHandler {
    
    @Autowired
    private WebSocketSessionManager sessionManager;
    
    @Autowired
    private NotificationService notificationService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessionManager.addUserSession(userId, session);
            
            // 发送连接成功确认
            Map<String, Object> confirmMessage = Map.of(
                "type", "connection",
                "status", "connected",
                "message", "通知连接已建立"
            );
            sessionManager.sendMessageToUser(userId, confirmMessage);
            
            log.info("用户 {} 通知WebSocket连接建立", userId);
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
            
            log.info("收到用户 {} 的WebSocket消息: type={}", userId, messageType);
            
            // 处理不同类型的消息
            switch (messageType) {
                case "ping":
                    handlePingMessage(session, userId);
                    break;
                case "mark_read":
                    handleMarkReadMessage(session, userId, messageData);
                    break;
                case "get_unread_count":
                    handleGetUnreadCountMessage(session, userId);
                    break;
                case "mark_all_read":
                    handleMarkAllReadMessage(session, userId);
                    break;
                default:
                    log.warn("未知的消息类型: {}", messageType);
                    sessionManager.sendErrorMessage(session, "未知的消息类型");
            }
            
        } catch (Exception e) {
            log.error("处理WebSocket消息失败: {}", e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "消息处理失败");
        }
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        log.error("用户 {} 通知WebSocket传输错误: {}", userId, exception.getMessage());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            sessionManager.removeUserSession(userId);
            log.info("用户 {} 通知WebSocket连接关闭", userId);
        }
    }
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 处理心跳消息
     */
    private void handlePingMessage(WebSocketSession session, Long userId) {
        Map<String, Object> pongMessage = Map.of(
            "type", "pong",
            "timestamp", System.currentTimeMillis()
        );
        sessionManager.sendMessageToUser(userId, pongMessage);
    }
    
    /**
     * 处理标记已读消息
     */
    private void handleMarkReadMessage(WebSocketSession session, Long userId, Map<String, Object> messageData) {
        try {
            Object notificationIdObj = messageData.get("notification_id");
            if (notificationIdObj != null) {
                Long notificationId = Long.valueOf(notificationIdObj.toString());
                boolean success = notificationService.markAsRead(userId, notificationId);
                
                Map<String, Object> confirmMessage = Map.of(
                    "type", "mark_read_confirm",
                    "notification_id", notificationId,
                    "status", success ? "success" : "failed"
                );
                sessionManager.sendMessageToUser(userId, confirmMessage);
            } else {
                sessionManager.sendErrorMessage(session, "缺少notification_id参数");
            }
        } catch (Exception e) {
            log.error("处理标记已读消息失败: {}", e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "标记已读失败");
        }
    }
    
    /**
     * 处理获取未读数量消息
     */
    private void handleGetUnreadCountMessage(WebSocketSession session, Long userId) {
        try {
            long unreadCount = notificationService.getUnreadCount(userId);
            Map<String, Object> countMessage = Map.of(
                "type", "unread_count",
                "count", unreadCount
            );
            sessionManager.sendMessageToUser(userId, countMessage);
        } catch (Exception e) {
            log.error("获取未读数量失败: {}", e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "获取未读数量失败");
        }
    }
    
    /**
     * 处理标记所有通知已读消息
     */
    private void handleMarkAllReadMessage(WebSocketSession session, Long userId) {
        try {
            int updatedCount = notificationService.markAllAsRead(userId);
            Map<String, Object> confirmMessage = Map.of(
                "type", "mark_all_read_confirm",
                "updated_count", updatedCount,
                "status", "success"
            );
            sessionManager.sendMessageToUser(userId, confirmMessage);
        } catch (Exception e) {
            log.error("标记所有通知已读失败: {}", e.getMessage(), e);
            sessionManager.sendErrorMessage(session, "标记所有通知已读失败");
        }
    }
}
