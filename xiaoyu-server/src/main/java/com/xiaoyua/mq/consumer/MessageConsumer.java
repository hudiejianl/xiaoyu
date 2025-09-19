package com.xiaoyua.mq.consumer;

import com.xiaoyua.config.MessageQueueConstants;
import com.xiaoyua.mq.message.PrivateMessage;
import com.xiaoyua.mq.producer.MessageProducer;
import com.xiaoyua.service.OfflineMessageService;
import com.xiaoyua.websocket.UnifiedWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * 私信消息消费者
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class MessageConsumer {
    
    @Autowired
    private UnifiedWebSocketHandler webSocketHandler;
    
    @Autowired
    private MessageProducer messageProducer;
    
    @Autowired
    private OfflineMessageService offlineMessageService;
    
    /**
     * 处理私信推送消息
     */
    @RabbitListener(queues = MessageQueueConstants.MESSAGE_PUSH_QUEUE)
    public void handleMessagePush(PrivateMessage message) {
        try {
            log.info("处理私信推送消息: messageId={}, fromUserId={}, toUserId={}", 
                    message.getMessageId(), message.getFromUserId(), message.getToUserId());
            
            // 检查接收者是否在线
            if (webSocketHandler.isUserOnline(message.getToUserId())) {
                // 用户在线，直接推送
                pushToWebSocket(message);
                log.info("私信实时推送成功: messageId={}, toUserId={}", 
                        message.getMessageId(), message.getToUserId());
            } else {
                // 用户离线，存储到离线消息表
                log.info("用户离线，存储离线消息: userId={}, messageId={}", 
                        message.getToUserId(), message.getMessageId());
                
                // 存储离线私信消息
                boolean stored = offlineMessageService.storeOfflinePrivateMessage(message);
                if (!stored) {
                    log.error("存储离线私信消息失败: messageId={}, toUserId={}", 
                            message.getMessageId(), message.getToUserId());
                }
            }
            
        } catch (Exception e) {
            log.error("处理私信推送消息失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            
            // 增加重试次数
            message.incrementRetry();
            if (!message.isMaxRetryExceeded()) {
                log.warn("私信消息将重试: messageId={}, retryCount={}", 
                        message.getMessageId(), message.getRetryCount());
            } else {
                log.error("私信消息超过最大重试次数，转为离线存储: messageId={}", 
                        message.getMessageId());
                // 超过重试次数，转为离线存储
                messageProducer.sendOfflineMessageStorage(message);
            }
            throw e;
        }
    }
    
    /**
     * 处理离线消息存储
     */
    @RabbitListener(queues = MessageQueueConstants.OFFLINE_MESSAGE_QUEUE)
    public void handleOfflineMessageStorage(PrivateMessage message) {
        try {
            log.info("处理离线消息存储: messageId={}, toUserId={}", 
                    message.getMessageId(), message.getToUserId());
            
            // TODO: 实现离线消息存储逻辑
            // 这里暂时只记录日志，后续会实现OfflineMessageService
            log.info("离线消息已存储: messageId={}, toUserId={}, content={}", 
                    message.getMessageId(), message.getToUserId(), 
                    truncateContent(message.getContent(), 50));
            
        } catch (Exception e) {
            log.error("处理离线消息存储失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 推送到WebSocket
     */
    private void pushToWebSocket(PrivateMessage message) {
        try {
            Map<String, Object> wsMessage = Map.of(
                "type", "private_message",
                "message_id", message.getOriginalMessageId(),
                "from_user_id", message.getFromUserId(),
                "from_user_nickname", message.getFromUserNickname() != null ? message.getFromUserNickname() : "",
                "from_user_avatar", message.getFromUserAvatar() != null ? message.getFromUserAvatar() : "",
                "content", message.getContent(),
                "message_type", message.getMessageType(),
                "created_at", message.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "timestamp", System.currentTimeMillis()
            );
            
            WebSocketSession session = webSocketHandler.getUserSession(message.getToUserId());
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
            
            if (!sent) {
                log.warn("WebSocket推送失败，用户可能已离线: toUserId={}, messageId={}", 
                        message.getToUserId(), message.getMessageId());
                // 推送失败，转为离线存储
                messageProducer.sendOfflineMessageStorage(message);
            }
            
        } catch (Exception e) {
            log.error("WebSocket推送异常: toUserId={}, messageId={}, error={}", 
                    message.getToUserId(), message.getMessageId(), e.getMessage(), e);
            // 推送异常，转为离线存储
            messageProducer.sendOfflineMessageStorage(message);
        }
    }
    
    /**
     * 截断内容用于日志
     */
    private String truncateContent(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
