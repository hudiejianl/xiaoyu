package com.xiaoyua.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 创建任务评价 DTO
 * 用于接收任务评价提交请求数据
 *
 * @author xiaoyu
 */
@Data
public class TaskReviewCreateDTO {

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 被评价者ID
     */
    @NotNull(message = "被评价者ID不能为空")
    private Long revieweeId;

    /**
     * 评价方角色
     * PUBLISHER - 发布者评价接单者
     * RECEIVER - 接单者评价发布者
     */
    @NotBlank(message = "评价方角色不能为空")
    @Pattern(regexp = "^(PUBLISHER|RECEIVER)$", message = "评价方角色只能是PUBLISHER或RECEIVER")
    private String roleType;

    /**
     * 评分 (1-5分)
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分必须在1-5之间")
    @Max(value = 5, message = "评分必须在1-5之间")
    private Integer score;

    /**
     * 评价标签列表
     */
    private List<String> tags;

    /**
     * 评价内容
     */
    @Size(max = 500, message = "评价内容最多500个字符")
    private String content;
}