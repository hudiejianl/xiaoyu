package com.xiaoyua.dto.search;

import com.xiaoyua.dto.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * 搜索请求 DTO
 * 用于接收客户端的搜索参数，支持关键词搜索和类型筛选
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDTO extends PageDTO {
    
    /**
     * 搜索关键词
     * 必填，用于搜索用户、动态、任务等内容
     * 最小长度：1个字符
     * 最大长度：100个字符
     */
    @NotBlank(message = "搜索关键词不能为空")
    @Size(min = 1, max = 100, message = "搜索关键词长度必须在1-100个字符之间")
    private String keyword;
    
    /**
     * 搜索类型
     * 可选值：ALL(全部)、USER(用户)、POST(动态)、TASK(任务)、TOPIC(话题)、TAG(标签)
     * 默认值：ALL
     */
    @Pattern(regexp = "^(ALL|USER|POST|TASK|TOPIC|TAG)$", 
             message = "搜索类型只能是：ALL、USER、POST、TASK、TOPIC、TAG")
    private String type = "ALL";
    
    /**
     * 排序方式
     * 可选值：RELEVANCE(相关度)、TIME(时间)、HOT(热度)
     * 默认值：RELEVANCE
     */
    @Pattern(regexp = "^(RELEVANCE|TIME|HOT)$", 
             message = "排序方式只能是：RELEVANCE、TIME、HOT")
    private String sort = "RELEVANCE";
    
    /**
     * 时间范围筛选
     * 可选值：ALL(全部)、DAY(一天内)、WEEK(一周内)、MONTH(一月内)、YEAR(一年内)
     * 默认值：ALL
     */
    @Pattern(regexp = "^(ALL|DAY|WEEK|MONTH|YEAR)$", 
             message = "时间范围只能是：ALL、DAY、WEEK、MONTH、YEAR")
    private String timeRange = "ALL";
    
    /**
     * 校园ID筛选
     * 可选，用于筛选特定校园的内容
     */
    private Long campusId;
    
    /**
     * 标签ID筛选
     * 可选，用于筛选特定标签的内容
     */
    private Integer tagId;
    
    /**
     * 验证搜索参数
     * 确保参数在合理范围内
     */
    @Override
    public void validate() {
        super.validate();
        
        // 清理关键词前后空格
        if (keyword != null) {
            keyword = keyword.trim();
        }
        
        // 设置默认值
        if (type == null || type.trim().isEmpty()) {
            type = "ALL";
        }
        if (sort == null || sort.trim().isEmpty()) {
            sort = "RELEVANCE";
        }
        if (timeRange == null || timeRange.trim().isEmpty()) {
            timeRange = "ALL";
        }
    }
}