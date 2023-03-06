package com.lee.xnxydev.controller;

import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.service.VerifyService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/verify")
public class VerifyController {
    @Value("${verifyUrl}")
    private String verifyUrl;
    @Resource
    private UserService userService;

    @Resource
    private VerifyService verifyService;

    /**
     *
     * @param params 包含username和password，分别表示教务处用户名和教务处密码
     * @param token
     * @return
     */
    @PostMapping("")
    @LogAnnotation(module="认证", operator="登录教务处认证")
    public ResponseResult verify(@RequestBody Map<String, Object> params, @RequestHeader String token) {
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        Integer power = userService.getPower(uId);
        if (power.equals(1)) {
            return ResponseResult.fail("您已经验证成功啦~");
        }

        verifyService.asyncCrawlData(params, uId, verifyUrl);

        return ResponseResult.success("系统正在验证，请先逛逛别的模块吧~");
    }

}
