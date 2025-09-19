package com.xiaoyua.vo.common;

/**
 * 系统统计信息VO
 * 用于返回系统整体的统计数据
 * 
 * @author xiaoyu
 */
public class SystemStatsVO {
    
    /**
     * 用户总数
     */
    private Long totalUsers;
    
    /**
     * 动态总数
     */
    private Long totalPosts;
    
    /**
     * 任务总数
     */
    private Long totalTasks;
    
    /**
     * 评论总数
     */
    private Long totalComments;
    
    /**
     * 今日活跃用户数
     */
    private Integer todayActiveUsers;
    
    /**
     * 今日新增用户数
     */
    private Integer todayNewUsers;
    
    /**
     * 今日新增动态数
     */
    private Integer todayNewPosts;
    
    /**
     * 今日新增任务数
     */
    private Integer todayNewTasks;
    
    /**
     * 今日新增评论数
     */
    private Integer todayNewComments;
    
    /**
     * 系统运行天数
     */
    private Integer runningDays;
    
    public SystemStatsVO() {}
    
    public SystemStatsVO(Long totalUsers, Long totalPosts, Long totalTasks, Long totalComments,
                        Integer todayActiveUsers, Integer todayNewUsers, Integer todayNewPosts,
                        Integer todayNewTasks, Integer todayNewComments, Integer runningDays) {
        this.totalUsers = totalUsers;
        this.totalPosts = totalPosts;
        this.totalTasks = totalTasks;
        this.totalComments = totalComments;
        this.todayActiveUsers = todayActiveUsers;
        this.todayNewUsers = todayNewUsers;
        this.todayNewPosts = todayNewPosts;
        this.todayNewTasks = todayNewTasks;
        this.todayNewComments = todayNewComments;
        this.runningDays = runningDays;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getTotalPosts() {
        return totalPosts;
    }
    
    public void setTotalPosts(Long totalPosts) {
        this.totalPosts = totalPosts;
    }
    
    public Long getTotalTasks() {
        return totalTasks;
    }
    
    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }
    
    public Long getTotalComments() {
        return totalComments;
    }
    
    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }
    
    public Integer getTodayActiveUsers() {
        return todayActiveUsers;
    }
    
    public void setTodayActiveUsers(Integer todayActiveUsers) {
        this.todayActiveUsers = todayActiveUsers;
    }
    
    public Integer getTodayNewUsers() {
        return todayNewUsers;
    }
    
    public void setTodayNewUsers(Integer todayNewUsers) {
        this.todayNewUsers = todayNewUsers;
    }
    
    public Integer getTodayNewPosts() {
        return todayNewPosts;
    }
    
    public void setTodayNewPosts(Integer todayNewPosts) {
        this.todayNewPosts = todayNewPosts;
    }
    
    public Integer getTodayNewTasks() {
        return todayNewTasks;
    }
    
    public void setTodayNewTasks(Integer todayNewTasks) {
        this.todayNewTasks = todayNewTasks;
    }
    
    public Integer getTodayNewComments() {
        return todayNewComments;
    }
    
    public void setTodayNewComments(Integer todayNewComments) {
        this.todayNewComments = todayNewComments;
    }
    
    public Integer getRunningDays() {
        return runningDays;
    }
    
    public void setRunningDays(Integer runningDays) {
        this.runningDays = runningDays;
    }
    
    @Override
    public String toString() {
        return "SystemStatsVO{" +
                "totalUsers=" + totalUsers +
                ", totalPosts=" + totalPosts +
                ", totalTasks=" + totalTasks +
                ", totalComments=" + totalComments +
                ", todayActiveUsers=" + todayActiveUsers +
                ", todayNewUsers=" + todayNewUsers +
                ", todayNewPosts=" + todayNewPosts +
                ", todayNewTasks=" + todayNewTasks +
                ", todayNewComments=" + todayNewComments +
                ", runningDays=" + runningDays +
                '}';
    }
}