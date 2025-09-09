package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务统计表
 */
@Data
@TableName("task_stats")
public class TaskStatPO {

    /** 任务 ID */
    @TableId
    private Long taskId;

    /** 浏览量 */
    private Integer viewCnt;

    /** 接单数 */
    private Integer orderCnt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}