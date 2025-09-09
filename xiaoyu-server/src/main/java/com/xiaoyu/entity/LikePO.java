package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 点赞表
 */
@Data
@TableName("likes")
public class LikePO {

    /** 点赞者 UID */
    @TableId
    private Long userId;

    /** 业务对象 ID */
    @TableId
    private Long itemId;

    /** 业务类型 */
    @TableId
    @EnumValue
    private ItemType itemType;

    /** 点赞时间 */
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK, COMMENT
    }
}