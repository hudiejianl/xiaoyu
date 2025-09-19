package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 点赞表
 */
@Data
@TableName("likes")
public class LikePO {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 点赞者 UID */
    @TableField("user_id")
    private Long userId;

    /** 业务对象 ID */
    @TableField("item_id")
    private Long itemId;

    /** 业务类型 */
    @TableField("item_type")
    @EnumValue
    private ItemType itemType;

    /** 点赞时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK, COMMENT
    }
}