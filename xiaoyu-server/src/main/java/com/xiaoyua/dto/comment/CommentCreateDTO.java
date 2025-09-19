package com.xiaoyua.dto.comment;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 创建评论请求DTO
 * <p>
 * 用于接收用户发表评论的请求数据。
 * 支持一级评论和二级评论，以及@用户功能。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * // 一级评论
 * CommentCreateDTO comment = new CommentCreateDTO();
 * comment.setContent("这个动态很棒！");
 * comment.setParentId(0L);
 * 
 * // 二级评论（回复）
 * CommentCreateDTO reply = new CommentCreateDTO();
 * reply.setContent("@张三 我也觉得很不错");
 * reply.setParentId(123L);
 * reply.setAtUsers(Arrays.asList(456L));
 * }</pre>
 * 
 * <h3>评论层级：</h3>
 * <ul>
 *   <li>一级评论：parentId = 0，直接对动态/任务的评论</li>
 *   <li>二级评论：parentId > 0，对一级评论的回复</li>
 *   <li>不支持三级及以上评论，所有回复都归为二级评论</li>
 * </ul>
 * 
 * <h3>@用户功能：</h3>
 * <ul>
 *   <li>在评论内容中使用@用户名格式</li>
 *   <li>atUsers数组包含被@用户的ID</li>
 *   <li>被@用户会收到评论通知</li>
 *   <li>最多可以@10个用户</li>
 * </ul>
 * 
 * <h3>内容限制：</h3>
 * <ul>
 *   <li>评论内容最多500个字符</li>
 *   <li>不支持图片、视频等富媒体内容</li>
 *   <li>支持表情符号和特殊字符</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.comment.CommentVO
 * @see com.xiaoyua.dto.comment.CommentQueryDTO
 */
@Data
public class CommentCreateDTO {
    
    /**
     * 评论内容
     * 必填，最多500个字符
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容最多500个字符")
    private String content;
    
    /**
     * 父评论ID
     * 0为一级评论，其他值为二级评论
     * 默认值：0
     */
    private Long parentId = 0L;

    private Long postId = 0L;
    
    /**
     * @的用户ID数组
     * 可选，用于@功能
     */
    private List<Long> atUsers;
}