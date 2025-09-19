package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 任务-文件关联表
 */
@Data
@TableName("task_files")
public class TaskFilePO {

    /** 任务 ID */
    @TableId
    private Long taskId;

    /** 文件 ID */
    @TableId
    private Long fileId;
}