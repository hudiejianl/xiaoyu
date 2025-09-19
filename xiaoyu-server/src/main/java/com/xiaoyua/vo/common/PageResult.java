package com.xiaoyua.vo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果 VO
 * 用于包装分页数据和分页信息
 * 
 * @param <T> 数据类型
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 分页信息
     */
    private PaginationVO pagination;
    
    /**
     * 构造分页结果
     * 
     * @param list 数据列表
     * @param page 当前页码
     * @param size 每页数量
     * @param total 总记录数
     */
    public PageResult(List<T> list, Integer page, Integer size, Long total) {
        this.list = list;
        this.pagination = new PaginationVO(page, size, total);
    }
    
    /**
     * 创建空的分页结果
     * 
     * @param page 当前页码
     * @param size 每页数量
     * @param <T> 数据类型
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty(Integer page, Integer size) {
        return new PageResult<>(List.of(), page, size, 0L);
    }
    
    /**
     * 创建分页结果
     * 
     * @param list 数据列表
     * @param page 当前页码
     * @param size 每页数量
     * @param total 总记录数
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> list, Integer page, Integer size, Long total) {
        return new PageResult<>(list, page, size, total);
    }
}