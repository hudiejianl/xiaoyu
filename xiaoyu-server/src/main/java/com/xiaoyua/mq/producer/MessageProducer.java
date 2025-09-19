package com.xiaoyua.mq.producer;

import com.xiaoyua.config.MessageQueueConstants;
import com.xiaoyua.mq.message.PrivateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 私信消息生产者
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class MessageProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送私信推送消息
     * 
     * @param message 私信消息
     */
    public void sendMessagePush(PrivateMessage message) {
        try {
            log.info("发送私信推送消息: messageId={}, fromUserId={}, toUserId={}", 
                    message.getMessageId(), message.getFromUserId(), message.getToUserId());
            
            rabbitTemplate.convertAndSend(
                    MessageQueueConstants.MESSAGE_EXCHANGE,
                    MessageQueueConstants.MESSAGE_PUSH_ROUTING_KEY,
                    message
            );
            
            log.debug("私信推送消息发送成功: {}", message.getMessageId());
            
        } catch (Exception e) {
            log.error("发送私信推送消息失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("发送私信推送消息失败", e);
        }
    }
    
    /**
     * 发送离线消息存储请求
     * 
     * @param message 私信消息
     */
    public void sendOfflineMessageStorage(PrivateMessage message) {
        try {
            log.info("发送离线消息存储请求: messageId={}, toUserId={}", 
                    message.getMessageId(), message.getToUserId());
            
            // 标记需要离线存储
            message.setNeedOfflineStorage(true);
            
            rabbitTemplate.convertAndSend(
                    MessageQueueConstants.MESSAGE_EXCHANGE,
                    MessageQueueConstants.OFFLINE_MESSAGE_ROUTING_KEY,
                    message
            );
            
            log.debug("离线消息存储请求发送成功: {}", message.getMessageId());
            
        } catch (Exception e) {
            log.error("发送离线消息存储请求失败: messageId={}, error={}", 
                    message.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("发送离线消息存储请求失败", e);
        }
    }
}
