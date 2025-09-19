package com.xiaoyua.vo.comment;

import com.xiaoyua.vo.user.UserSimpleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论信息 VO
 * 用于返回评论的详细信息，包含作者信息、@用户信息等
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class CommentVO {
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 评论者信息
     */
    private UserSimpleVO user;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 父评论ID
     * 0为一级评论，其他值为二级评论
     */
    private Long parentId;
    
    /**
     * @的用户信息列表
     */
    private List<UserSimpleVO> atUsers;
    
    /**
     * 点赞数量
     */
    private Integer likeCnt;
    
    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;
    
    /**
     * 回复数量
     * 仅对一级评论有效
     */
    private Integer replyCount;
    
    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 二级评论列表
     * 仅对一级评论有效，包含该评论下的回复
     */
    private List<CommentVO> replies;
}