package com.xiaoyua.vo.task;

import lombok.Data;

/**
 * 用户对任务的操作状态 VO
 * 用于返回当前用户对任务的操作状态
 *
 * @author xiaoyu
 */
@Data
public class TaskUserActionsVO {

    /**
     * 是否已收藏
     */
    private Boolean isFavorited;
}