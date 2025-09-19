package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务订单表
 */
@Data
@TableName("task_orders")
public class TaskOrderPO {

    /** 订单主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务 ID */
    private Long taskId;

    /** 接单者 UID */
    private Long receiverId;

    /** 订单状态 */
    @EnumValue
    private Status status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public enum Status {
        WAIT_ACCEPT, ACCEPTED, REFUSED, CANCELLED, FINISH
    }
}