package com.xiaoyua.dto.task;

import com.xiaoyua.dto.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务查询 DTO
 * 用于接收任务查询请求参数
 *
 * @author xiaoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQueryDTO extends PageDTO {

    /**
     * 查询类型
     * all - 全部任务
     * my_published - 我发布的
     * my_received - 我接收的
     * campus - 校园任务
     * hot - 热门任务
     */
    private String type;

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
     * 标签ID
     */
    private Integer tagId;

    /**
     * 搜索关键词
     */
    private String keyword;
}