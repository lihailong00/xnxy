package com.lee.xnxydev.controller;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private LoginService loginService;
    @PostMapping("/wx")
    @LogAnnotation(module="登录", operator="微信登录")
    public ResponseResult loginWX(@RequestBody Map<String, Object> info) {
        return loginService.loginWX(info);
    }
}