package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 个人主页统计表
 */
@Data
@TableName("homepages")
public class HomepagePO {

    /** 用户 ID */
    @TableId
    private Long userId;

    /** 被访问次数 */
    private Integer visitCnt;

    /** 最近一次访问时间 */
    private LocalDateTime lastVisit;
}