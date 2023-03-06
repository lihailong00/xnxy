package com.lee.xnxydev.service.impl;
import java.time.LocalDateTime;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.LoginService;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 晓龙coding
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final String appid = "wxd917d6843a746a45";
    private static final String secret = "b228ec947678dba43ab0fc9c9c958bb7";
    @Resource
    private UserService userService;
    @Override
    @LogAnnotation(module="登录", operator="微信登录（service层）")
    public ResponseResult loginWX(Map<String, Object> info) {
        String code = info.get("js_code").toString();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        String replaceUrl = url.replace("{0}", appid).replace("{1}", secret).replace("{2}", code);
        // 向微信服务器发送请求
        String res = HttpUtil.get(replaceUrl);
        if (res.contains("errmsg")) {
            return ResponseResult.fail("访问微信服务器异常");
        }

        // 字符串转Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map resp = null;
        try {
            resp = objectMapper.readValue(res, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 查询openid是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, resp.get("openid"));
        User one = userService.getOne(queryWrapper);
        // 如果openid不存在，则注册新用户
        if (one == null) {
            User newUser = new User();
            // 不指定uId，mybatis-plus会按照雪花算法自动生成一个uId
            newUser.setUsername("用户" + UUID.randomUUID().toString().substring(0, 8));
            newUser.setSessionKey(resp.get("session_key").toString());
            newUser.setOpenid(resp.get("openid").toString());
            newUser.setPhotoImg("");
            newUser.setCreatetime(LocalDateTime.now());
            newUser.setHasDeleted(0);
            newUser.setPower(0);
            userService.save(newUser);
        }

        // 查找该用户的uId
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getOpenid, resp.get("openid"));
        queryWrapper1.select(User::getUId, User::getUsername, User::getPhotoImg, User::getPower);
        User one1 = userService.getOne(queryWrapper1);

        // 生成JWT token 并返回给前端
        Map<String, String> data = new HashMap<>();
        data.put("uId", one1.getUId().toString());
        data.put("username", one1.getUsername());
        data.put("photoImg", one1.getPhotoImg());
        data.put("power", one1.getPower().toString());
        Map<String, Object> token = new HashMap<>();
        token.put("token", JWTUtil.getToken(data, 7 * 24 * 3600));
        return ResponseResult.success(token);
    }
}
