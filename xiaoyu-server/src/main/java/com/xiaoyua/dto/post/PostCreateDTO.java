package com.xiaoyua.dto.post;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建动态请求DTO
 * <p>
 * 用于接收用户发布朋友圈动态的请求数据。
 * 支持文本内容、图片/视频文件、位置信息、话题标签等多种内容形式。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * PostCreateDTO postDTO = new PostCreateDTO();
 * postDTO.setContent("今天天气真好！#美好生活");
 * postDTO.setVisibility("PUBLIC");
 * postDTO.setPoiName("北京大学");
 * postDTO.setFileIds(Arrays.asList(1L, 2L, 3L));
 * postDTO.setTopicNames(Arrays.asList("美好生活", "校园日常"));
 * postDTO.setTagIds(Arrays.asList(1, 2));
 * }</pre>
 * 
 * <h3>内容限制：</h3>
 * <ul>
 *   <li>文本内容：最多2000个字符</li>
 *   <li>图片文件：最多9张，支持JPG/PNG格式</li>
 *   <li>视频文件：最多1个，支持MP4格式，最大100MB</li>
 *   <li>位置信息：支持GPS坐标和位置名称</li>
 *   <li>话题标签：最多10个</li>
 * </ul>
 * 
 * <h3>可见范围说明：</h3>
 * <ul>
 *   <li>PUBLIC：所有用户可见</li>
 *   <li>FRIEND：仅好友可见</li>
 *   <li>CAMPUS：仅同校用户可见</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.post.PostVO
 * @see com.xiaoyua.dto.post.PostUpdateDTO
 */
@Data

public class PostCreateDTO {
    
    /**
     * 动态标题
     */
    @Size(max = 100, message = "动态标题最多100个字符")
    private String title;
    
    /**
     * 动态内容
     */
    @NotBlank(message = "动态内容不能为空")
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
    private String visibility = "PUBLIC";
    
    /**
     * 位置纬度
     */
    @DecimalMax(value = "90.0", message = "纬度范围不正确")
    @DecimalMin(value = "-90.0", message = "纬度范围不正确")
    private BigDecimal poiLat;
    
    /**
     * 位置经度
     */
    @DecimalMax(value = "180.0", message = "经度范围不正确")
    @DecimalMin(value = "-180.0", message = "经度范围不正确")
    private BigDecimal poiLng;
    
    /**
     * 位置名称
     */
    @Size(max = 100, message = "位置名称最多100个字符")
    private String poiName;
    
    /**
     * 文件ID列表
     */
    private List<Long> fileIds;
    
    /**
     * 话题名称列表
     */
    private List<String> topicNames;
    
    /**
     * 标签ID列表
     */
    private List<Integer> tagIds;
    
//    public PostCreateDTO() {}
//
//    public PostCreateDTO(String title, String content, Long campusId, String visibility, BigDecimal poiLat, BigDecimal poiLng,
//                        String poiName, List<Long> fileIds, List<String> topicNames, List<Integer> tagIds) {
//        this.title = title;
//        this.content = content;
//        this.campusId = campusId;
//        this.visibility = visibility;
//        this.poiLat = poiLat;
//        this.poiLng = poiLng;
//        this.poiName = poiName;
//        this.fileIds = fileIds;
//        this.topicNames = topicNames;
//        this.tagIds = tagIds;
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
//    public BigDecimal getPoiLat() {
//        return poiLat;
//    }
//
//    public void setPoiLat(BigDecimal poiLat) {
//        this.poiLat = poiLat;
//    }
//
//    public BigDecimal getPoiLng() {
//        return poiLng;
//    }
//
//    public void setPoiLng(BigDecimal poiLng) {
//        this.poiLng = poiLng;
//    }
//
//    public String getPoiName() {
//        return poiName;
//    }
//
//    public void setPoiName(String poiName) {
//        this.poiName = poiName;
//    }
//
//    public List<Long> getFileIds() {
//        return fileIds;
//    }
//
//    public void setFileIds(List<Long> fileIds) {
//        this.fileIds = fileIds;
//    }
//
//    public List<String> getTopicNames() {
//        return topicNames;
//    }
//
//    public void setTopicNames(List<String> topicNames) {
//        this.topicNames = topicNames;
//    }
//
//    public List<Integer> getTagIds() {
//        return tagIds;
//    }
//
//    public void setTagIds(List<Integer> tagIds) {
//        this.tagIds = tagIds;
//    }
//
//    @Override
//    public String toString() {
//        return "PostCreateDTO{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", campusId=" + campusId +
//                ", visibility='" + visibility + '\'' +
//                ", poiLat=" + poiLat +
//                ", poiLng=" + poiLng +
//                ", poiName='" + poiName + '\'' +
//                ", fileIds=" + fileIds +
//                ", topicNames=" + topicNames +
//                ", tagIds=" + tagIds +
//                '}';
//    }
}