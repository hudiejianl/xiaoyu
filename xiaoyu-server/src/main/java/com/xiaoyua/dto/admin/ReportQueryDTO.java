package com.xiaoyua.dto.admin;

import com.xiaoyua.dto.common.PageDTO;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 举报查询请求DTO
 * 用于接收举报列表查询的请求参数
 * 
 * @author xiaoyu
 */
@Data

public class ReportQueryDTO extends PageDTO {
    
    /**
     * 被举报对象类型筛选：POST-动态，TASK-任务，COMMENT-评论，USER-用户
     */
    @Pattern(regexp = "^(POST|TASK|COMMENT|USER)$", message = "被举报对象类型不正确")
    private String itemType;
    
    /**
     * 处理状态筛选：PENDING-待处理，ACCEPTED-已受理，REFUSED-已拒绝
     */
    @Pattern(regexp = "^(PENDING|ACCEPTED|REFUSED)$", message = "处理状态不正确")
    private String status;
    
    /**
     * 举报人ID筛选
     */
    private Long reporterId;
    
    /**
     * 排序方式：latest-最新，oldest-最早
     */
    private String sort = "latest";
//
//    public ReportQueryDTO() {}
//
//    public ReportQueryDTO(String itemType, String status, Long reporterId, String sort) {
//        this.itemType = itemType;
//        this.status = status;
//        this.reporterId = reporterId;
//        this.sort = sort;
//    }
//
//    public String getItemType() {
//        return itemType;
//    }
//
//    public void setItemType(String itemType) {
//        this.itemType = itemType;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Long getReporterId() {
//        return reporterId;
//    }
//
//    public void setReporterId(Long reporterId) {
//        this.reporterId = reporterId;
//    }
//
//    public String getSort() {
//        return sort;
//    }
//
//    public void setSort(String sort) {
//        this.sort = sort;
//    }
//
//    @Override
//    public String toString() {
//        return "ReportQueryDTO{" +
//                "itemType='" + itemType + '\'' +
//                ", status='" + status + '\'' +
//                ", reporterId=" + reporterId +
//                ", sort='" + sort + '\'' +
//                ", page=" + getPage() +
//                ", size=" + getSize() +
//                '}';
//    }
}