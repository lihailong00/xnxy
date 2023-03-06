package com.lee.xnxydev.mapper;

import com.lee.xnxydev.pojo.Trade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【trade】的数据库操作Mapper
* @createDate 2023-02-03 13:30:54
* @Entity com.lee.xnxydev.pojo.Trade
*/
public interface TradeMapper extends BaseMapper<Trade> {

    List<Trade> listGoods(Map<String, Object> data);
}




