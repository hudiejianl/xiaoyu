package com.xiaoyua.dto.friend;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 好友申请请求DTO
 * 用于处理好友申请请求
 * 
 * @author xiaoyu
 */
public class FriendRequestDTO {
    
    /**
     * 目标用户ID
     */
    @NotNull(message = "目标用户ID不能为空")
    private Long targetUserId;
    
    /**
     * 验证消息
     */
    @Size(max = 100, message = "验证消息最多100个字符")
    private String message;
    
    public FriendRequestDTO() {}
    
    public FriendRequestDTO(Long targetUserId, String message) {
        this.targetUserId = targetUserId;
        this.message = message;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "FriendRequestDTO{" +
                "targetUserId=" + targetUserId +
                ", message='" + message + '\'' +
                '}';
    }
}