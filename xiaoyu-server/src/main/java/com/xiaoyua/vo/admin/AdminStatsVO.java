package com.xiaoyua.vo.admin;

/**
 * 管理后台统计信息VO
 * 用于返回管理后台相关的统计数据
 * 
 * @author xiaoyu
 */
public class AdminStatsVO {
    
    /**
     * 待处理举报数
     */
    private Integer pendingReportCount;
    
    /**
     * 今日新增举报数
     */
    private Integer todayReportCount;
    
    /**
     * 待审核内容数
     */
    private Integer pendingAuditCount;
    
    /**
     * 今日审核通过数
     */
    private Integer todayApprovedCount;
    
    /**
     * 今日审核拒绝数
     */
    private Integer todayRejectedCount;
    
    /**
     * 封禁用户数
     */
    private Integer bannedUserCount;
    
    /**
     * 敏感词总数
     */
    private Integer sensitiveWordCount;
    
    /**
     * 系统公告数
     */
    private Integer systemNoticeCount;
    
    public AdminStatsVO() {}
    
    public AdminStatsVO(Integer pendingReportCount, Integer todayReportCount, Integer pendingAuditCount,
                       Integer todayApprovedCount, Integer todayRejectedCount, Integer bannedUserCount,
                       Integer sensitiveWordCount, Integer systemNoticeCount) {
        this.pendingReportCount = pendingReportCount;
        this.todayReportCount = todayReportCount;
        this.pendingAuditCount = pendingAuditCount;
        this.todayApprovedCount = todayApprovedCount;
        this.todayRejectedCount = todayRejectedCount;
        this.bannedUserCount = bannedUserCount;
        this.sensitiveWordCount = sensitiveWordCount;
        this.systemNoticeCount = systemNoticeCount;
    }
    
    public Integer getPendingReportCount() {
        return pendingReportCount;
    }
    
    public void setPendingReportCount(Integer pendingReportCount) {
        this.pendingReportCount = pendingReportCount;
    }
    
    public Integer getTodayReportCount() {
        return todayReportCount;
    }
    
    public void setTodayReportCount(Integer todayReportCount) {
        this.todayReportCount = todayReportCount;
    }
    
    public Integer getPendingAuditCount() {
        return pendingAuditCount;
    }
    
    public void setPendingAuditCount(Integer pendingAuditCount) {
        this.pendingAuditCount = pendingAuditCount;
    }
    
    public Integer getTodayApprovedCount() {
        return todayApprovedCount;
    }
    
    public void setTodayApprovedCount(Integer todayApprovedCount) {
        this.todayApprovedCount = todayApprovedCount;
    }
    
    public Integer getTodayRejectedCount() {
        return todayRejectedCount;
    }
    
    public void setTodayRejectedCount(Integer todayRejectedCount) {
        this.todayRejectedCount = todayRejectedCount;
    }
    
    public Integer getBannedUserCount() {
        return bannedUserCount;
    }
    
    public void setBannedUserCount(Integer bannedUserCount) {
        this.bannedUserCount = bannedUserCount;
    }
    
    public Integer getSensitiveWordCount() {
        return sensitiveWordCount;
    }
    
    public void setSensitiveWordCount(Integer sensitiveWordCount) {
        this.sensitiveWordCount = sensitiveWordCount;
    }
    
    public Integer getSystemNoticeCount() {
        return systemNoticeCount;
    }
    
    public void setSystemNoticeCount(Integer systemNoticeCount) {
        this.systemNoticeCount = systemNoticeCount;
    }
    
    @Override
    public String toString() {
        return "AdminStatsVO{" +
                "pendingReportCount=" + pendingReportCount +
                ", todayReportCount=" + todayReportCount +
                ", pendingAuditCount=" + pendingAuditCount +
                ", todayApprovedCount=" + todayApprovedCount +
                ", todayRejectedCount=" + todayRejectedCount +
                ", bannedUserCount=" + bannedUserCount +
                ", sensitiveWordCount=" + sensitiveWordCount +
                ", systemNoticeCount=" + systemNoticeCount +
                '}';
    }
}