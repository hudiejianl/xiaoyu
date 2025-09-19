package com.xiaoyua.vo.friend;

import com.xiaoyua.vo.user.UserSimpleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 好友信息VO
 * 用于返回好友信息和关系状态
 * 
 * @author xiaoyu
 */
public class FriendVO {
    
    /**
     * 好友关系ID
     */
    private Long id;
    
    /**
     * 好友用户信息
     */
    private UserSimpleVO friend;
    
    /**
     * 好友关系状态：PENDING-待确认，ACCEPTED-已接受，REFUSED-已拒绝
     */
    private String status;
    
    /**
     * 是否在线
     */
    private Boolean isOnline;
    
    /**
     * 最后在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineAt;
    
    /**
     * 好友关系创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 好友关系更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    public FriendVO() {}
    
    public FriendVO(Long id, UserSimpleVO friend, String status, Boolean isOnline, 
                   LocalDateTime lastOnlineAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.friend = friend;
        this.status = status;
        this.isOnline = isOnline;
        this.lastOnlineAt = lastOnlineAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserSimpleVO getFriend() {
        return friend;
    }
    
    public void setFriend(UserSimpleVO friend) {
        this.friend = friend;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Boolean getIsOnline() {
        return isOnline;
    }
    
    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }
    
    public LocalDateTime getLastOnlineAt() {
        return lastOnlineAt;
    }
    
    public void setLastOnlineAt(LocalDateTime lastOnlineAt) {
        this.lastOnlineAt = lastOnlineAt;
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
        return "FriendVO{" +
                "id=" + id +
                ", friend=" + friend +
                ", status='" + status + '\'' +
                ", isOnline=" + isOnline +
                ", lastOnlineAt=" + lastOnlineAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}