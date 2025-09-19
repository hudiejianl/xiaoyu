package com.xiaoyua.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 
 * @author xiaoyu
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 消息转换器 ====================
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    // ==================== 交换机 ====================
    
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(MessageQueueConstants.NOTIFICATION_EXCHANGE, true, false);
    }
    
    @Bean
    public TopicExchange messageExchange() {
        return new TopicExchange(MessageQueueConstants.MESSAGE_EXCHANGE, true, false);
    }
    
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(MessageQueueConstants.DLX_EXCHANGE, true, false);
    }

    // ==================== 队列 ====================
    
    @Bean
    public Queue notificationPushQueue() {
        return QueueBuilder.durable(MessageQueueConstants.NOTIFICATION_PUSH_QUEUE)
                .withArgument("x-dead-letter-exchange", MessageQueueConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notification.dead")
                .withArgument("x-message-ttl", 300000) // 5分钟TTL
                .build();
    }
    
    @Bean
    public Queue messagePushQueue() {
        return QueueBuilder.durable(MessageQueueConstants.MESSAGE_PUSH_QUEUE)
                .withArgument("x-dead-letter-exchange", MessageQueueConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "message.dead")
                .withArgument("x-message-ttl", 300000) // 5分钟TTL
                .build();
    }
    
    @Bean
    public Queue offlineMessageQueue() {
        return QueueBuilder.durable(MessageQueueConstants.OFFLINE_MESSAGE_QUEUE)
                .withArgument("x-dead-letter-exchange", MessageQueueConstants.DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "offline.dead")
                .build();
    }
    
    // 死信队列
    @Bean
    public Queue notificationDlq() {
        return QueueBuilder.durable(MessageQueueConstants.NOTIFICATION_DLQ).build();
    }
    
    @Bean
    public Queue messageDlq() {
        return QueueBuilder.durable(MessageQueueConstants.MESSAGE_DLQ).build();
    }
    
    @Bean
    public Queue offlineMessageDlq() {
        return QueueBuilder.durable(MessageQueueConstants.OFFLINE_MESSAGE_DLQ).build();
    }

    // ==================== 绑定关系 ====================

    @Bean
    public Binding notificationPushBinding() {
        return BindingBuilder.bind(notificationPushQueue())
                .to(notificationExchange())
                .with(MessageQueueConstants.NOTIFICATION_PUSH_ROUTING_KEY);
    }
    
    @Bean
    public Binding messagePushBinding() {
        return BindingBuilder.bind(messagePushQueue())
                .to(messageExchange())
                .with(MessageQueueConstants.MESSAGE_PUSH_ROUTING_KEY);
    }
    
    @Bean
    public Binding offlineMessageBinding() {
        return BindingBuilder.bind(offlineMessageQueue())
                .to(messageExchange())
                .with(MessageQueueConstants.OFFLINE_MESSAGE_ROUTING_KEY);
    }
    
    // 死信队列绑定
    @Bean
    public Binding notificationDlqBinding() {
        return BindingBuilder.bind(notificationDlq())
                .to(dlxExchange())
                .with("notification.dead");
    }
    
    @Bean
    public Binding messageDlqBinding() {
        return BindingBuilder.bind(messageDlq())
                .to(dlxExchange())
                .with("message.dead");
    }
    
    @Bean
    public Binding offlineMessageDlqBinding() {
        return BindingBuilder.bind(offlineMessageDlq())
                .to(dlxExchange())
                .with("offline.dead");
    }
}
