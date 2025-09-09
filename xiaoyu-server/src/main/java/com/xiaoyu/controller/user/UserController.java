package com.xiaoyu.controller.user;


import com.xiaoyu.result.Result;
import com.xiaoyu.service.UserService;
import com.xiaoyu.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public Result<UserVO> getUserPublicInfo(@PathVariable Long userId){
        log.info("获取用户的公开信息：{}",userId);
        return null;

    }


}
