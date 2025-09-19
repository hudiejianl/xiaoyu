package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论表
 */
@Data
@TableName("comments")
public class CommentPO {

    /** 评论 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 评论者 UID */
    private Long userId;

    /** 业务对象 ID */
    private Long itemId;

    /** 业务类型 */
    @EnumValue
    private ItemType itemType;

    /** 父评论 ID，0 为一级 */
    private Long parentId;

    /** 评论内容 */
    private String content;

    /** @用户 JSON 数组 */
    private String atUsers;

    /** 可见状态 */
    @EnumValue
    private Status status;

    /** 评论时间 */
    private LocalDateTime createdAt;

    public enum ItemType {
        POST, TASK
    }

    public enum Status {
        VISIBLE, HIDDEN, AUDITING, REJECTED
    }
}