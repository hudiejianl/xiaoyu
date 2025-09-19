package com.xiaoyua.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 发送验证码请求DTO
 * 用于接收发送验证码的请求参数
 * 
 * @author xiaoyu
 */
public class SendCodeDTO {
    
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    
    /**
     * 验证码类型：LOGIN-登录，REGISTER-注册，RESET-重置密码
     */
    @NotBlank(message = "验证码类型不能为空")
    @Pattern(regexp = "^(LOGIN|REGISTER|RESET)$", message = "验证码类型不正确")
    private String type;
    
    public SendCodeDTO() {}
    
    public SendCodeDTO(String mobile, String type) {
        this.mobile = mobile;
        this.type = type;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "SendCodeDTO{" +
                "mobile='" + mobile + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}