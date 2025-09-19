package com.xiaoyua.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * QQ登录请求DTO
 * <p>
 * 用于接收QQ第三方登录的授权码和状态参数。
 * 基于OAuth 2.0协议，通过QQ互联平台进行用户身份验证。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * QQLoginDTO qqLoginDTO = new QQLoginDTO();
 * qqLoginDTO.setCode("authorization_code_from_qq");
 * qqLoginDTO.setState("random_state_string");
 * }</pre>
 * 
 * <h3>OAuth 2.0 流程：</h3>
 * <ol>
 *   <li>用户点击QQ登录，跳转到QQ授权页面</li>
 *   <li>用户授权后，QQ回调返回授权码(code)和状态参数(state)</li>
 *   <li>客户端将code和state发送到后端</li>
 *   <li>后端使用code换取access_token，获取用户信息</li>
 * </ol>
 * 
 * <h3>安全注意事项：</h3>
 * <ul>
 *   <li>state参数用于防止CSRF攻击，必须验证一致性</li>
 *   <li>授权码有效期很短（通常10分钟），需要及时使用</li>
 *   <li>授权码只能使用一次，重复使用会失败</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyua.vo.auth.LoginVO
 * @see com.xiaoyua.dto.auth.MobileLoginDTO
 */
public class QQLoginDTO {
    
    /**
     * QQ授权码
     * <p>
     * 用户在QQ授权页面同意授权后，QQ服务器返回的临时授权码。
     * 该授权码用于换取access_token，有效期通常为10分钟，只能使用一次。
     * </p>
     * 
     * @example "A1B2C3D4E5F6G7H8I9J0"
     * @validation 不能为空
     */
    @NotBlank(message = "授权码不能为空")
    private String code;
    
    /**
     * 状态参数
     * <p>
     * 用于防止CSRF攻击的随机字符串。
     * 客户端在发起授权请求时生成，QQ服务器会原样返回。
     * 后端需要验证该参数与发起请求时的值是否一致。
     * </p>
     * 
     * @example "random_state_abc123"
     * @validation 不能为空
     */
    @NotBlank(message = "状态参数不能为空")
    private String state;
    
    public QQLoginDTO() {}
    
    public QQLoginDTO(String code, String state) {
        this.code = code;
        this.state = state;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        return "QQLoginDTO{" +
                "code='" + code + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}