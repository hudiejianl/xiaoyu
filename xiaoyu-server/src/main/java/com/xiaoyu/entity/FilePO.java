package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件资源表
 */
@Data
@TableName("files")
public class FilePO {

    /** 文件主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 上传者 UID */
    private Long userId;

    /** 业务类型 */
    @EnumValue
    private BizType bizType;

    /** OSS 原图地址 */
    private String fileUrl;

    /** CDN 缩略图地址 */
    private String thumbUrl;

    /** 文件大小（字节） */
    private Integer size;

    /** 上传时间 */
    private LocalDateTime createdAt;

    public enum BizType {
        AVATAR, BG, POST, TASK, COMMENT
    }
}