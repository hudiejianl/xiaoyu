package com.xiaoyua.vo.tag;

/**
 * 标签统计信息VO
 * 用于返回标签的统计数据
 * 
 * @author xiaoyu
 */
public class TagStatsVO {
    
    /**
     * 使用次数
     */
    private Integer usageCount;
    
    /**
     * 关联动态数量
     */
    private Integer postCount;
    
    /**
     * 关联任务数量
     */
    private Integer taskCount;
    
    /**
     * 今日使用次数
     */
    private Integer todayUsageCount;
    
    /**
     * 热度排名
     */
    private Integer hotRank;
    
    public TagStatsVO() {}
    
    public TagStatsVO(Integer usageCount, Integer postCount, Integer taskCount, Integer todayUsageCount, Integer hotRank) {
        this.usageCount = usageCount;
        this.postCount = postCount;
        this.taskCount = taskCount;
        this.todayUsageCount = todayUsageCount;
        this.hotRank = hotRank;
    }
    
    public Integer getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
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
    
    public Integer getTodayUsageCount() {
        return todayUsageCount;
    }
    
    public void setTodayUsageCount(Integer todayUsageCount) {
        this.todayUsageCount = todayUsageCount;
    }
    
    public Integer getHotRank() {
        return hotRank;
    }
    
    public void setHotRank(Integer hotRank) {
        this.hotRank = hotRank;
    }
    
    @Override
    public String toString() {
        return "TagStatsVO{" +
                "usageCount=" + usageCount +
                ", postCount=" + postCount +
                ", taskCount=" + taskCount +
                ", todayUsageCount=" + todayUsageCount +
                ", hotRank=" + hotRank +
                '}';
    }
}