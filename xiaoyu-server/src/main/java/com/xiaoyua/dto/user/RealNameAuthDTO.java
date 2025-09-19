package com.xiaoyua.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 实名认证请求DTO
 * 用于接收用户实名认证信息
 * 
 * @author xiaoyu
 */
public class RealNameAuthDTO {
    
    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 30, message = "姓名最多30个字符")
    private String realName;
    
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确")
    private String idCardNo;
    
    public RealNameAuthDTO() {}
    
    public RealNameAuthDTO(String realName, String idCardNo) {
        this.realName = realName;
        this.idCardNo = idCardNo;
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
    
    @Override
    public String toString() {
        return "RealNameAuthDTO{" +
                "realName='" + realName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                '}';
    }
}