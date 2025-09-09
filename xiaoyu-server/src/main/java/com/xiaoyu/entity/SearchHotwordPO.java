package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 热搜词库表
 */
@Data
@TableName("search_hotwords")
public class SearchHotwordPO {

    /** 热词 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关键词 */
    private String keyword;

    /** 搜索次数 */
    private Integer searchCnt;

    /** 后台手动权重 */
    private Integer weight;

    /** 是否上榜：0否 1是 */
    private Integer isHot;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}