package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 动态统计表
 */
@Data
@TableName("post_stats")
public class PostStatPO {

    /** 动态 ID */
    @TableId
    private Long postId;

    /** 浏览量 */
    private Integer viewCnt;

    /** 点赞数 */
    private Integer likeCnt;

    /** 收藏数 */
    @TableField("fav_cnt")
    private Integer favCnt;

    /** 评论数 */
    private Integer commentCnt;

    /** 转发数 */
    private Integer shareCnt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}