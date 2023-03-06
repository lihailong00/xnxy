package com.lee.xnxydev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.xnxydev.mapper.LifeMapper;
import com.lee.xnxydev.mapper.TradeMapper;
import com.lee.xnxydev.mapper.UserMapper;
import com.lee.xnxydev.pojo.Life;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.LifeVO;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.pojo.VO.TradeVO;
import com.lee.xnxydev.service.TradeService;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【trade】的数据库操作Service实现
* @createDate 2023-02-03 12:57:14
*/
@Service
@Slf4j
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade>
    implements TradeService {
    @Resource
    private UserService userService;

    @Resource
    private TradeMapper tradeMapper;
    @Override
    public ResponseResult listGoods(Map<String, Object> data) {
        if (!"".equals(data.get("keyword"))) {
            List<Trade> trades = tradeMapper.listGoods(data);
            List<TradeVO> tradeVOList = tradeList2VO(trades);
            return ResponseResult.success(tradeVOList);
        }
        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
        // 未被删除的数据
        queryWrapper.eq(Trade::getHasDeleted, 0);
        queryWrapper.eq(Trade::getType, data.get("type").toString());
        if (data.get("cond1").toString().equals("time")) {
            queryWrapper.orderByDesc(Trade::getCreateTime);
        }
        else {
            queryWrapper.orderByAsc(Trade::getPrice);
        }

        queryWrapper.select(Trade::getGId, Trade::getUId, Trade::getGoodsName, Trade::getGoodsDesc, Trade::getPrice, Trade::getType, Trade::getCreateTime, Trade::getGoodsImage);

        Page<Trade> page = new Page<>(Integer.valueOf(data.get("count").toString()), 30, false);
        Page<Trade> tradePage = this.page(page, queryWrapper);
        List<Trade> records = tradePage.getRecords();

        List<TradeVO> tradeVOList = tradeList2VO(records);

        return ResponseResult.success(tradeVOList);
    }

    private List<TradeVO> tradeList2VO(List<Trade> trades) {
        List<TradeVO> tradeVOList = new ArrayList<>();
        for (Trade trade : trades) {
            TradeVO tradeVO = new TradeVO();
            tradeVO.setGId(trade.getGId());
            tradeVO.setGoodsName(trade.getGoodsName());
            tradeVO.setGoodsDesc(trade.getGoodsDesc());
            tradeVO.setPrice(trade.getPrice());
            tradeVO.setType(trade.getType());
            tradeVO.setCreateTime(trade.getCreateTime());
            tradeVO.setGoodsImage(trade.getGoodsImage());

            // 通过 uId 查询username
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUId, trade.getUId());
            queryWrapper1.select(User::getUsername);
            User one = userService.getOne(queryWrapper1);
            if (one == null) {
                log.info("系统异常");
                log.info("当前商品对应的用户id在用户表中无法找到，请检查代码逻辑");
                tradeVO.setUsername("未知用户");
                tradeVOList.add(tradeVO);
                continue;
            }
            tradeVO.setUsername(one.getUsername());
            tradeVOList.add(tradeVO);
        }
        return tradeVOList;
    }

    @Override
    public ResponseResult saveGoods(Trade trade) {
        this.save(trade);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult listGoodsById(Long uId, String username) {
        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Trade::getUId, uId);
        // 未被删除的数据
        queryWrapper.eq(Trade::getHasDeleted, 0);
        queryWrapper.orderByDesc(Trade::getCreateTime);
        queryWrapper.select(Trade::getGId, Trade::getGoodsName, Trade::getGoodsDesc, Trade::getType, Trade::getPrice, Trade::getCreateTime);

        List<Trade> tradeList = this.list(queryWrapper);
        List<TradeVO> tradeVOList = new ArrayList<>();

        for (Trade trade : tradeList) {
            TradeVO tradeVO = new TradeVO();

            tradeVO.setGId(trade.getGId());
            tradeVO.setGoodsName(trade.getGoodsName());
            tradeVO.setGoodsDesc(trade.getGoodsDesc());
            tradeVO.setPrice(trade.getPrice());
            tradeVO.setType(trade.getType());
            tradeVO.setCreateTime(trade.getCreateTime());

            tradeVO.setUsername(username);

            tradeVOList.add(tradeVO);
        }

        return ResponseResult.success(tradeVOList);
    }

    @Override
    public ResponseResult deleteGoodsById(Long gId, Long uId) {
        // 通过pId查询商品的uId，判断uId是否属于该用户。如果属于，执行软删除。
        LambdaQueryWrapper<Trade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Trade::getGId, gId);
        queryWrapper.select(Trade::getUId);
        Trade one = this.getOne(queryWrapper);
        if (one == null || one.getUId() == null || !one.getUId().equals(uId)) {
            return ResponseResult.fail("无权删除该商品");
        }

        LambdaUpdateWrapper<Trade> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Trade::getGId, gId);
        updateWrapper.set(Trade::getHasDeleted, 1);
        boolean hasDeleted = this.update(updateWrapper);
        if (hasDeleted) {
            return ResponseResult.success("删除成功，gId=" + gId);
        }

        return ResponseResult.fail("删除失败，请联系管理员");
    }
}




