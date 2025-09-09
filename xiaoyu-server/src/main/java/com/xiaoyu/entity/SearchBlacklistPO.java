package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 搜索黑名单
 */
@Data
@TableName("search_blacklist")
public class SearchBlacklistPO {

    /** 屏蔽关键词 */
    @TableId
    private String keyword;
}