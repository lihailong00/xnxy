package com.lee.xnxydev.mapper;

import com.lee.xnxydev.pojo.Life;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【life】的数据库操作Mapper
* @createDate 2023-02-02 22:05:10
* @Entity com.lee.xnxydev.pojo.Life
*/
public interface LifeMapper extends BaseMapper<Life> {
    List<Life> listPost(Map<String, Object> data);
}




