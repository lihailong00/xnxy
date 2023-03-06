package com.lee.xnxydev.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.pojo.VO.ResponseResult;

import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【trade】的数据库操作Service
* @createDate 2023-02-03 12:57:14
*/
public interface TradeService extends IService<Trade> {

    ResponseResult listGoods(Map<String, Object> data);

    ResponseResult saveGoods(Trade trade);

    ResponseResult listGoodsById(Long uId, String username);

    ResponseResult deleteGoodsById(Long gId, Long uId);
}
