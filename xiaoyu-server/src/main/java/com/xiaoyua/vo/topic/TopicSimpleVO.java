package com.xiaoyua.vo.topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 话题简要信息VO
 * 用于返回话题的基本信息
 * 
 * @author xiaoyu
 */
@Data

public class TopicSimpleVO {
    
    /**
     * 话题ID
     */
    private Long id;
    
    /**
     * 话题名称
     */
    private String name;
    
    /**
     * 话题描述
     */
    private String description;
    
    /**
     * 动态数量
     */
    @JsonProperty("post_cnt")
    private Integer postCount;
    
}