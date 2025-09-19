package com.xiaoyua.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新Token请求DTO
 * 用于接收刷新访问令牌的请求参数
 * 
 * @author xiaoyu
 */
public class RefreshTokenDTO {
    
    /**
     * 刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
    
    public RefreshTokenDTO() {}
    
    public RefreshTokenDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    @Override
    public String toString() {
        return "RefreshTokenDTO{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}