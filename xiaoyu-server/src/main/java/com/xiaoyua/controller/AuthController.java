package com.xiaoyua.controller;

import com.xiaoyua.dto.auth.PasswordLoginDTO;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.AuthService;
import com.xiaoyua.vo.auth.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 用户名密码登录
     * @param loginDTO 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginVO> passwordLogin(@RequestBody PasswordLoginDTO loginDTO) {
        try {
            // 基本参数验证
            if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (loginDTO.getUsername().length() < 3 || loginDTO.getUsername().length() > 50) {
                return Result.error("用户名长度必须在3-50个字符之间");
            }
            if (loginDTO.getPassword().length() < 6 || loginDTO.getPassword().length() > 20) {
                return Result.error("密码长度必须在6-20个字符之间");
            }

            LoginVO loginVO = authService.passwordLogin(loginDTO);
            log.info("用户登录成功: {}", loginDTO.getUsername());
            return Result.success(loginVO);
        } catch (Exception e) {
            log.error("用户登录失败: {}, 错误: {}", loginDTO.getUsername(), e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<LoginVO> register(@RequestParam String username, 
                                   @RequestParam String password, 
                                   @RequestParam String nickname) {
        try {
            // 基本参数验证
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (nickname == null || nickname.trim().isEmpty()) {
                return Result.error("昵称不能为空");
            }
            if (username.length() < 3 || username.length() > 50) {
                return Result.error("用户名长度必须在3-50个字符之间");
            }
            if (password.length() < 6 || password.length() > 20) {
                return Result.error("密码长度必须在6-20个字符之间");
            }
            if (nickname.length() > 30) {
                return Result.error("昵称长度不能超过30个字符");
            }

            LoginVO loginVO = authService.register(username, password, nickname);
            log.info("用户注册成功: {}", username);
            return Result.success(loginVO);
        } catch (Exception e) {
            log.error("用户注册失败: {}, 错误: {}", username, e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
