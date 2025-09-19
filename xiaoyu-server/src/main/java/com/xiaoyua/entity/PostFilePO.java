package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 动态-文件关联表
 */
@Data
@TableName("post_files")
public class PostFilePO {

    /** 动态 ID */
    @TableId
    private Long postId;

    /** 文件 ID */
    private Long fileId;

    /** 顺序号 */
    private Integer sort;
}