package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 收藏表
 */
@Data
@TableName("favorites")
public class FavoritePO {

    /** 收藏者 UID */
    @TableId
    private Long userId;

    /** 业务对象 ID */
    @TableId
    private Long itemId;

    /** 业务类型 */
    @TableId
    @EnumValue
    private ItemType itemType;

    /** 收藏时间 */
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK
    }
}