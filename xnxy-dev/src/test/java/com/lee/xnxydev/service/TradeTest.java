package com.lee.xnxydev.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.TradeVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TradeTest {
    @Resource
    private TradeService tradeService;

    @Resource
    private UserService userService;
    @Test
    void listGoods() {
//        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Trade::getType, "sell");
//        queryWrapper.orderByDesc(Trade::getCreateTime);
//        queryWrapper.select(Trade::getGId, Trade::getUId, Trade::getGoodsName, Trade::getGoodsDesc, Trade::getPrice, Trade::getPrice);
//
//        Page<Trade> page = new Page<>(1, 10);
//        Page<Trade> tradePage = tradeService.page(page, queryWrapper);
//        List<Trade> records = tradePage.getRecords();
//
//        List<TradeVO> tradeVOList = new ArrayList<>();
//
//        for (Trade record : records) {
//            TradeVO tradeVO = new TradeVO();
//            tradeVO.setGId(record.getGId());
//            tradeVO.setGoodsName(record.getGoodsName());
//            tradeVO.setGoodsDesc(record.getGoodsDesc());
//            tradeVO.setPrice(record.getPrice());
//            tradeVO.setCreateTime(record.getCreateTime());
//
//            // 通过 uId 查询username
//            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
//            queryWrapper1.eq(User::getUId, record.getUId());
//            queryWrapper1.select(User::getUsername);
//            User one = userService.getOne(queryWrapper1);
//            tradeVO.setUsername(one.getUsername());
//
//            tradeVOList.add(tradeVO);
//        }
//
//        for (TradeVO tradeVO : tradeVOList) {
//            System.out.println(tradeVO);
//        }
    }

    @Test
    void saveGoods() {
        Trade trade = new Trade();
        trade.setUId(1L);
        trade.setGoodsName("我要卖一本书，叫高等数学");
        trade.setGoodsDesc("很便宜，快来买");
        trade.setPrice(new BigDecimal("12.2"));
        trade.setHasDeleted(0);
        trade.setCreateTime(LocalDateTime.now());
        trade.setType("sell");
        tradeService.save(trade);
    }
}
