package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 标签与业务对象关联表
 */
@Data
@TableName("tag_items")
public class TagItemPO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签 ID */
    private Integer tagId;

    /** 业务对象 ID */
    private Long itemId;

    /** 业务类型 */
    @EnumValue
    private ItemType itemType;

    public enum ItemType {
        POST, TASK
    }
}