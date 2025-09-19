package com.xiaoyua.vo.task;

import com.xiaoyua.vo.file.FileSimpleVO;
import com.xiaoyua.vo.tag.TagSimpleVO;
import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务信息 VO
 * 用于返回任务详细信息
 *
 * @author xiaoyu
 */
@Data
public class TaskVO {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 发布者信息
     */
    private UserSimpleVO publisher;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务详情
     */
    private String content;

    /**
     * 悬赏金额
     */
    private BigDecimal reward;

    /**
     * 任务状态
     * DRAFT - 草稿
     * AUDITING - 审核中
     * RECRUIT - 招募中
     * RUNNING - 进行中
     * DELIVER - 待交付
     * FINISH - 已完成
     * CLOSED - 已关闭
     * ARBITRATED - 仲裁中
     */
    private String status;

    /**
     * 可见范围
     * PUBLIC - 公开
     * FRIEND - 好友可见
     * CAMPUS - 校园可见
     */
    private String visibility;

    /**
     * 截止时间
     */
    private LocalDateTime expireAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 关联文件列表
     */
    private List<FileSimpleVO> files;

    /**
     * 标签列表
     */
    private List<TagSimpleVO> tags;

    /**
     * 任务统计信息
     */
    private TaskStatsVO stats;

    /**
     * 用户操作状态
     */
    private TaskUserActionsVO userActions;
}