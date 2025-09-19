package com.xiaoyua.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户主表
 */
@Data
@TableName("users")
public class UserPO {

    /** 用户主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（用于登录） */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 头像 OSS 地址 */
    private String avatarUrl;

    /** 生日 */
    private LocalDateTime birthday;

    /** 性别：0未知 1男 2女 */
    private Integer gender;

    /** 所属校区 ID */
    private Long campusId;

    /** QQ 授权 openid */
    private String qqOpenid;

    /** 手机号 */
    private String mobile;

    /** 实名姓名 */
    private String realName;

    /** 身份证号 */
    private String idCardNo;

    /** 是否已实名：0否 1是 */
    private Integer isRealName;

    /** 手机号可见范围：0公开 1好友 2仅自己 */
    private Integer privacyMobile;

    /** 生日可见范围 */
    private Integer privacyBirthday;

    /** 粉丝列表可见范围 */
    private Integer privacyFans;

    /** 账号状态：0正常 1封号 */
    private Integer status;

    /** 注册时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}