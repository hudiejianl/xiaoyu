package com.xiaoyua.dto.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 文件上传请求 DTO
 * 
 * @author xiaoyu
 */
@Data
public class FileUploadDTO {
    
    /**
     * 业务类型：AVATAR/BG/POST/TASK/COMMENT
     */
    @NotBlank(message = "业务类型不能为空")
    @Pattern(regexp = "^(AVATAR|BG|POST|TASK|COMMENT)$", message = "业务类型必须是：AVATAR/BG/POST/TASK/COMMENT")
    private String bizType;
    
}