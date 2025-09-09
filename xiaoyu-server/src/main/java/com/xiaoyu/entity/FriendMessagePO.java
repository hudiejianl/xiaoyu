package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 好友私信表
 */
@Data
@TableName("friend_messages")
public class FriendMessagePO {

    /** 私信 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发送者 UID */
    private Long fromId;

    /** 接收者 UID */
    private Long toId;

    /** 消息内容 */
    private String content;

    /** 发送时间 */
    private LocalDateTime createdAt;
}