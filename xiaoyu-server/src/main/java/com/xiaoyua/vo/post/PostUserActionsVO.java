package com.xiaoyua.vo.post;

import lombok.Data;

/**
 * 用户对动态的操作状态VO
 * 用于返回当前用户对动态的操作状态
 * 
 * @author xiaoyu
 */
@Data

public class PostUserActionsVO {
    
    /**
     * 是否已点赞
     */
    private Boolean isLiked;
    
    /**
     * 是否已收藏
     */
    private Boolean isFavorited;
    
//    public PostUserActionsVO() {}
//
//    public PostUserActionsVO(Boolean isLiked, Boolean isFavorited) {
//        this.isLiked = isLiked;
//        this.isFavorited = isFavorited;
//    }
//
//    public Boolean getIsLiked() {
//        return isLiked;
//    }
//
//    public void setIsLiked(Boolean isLiked) {
//        this.isLiked = isLiked;
//    }
//
//    public Boolean getIsFavorited() {
//        return isFavorited;
//    }
//
//    public void setIsFavorited(Boolean isFavorited) {
//        this.isFavorited = isFavorited;
//    }
//
//    @Override
//    public String toString() {
//        return "PostUserActionsVO{" +
//                "isLiked=" + isLiked +
//                ", isFavorited=" + isFavorited +
//                '}';
//    }
}