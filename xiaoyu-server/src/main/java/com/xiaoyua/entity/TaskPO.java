package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务主表
 */
@Data
@TableName("tasks")
public class TaskPO {

    /** 任务 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发布者 UID */
    private Long publisherId;

    /** 任务标题 */
    private String title;

    /** 任务详情 */
    private String content;

    /** 悬赏金额 */
    private BigDecimal reward;

    /** 生命周期状态 */
    @EnumValue
    private Status status;

    /** 可见范围 */
    @EnumValue
    private Visibility visibility;

    /** 截止报名时间 */
    private LocalDateTime expireAt;

    /** 发布时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public enum Status {
        DRAFT, AUDITING, RECRUIT, RUNNING, DELIVER, FINISH, CLOSED, ARBITRATED
    }

    public enum Visibility {
        PUBLIC, FRIEND, CAMPUS
    }
}