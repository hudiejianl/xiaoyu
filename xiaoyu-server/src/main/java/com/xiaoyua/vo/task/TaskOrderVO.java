package com.xiaoyua.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoyua.vo.user.UserSimpleVO;
import java.time.LocalDateTime;

/**
 * 任务订单信息 VO
 * 用于返回任务订单的详细信息
 * 
 * @author xiaoyu
 */
public class TaskOrderVO {
    
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 任务标题
     */
    private String taskTitle;
    
    /**
     * 任务发布者信息
     */
    private UserSimpleVO publisher;
    
    /**
     * 任务接单者信息
     */
    private UserSimpleVO receiver;
    
    /**
     * 订单状态：WAIT_ACCEPT-等待接受，ACCEPTED-已接受，REFUSED-已拒绝，CANCELLED-已取消，FINISH-已完成
     */
    private String status;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    public TaskOrderVO() {}
    
    public TaskOrderVO(Long id, Long taskId, String taskTitle, UserSimpleVO publisher, 
                      UserSimpleVO receiver, String status, LocalDateTime createdAt, 
                      LocalDateTime updatedAt) {
        this.id = id;
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.publisher = publisher;
        this.receiver = receiver;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskTitle() {
        return taskTitle;
    }
    
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    
    public UserSimpleVO getPublisher() {
        return publisher;
    }
    
    public void setPublisher(UserSimpleVO publisher) {
        this.publisher = publisher;
    }
    
    public UserSimpleVO getReceiver() {
        return receiver;
    }
    
    public void setReceiver(UserSimpleVO receiver) {
        this.receiver = receiver;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "TaskOrderVO{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", publisher=" + publisher +
                ", receiver=" + receiver +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}