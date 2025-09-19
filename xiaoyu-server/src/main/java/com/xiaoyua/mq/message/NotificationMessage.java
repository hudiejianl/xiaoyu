package com.xiaoyua.mq.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 通知消息实体
 * 用于MQ传输的通知消息
 * 
 * @author xiaoyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 消息ID */
    private String messageId;
    
    /** 接收者用户ID */
    private Long userId;
    
    /** 通知类型：LIKE、FAVORITE、COMMENT、SHARE、FRIEND_MESSAGE等 */
    private String type;
    
    /** 通知标题 */
    private String title;
    
    /** 通知内容 */
    private String content;
    
    /** 关联业务对象ID */
    private Long refId;
    
    /** 关联业务类型：POST、TASK、COMMENT、MESSAGE */
    private String refType;
    
    /** 发送者用户ID（如果有） */
    private Long fromUserId;
    
    /** 发送者昵称（如果有） */
    private String fromUserNickname;
    
    /** 发送者头像（如果有） */
    private String fromUserAvatar;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
    
    /** 重试次数 */
    private Integer retryCount = 0;
    
    /** 最大重试次数 */
    private Integer maxRetry = 3;
    
    public NotificationMessage(Long userId, String type, String title, String content) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.messageId = generateMessageId();
    }
    
    public NotificationMessage(Long userId, String type, String title, String content, 
                              Long refId, String refType, Long fromUserId) {
        this(userId, type, title, content);
        this.refId = refId;
        this.refType = refType;
        this.fromUserId = fromUserId;
    }
    
    /**
     * 生成消息ID
     */
    private String generateMessageId() {
        return "NOTIF_" + System.currentTimeMillis() + "_" + 
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
