package com.xiaoyua.vo.file;

import lombok.Data;

/**
 * 文件简要信息VO
 * 用于返回文件的基本信息
 * 
 * @author xiaoyu
 */
@Data

public class FileSimpleVO {
    
    /**
     * 文件ID
     */
    private Long id;
    
    /**
     * 文件URL
     */
    private String fileUrl;
    
    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
    
    /**
     * 文件类型：IMAGE图片 VIDEO视频 AUDIO音频 DOCUMENT文档
     */
    private String fileType;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 原始文件名
     */
    private String originalName;
}