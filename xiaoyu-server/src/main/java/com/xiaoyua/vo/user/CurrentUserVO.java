package com.xiaoyua.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 当前用户详细信息VO
 * 用于返回当前登录用户的详细信息，包含私密信息
 * 继承自UserVO，添加了敏感信息字段
 * 
 * @author xiaoyu
 */
public class CurrentUserVO extends UserVO {
    
    /**
     * QQ授权openid
     */
    private String qqOpenid;
    
    /**
     * 实名姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCardNo;
    
    /**
     * 账号状态：0-正常，1-封号
     */
    private Integer status;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    public CurrentUserVO() {
        super();
    }
    
    public CurrentUserVO(Long id, String nickname, String avatarUrl, LocalDate birthday, String mobile, Integer gender,
                        Long campusId, String campusName, Integer isRealName, Integer privacyMobile,
                        Integer privacyBirthday, Integer privacyFans, LocalDateTime createdAt, UserStatsVO stats,
                        String qqOpenid, String realName, String idCardNo, Integer status,
                        LocalDateTime updatedAt) {
        super(id, nickname, avatarUrl, birthday, mobile, gender, campusId, campusName, isRealName,
              privacyMobile, privacyBirthday, privacyFans, createdAt, stats);
        this.qqOpenid = qqOpenid;
        this.realName = realName;
        this.idCardNo = idCardNo;
        this.status = status;
        this.updatedAt = updatedAt;
    }
    
    public String getQqOpenid() {
        return qqOpenid;
    }
    
    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid;
    }
    
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getIdCardNo() {
        return idCardNo;
    }
    
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "CurrentUserVO{" +
                "qqOpenid='" + qqOpenid + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", status=" + status +
                ", updatedAt=" + updatedAt +
                "} " + super.toString();
    }
}