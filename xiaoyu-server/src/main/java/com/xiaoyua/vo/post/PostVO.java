package com.xiaoyua.vo.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoyua.vo.user.UserSimpleVO;
import com.xiaoyua.vo.file.FileSimpleVO;
import com.xiaoyua.vo.topic.TopicSimpleVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动态信息VO
 * 用于返回动态的详细信息
 * 
 * @author xiaoyu
 */
@Data

public class PostVO {
    
    /**
     * 动态ID
     */
    private Long id;
    
    /**
     * 发布用户信息
     */
    private UserSimpleVO user;
    
    /**
     * 动态标题
     */
    private String title;
    
    /**
     * 动态内容
     */
    private String content;
    
    /**
     * 校区ID
     */
    private Long campusId;
    
    /**
     * 可见范围：PUBLIC公开 FRIEND好友 CAMPUS校园
     */
    private String visibility;
    
    /**
     * 位置名称
     */
    private String poiName;
    
    /**
     * 是否置顶：0否 1是
     */
    private Integer isTop;
    
    /**
     * 状态：DRAFT草稿 AUDITING审核中 PUBLISHED已发布 REJECTED已拒绝 DELETED已删除
     */
    private String status;
    
    /**
     * 文件列表
     */
    private List<FileSimpleVO> files;
    
    /**
     * 话题列表
     */
    private List<TopicSimpleVO> topics;
    
    /**
     * 统计信息
     */
    private PostStatsVO stats;
    
    /**
     * 用户操作状态
     */
    private PostUserActionsVO userActions;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /*public PostVO() {}
    
    public PostVO(Long id, UserSimpleVO user, String title, String content, Long campusId, String visibility, String poiName, 
                 Integer isTop, String status, List<FileSimpleVO> files, List<TopicSimpleVO> topics, 
                 PostStatsVO stats, PostUserActionsVO userActions, 
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.campusId = campusId;
        this.visibility = visibility;
        this.poiName = poiName;
        this.isTop = isTop;
        this.status = status;
        this.files = files;
        this.topics = topics;
        this.stats = stats;
        this.userActions = userActions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserSimpleVO getUser() {
        return user;
    }
    
    public void setUser(UserSimpleVO user) {
        this.user = user;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getCampusId() {
        return campusId;
    }
    
    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }
    
    public String getVisibility() {
        return visibility;
    }
    
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    
    public String getPoiName() {
        return poiName;
    }
    
    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }
    
    public Integer getIsTop() {
        return isTop;
    }
    
    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<FileSimpleVO> getFiles() {
        return files;
    }
    
    public void setFiles(List<FileSimpleVO> files) {
        this.files = files;
    }
    
    public List<TopicSimpleVO> getTopics() {
        return topics;
    }
    
    public void setTopics(List<TopicSimpleVO> topics) {
        this.topics = topics;
    }
    
    public PostStatsVO getStats() {
        return stats;
    }
    
    public void setStats(PostStatsVO stats) {
        this.stats = stats;
    }
    
    public PostUserActionsVO getUserActions() {
        return userActions;
    }
    
    public void setUserActions(PostUserActionsVO userActions) {
        this.userActions = userActions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "PostVO{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", campusId=" + campusId +
                ", visibility='" + visibility + '\'' +
                ", poiName='" + poiName + '\'' +
                ", isTop=" + isTop +
                ", status='" + status + '\'' +
                ", files=" + files +
                ", topics=" + topics +
                ", stats=" + stats +
                ", userActions=" + userActions +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    */
}