package com.xiaoyua.vo.topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 话题详细信息 VO
 * 用于返回话题的详细信息，包含话题名称、描述、动态数量等信息
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class TopicVO {
    
    /**
     * 话题ID
     */
    private Long id;
    
    /**
     * 话题名称
     * 以 # 开头的话题名称，如：#校园生活
     */
    private String name;
    
    /**
     * 话题描述
     */
    private String description;
    
    /**
     * 话题封面图URL
     */
    private String coverUrl;
    
    /**
     * 话题分类
     * 可选值：CAMPUS(校园)、LIFE(生活)、STUDY(学习)、ENTERTAINMENT(娱乐)、OTHER(其他)
     */
    private String category;
    
    /**
     * 动态数量
     * 该话题下的动态总数
     */
    private Integer postCount;
    
    /**
     * 参与用户数
     * 参与该话题讨论的用户总数
     */
    private Integer participantCount;
    
    /**
     * 今日新增动态数
     */
    private Integer todayPostCount;
    
    /**
     * 热度值
     * 基于动态数量、参与度和时间衰减计算的热度分数
     */
    private Integer hotScore;
    
    /**
     * 是否推荐
     * 0-不推荐，1-推荐
     */
    private Integer isRecommend;
    
    /**
     * 话题状态
     * ACTIVE-活跃，INACTIVE-不活跃，BANNED-已禁用
     */
    private String status;
    
    /**
     * 排序权重
     * 数值越大排序越靠前
     */
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * 构造函数
     */
//    public TopicVO() {}
//
    /**
     * 构造函数
     * 
     * @param id 话题ID
     * @param name 话题名称
     * @param description 话题描述
     * @param postCount 动态数量
     */
//    public TopicVO(Long id, String name, String description, Integer postCount) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.postCount = postCount;
//    }
    
    /**
     * 转换为简要信息VO
     * 
     * @return TopicSimpleVO
     */
//    public TopicSimpleVO toSimpleVO() {
//        return new TopicSimpleVO(this.id, this.name, this.description, this.postCount);
//    }
}