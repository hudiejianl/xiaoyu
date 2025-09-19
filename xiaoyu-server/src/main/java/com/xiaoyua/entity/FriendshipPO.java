package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 好友关系表
 */
@Data
@TableName("friendships")
public class FriendshipPO {

    /** 好友关系 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 UID */
    private Long userId;

    /** 好友 UID */
    private Long friendId;

    /** 好友状态 */
    @EnumValue
    private Status status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, ACCEPTED, REFUSED
    }
}