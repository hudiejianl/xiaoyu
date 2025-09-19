package com.xiaoyua.dto.post;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 更新动态请求DTO
 * 用于接收动态更新请求数据
 * 
 * @author xiaoyu
 */
@Data

public class PostUpdateDTO {
    
    /**
     * 动态标题
     */
    @Size(max = 100, message = "动态标题最多100个字符")
    private String title;
    
    /**
     * 动态内容
     */
    @Size(max = 2000, message = "动态内容最多2000个字符")
    private String content;
    
    /**
     * 校区ID
     */
    private Long campusId;
    
    /**
     * 可见范围：PUBLIC公开 FRIEND好友 CAMPUS校园
     */
    @Pattern(regexp = "^(PUBLIC|FRIEND|CAMPUS)$", message = "可见范围值不正确")
    private String visibility;
    
    /**
     * 是否置顶：0否 1是
     */
    @Min(value = 0, message = "置顶设置值不正确")
    @Max(value = 1, message = "置顶设置值不正确")
    private Integer isTop;
    
//    public PostUpdateDTO() {}
//
//    public PostUpdateDTO(String title, String content, Long campusId, String visibility, Integer isTop) {
//        this.title = title;
//        this.content = content;
//        this.campusId = campusId;
//        this.visibility = visibility;
//        this.isTop = isTop;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
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
//    public String getVisibility() {
//        return visibility;
//    }
//
//    public void setVisibility(String visibility) {
//        this.visibility = visibility;
//    }
//
//    public Integer getIsTop() {
//        return isTop;
//    }
//
//    public void setIsTop(Integer isTop) {
//        this.isTop = isTop;
//    }
//
//    @Override
//    public String toString() {
//        return "PostUpdateDTO{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", campusId=" + campusId +
//                ", visibility='" + visibility + '\'' +
//                ", isTop=" + isTop +
//                '}';
//    }
}