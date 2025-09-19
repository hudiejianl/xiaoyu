package com.xiaoyua.vo.post;

import lombok.Data;

/**
 * 动态统计信息VO
 * <p>
 * 用于返回动态的各项统计数据，包括浏览量、点赞数、评论数、分享数等。
 * 这些统计数据用于展示动态的热度和互动情况。
 * </p>
 * 
 * <h3>统计指标说明：</h3>
 * <ul>
 *   <li>浏览次数：用户查看动态详情的次数，同一用户多次查看会累计</li>
 *   <li>点赞次数：用户对动态点赞的总数，每个用户最多点赞一次</li>
 *   <li>评论次数：动态下的评论总数，包括一级和二级评论</li>
 *   <li>分享次数：用户分享动态的总数，包括转发和外部分享</li>
 * </ul>
 * 
 * <h3>数据更新：</h3>
 * <ul>
 *   <li>浏览次数：实时更新</li>
 *   <li>点赞次数：实时更新，支持取消点赞</li>
 *   <li>评论次数：实时更新，删除评论时会减少</li>
 *   <li>分享次数：实时更新</li>
 * </ul>
 * 
 * <h3>使用场景：</h3>
 * <ul>
 *   <li>动态列表展示互动数据</li>
 *   <li>动态详情页显示完整统计</li>
 *   <li>热门动态排序依据</li>
 *   <li>用户活跃度分析</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.post.PostVO
 * @see com.xiaoyua.vo.post.PostUserActionsVO
 */
@Data

public class PostStatsVO {
    
    /**
     * 浏览次数
     */
    private Integer viewCnt;
    
    /**
     * 点赞次数
     */
    private Integer likeCnt;
    
    /**
     * 收藏次数
     */
    private Integer favCnt;
    
    /**
     * 评论次数
     */
    private Integer commentCnt;
    
    /**
     * 分享次数
     */
    private Integer shareCnt;
//
//    public PostStatsVO() {}
//
//    public PostStatsVO(Integer viewCnt, Integer likeCnt, Integer commentCnt, Integer shareCnt) {
//        this.viewCnt = viewCnt;
//        this.likeCnt = likeCnt;
//        this.commentCnt = commentCnt;
//        this.shareCnt = shareCnt;
//    }
//
//    public Integer getViewCnt() {
//        return viewCnt;
//    }
//
//    public void setViewCnt(Integer viewCnt) {
//        this.viewCnt = viewCnt;
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
//    public Integer getCommentCnt() {
//        return commentCnt;
//    }
//
//    public void setCommentCnt(Integer commentCnt) {
//        this.commentCnt = commentCnt;
//    }
//
//    public Integer getShareCnt() {
//        return shareCnt;
//    }
//
//    public void setShareCnt(Integer shareCnt) {
//        this.shareCnt = shareCnt;
//    }
//
//    @Override
//    public String toString() {
//        return "PostStatsVO{" +
//                "viewCnt=" + viewCnt +
//                ", likeCnt=" + likeCnt +
//                ", commentCnt=" + commentCnt +
//                ", shareCnt=" + shareCnt +
//                '}';
//    }
}