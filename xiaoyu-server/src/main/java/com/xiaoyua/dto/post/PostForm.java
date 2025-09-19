package com.xiaoyua.dto.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用于接收前端未分 part 的 multipart/form-data 表单：
 * - 普通文本字段直接作为表单字段提交
 * - 文件通过同名字段追加，如 files
 */
@Data
public class PostForm {
    private String title;
    private String content;
    private Long campusId;
    private String visibility; // PUBLIC/FRIEND/CAMPUS
    private BigDecimal poiLat;
    private BigDecimal poiLng;
    private String poiName;

    // 前端可多次 append("files", file)
    private List<MultipartFile> files;
}
