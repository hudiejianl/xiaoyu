package com.xiaoyua.vo.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件详细信息 VO
 * 用于返回文件的完整信息
 * 
 * @author xiaoyu
 */
@Data
public class FileVO {
    
    /**
     * 文件ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Long userId;
    
    /**
     * 业务类型：AVATAR/BG/POST/TASK/COMMENT
     */
    @JsonProperty("biz_type")
    private String bizType;
    
    /**
     * 文件原始URL
     */
    @JsonProperty("file_url")
    private String fileUrl;
    
    /**
     * 缩略图URL
     */
    @JsonProperty("thumb_url")
    private String thumbUrl;
    
    /**
     * 文件大小（字节）
     */
    private Long size;
    
    /**
     * 上传时间
     */
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    
}