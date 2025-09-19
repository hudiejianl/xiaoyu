package com.xiaoyua.dto.admin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统公告创建请求DTO
 * 用于接收管理员发布系统公告的请求数据
 * 
 * @author xiaoyu
 */
@Data

public class SystemNoticeCreateDTO {
    
    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @Size(max = 100, message = "公告标题最多100个字符")
    private String title;
    
    /**
     * 公告内容
     */
    @NotBlank(message = "公告内容不能为空")
    private String content;
    
    /**
     * 生效开始时间
     */
    @NotNull(message = "生效开始时间不能为空")
    @Future(message = "生效开始时间必须是未来时间")
    private LocalDateTime startTime;
    
    /**
     * 生效结束时间
     */
    @NotNull(message = "生效结束时间不能为空")
    @Future(message = "生效结束时间必须是未来时间")
    private LocalDateTime endTime;
    
//    public SystemNoticeCreateDTO() {}
//
//    public SystemNoticeCreateDTO(String title, String content, LocalDateTime startTime, LocalDateTime endTime) {
//        this.title = title;
//        this.content = content;
//        this.startTime = startTime;
//        this.endTime = endTime;
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
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(LocalDateTime endTime) {
//        this.endTime = endTime;
//    }
//
//    @Override
//    public String toString() {
//        return "SystemNoticeCreateDTO{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", startTime=" + startTime +
//                ", endTime=" + endTime +
//                '}';
//    }
}