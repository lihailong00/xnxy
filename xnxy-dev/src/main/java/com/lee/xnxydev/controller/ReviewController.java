package com.lee.xnxydev.controller;

import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.mapper.LifeMapper;
import com.lee.xnxydev.pojo.Review;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.ReviewService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/review")
public class ReviewController {
    @Resource
    private ReviewService reviewService;
    @PostMapping("/get")
    @LogAnnotation(module="评论", operator="获取评论")
    ResponseResult getReviewByPId(@RequestBody Map<String, Object> data) {
        Long pId = Long.parseLong(data.get("pId").toString());
        return reviewService.getReviewByPId(pId);
    }

    @PostMapping("/create")
    @LogAnnotation(module="评论", operator="创建评论")
    ResponseResult saveReview(@RequestBody Review review, @RequestHeader String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        review.setUId(uId);
        review.setCreateTime(LocalDateTime.now());
        return reviewService.saveReview(review);
    }
}
