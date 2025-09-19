package com.xiaoyua.vo.task;

import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务评价信息 VO
 * 用于返回任务评价详细信息
 *
 * @author xiaoyu
 */
@Data
public class TaskReviewVO {

    /**
     * 评价ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 评价者信息
     */
    private UserSimpleVO reviewer;

    /**
     * 被评价者信息
     */
    private UserSimpleVO reviewee;

    /**
     * 评价方角色
     * PUBLISHER - 发布者评价接单者
     * RECEIVER - 接单者评价发布者
     */
    private String roleType;

    /**
     * 评分 (1-5分)
     */
    private Integer score;

    /**
     * 评价标签列表
     */
    private List<String> tags;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}