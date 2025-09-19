package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 好友关系实体类
 * 
 * @author xiaoyu
 */
@Data
@TableName("friends")
public class FriendPO {
    
    /**
     * 好友关系ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 好友ID
     */
    @TableField("friend_id")
    private Long friendId;
    
    /**
     * 好友关系状态：PENDING/ACCEPTED/REFUSED
     */
    private String status;
    
    /**
     * 验证消息
     */
    private String message;
    
    /**
     * 是否为接收方：0发送方 1接收方
     */
    @TableField("is_receiver")
    private Integer isReceiver;
    
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
