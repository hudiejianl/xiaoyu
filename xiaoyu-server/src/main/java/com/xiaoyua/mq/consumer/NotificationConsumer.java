package com.xiaoyua.mq.consumer;

import com.xiaoyua.config.MessageQueueConstants;
import com.xiaoyua.entity.NotificationPO;
import com.xiaoyua.mapper.NotificationMapper;
import com.xiaoyua.mq.message.NotificationMessage;
import com.xiaoyua.service.OfflineMessageService;
import com.xiaoyua.websocket.UnifiedWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知消息消费者
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class NotificationConsumer {
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private UnifiedWebSocketHandler webSocketHandler;
    
    @Autowired
    private OfflineMessageService offlineMessageService;
    
    /**
     * 处理通知推送消息
     */
    @RabbitListener(queues = MessageQueueConstants.NOTIFICATION_PUSH_QUEUE)
    public void handleNotificationPush(NotificationMessage message) {
        try {
            log.info("处理通知推送消息: messageId={}, userId={}, type={}", 
                    message.getMessageId(), message.getUserId(), message.getType());
            
            // 1. 保存通知到数据库
            NotificationPO notification = convertToNotificationPO(message);
            int result = notificationMapper.insert(notification);
            
            if (result <= 0) {
                log.error("保存通知到数据库失败: messageId={}", message.getMessageId());
                throw new RuntimeException("保存通知失败");
            }
            
            // 2. 检查用户是否在线，如果在线则实时推送
            if (webSocketHandler.isUserOnline(message.getUserId())) {
                pushToWebSocket(message, notification.getId());
            } else {
                // 用户离线，存储到离线消息表
                log.info("用户离线，存储离线通知: userId={}, messageId={}", 
                        message.getUserId(), message.getMessageId());
                
                boolean stored = offlineMessageService.storeOfflineNotification(message, notification.getId());
                if (!stored) {
                    log.error("存储离线通知消息失败: messageId={}, userId={}", 
                            message.getMessageId(), message.getUserId());
                }
            }
            
            log.info("通知推送处理完成: messageId={}", message.getMessageId());
            
        } catch (Exception e) {
            log.error("处理通知推送消息失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            
            // 增加重试次数
            message.incrementRetry();
            if (!message.isMaxRetryExceeded()) {
                // 可以选择重新入队或者其他重试机制
                log.warn("通知消息将重试: messageId={}, retryCount={}", 
                        message.getMessageId(), message.getRetryCount());
            } else {
                log.error("通知消息超过最大重试次数，放弃处理: messageId={}", 
                        message.getMessageId());
            }
            throw e;
        }
    }
    
    /**
     * 将NotificationMessage转换为NotificationPO
     */
    private NotificationPO convertToNotificationPO(NotificationMessage message) {
        NotificationPO notification = new NotificationPO();
        notification.setUserId(message.getUserId());
        notification.setType(NotificationPO.Type.valueOf(message.getType()));
        notification.setTitle(message.getTitle());
        notification.setContent(message.getContent());
        notification.setRefId(message.getRefId());
        
        // 设置关联类型
        if (message.getRefType() != null) {
            notification.setRefType(NotificationPO.RefType.valueOf(message.getRefType()));
        }
        
        notification.setStatus(NotificationPO.Status.UNREAD);
        notification.setCreatedAt(message.getCreatedAt() != null ? 
                message.getCreatedAt() : LocalDateTime.now());
        
        return notification;
    }
    
    /**
     * 推送到WebSocket
     */
    private void pushToWebSocket(NotificationMessage message, Long notificationId) {
        try {
            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", "notification");
            wsMessage.put("id", notificationId);
            wsMessage.put("notification_type", message.getType());
            wsMessage.put("title", message.getTitle());
            wsMessage.put("content", message.getContent());
            wsMessage.put("ref_id", message.getRefId() != null ? message.getRefId() : 0L);
            wsMessage.put("ref_type", message.getRefType() != null ? message.getRefType() : "");
            wsMessage.put("from_user_id", message.getFromUserId() != null ? message.getFromUserId() : 0L);
            wsMessage.put("from_user_nickname", message.getFromUserNickname() != null ? message.getFromUserNickname() : "");
            wsMessage.put("from_user_avatar", message.getFromUserAvatar() != null ? message.getFromUserAvatar() : "");
            wsMessage.put("created_at", message.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            wsMessage.put("timestamp", System.currentTimeMillis());
            
            WebSocketSession session = webSocketHandler.getUserSession(message.getUserId());
            boolean sent = false;
            if (session != null && session.isOpen()) {
                try {
                    String messageJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(wsMessage);
                    session.sendMessage(new org.springframework.web.socket.TextMessage(messageJson));
                    sent = true;
                } catch (Exception e) {
                    log.error("WebSocket发送失败: {}", e.getMessage());
                }
            }
            
            if (sent) {
                log.info("WebSocket推送成功: userId={}, messageId={}", 
                        message.getUserId(), message.getMessageId());
            } else {
                log.warn("WebSocket推送失败，用户可能已离线: userId={}, messageId={}", 
                        message.getUserId(), message.getMessageId());
            }
            
        } catch (Exception e) {
            log.error("WebSocket推送异常: userId={}, messageId={}, error={}", 
                    message.getUserId(), message.getMessageId(), e.getMessage(), e);
        }
    }
}
