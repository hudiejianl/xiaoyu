package com.xiaoyua.vo.search;

import lombok.Data;

/**
 * 搜索统计信息VO
 * 用于返回搜索相关的统计数据
 * 
 * @author xiaoyu
 */
@Data

public class SearchStatsVO {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 搜索次数
     */
    private Integer searchCount;
    
    /**
     * 搜索结果数量
     */
    private Integer resultCount;
    
    /**
     * 今日搜索次数
     */
    private Integer todaySearchCount;
    
    /**
     * 热度排名
     */
    private Integer hotRank;
    
    /**
     * 是否为热搜词
     */
    private Boolean isHot;
    
//    public SearchStatsVO() {}
//
//    public SearchStatsVO(String keyword, Integer searchCount, Integer resultCount,
//                        Integer todaySearchCount, Integer hotRank, Boolean isHot) {
//        this.keyword = keyword;
//        this.searchCount = searchCount;
//        this.resultCount = resultCount;
//        this.todaySearchCount = todaySearchCount;
//        this.hotRank = hotRank;
//        this.isHot = isHot;
//    }
//
//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }
//
//    public Integer getSearchCount() {
//        return searchCount;
//    }
//
//    public void setSearchCount(Integer searchCount) {
//        this.searchCount = searchCount;
//    }
//
//    public Integer getResultCount() {
//        return resultCount;
//    }
//
//    public void setResultCount(Integer resultCount) {
//        this.resultCount = resultCount;
//    }
//
//    public Integer getTodaySearchCount() {
//        return todaySearchCount;
//    }
//
//    public void setTodaySearchCount(Integer todaySearchCount) {
//        this.todaySearchCount = todaySearchCount;
//    }
//
//    public Integer getHotRank() {
//        return hotRank;
//    }
//
//    public void setHotRank(Integer hotRank) {
//        this.hotRank = hotRank;
//    }
//
//    public Boolean getIsHot() {
//        return isHot;
//    }
//
//    public void setIsHot(Boolean isHot) {
//        this.isHot = isHot;
//    }
//
//    @Override
//    public String toString() {
//        return "SearchStatsVO{" +
//                "keyword='" + keyword + '\'' +
//                ", searchCount=" + searchCount +
//                ", resultCount=" + resultCount +
//                ", todaySearchCount=" + todaySearchCount +
//                ", hotRank=" + hotRank +
//                ", isHot=" + isHot +
//                '}';
//    }
}