package com.xiaoyua.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 手机号登录请求DTO
 * <p>
 * 用于接收手机号和验证码登录信息，支持中国大陆手机号格式验证。
 * 验证码为6位数字，通过短信发送给用户。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * MobileLoginDTO loginDTO = new MobileLoginDTO();
 * loginDTO.setMobile("13812345678");
 * loginDTO.setCode("123456");
 * }</pre>
 * 
 * <h3>验证规则：</h3>
 * <ul>
 *   <li>手机号：必须为中国大陆手机号格式（1开头，第二位为3-9，共11位数字）</li>
 *   <li>验证码：必须为6位数字</li>
 * </ul>
 * 
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>验证码有效期通常为5分钟</li>
 *   <li>同一手机号1分钟内只能发送一次验证码</li>
 *   <li>验证码错误次数过多会被临时锁定</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.dto.auth.SendCodeDTO
 * @see com.xiaoyua.vo.auth.LoginVO
 */

public class MobileLoginDTO {
    
    /**
     * 手机号
     * <p>
     * 中国大陆手机号，格式为1开头的11位数字。
     * 第二位数字必须为3-9之间。
     * </p>
     * 
     * @example "13812345678"
     * @validation 不能为空，必须符合中国大陆手机号格式
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    
    /**
     * 验证码
     * <p>
     * 6位数字验证码，通过短信发送给用户。
     * 验证码有效期为5分钟，过期后需要重新获取。
     * </p>
     * 
     * @example "123456"
     * @validation 不能为空，必须为6位数字
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String code;
    
    public MobileLoginDTO() {}
    
    public MobileLoginDTO(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String toString() {
        return "MobileLoginDTO{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}