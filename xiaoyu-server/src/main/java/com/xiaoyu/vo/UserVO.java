package com.xiaoyu.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户公开信息 VO
 * 对应 GET /api/users/{user_id}
 */
@Data
public class UserVO {

    /** 用户ID */
    private Long id;

    /** 用户昵称 */
    private String nickname;

    /** 头像OSS地址 */
    private String avatarUrl;

    /** 生日 */
    private LocalDateTime birthday;

    /** 性别：0未知 1男 2女 */
    private Integer gender;

    /** 校区ID */
    private Long campusId;

    /** 校区名称 */
    private String campusName;

    /** 是否已实名：0否 1是 */
    private Integer isRealName;

    /** 手机号可见范围：0公开 1好友 2仅自己 */
    private Integer privacyMobile;

    /** 生日可见范围 */
    private Integer privacyBirthday;

    /** 粉丝列表可见范围 */
    private Integer privacyFans;

    /** 注册时间 */
    private LocalDateTime createdAt;

    /** 用户统计信息 */
    private UserStatsVO stats;

    /* —— 内部静态类 —— */
    @Data
    public static class UserStatsVO {
        /** 动态数 */
        private Integer postCount;
        /** 粉丝数 */
        private Integer followerCount;
        /** 关注数 */
        private Integer followingCount;
    }
}