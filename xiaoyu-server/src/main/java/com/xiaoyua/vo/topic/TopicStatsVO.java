package com.xiaoyua.vo.topic;

import lombok.Data;

/**
 * 话题统计信息VO
 * 用于返回话题的统计数据
 * 
 * @author xiaoyu
 */
@Data

public class TopicStatsVO {
    
    /**
     * 关联动态数量
     */
    private Integer postCount;
    
    /**
     * 参与用户数量
     */
    private Integer participantCount;
    
    /**
     * 今日新增动态数
     */
    private Integer todayPostCount;
    
    /**
     * 热度值
     */
    private Integer hotScore;
    
//    public TopicStatsVO() {}
//
//    public TopicStatsVO(Integer postCount, Integer participantCount, Integer todayPostCount, Integer hotScore) {
//        this.postCount = postCount;
//        this.participantCount = participantCount;
//        this.todayPostCount = todayPostCount;
//        this.hotScore = hotScore;
//    }
//
//    public Integer getPostCount() {
//        return postCount;
//    }
//
//    public void setPostCount(Integer postCount) {
//        this.postCount = postCount;
//    }
//
//    public Integer getParticipantCount() {
//        return participantCount;
//    }
//
//    public void setParticipantCount(Integer participantCount) {
//        this.participantCount = participantCount;
//    }
//
//    public Integer getTodayPostCount() {
//        return todayPostCount;
//    }
//
//    public void setTodayPostCount(Integer todayPostCount) {
//        this.todayPostCount = todayPostCount;
//    }
//
//    public Integer getHotScore() {
//        return hotScore;
//    }
//
//    public void setHotScore(Integer hotScore) {
//        this.hotScore = hotScore;
//    }
//
//    @Override
//    public String toString() {
//        return "TopicStatsVO{" +
//                "postCount=" + postCount +
//                ", participantCount=" + participantCount +
//                ", todayPostCount=" + todayPostCount +
//                ", hotScore=" + hotScore +
//                '}';
//    }
}