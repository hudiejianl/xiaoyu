package com.xiaoyua.vo.user;

import java.time.LocalDateTime;

/**
 * 用户活动统计信息VO
 * 用于返回用户活动相关的统计数据
 * 
 * @author xiaoyu
 */
public class UserActivityStatsVO {
    
    /**
     * 总登录次数
     */
    private Integer totalLoginCount;
    
    /**
     * 连续登录天数
     */
    private Integer consecutiveLoginDays;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 本月发布动态数
     */
    private Integer monthlyPostCount;
    
    /**
     * 本月发布任务数
     */
    private Integer monthlyTaskCount;
    
    /**
     * 本月评论数
     */
    private Integer monthlyCommentCount;
    
    /**
     * 获得点赞总数
     */
    private Integer totalLikeReceived;
    
    /**
     * 主页访问次数
     */
    private Integer profileViewCount;
    
    public UserActivityStatsVO() {}
    
    public UserActivityStatsVO(Integer totalLoginCount, Integer consecutiveLoginDays, LocalDateTime lastLoginTime,
                              Integer monthlyPostCount, Integer monthlyTaskCount, Integer monthlyCommentCount,
                              Integer totalLikeReceived, Integer profileViewCount) {
        this.totalLoginCount = totalLoginCount;
        this.consecutiveLoginDays = consecutiveLoginDays;
        this.lastLoginTime = lastLoginTime;
        this.monthlyPostCount = monthlyPostCount;
        this.monthlyTaskCount = monthlyTaskCount;
        this.monthlyCommentCount = monthlyCommentCount;
        this.totalLikeReceived = totalLikeReceived;
        this.profileViewCount = profileViewCount;
    }
    
    public Integer getTotalLoginCount() {
        return totalLoginCount;
    }
    
    public void setTotalLoginCount(Integer totalLoginCount) {
        this.totalLoginCount = totalLoginCount;
    }
    
    public Integer getConsecutiveLoginDays() {
        return consecutiveLoginDays;
    }
    
    public void setConsecutiveLoginDays(Integer consecutiveLoginDays) {
        this.consecutiveLoginDays = consecutiveLoginDays;
    }
    
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }
    
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public Integer getMonthlyPostCount() {
        return monthlyPostCount;
    }
    
    public void setMonthlyPostCount(Integer monthlyPostCount) {
        this.monthlyPostCount = monthlyPostCount;
    }
    
    public Integer getMonthlyTaskCount() {
        return monthlyTaskCount;
    }
    
    public void setMonthlyTaskCount(Integer monthlyTaskCount) {
        this.monthlyTaskCount = monthlyTaskCount;
    }
    
    public Integer getMonthlyCommentCount() {
        return monthlyCommentCount;
    }
    
    public void setMonthlyCommentCount(Integer monthlyCommentCount) {
        this.monthlyCommentCount = monthlyCommentCount;
    }
    
    public Integer getTotalLikeReceived() {
        return totalLikeReceived;
    }
    
    public void setTotalLikeReceived(Integer totalLikeReceived) {
        this.totalLikeReceived = totalLikeReceived;
    }
    
    public Integer getProfileViewCount() {
        return profileViewCount;
    }
    
    public void setProfileViewCount(Integer profileViewCount) {
        this.profileViewCount = profileViewCount;
    }
    
    @Override
    public String toString() {
        return "UserActivityStatsVO{" +
                "totalLoginCount=" + totalLoginCount +
                ", consecutiveLoginDays=" + consecutiveLoginDays +
                ", lastLoginTime=" + lastLoginTime +
                ", monthlyPostCount=" + monthlyPostCount +
                ", monthlyTaskCount=" + monthlyTaskCount +
                ", monthlyCommentCount=" + monthlyCommentCount +
                ", totalLikeReceived=" + totalLikeReceived +
                ", profileViewCount=" + profileViewCount +
                '}';
    }
}