package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 主页访问日志表
 */
@Data
@TableName("visit_logs")
public class VisitLogPO {

    /** 日志 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 被访问者 UID */
    private Long userId;

    /** 访问者 UID */
    private Long visitorId;

    /** 访问时间 */
    private LocalDateTime visitTime;
}