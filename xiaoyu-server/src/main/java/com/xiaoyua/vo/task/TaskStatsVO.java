package com.xiaoyua.vo.task;

import lombok.Data;

/**
 * 任务统计信息 VO
 * 用于返回任务的统计数据
 *
 * @author xiaoyu
 */
@Data
public class TaskStatsVO {

    /**
     * 浏览次数
     */
    private Integer viewCnt;

    /**
     * 接单次数
     */
    private Integer orderCnt;
}