package com.xiaoyua.dto.task;

import com.xiaoyua.dto.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务评价查询 DTO
 * 用于接收任务评价查询请求参数
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskReviewQueryDTO extends PageDTO {

    /**
     * 评价方角色筛选
     * PUBLISHER - 查询作为发布者的评价
     * RECEIVER - 查询作为接单者的评价
     */
    private String roleType;

    /**
     * 任务ID筛选
     */
    private Long taskId;

    /**
     * 被评价者ID筛选
     */
    private Long revieweeId;

    /**
     * 评价者ID筛选
     */
    private Long reviewerId;
}