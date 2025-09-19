package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 收藏表
 */
@Data
@TableName("favorites")
public class FavoritePO {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 收藏者 UID */
    @TableField("user_id")
    private Long userId;

    /** 业务对象 ID */
    @TableField("item_id")
    private Long itemId;

    /** 业务类型 */
    @TableField("item_type")
    @EnumValue
    private ItemType itemType;

    /** 收藏时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK
    }
}