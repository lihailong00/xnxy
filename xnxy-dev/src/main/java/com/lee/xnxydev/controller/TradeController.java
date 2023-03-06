package com.lee.xnxydev.controller;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.mapper.TradeMapper;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.TradeService;
import com.lee.xnxydev.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@RestController
@RequestMapping("/trade")
public class TradeController {
    @Resource
    private TradeService tradeService;

    @PostMapping("/list")
    @LogAnnotation(module="商品", operator="展示所有商品")
    ResponseResult listGoods(@RequestBody Map<String, Object> data) {
        String type = data.get("type").toString();
        if (!"sell".equals(type) && !"want".equals(type)) {
            return ResponseResult.fail("商品类型参数错误");
        }
        String cond1 = data.get("cond1").toString();
        if (!"price".equals(cond1) && !"time".equals(cond1)) {
            return ResponseResult.fail("商品排序参数错误");
        }
        try {
            Integer.parseInt(data.get("count").toString());
        } catch (Exception e) {
            return ResponseResult.fail("商品页码不是整数");
        }
        return tradeService.listGoods(data);
    }

    @PostMapping("/list/me")
    @LogAnnotation(module="商品", operator="展示我的商品")
    // 可以用分页优化
    ResponseResult listGoodsById(@RequestHeader("token") String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        // 从token中获取username
        String username = JWTUtil.getTokenInfo(token).getClaim("username").asString();
        return tradeService.listGoodsById(uId, username);
    }

    @PostMapping("/create")
    @LogAnnotation(module="商品", operator="发布商品")
    ResponseResult saveGoods(@RequestBody Trade trade, @RequestHeader("token") String token) {
        System.out.println(trade);
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        if (trade.getGoodsName() == null || trade.getGoodsName().length() < 1) {
            return ResponseResult.fail("商品名至少有1个字");
        }
        if (trade.getGoodsName().length() > 40) {
            return ResponseResult.fail("商品名最多有40个字");
        }
        if (trade.getGoodsDesc() == null || trade.getGoodsDesc().length() < 5) {
            return ResponseResult.fail("商品描述至少有5个字");
        }
        if (trade.getGoodsDesc().length() > 200) {
            return ResponseResult.fail("商品描述最多有200个字");
        }
        if (!"want".equals(trade.getType()) && !"sell".equals(trade.getType())) {
            return ResponseResult.fail("商品类型错误");
        }
        // TODO 暂时没有验证价格为负的情况和价格很大的情况
        trade.setUId(uId);
        trade.setHasDeleted(0);
        trade.setCreateTime(LocalDateTime.now());
        trade.setIp("");
        trade.setIpArea("");
        return tradeService.saveGoods(trade);
    }

    @PostMapping("/delete")
    @LogAnnotation(module="商品", operator="删除商品记录")
    ResponseResult deletePostById(@RequestBody Map<String, Object> data, @RequestHeader("token") String token) {
        // 从token中获取uId
        Long uId = Long.parseLong(JWTUtil.getTokenInfo(token).getClaim("uId").asString());
        Long gId = 0L;
        try {
            gId = Long.parseLong(data.get("gId").toString());
        } catch (NumberFormatException e) {
            return ResponseResult.fail("商品id格式错误");
        } catch (Exception e) {
            return ResponseResult.fail("未知错误");
        }
        return tradeService.deleteGoodsById(gId, uId);
    }
}
