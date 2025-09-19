package com.xiaoyua.dto.user;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * 用户信息更新请求DTO
 * 用于接收用户信息更新请求数据
 * 
 * @author xiaoyu
 */
public class UserUpdateDTO {
    
    /**
     * 昵称
     */
    @Size(max = 30, message = "昵称最多30个字符")
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatarUrl;
    
    /**
     * 生日
     */
    @Past(message = "生日必须是过去的日期")
    private LocalDate birthday;
    
    /**
     * 性别：0未知 1男 2女
     */
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer gender;
    
    /**
     * 所属校区ID
     */
    private Long campusId;
    
    /**
     * 手机号可见范围：0公开 1好友 2仅自己
     */
    @Min(value = 0, message = "隐私设置值不正确")
    @Max(value = 2, message = "隐私设置值不正确")
    private Integer privacyMobile;
    
    /**
     * 生日可见范围：0公开 1好友 2仅自己
     */
    @Min(value = 0, message = "隐私设置值不正确")
    @Max(value = 2, message = "隐私设置值不正确")
    private Integer privacyBirthday;
    
    /**
     * 粉丝列表可见范围：0公开 1好友 2仅自己
     */
    @Min(value = 0, message = "隐私设置值不正确")
    @Max(value = 2, message = "隐私设置值不正确")
    private Integer privacyFans;
    
    public UserUpdateDTO() {}
    
    public UserUpdateDTO(String nickname, String avatarUrl, LocalDate birthday, Integer gender, 
                        Long campusId, Integer privacyMobile, Integer privacyBirthday, Integer privacyFans) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.birthday = birthday;
        this.gender = gender;
        this.campusId = campusId;
        this.privacyMobile = privacyMobile;
        this.privacyBirthday = privacyBirthday;
        this.privacyFans = privacyFans;
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
    
    @Override
    public String toString() {
        return "UserUpdateDTO{" +
                "nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", campusId=" + campusId +
                ", privacyMobile=" + privacyMobile +
                ", privacyBirthday=" + privacyBirthday +
                ", privacyFans=" + privacyFans +
                '}';
    }
}