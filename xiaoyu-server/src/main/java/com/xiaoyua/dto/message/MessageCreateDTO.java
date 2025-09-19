package com.xiaoyua.dto.message;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 发送私信DTO
 * 
 * @author xiaoyu
 */
@Data
public class MessageCreateDTO {
    
    /**
     * 接收者ID
     */
    @NotNull(message = "接收者ID不能为空")
    private Long toId;
    
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;
    
    /**
     * 消息类型：TEXT/IMAGE/FILE，默认TEXT
     */
    private String messageType = "TEXT";
}
