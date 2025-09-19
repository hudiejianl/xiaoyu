package com.xiaoyua.dto.post;

import com.xiaoyua.dto.common.PageDTO;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 动态查询请求DTO
 * 用于接收动态查询请求参数
 * 
 * @author xiaoyu
 */
@Data
public class PostQueryDTO extends PageDTO {
    
    /**
     * 查询类型：timeline时间线 user用户 campus校园 hot热门
     */
    @Pattern(regexp = "^(timeline|user|campus|hot)$", message = "查询类型值不正确")
    private String type;
    
    /**
     * 用户ID（当type为user时使用）
     */
    private Long userId;

    /**
     * 校园ID(当type为campus时使用)
     */
    private Long campusId;

    /**
     * 排序方式：hot热门 latest最新
     */
    @Pattern(regexp = "^(hot|latest)$", message = "排序方式值不正确")
    private String sort = "latest";
    
//    public PostQueryDTO() {}
//
//    public PostQueryDTO(String type, Long userId, String sort) {
//        this.type = type;
//        this.userId = userId;
//        this.sort = sort;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
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
//    public Long getCampusId() {
//        return campusId;
//    }
//
//    public void setCampusId(Long campusId) {
//        this.campusId = campusId;
//    }
//
//    @Override
//    public String toString() {
//        return "PostQueryDTO{" +
//                "type='" + type + '\'' +
//                ", userId=" + userId +
//                ", sort='" + sort + '\'' +
//                ", page=" + getPage() +
//                ", size=" + getSize() +
//                '}';
//    }
}