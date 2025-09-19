package com.xiaoyua.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新任务 DTO
 * 用于接收任务更新请求数据
 *
 * @author xiaoyu
 */
@Data
public class TaskUpdateDTO {

    /**
     * 任务标题
     */
    @Size(max = 100, message = "任务标题最多100个字符")
    private String title;

    /**
     * 任务详情
     */
    private String content;

    /**
     * 悬赏金额
     */
    @DecimalMin(value = "0.01", message = "悬赏金额必须大于0")
    private BigDecimal reward;

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
    @Future(message = "截止时间必须是未来时间")
    private LocalDateTime expireAt;

    /**
     * 关联文件ID列表
     */
    private List<Long> fileIds;

    /**
     * 标签ID列表
     */
    private List<Integer> tagIds;
}