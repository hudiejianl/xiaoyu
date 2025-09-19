package com.xiaoyua.vo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页信息VO
 * <p>
 * 用于返回分页查询的元数据信息，包含当前页码、每页数量、总记录数等。
 * 配合PageResult使用，为客户端提供完整的分页信息。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * PaginationVO pagination = new PaginationVO(1, 20, 100L);
 * // 自动计算：pages=5, hasPrevious=false, hasNext=true
 * }</pre>
 * 
 * <h3>自动计算字段：</h3>
 * <ul>
 *   <li>总页数(pages)：根据总记录数和每页数量自动计算</li>
 *   <li>是否有上一页(hasPrevious)：当前页码大于1时为true</li>
 *   <li>是否有下一页(hasNext)：当前页码小于总页数时为true</li>
 * </ul>
 * 
 * <h3>分页规则：</h3>
 * <ul>
 *   <li>页码从1开始计数</li>
 *   <li>每页数量建议范围：1-100</li>
 *   <li>总页数向上取整</li>
 *   <li>空结果时总页数为0</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.common.PageResult
 * @see com.xiaoyua.dto.common.PageDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationVO {
    
    /**
     * 当前页码
     */
    private Integer page;
    
    /**
     * 每页数量
     */
    private Integer size;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Integer pages;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 构造分页信息
     * 
     * @param page 当前页码
     * @param size 每页数量
     * @param total 总记录数
     */
    public PaginationVO(Integer page, Integer size, Long total) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.pages = (int) Math.ceil((double) total / size);
        this.hasPrevious = page > 1;
        this.hasNext = page < pages;
    }
}