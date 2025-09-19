package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 私信消息实体类
 * 
 * @author xiaoyu
 */
@Data
@TableName("messages")
public class MessagePO {
    
    /**
     * 消息ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 发送者ID
     */
    @TableField("from_id")
    private Long fromId;
    
    /**
     * 接收者ID
     */
    @TableField("to_id")
    private Long toId;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型：TEXT/IMAGE/FILE
     */
    @TableField("message_type")
    private String messageType;
    
    /**
     * 消息状态：SENT/DELIVERED/READ
     */
    private String status;
    
    /**
     * 是否已读：0未读 1已读
     */
    @TableField("is_read")
    private Integer isRead;
    
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
