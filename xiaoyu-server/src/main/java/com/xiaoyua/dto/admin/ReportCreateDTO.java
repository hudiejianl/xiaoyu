package com.xiaoyua.dto.admin;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 举报提交请求DTO
 * 用于接收用户提交举报的请求数据
 * 
 * @author xiaoyu
 */
@Data
public class ReportCreateDTO {
    
    /**
     * 被举报对象ID
     */
    @NotNull(message = "被举报对象ID不能为空")
    private Long itemId;
    
    /**
     * 被举报对象类型：POST-动态，TASK-任务，COMMENT-评论，USER-用户
     */
    @NotBlank(message = "被举报对象类型不能为空")
    @Pattern(regexp = "^(POST|TASK|COMMENT|USER)$", message = "被举报对象类型不正确")
    private String itemType;
    
    /**
     * 举报理由
     */
    @NotBlank(message = "举报理由不能为空")
    @Size(max = 255, message = "举报理由最多255个字符")
    private String reason;
    
//    public ReportCreateDTO() {}
//
//    public ReportCreateDTO(Long itemId, String itemType, String reason) {
//        this.itemId = itemId;
//        this.itemType = itemType;
//        this.reason = reason;
//    }
//
//    public Long getItemId() {
//        return itemId;
//    }
//
//    public void setItemId(Long itemId) {
//        this.itemId = itemId;
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
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
//
//    @Override
//    public String toString() {
//        return "ReportCreateDTO{" +
//                "itemId=" + itemId +
//                ", itemType='" + itemType + '\'' +
//                ", reason='" + reason + '\'' +
//                '}';
//    }
}