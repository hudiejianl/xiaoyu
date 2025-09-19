package com.xiaoyua.dto.auth;

import lombok.Data;

/**
 * 用户名密码登录请求DTO
 */
@Data
public class PasswordLoginDTO {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}
