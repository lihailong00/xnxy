package com.lee.xnxydev.controller;

import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用途 这个类用于获取教务处信息
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/office")
public class OfficeController {
    @Resource
    private UserService userService;
    @PostMapping("/coursetable")
    @LogAnnotation(module="课程", operator="课表")
    public ResponseResult getCourseTable(@RequestHeader String token) {
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        Integer power = userService.getPower(uId);
        if (power.equals(0)) {
            return ResponseResult.fail("请先认证~");
        }
        return userService.getCourseTable(uId);
    }

    @PostMapping("/scoretable")
    @LogAnnotation(module="课程", operator="成绩")
    public ResponseResult getScoreTable(@RequestHeader String token) {
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        Integer power = userService.getPower(uId);
        if (power.equals(0)) {
            return ResponseResult.fail("请先认证~");
        }
        return userService.getScoreTable(uId);
    }
}
