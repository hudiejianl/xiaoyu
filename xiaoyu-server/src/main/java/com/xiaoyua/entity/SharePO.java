package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 转发记录表
 */
@Data
@TableName("shares")
public class SharePO {

    /** 转发 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 转发者 UID */
    private Long userId;

    /** 业务对象 ID */
    private Long itemId;

    /** 业务类型 */
    @EnumValue
    private ItemType itemType;

    /** 转发附言 */
    private String reason;

    /** 转发时间 */
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK, COMMENT
    }
}