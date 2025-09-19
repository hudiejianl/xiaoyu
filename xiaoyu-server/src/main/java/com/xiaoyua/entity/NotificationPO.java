package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息通知表
 */
@Data
@TableName("notifications")
public class NotificationPO {

    /** 通知 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 接收者 UID */
    private Long userId;

    /** 通知类型 */
    @EnumValue
    private Type type;

    /** 标题 */
    private String title;

    /** 正文 */
    private String content;

    /** 关联业务对象 ID */
    private Long refId;

    /** 关联业务类型 */
    @EnumValue
    private RefType refType;

    /** 阅读状态 */
    @EnumValue
    private Status status;

    /** 通知时间 */
    private LocalDateTime createdAt;

    public enum Type {
        LIKE, FAVORITE, COMMENT, SHARE, TASK_ORDER, SYSTEM, VIOLATION
    }

    public enum RefType {
        POST, TASK, COMMENT
    }

    public enum Status {
        UNREAD, READ
    }
}