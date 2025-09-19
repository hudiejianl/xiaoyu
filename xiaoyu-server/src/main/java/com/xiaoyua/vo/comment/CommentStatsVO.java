package com.xiaoyua.vo.comment;

import lombok.Data;

/**
 * 评论统计信息VO
 * 用于返回评论的统计数据
 * 
 * @author xiaoyu
 */
@Data
public class CommentStatsVO {
    
    /**
     * 点赞次数
     */
    private Integer likeCnt;
    
    /**
     * 回复次数
     */
    private Integer replyCount;
    
//    public CommentStatsVO() {}
//
//    public CommentStatsVO(Integer likeCnt, Integer replyCount) {
//        this.likeCnt = likeCnt;
//        this.replyCount = replyCount;
//    }
//
//    public Integer getLikeCnt() {
//        return likeCnt;
//    }
//
//    public void setLikeCnt(Integer likeCnt) {
//        this.likeCnt = likeCnt;
//    }
//
//    public Integer getReplyCount() {
//        return replyCount;
//    }
//
//    public void setReplyCount(Integer replyCount) {
//        this.replyCount = replyCount;
//    }
//
//    @Override
//    public String toString() {
//        return "CommentStatsVO{" +
//                "likeCnt=" + likeCnt +
//                ", replyCount=" + replyCount +
//                '}';
//    }
}