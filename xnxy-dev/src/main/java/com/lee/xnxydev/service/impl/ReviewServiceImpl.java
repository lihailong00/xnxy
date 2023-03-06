package com.lee.xnxydev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.xnxydev.pojo.Review;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.pojo.VO.ReviewVO;
import com.lee.xnxydev.service.ReviewService;
import com.lee.xnxydev.mapper.ReviewMapper;
import com.lee.xnxydev.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 晓龙coding
* @description 针对表【review】的数据库操作Service实现
* @createDate 2023-02-08 23:24:57
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService {
    @Resource
    private UserService userService;
    @Override
    public ResponseResult getReviewByPId(Long pId) {
        LambdaQueryWrapper<Review> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Review::getPId, pId);
        List<Review> reviewList = this.list(queryWrapper);
        List<ReviewVO> reviewVOList = reviewList2VO(reviewList);
        return ResponseResult.success(reviewVOList);
    }

    public List<ReviewVO> reviewList2VO(List<Review> reviewList) {
        List<ReviewVO> reviewVOList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewVO reviewVO = new ReviewVO();
            reviewVO.setRId(review.getRId());
            reviewVO.setContent(review.getContent());
            reviewVO.setCreateTime(review.getCreateTime());

            // 通过uId查询username
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUId, review.getUId());
            queryWrapper.select(User::getUsername);
            User one = userService.getOne(queryWrapper);
            reviewVO.setUsername(one.getUsername());

            reviewVOList.add(reviewVO);
        }
        return reviewVOList;
    }

    @Override
    public ResponseResult saveReview(Review review) {
        if (review.getContent().length() < 2) {
            return ResponseResult.fail("评论必须大于2个字");
        }
        boolean success = this.save(review);
        if (success) {
            return ResponseResult.success("评论发布成功！");
        }
        return ResponseResult.fail("评论发布失败，请联系管理员！");
    }
}




