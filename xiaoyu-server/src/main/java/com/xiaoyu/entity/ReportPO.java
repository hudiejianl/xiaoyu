package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 举报记录表
 */
@Data
@TableName("reports")
public class ReportPO {

    /** 举报 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 举报人 UID */
    private Long userId;

    /** 被举报对象 ID */
    private Long itemId;

    /** 被举报类型 */
    @EnumValue
    private ItemType itemType;

    /** 举报理由 */
    private String reason;

    /** 处理状态 */
    @EnumValue
    private Status status;

    /** 举报时间 */
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK, COMMENT, USER
    }

    public enum Status {
        PENDING, ACCEPTED, REFUSED
    }
}