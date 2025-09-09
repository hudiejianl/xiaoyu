package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 黑名单
 */
@Data
@TableName("blacklists")
public class BlacklistPO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 拉黑者 UID */
    private Long ownerId;

    /** 被拉黑者 UID */
    private Long targetId;

    /** 拉黑时间 */
    private LocalDateTime createdAt;
}