package com.xiaoyua.vo.common;

/**
 * 校区统计信息VO
 * 用于返回校区的统计数据
 * 
 * @author xiaoyu
 */
public class CampusStatsVO {
    
    /**
     * 用户总数
     */
    private Integer userCount;
    
    /**
     * 活跃用户数（最近7天）
     */
    private Integer activeUserCount;
    
    /**
     * 动态总数
     */
    private Integer postCount;
    
    /**
     * 任务总数
     */
    private Integer taskCount;
    
    /**
     * 今日新增用户数
     */
    private Integer todayNewUserCount;
    
    /**
     * 今日新增动态数
     */
    private Integer todayPostCount;
    
    /**
     * 今日新增任务数
     */
    private Integer todayTaskCount;
    
    public CampusStatsVO() {}
    
    public CampusStatsVO(Integer userCount, Integer activeUserCount, Integer postCount, Integer taskCount, 
                        Integer todayNewUserCount, Integer todayPostCount, Integer todayTaskCount) {
        this.userCount = userCount;
        this.activeUserCount = activeUserCount;
        this.postCount = postCount;
        this.taskCount = taskCount;
        this.todayNewUserCount = todayNewUserCount;
        this.todayPostCount = todayPostCount;
        this.todayTaskCount = todayTaskCount;
    }
    
    public Integer getUserCount() {
        return userCount;
    }
    
    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
    
    public Integer getActiveUserCount() {
        return activeUserCount;
    }
    
    public void setActiveUserCount(Integer activeUserCount) {
        this.activeUserCount = activeUserCount;
    }
    
    public Integer getPostCount() {
        return postCount;
    }
    
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
    
    public Integer getTaskCount() {
        return taskCount;
    }
    
    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
    
    public Integer getTodayNewUserCount() {
        return todayNewUserCount;
    }
    
    public void setTodayNewUserCount(Integer todayNewUserCount) {
        this.todayNewUserCount = todayNewUserCount;
    }
    
    public Integer getTodayPostCount() {
        return todayPostCount;
    }
    
    public void setTodayPostCount(Integer todayPostCount) {
        this.todayPostCount = todayPostCount;
    }
    
    public Integer getTodayTaskCount() {
        return todayTaskCount;
    }
    
    public void setTodayTaskCount(Integer todayTaskCount) {
        this.todayTaskCount = todayTaskCount;
    }
    
    @Override
    public String toString() {
        return "CampusStatsVO{" +
                "userCount=" + userCount +
                ", activeUserCount=" + activeUserCount +
                ", postCount=" + postCount +
                ", taskCount=" + taskCount +
                ", todayNewUserCount=" + todayNewUserCount +
                ", todayPostCount=" + todayPostCount +
                ", todayTaskCount=" + todayTaskCount +
                '}';
    }
}