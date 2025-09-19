package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务双向评价表
 */
@Data
@TableName("task_reviews")
public class TaskReviewPO {

    /** 评价 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务 ID */
    private Long taskId;

    /** 评价者 UID */
    private Long reviewerId;

    /** 被评价者 UID */
    private Long revieweeId;

    /** 评价方角色 */
    @EnumValue
    private RoleType roleType;

    /** 1~5 星 */
    private Integer score;

    /** 评价标签 JSON 数组 */
    private String tags;

    /** 文字评价 */
    private String content;

    /** 评价时间 */
    private LocalDateTime createdAt;

    public enum RoleType {
        PUBLISHER, RECEIVER
    }
}