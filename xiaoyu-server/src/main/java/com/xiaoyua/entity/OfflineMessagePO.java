package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 离线消息实体类
 * 用于存储用户离线时收到的消息
 * 
 * @author xiaoyu
 */
@Data
@TableName("offline_messages")
public class OfflineMessagePO {
    
    /**
     * 离线消息ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 接收者用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 消息类型：NOTIFICATION通知 PRIVATE_MESSAGE私信
     */
    @TableField("message_type")
    private String messageType;
    
    /**
     * 原始消息ID（如果是私信消息）
     */
    @TableField("original_message_id")
    private Long originalMessageId;
    
    /**
     * 通知ID（如果是通知消息）
     */
    @TableField("notification_id")
    private Long notificationId;
    
    /**
     * 消息内容JSON
     */
    @TableField("message_content")
    private String messageContent;
    
    /**
     * 发送者用户ID
     */
    @TableField("from_user_id")
    private Long fromUserId;
    
    /**
     * 消息状态：PENDING待推送 PUSHED已推送 EXPIRED已过期
     */
    private String status;
    
    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;
    
    /**
     * 过期时间
     */
    @TableField("expire_at")
    private LocalDateTime expireAt;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 逻辑删除标记：0正常 1删除
     */
    @TableLogic
    private Integer deleted;
}
