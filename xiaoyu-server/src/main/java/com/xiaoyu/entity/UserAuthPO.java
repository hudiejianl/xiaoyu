package com.xiaoyu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户授权表
 */
@Data
@TableName("user_auths")
public class UserAuthPO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 ID */
    private Long userId;

    /** 授权方式 */
    @EnumValue
    private IdentityType identityType;

    /** 唯一标识：openid/手机号 */
    private String identifier;

    /** 密码或 access_token */
    private String credential;

    /** 验证通过时间 */
    private LocalDateTime verifiedAt;

    public enum IdentityType {
        QQ, WECHAT, MOBILE, APPLE
    }
}