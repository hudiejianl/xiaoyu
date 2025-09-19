package com.xiaoyua.dto.common;

import lombok.Data;

/**
 * 分页请求 DTO
 * 用于接收客户端的分页参数
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class PageDTO {
    
    /**
     * 页码，从1开始
     * 默认值：1
     * 最小值：1
     * 最大值：10000
     */
    private Integer page = 1;
    
    /**
     * 每页数量
     * 默认值：20
     * 最小值：1
     * 最大值：100
     */
    private Integer size = 20;
    
    /**
     * 获取偏移量
     * 用于数据库查询的 OFFSET 参数
     * 
     * @return 偏移量
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
    
    /**
     * 验证并修正分页参数
     * 确保参数在合理范围内
     */
    public void validate() {
        if (page == null || page < 1) {
            page = 1;
        }
        if (page > 10000) {
            page = 10000;
        }
        if (size == null || size < 1) {
            size = 20;
        }
        if (size > 100) {
            size = 100;
        }
    }
}