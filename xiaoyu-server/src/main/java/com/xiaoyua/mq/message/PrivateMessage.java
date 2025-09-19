package com.xiaoyua.mq.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 私信消息实体
 * 用于MQ传输的私信消息
 * 
 * @author xiaoyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 消息ID */
    private String messageId;
    
    /** 原始消息ID */
    private Long originalMessageId;
    
    /** 发送者用户ID */
    private Long fromUserId;
    
    /** 接收者用户ID */
    private Long toUserId;
    
    /** 消息内容 */
    private String content;
    
    /** 消息类型：TEXT、IMAGE、FILE */
    private String messageType;
    
    /** 发送者昵称 */
    private String fromUserNickname;
    
    /** 发送者头像 */
    private String fromUserAvatar;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
    
    /** 重试次数 */
    private Integer retryCount = 0;
    
    /** 最大重试次数 */
    private Integer maxRetry = 3;
    
    /** 是否需要存储为离线消息 */
    private Boolean needOfflineStorage = false;
    
    public PrivateMessage(Long originalMessageId, Long fromUserId, Long toUserId, String content, String messageType) {
        this.originalMessageId = originalMessageId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.messageType = messageType;
        this.createdAt = LocalDateTime.now();
        this.messageId = generateMessageId();
    }
    
    /**
     * 生成消息ID
     */
    private String generateMessageId() {
        return "MSG_" + System.currentTimeMillis() + "_" + 
               (int)(Math.random() * 1000);
    }
    
    /**
     * 增加重试次数
     */
    public void incrementRetry() {
        this.retryCount++;
    }
    
    /**
     * 是否超过最大重试次数
     */
    public boolean isMaxRetryExceeded() {
        return this.retryCount >= this.maxRetry;
    }
}
