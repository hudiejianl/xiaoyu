package com.xiaoyua.vo.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知信息VO
 * 用于返回通知的详细信息
 * 
 * @author xiaoyu
 */
@Data
public class NotificationVO {
    
    /**
     * 通知ID
     */
    private Long id;
    
    /**
     * 通知类型：LIKE点赞 FAVORITE收藏 COMMENT评论 SHARE转发 TASK_ORDER任务订单 SYSTEM系统通知 VIOLATION违规通知
     */
    private String type;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 关联业务对象ID
     */
    @JsonProperty("ref_id")
    private Long refId;
    
    /**
     * 关联业务类型：POST动态 TASK任务 COMMENT评论
     */
    @JsonProperty("ref_type")
    private String refType;
    
    /**
     * 阅读状态：UNREAD未读 READ已读
     */
    private String status;
    
    /**
     * 通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 来源用户信息（如果是用户互动类通知）
     */
    @JsonProperty("from_user")
    private UserSimpleVO fromUser;
    
    /**
     * 通知数量（聚合通知时使用）
     */
    private Integer count;
    
    public NotificationVO() {}
    
    public NotificationVO(Long id, String type, String title, String content, 
                         Long refId, String refType, String status, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.refId = refId;
        this.refType = refType;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserSimpleVO getFromUser() {
        return fromUser;
    }
    
    public void setFromUser(UserSimpleVO fromUser) {
        this.fromUser = fromUser;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getRefId() {
        return refId;
    }
    
    public void setRefId(Long refId) {
        this.refId = refId;
    }
    
    public String getRefType() {
        return refType;
    }
    
    public void setRefType(String refType) {
        this.refType = refType;
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
    
    @Override
    public String toString() {
        return "NotificationVO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", refId=" + refId +
                ", refType='" + refType + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", fromUser=" + fromUser +
                ", count=" + count +
                '}';
    }
}