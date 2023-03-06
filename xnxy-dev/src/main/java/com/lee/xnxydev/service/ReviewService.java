package com.lee.xnxydev.service;

import com.lee.xnxydev.pojo.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.xnxydev.pojo.VO.ResponseResult;

/**
* @author 晓龙coding
* @description 针对表【review】的数据库操作Service
* @createDate 2023-02-08 23:24:57
*/
public interface ReviewService extends IService<Review> {

    ResponseResult getReviewByPId(Long pId);

    ResponseResult saveReview(Review review);
}
