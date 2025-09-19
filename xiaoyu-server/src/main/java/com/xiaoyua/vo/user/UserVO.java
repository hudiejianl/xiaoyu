package com.xiaoyua.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户详细信息VO
 * 用于返回用户的公开详细信息，根据隐私设置控制信息可见性
 * 
 * @author xiaoyu
 */
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatarUrl;
    
    /**
     * 生日（根据隐私设置可能为空）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    /**
     * 手机号（根据隐私设置可能为空）
     */
    private String mobile;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 校园ID
     */
    private Long campusId;
    
    /**
     * 校园名称
     */
    private String campusName;
    
    /**
     * 是否实名认证：0-未认证，1-已认证
     */
    private Integer isRealName;
    
    /**
     * 手机号可见范围：0-公开，1-好友，2-仅自己
     */
    private Integer privacyMobile;
    
    /**
     * 生日可见范围：0-公开，1-好友，2-仅自己
     */
    private Integer privacyBirthday;
    
    /**
     * 粉丝列表可见范围：0-公开，1-好友，2-仅自己
     */
    private Integer privacyFans;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 用户统计信息
     */
    private UserStatsVO stats;
    
    public UserVO() {}
    
    public UserVO(Long id, String nickname, String avatarUrl, LocalDate birthday, String mobile, Integer gender,
                 Long campusId, String campusName, Integer isRealName, Integer privacyMobile,
                 Integer privacyBirthday, Integer privacyFans, LocalDateTime createdAt, UserStatsVO stats) {
        this.id = id;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.birthday = birthday;
        this.mobile = mobile;
        this.gender = gender;
        this.campusId = campusId;
        this.campusName = campusName;
        this.isRealName = isRealName;
        this.privacyMobile = privacyMobile;
        this.privacyBirthday = privacyBirthday;
        this.privacyFans = privacyFans;
        this.createdAt = createdAt;
        this.stats = stats;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public Integer getGender() {
        return gender;
    }
    
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    public Long getCampusId() {
        return campusId;
    }
    
    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }
    
    public String getCampusName() {
        return campusName;
    }
    
    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }
    
    public Integer getIsRealName() {
        return isRealName;
    }
    
    public void setIsRealName(Integer isRealName) {
        this.isRealName = isRealName;
    }
    
    public Integer getPrivacyMobile() {
        return privacyMobile;
    }
    
    public void setPrivacyMobile(Integer privacyMobile) {
        this.privacyMobile = privacyMobile;
    }
    
    public Integer getPrivacyBirthday() {
        return privacyBirthday;
    }
    
    public void setPrivacyBirthday(Integer privacyBirthday) {
        this.privacyBirthday = privacyBirthday;
    }
    
    public Integer getPrivacyFans() {
        return privacyFans;
    }
    
    public void setPrivacyFans(Integer privacyFans) {
        this.privacyFans = privacyFans;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public UserStatsVO getStats() {
        return stats;
    }
    
    public void setStats(UserStatsVO stats) {
        this.stats = stats;
    }
    
    @Override
    public String toString() {
        return "UserVO{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", birthday=" + birthday +
                ", mobile='" + mobile + '\'' +
                ", gender=" + gender +
                ", campusId=" + campusId +
                ", campusName='" + campusName + '\'' +
                ", isRealName=" + isRealName +
                ", privacyMobile=" + privacyMobile +
                ", privacyBirthday=" + privacyBirthday +
                ", privacyFans=" + privacyFans +
                ", createdAt=" + createdAt +
                ", stats=" + stats +
                '}';
    }
}