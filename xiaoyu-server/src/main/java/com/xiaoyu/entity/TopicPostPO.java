package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 话题-动态关联表
 */
@Data
@TableName("topic_posts")
public class TopicPostPO {

    /** 话题 ID */
    @TableId
    private Long topicId;

    /** 动态 ID */
    @TableId
    private Long postId;
}