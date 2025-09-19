package com.xiaoyua.dto.comment;

import com.xiaoyua.dto.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论查询 DTO
 * 用于接收客户端的评论查询请求
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryDTO extends PageDTO {
    
    /**
     * 排序方式
     * hot: 按热度排序（点赞数）
     * latest: 按时间排序（最新）
     * 默认值：latest
     */
    private String sort = "latest";
}