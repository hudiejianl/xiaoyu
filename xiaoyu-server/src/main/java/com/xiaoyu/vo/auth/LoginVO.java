package com.xiaoyu.vo.auth;

import com.xiaoyu.vo.user.UserSimpleVO;

/**
 * 登录响应VO
 * <p>
 * 用于返回登录成功后的用户信息和访问令牌。
 * 包含访问令牌、刷新令牌、过期时间和用户基本信息。
 * </p>
 * 
 * <h3>使用示例：</h3>
 * <pre>{@code
 * LoginVO loginVO = new LoginVO();
 * loginVO.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
 * loginVO.setRefreshToken("refresh_token_here");
 * loginVO.setExpiresIn(7200L); // 2小时
 * loginVO.setUser(userSimpleVO);
 * }</pre>
 * 
 * <h3>令牌说明：</h3>
 * <ul>
 *   <li>访问令牌：用于API调用的身份验证，有效期通常为2小时</li>
 *   <li>刷新令牌：用于获取新的访问令牌，有效期通常为30天</li>
 *   <li>过期时间：访问令牌的剩余有效时间（秒）</li>
 * </ul>
 * 
 * <h3>安全注意事项：</h3>
 * <ul>
 *   <li>访问令牌应存储在内存中，不要持久化到本地存储</li>
 *   <li>刷新令牌应安全存储，用于自动刷新访问令牌</li>
 *   <li>令牌泄露时应立即调用登出接口使令牌失效</li>
 * </ul>
 * 
 * @author xiaoyu
 * @since 1.0.0
 * @see com.xiaoyu.dto.auth.MobileLoginDTO
 * @see com.xiaoyu.dto.auth.QQLoginDTO
 * @see com.xiaoyu.vo.user.UserSimpleVO
 */
public class LoginVO {
    
    /**
     * 访问令牌
     * <p>
     * JWT格式的访问令牌，用于API调用时的身份验证。
     * 令牌包含用户ID、角色等信息，有效期通常为2小时。
     * </p>
     * 
     * @example "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     * <p>
     * 用于获取新访问令牌的刷新令牌。
     * 有效期通常为30天，用于自动续期访问令牌。
     * </p>
     * 
     * @example "refresh_token_abc123def456"
     */
    private String refreshToken;
    
    /**
     * 令牌过期时间（秒）
     * <p>
     * 访问令牌的剩余有效时间，单位为秒。
     * 客户端应在令牌过期前使用刷新令牌获取新的访问令牌。
     * </p>
     * 
     * @example 7200L (2小时)
     */
    private Long expiresIn;
    
    /**
     * 用户基本信息
     * <p>
     * 登录用户的基本信息，包含用户ID、昵称、头像等公开信息。
     * 不包含敏感信息如手机号、身份证号等。
     * </p>
     * 
     * @see com.xiaoyu.vo.user.UserSimpleVO
     */
    private UserSimpleVO user;
    
    public LoginVO() {}
    
    public LoginVO(String accessToken, String refreshToken, Long expiresIn, UserSimpleVO user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public UserSimpleVO getUser() {
        return user;
    }
    
    public void setUser(UserSimpleVO user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "LoginVO{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", user=" + user +
                '}';
    }
}