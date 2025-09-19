package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 敏感词库表
 */
@Data
@TableName("sensitive_words")
public class SensitiveWordPO{

    /** 敏感词 */
    @TableId
    private String word;

    /** 危险等级：1低 2中 3高 */
    private Integer level;
}