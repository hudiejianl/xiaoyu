package com.xiaoyua.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建任务请求DTO
 * <p>
 * 用于接收用户发布任务的请求数据。
 * 支持悬赏任务发布，包含任务描述、悬赏金额、截止时间等信息。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * TaskCreateDTO taskDTO = new TaskCreateDTO();
 * taskDTO.setTitle("帮忙取快递");
 * taskDTO.setContent("明天下午帮忙到菜鸟驿站取快递，取件码：1234");
 * taskDTO.setReward(new BigDecimal("10.00"));
 * taskDTO.setVisibility("CAMPUS");
 * taskDTO.setExpireAt(LocalDateTime.now().plusDays(1));
 * taskDTO.setTagIds(Arrays.asList(1, 2));
 * }</pre>
 * 
 * <h3>任务类型：</h3>
 * <ul>
 *   <li>跑腿服务：取快递、买东西、送文件等</li>
 *   <li>学习辅导：作业答疑、考试复习、技能教学等</li>
 *   <li>生活服务：打扫卫生、修理电脑、设计制作等</li>
 *   <li>其他服务：根据用户需求自定义</li>
 * </ul>
 * 
 * <h3>悬赏规则：</h3>
 * <ul>
 *   <li>最低悬赏金额：0.01元</li>
 *   <li>发布任务时冻结悬赏金额</li>
 *   <li>任务完成后自动转账给接单者</li>
 *   <li>任务取消时解冻悬赏金额</li>
 * </ul>
 * 
 * <h3>可见范围：</h3>
 * <ul>
 *   <li>PUBLIC：所有用户可见</li>
 *   <li>FRIEND：仅好友可见</li>
 *   <li>CAMPUS：仅同校用户可见</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.task.TaskVO
 * @see com.xiaoyua.dto.task.TaskUpdateDTO
 */
@Data
public class TaskCreateDTO {

    /**
     * 任务标题
     */
    @NotBlank(message = "任务标题不能为空")
    @Size(max = 100, message = "任务标题最多100个字符")
    private String title;

    /**
     * 任务详情
     */
    @NotBlank(message = "任务详情不能为空")
    private String content;

    /**
     * 悬赏金额
     */
    @NotNull(message = "悬赏金额不能为空")
    @DecimalMin(value = "0.01", message = "悬赏金额必须大于0")
    private BigDecimal reward;

    /**
     * 可见范围
     * PUBLIC - 公开
     * FRIEND - 好友可见
     * CAMPUS - 校园可见
     */
    private String visibility = "PUBLIC";

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