package com.xiaoyua.vo.friend;

import com.xiaoyua.vo.user.UserSimpleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 私信消息VO
 * 用于返回私信内容和时间
 * 
 * @author xiaoyu
 */
public class MessageVO {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 发送者信息
     */
    private UserSimpleVO fromUser;
    
    /**
     * 接收者信息
     */
    private UserSimpleVO toUser;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 是否为当前用户发送的消息
     */
    private Boolean isSelf;
    
    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public MessageVO() {}
    
    public MessageVO(Long id, UserSimpleVO fromUser, UserSimpleVO toUser, 
                    String content, Boolean isSelf, LocalDateTime createdAt) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.isSelf = isSelf;
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
    
    public UserSimpleVO getToUser() {
        return toUser;
    }
    
    public void setToUser(UserSimpleVO toUser) {
        this.toUser = toUser;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Boolean getIsSelf() {
        return isSelf;
    }
    
    public void setIsSelf(Boolean isSelf) {
        this.isSelf = isSelf;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "MessageVO{" +
                "id=" + id +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", content='" + content + '\'' +
                ", isSelf=" + isSelf +
                ", createdAt=" + createdAt +
                '}';
    }
}