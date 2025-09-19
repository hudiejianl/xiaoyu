package com.xiaoyua.vo.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签详细信息 VO
 * 用于返回标签的详细信息，包含标签名称、分类、热度等信息
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class TagVO {
    
    /**
     * 标签ID
     */
    private Integer id;
    
    /**
     * 标签名称
     */
    private String name;
    
    /**
     * 标签分类
     * 可选值：POST(动态标签)、TASK(任务标签)、USER(用户标签)、SYSTEM(系统标签)
     */
    private String category;
    
    /**
     * 标签颜色
     * 十六进制颜色值，如：#FF5722
     */
    private String color;
    
    /**
     * 标签描述
     */
    private String description;
    
    /**
     * 标签图标URL
     */
    private String iconUrl;
    
    /**
     * 使用次数
     * 该标签被使用的总次数
     */
    private Integer useCount;
    
    /**
     * 热度值
     * 基于使用频率和时间衰减计算的热度分数
     */
    private Integer hotScore;
    
    /**
     * 是否推荐
     * 0-不推荐，1-推荐
     */
    private Integer isRecommend;
    
    /**
     * 标签状态
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
    public TagVO() {}
    
    /**
     * 构造函数
     * 
     * @param id 标签ID
     * @param name 标签名称
     * @param category 标签分类
     * @param color 标签颜色
     * @param useCount 使用次数
     */
    public TagVO(Integer id, String name, String category, String color, Integer useCount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color = color;
        this.useCount = useCount;
    }
    
    /**
     * 转换为简要信息VO
     * 
     * @return TagSimpleVO
     */
    public TagSimpleVO toSimpleVO() {
        return new TagSimpleVO(this.id, this.name, this.category, this.color, this.useCount);
    }
}