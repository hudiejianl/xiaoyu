package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 朋友圈动态表
 */
@Data
@TableName("posts")
public class PostPO {

    /** 动态 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发布者 UID */
    private Long userId;

    /** 文本内容 */
    private String content;

    /** 可见范围 */
    @EnumValue
    private Visibility visibility;

    /** 纬度 */
    private BigDecimal poiLat;

    /** 经度 */
    private BigDecimal poiLng;

    /** POI 名称 */
    private String poiName;

    /** 是否置顶：0否 1是 */
    private Integer isTop;

    /** 状态 */
    @EnumValue
    private Status status;

    /** 发布时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public enum Visibility {
        PUBLIC, FRIEND, CAMPUS
    }

    public enum Status {
        DRAFT, PUBLISHED, HIDDEN, AUDITING, REJECTED
    }
}