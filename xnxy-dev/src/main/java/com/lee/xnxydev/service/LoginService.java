package com.lee.xnxydev.service;

import com.lee.xnxydev.pojo.VO.ResponseResult;

import java.util.Map;

/**
 * @author 晓龙coding
 */
public interface LoginService {
    /**
     * 通过传入的信息，验证微信用户的身份。如果用户不存在，则创建新用户。
     * @param info 一定包含js_code属性
     * @return 微信用户身份验证成功还是失败的提示
     */
    ResponseResult loginWX(Map<String, Object> info);
}
