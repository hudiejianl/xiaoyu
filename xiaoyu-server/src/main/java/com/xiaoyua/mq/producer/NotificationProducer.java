package com.xiaoyua.mq.producer;

import com.xiaoyua.config.MessageQueueConstants;
import com.xiaoyua.mq.message.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通知消息生产者
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class NotificationProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送通知推送消息
     * 
     * @param message 通知消息
     */
    public void sendNotificationPush(NotificationMessage message) {
        try {
            log.info("发送通知推送消息: messageId={}, userId={}, type={}", 
                    message.getMessageId(), message.getUserId(), message.getType());
            
            rabbitTemplate.convertAndSend(
                    MessageQueueConstants.NOTIFICATION_EXCHANGE,
                    MessageQueueConstants.NOTIFICATION_PUSH_ROUTING_KEY,
                    message
            );
            
            log.debug("通知推送消息发送成功: {}", message.getMessageId());
            
        } catch (Exception e) {
            log.error("发送通知推送消息失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("发送通知推送消息失败", e);
        }
    }
    
    /**
     * 批量发送通知推送消息
     * 
     * @param messages 通知消息列表
     */
    public void sendNotificationPushBatch(NotificationMessage... messages) {
        for (NotificationMessage message : messages) {
            sendNotificationPush(message);
        }
    }
}
