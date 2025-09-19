package com.xiaoyua.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 私信消息VO
 * 
 * @author xiaoyu
 */
@Data
public class MessageVO {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 发送者ID
     */
    @JsonProperty("from_id")
    private Long fromId;
    
    /**
     * 接收者ID
     */
    @JsonProperty("to_id")
    private Long toId;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型
     */
    @JsonProperty("message_type")
    private String messageType;
    
    /**
     * 消息状态
     */
    private String status;
    
    /**
     * 是否已读
     */
    @JsonProperty("is_read")
    private Integer isRead;
    
    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
