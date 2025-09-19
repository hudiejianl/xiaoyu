package com.xiaoyua.dto.friend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 私信发送请求DTO
 * 用于处理私信发送请求
 * 
 * @author xiaoyu
 */
public class MessageCreateDTO {
    
    /**
     * 接收者用户ID
     */
    @NotNull(message = "接收者用户ID不能为空")
    private Long toUserId;
    
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 500, message = "消息内容最多500个字符")
    private String content;
    
    public MessageCreateDTO() {}
    
    public MessageCreateDTO(Long toUserId, String content) {
        this.toUserId = toUserId;
        this.content = content;
    }
    
    public Long getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "MessageCreateDTO{" +
                "toUserId=" + toUserId +
                ", content='" + content + '\'' +
                '}';
    }
}