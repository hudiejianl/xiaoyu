package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 话题字典表
 */
@Data
@TableName("topics")
public class TopicPO {

    /** 话题 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 话题名称（带 #） */
    private String name;

    /** 话题描述 */
    private String description;

    /** 关联动态数 */
    private Integer postCnt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}