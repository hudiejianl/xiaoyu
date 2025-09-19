package com.xiaoyua.vo.search;

import com.xiaoyua.vo.user.UserSimpleVO;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.task.TaskVO;
import com.xiaoyua.vo.topic.TopicSimpleVO;
import com.xiaoyua.vo.tag.TagSimpleVO;
import lombok.Data;

import java.util.List;

/**
 * 搜索结果 VO
 * 用于返回综合搜索结果，包含用户、动态、任务等不同类型的搜索结果
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class SearchResultVO {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 搜索类型
     */
    private String type;
    
    /**
     * 总结果数量
     */
    private Long totalCount;
    
    /**
     * 用户搜索结果
     * 当搜索类型为 ALL 或 USER 时返回
     */
    private SearchResultSection<UserSimpleVO> users;
    
    /**
     * 动态搜索结果
     * 当搜索类型为 ALL 或 POST 时返回
     */
    private SearchResultSection<PostVO> posts;
    
    /**
     * 任务搜索结果
     * 当搜索类型为 ALL 或 TASK 时返回
     */
    private SearchResultSection<TaskVO> tasks;
    
    /**
     * 话题搜索结果
     * 当搜索类型为 ALL 或 TOPIC 时返回
     */
    private SearchResultSection<TopicSimpleVO> topics;
    
    /**
     * 标签搜索结果
     * 当搜索类型为 ALL 或 TAG 时返回
     */
    private SearchResultSection<TagSimpleVO> tags;
    
    /**
     * 搜索结果分组
     * 用于包装每种类型的搜索结果
     * 
     * @param <T> 结果数据类型
     */
    @Data
    public static class SearchResultSection<T> {
        
        /**
         * 该类型的总数量
         */
        private Long count;
        
        /**
         * 结果列表
         */
        private List<T> list;
        
        /**
         * 是否还有更多结果
         */
        private Boolean hasMore;
        
        public SearchResultSection() {}
        
        public SearchResultSection(Long count, List<T> list, Boolean hasMore) {
            this.count = count;
            this.list = list;
            this.hasMore = hasMore;
        }
    }
    
    /**
     * 构造函数
     */
    public SearchResultVO() {}
    
    /**
     * 构造函数
     * 
     * @param keyword 搜索关键词
     * @param type 搜索类型
     */
    public SearchResultVO(String keyword, String type) {
        this.keyword = keyword;
        this.type = type;
        this.totalCount = 0L;
    }
    
    /**
     * 计算总结果数量
     */
    public void calculateTotalCount() {
        long total = 0L;
        
        if (users != null && users.getCount() != null) {
            total += users.getCount();
        }
        if (posts != null && posts.getCount() != null) {
            total += posts.getCount();
        }
        if (tasks != null && tasks.getCount() != null) {
            total += tasks.getCount();
        }
        if (topics != null && topics.getCount() != null) {
            total += topics.getCount();
        }
        if (tags != null && tags.getCount() != null) {
            total += tags.getCount();
        }
        
        this.totalCount = total;
    }
}