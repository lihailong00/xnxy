package com.lee.xnxydev.controller;
import java.time.LocalDateTime;

import com.lee.xnxydev.pojo.Life;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.LifeService;
import com.lee.xnxydev.service.TradeService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.web.bind.annotation.*;
import com.lee.xnxydev.aop.LogAnnotation;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/life")
public class LifeController {
    @Resource
    private LifeService lifeService;
    @PostMapping("/create")
    @LogAnnotation(module="帖子", operator="发布")
    ResponseResult savePost(@RequestBody Life life, @RequestHeader("token") String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        if (life.getTitle() == null || life.getTitle().length() < 1) {
            return ResponseResult.fail("文章title至少有1个字");
        }
        if (life.getTitle().length() > 100) {
            return ResponseResult.fail("文章title不要超过100字");
        }
        if (life.getContent() == null || life.getContent().length() < 1) {
            return ResponseResult.fail("文章content至少有1个字");
        }
        if (life.getContent().length() > 2000) {
            return ResponseResult.fail("文章content不能超过2000字");
        }
        if (!"study".equals(life.getType()) && !"life".equals(life.getType())) {
            return ResponseResult.fail("文章type错误");
        }

        life.setUId(uId);
        life.setCreateTime(LocalDateTime.now());
        life.setHasDeleted(0);
        return lifeService.savePost(life);
    }

    @PostMapping("/list")
    @LogAnnotation(module="帖子", operator="查询")
    ResponseResult listPost(@RequestBody Map<String, Object> data) {
        String cond = data.get("type").toString();
        if (!"study".equals(cond) && !"life".equals(cond)) {
            return ResponseResult.fail("帖子类型参数错误");
        }
        try {
            Integer.parseInt(data.get("count").toString());
        } catch (Exception e) {
            return ResponseResult.fail("帖子页码不是整数");
        }
        return lifeService.listPost(data);
    }

    @PostMapping("/list/me")
    @LogAnnotation(module="帖子", operator="个人查询")
    ResponseResult listPostById(@RequestHeader("token") String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        // 从token中获取username
        String username = JWTUtil.getTokenInfo(token).getClaim("username").asString();
        return lifeService.listPostById(uId, username);
    }

    @PostMapping("/delete")
    @LogAnnotation(module="帖子", operator="删除")
    ResponseResult deletePostById(@RequestBody Map<String, Object> data, @RequestHeader("token") String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        Long pId = 0L;
        try {
            pId = Long.parseLong(data.get("pId").toString());
        } catch (NumberFormatException e) {
            return ResponseResult.fail("帖子id格式错误");
        } catch (Exception e) {
            return ResponseResult.fail("未知错误");
        }
        return lifeService.deletePostById(pId, uId);
    }
}
