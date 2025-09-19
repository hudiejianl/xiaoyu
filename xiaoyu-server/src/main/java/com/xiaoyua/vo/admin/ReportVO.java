package com.xiaoyua.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoyua.vo.user.UserSimpleVO;
import java.time.LocalDateTime;

/**
 * 举报信息VO
 * 用于返回举报记录的详细信息
 * 
 * @author xiaoyu
 */
public class ReportVO {
    
    /**
     * 举报ID
     */
    private Long id;
    
    /**
     * 举报人信息
     */
    private UserSimpleVO reporter;
    
    /**
     * 被举报对象ID
     */
    private Long itemId;
    
    /**
     * 被举报对象类型：POST-动态，TASK-任务，COMMENT-评论，USER-用户
     */
    private String itemType;
    
    /**
     * 举报理由
     */
    private String reason;
    
    /**
     * 处理状态：PENDING-待处理，ACCEPTED-已受理，REFUSED-已拒绝
     */
    private String status;
    
    /**
     * 举报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 被举报对象的标题或内容摘要（用于管理员快速了解）
     */
    private String itemTitle;
    
    /**
     * 被举报对象的作者信息（如果适用）
     */
    private UserSimpleVO itemAuthor;
    
    public ReportVO() {}
    
    public ReportVO(Long id, UserSimpleVO reporter, Long itemId, String itemType, 
                   String reason, String status, LocalDateTime createdAt, 
                   String itemTitle, UserSimpleVO itemAuthor) {
        this.id = id;
        this.reporter = reporter;
        this.itemId = itemId;
        this.itemType = itemType;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
        this.itemTitle = itemTitle;
        this.itemAuthor = itemAuthor;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserSimpleVO getReporter() {
        return reporter;
    }
    
    public void setReporter(UserSimpleVO reporter) {
        this.reporter = reporter;
    }
    
    public Long getItemId() {
        return itemId;
    }
    
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    
    public String getItemType() {
        return itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getItemTitle() {
        return itemTitle;
    }
    
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    
    public UserSimpleVO getItemAuthor() {
        return itemAuthor;
    }
    
    public void setItemAuthor(UserSimpleVO itemAuthor) {
        this.itemAuthor = itemAuthor;
    }
    
    @Override
    public String toString() {
        return "ReportVO{" +
                "id=" + id +
                ", reporter=" + reporter +
                ", itemId=" + itemId +
                ", itemType='" + itemType + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemAuthor=" + itemAuthor +
                '}';
    }
}