package com.lee.xnxydev.service;

import com.lee.xnxydev.pojo.Life;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.xnxydev.pojo.VO.ResponseResult;

import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【life】的数据库操作Service
* @createDate 2023-02-02 22:05:10
*/
public interface LifeService extends IService<Life> {
    /**
     * 展示商品信息
     * @param data 一定包含属性 type，count，可能包含keyword
     * @return 返回商品信息
     */
    ResponseResult listPost(Map<String, Object> data);

    /**
     * 上传帖子并保存到数据库
     * @param life 上传的帖子信息
     * @return 上传成功或失败的提示
     */
    ResponseResult savePost(Life life);

    /**
     * 用于查询某个用户的所有发帖记录
     * @param uId 用户id
     * @param username 用户名
     * @return 返回该用户的所有发帖记录
     */
    ResponseResult listPostById(Long uId, String username);

    /**
     * 通过帖子id和用户id删除帖子，其中用户id用于鉴权
     * @param pId 帖子id
     * @param uId 用户id
     * @return 帖子删除成功或失败的提示
     */
    ResponseResult deletePostById(Long pId, Long uId);
}
