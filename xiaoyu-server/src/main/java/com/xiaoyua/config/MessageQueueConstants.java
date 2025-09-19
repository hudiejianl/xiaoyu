package com.xiaoyua.config;

/**
 * 消息队列常量定义
 * 
 * @author xiaoyu
 */
public class MessageQueueConstants {
    
    // ==================== 交换机 ====================
    /** 通知交换机 */
    public static final String NOTIFICATION_EXCHANGE = "xiaoyu.notification.exchange";
    
    /** 私信交换机 */
    public static final String MESSAGE_EXCHANGE = "xiaoyu.message.exchange";
    
    // ==================== 队列 ====================
    /** 通知推送队列 */
    public static final String NOTIFICATION_PUSH_QUEUE = "xiaoyu.notification.push.queue";
    
    /** 私信推送队列 */
    public static final String MESSAGE_PUSH_QUEUE = "xiaoyu.message.push.queue";
    
    /** 离线消息存储队列 */
    public static final String OFFLINE_MESSAGE_QUEUE = "xiaoyu.offline.message.queue";
    
    // ==================== 路由键 ====================
    /** 通知推送路由键 */
    public static final String NOTIFICATION_PUSH_ROUTING_KEY = "notification.push";
    
    /** 私信推送路由键 */
    public static final String MESSAGE_PUSH_ROUTING_KEY = "message.push";
    
    /** 离线消息路由键 */
    public static final String OFFLINE_MESSAGE_ROUTING_KEY = "message.offline";
    
    // ==================== 死信队列 ====================
    /** 死信交换机 */
    public static final String DLX_EXCHANGE = "xiaoyu.dlx.exchange";
    
    /** 通知死信队列 */
    public static final String NOTIFICATION_DLQ = "xiaoyu.notification.dlq";
    
    /** 私信死信队列 */
    public static final String MESSAGE_DLQ = "xiaoyu.message.dlq";
    
    /** 离线消息死信队列 */
    public static final String OFFLINE_MESSAGE_DLQ = "xiaoyu.offline.message.dlq";
}
