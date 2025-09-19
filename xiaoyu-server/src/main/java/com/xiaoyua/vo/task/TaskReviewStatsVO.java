package com.xiaoyua.vo.task;

import java.math.BigDecimal;

/**
 * 任务评价统计信息VO
 * 用于返回任务评价相关的统计数据
 * 
 * @author xiaoyu
 */
public class TaskReviewStatsVO {
    
    /**
     * 总评价数
     */
    private Integer totalReviewCount;
    
    /**
     * 平均评分
     */
    private BigDecimal averageScore;
    
    /**
     * 5星评价数
     */
    private Integer fiveStarCount;
    
    /**
     * 4星评价数
     */
    private Integer fourStarCount;
    
    /**
     * 3星评价数
     */
    private Integer threeStarCount;
    
    /**
     * 2星评价数
     */
    private Integer twoStarCount;
    
    /**
     * 1星评价数
     */
    private Integer oneStarCount;
    
    /**
     * 好评率（4星及以上）
     */
    private BigDecimal positiveRate;
    
    public TaskReviewStatsVO() {}
    
    public TaskReviewStatsVO(Integer totalReviewCount, BigDecimal averageScore, Integer fiveStarCount,
                            Integer fourStarCount, Integer threeStarCount, Integer twoStarCount,
                            Integer oneStarCount, BigDecimal positiveRate) {
        this.totalReviewCount = totalReviewCount;
        this.averageScore = averageScore;
        this.fiveStarCount = fiveStarCount;
        this.fourStarCount = fourStarCount;
        this.threeStarCount = threeStarCount;
        this.twoStarCount = twoStarCount;
        this.oneStarCount = oneStarCount;
        this.positiveRate = positiveRate;
    }
    
    public Integer getTotalReviewCount() {
        return totalReviewCount;
    }
    
    public void setTotalReviewCount(Integer totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }
    
    public BigDecimal getAverageScore() {
        return averageScore;
    }
    
    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }
    
    public Integer getFiveStarCount() {
        return fiveStarCount;
    }
    
    public void setFiveStarCount(Integer fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }
    
    public Integer getFourStarCount() {
        return fourStarCount;
    }
    
    public void setFourStarCount(Integer fourStarCount) {
        this.fourStarCount = fourStarCount;
    }
    
    public Integer getThreeStarCount() {
        return threeStarCount;
    }
    
    public void setThreeStarCount(Integer threeStarCount) {
        this.threeStarCount = threeStarCount;
    }
    
    public Integer getTwoStarCount() {
        return twoStarCount;
    }
    
    public void setTwoStarCount(Integer twoStarCount) {
        this.twoStarCount = twoStarCount;
    }
    
    public Integer getOneStarCount() {
        return oneStarCount;
    }
    
    public void setOneStarCount(Integer oneStarCount) {
        this.oneStarCount = oneStarCount;
    }
    
    public BigDecimal getPositiveRate() {
        return positiveRate;
    }
    
    public void setPositiveRate(BigDecimal positiveRate) {
        this.positiveRate = positiveRate;
    }
    
    @Override
    public String toString() {
        return "TaskReviewStatsVO{" +
                "totalReviewCount=" + totalReviewCount +
                ", averageScore=" + averageScore +
                ", fiveStarCount=" + fiveStarCount +
                ", fourStarCount=" + fourStarCount +
                ", threeStarCount=" + threeStarCount +
                ", twoStarCount=" + twoStarCount +
                ", oneStarCount=" + oneStarCount +
                ", positiveRate=" + positiveRate +
                '}';
    }
}