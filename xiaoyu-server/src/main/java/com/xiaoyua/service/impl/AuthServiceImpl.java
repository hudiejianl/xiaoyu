package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoyua.dto.auth.PasswordLoginDTO;
import com.xiaoyua.entity.UserAuthPO;
import com.xiaoyua.entity.UserPO;
import com.xiaoyua.mapper.UserAuthMapper;
import com.xiaoyua.mapper.UserMapper;
import com.xiaoyua.service.AuthService;
import com.xiaoyua.utils.JwtUtil;
import com.xiaoyua.utils.PasswordUtil;
import com.xiaoyua.vo.auth.LoginVO;
import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO passwordLogin(PasswordLoginDTO loginDTO) {
        // 1. 根据用户名查找用户认证信息
        LambdaQueryWrapper<UserAuthPO> authQuery = new LambdaQueryWrapper<>();
        authQuery.eq(UserAuthPO::getIdentityType, UserAuthPO.IdentityType.PASSWORD)
                .eq(UserAuthPO::getIdentifier, loginDTO.getUsername());
        
        UserAuthPO userAuth = userAuthMapper.selectOne(authQuery);
        if (userAuth == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!PasswordUtil.matches(loginDTO.getPassword(), userAuth.getCredential())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 查询用户信息
        UserPO user = userMapper.selectById(userAuth.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 4. 检查用户状态
        if (user.getStatus() != 0) {
            throw new RuntimeException("账号已被封禁");
        }

        // 5. 更新验证时间
        userAuth.setVerifiedAt(LocalDateTime.now());
        userAuthMapper.updateById(userAuth);

        // 6. 生成JWT令牌
        String accessToken = jwtUtil.generateToken(user.getId());
        String refreshToken = jwtUtil.generateToken(user.getId()); // 使用相同方法生成刷新令牌
        Long expiresIn = 7200L; // 2小时过期时间

        // 7. 构建用户简单信息
        UserSimpleVO userSimpleVO = new UserSimpleVO();
        userSimpleVO.setId(user.getId());
        userSimpleVO.setUsername(user.getUsername());
        userSimpleVO.setNickname(user.getNickname());
        userSimpleVO.setAvatarUrl(user.getAvatarUrl());
        userSimpleVO.setGender(user.getGender());

        // 8. 返回登录结果
        return new LoginVO(accessToken, refreshToken, expiresIn, userSimpleVO);
    }

    @Override
    @Transactional
    public LoginVO register(String username, String password, String nickname) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<UserAuthPO> authQuery = new LambdaQueryWrapper<>();
        authQuery.eq(UserAuthPO::getIdentityType, UserAuthPO.IdentityType.PASSWORD)
                .eq(UserAuthPO::getIdentifier, username);
        
        UserAuthPO existingAuth = userAuthMapper.selectOne(authQuery);
        if (existingAuth != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建用户
        UserPO user = new UserPO();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setGender(0);
        user.setStatus(0);
        user.setIsRealName(0);
        user.setPrivacyMobile(0);
        user.setPrivacyBirthday(0);
        user.setPrivacyFans(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        userMapper.insert(user);

        // 3. 创建用户认证信息
        UserAuthPO userAuth = new UserAuthPO();
        userAuth.setUserId(user.getId());
        userAuth.setIdentityType(UserAuthPO.IdentityType.PASSWORD);
        userAuth.setIdentifier(username);
        userAuth.setCredential(PasswordUtil.encode(password));
        userAuth.setVerifiedAt(LocalDateTime.now());
        userAuth.setCreatedAt(LocalDateTime.now());
        userAuth.setUpdatedAt(LocalDateTime.now());
        
        userAuthMapper.insert(userAuth);

        // 4. 生成JWT令牌
        String accessToken = jwtUtil.generateToken(user.getId());
        String refreshToken = jwtUtil.generateToken(user.getId()); // 使用相同方法生成刷新令牌
        Long expiresIn = 7200L; // 2小时过期时间

        // 5. 构建用户简单信息
        UserSimpleVO userSimpleVO = new UserSimpleVO();
        userSimpleVO.setId(user.getId());
        userSimpleVO.setUsername(user.getUsername());
        userSimpleVO.setNickname(user.getNickname());
        userSimpleVO.setAvatarUrl(user.getAvatarUrl());
        userSimpleVO.setGender(user.getGender());

        // 6. 返回注册结果
        return new LoginVO(accessToken, refreshToken, expiresIn, userSimpleVO);
    }
}
