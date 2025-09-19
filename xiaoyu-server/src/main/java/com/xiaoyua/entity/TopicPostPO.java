package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 话题-动态关联表
 */
@Data
@TableName("topic_posts")
public class TopicPostPO {

    /** 主键 ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 话题 ID */
    @TableField("topic_id")
    private Long topicId;

    /** 动态 ID */
    @TableField("post_id")
    private Long postId;
}