package com.lee.xnxydev.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.service.VerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@Component
public class VerifyServiceImpl implements VerifyService {
    @Resource
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Async("taskExecutor")
    public void asyncCrawlData(Map<String, Object> params, Long uId, String verifyUrl) {
        // 获取输入的用户名和密码
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        // 获取 验证后端 的地址
        String url = verifyUrl;

        String result = "";
        result = HttpUtil.createGet(url).form(params).execute().body();

        logger.info(result);
        Map mp = JSONObject.parseObject(result, Map.class);
        if (!(Boolean) mp.get("success")) {
            logger.info("当前时间：" + LocalDateTime.now());
            logger.info("当前用户id：" + uId);
            String wrongMsg = mp.get("msg").toString();
            logger.info(wrongMsg);
            if ("用户名或密码错误".equals(wrongMsg)) {
                userService.deleteJwcAccountAndPassword(uId);
            }
        }

        // 创建新对象，并将新数据更新到数据库
        User newUser = new User();
        newUser.setUId(uId);
        newUser.setClassTable(mp.get("courseTable").toString());
        newUser.setScoreTable(mp.get("scoreTable").toString());
        newUser.setRealName(mp.get("realName").toString());
        Boolean saveSuccess = userService.updateUserInfo(newUser);

        if (!saveSuccess) {
            logger.info("当前时间：" + LocalDateTime.now());
            logger.info("当前用户id：" + uId);
            logger.info("错误信息：课表存入错误，请联系管理员");
            return;
        }

        // 成功从教务处获取信息
        if ((Boolean) mp.get("success")) {
            Boolean success = userService.verifyUser(uId);
            if (success) {
                logger.info("当前时间：" + LocalDateTime.now());
                logger.info("当前用户id：" + uId);
                logger.info("信息：认证成功！");
                User user = new User();
                user.setUId(uId);
                user.setJwcUsername(username);
                user.setJwcPassword(password);
                userService.updateUserInfo(user);
            }
            else {
                logger.info("当前时间：" + LocalDateTime.now());
                logger.info("当前用户id：" + uId);
                logger.info("错误信息：成功从教务处取出数据，但是并没有保存到数据库中");
            }
        }
    }

    @Override
    public Boolean syncCrawlData(Map<String, Object> params, Long uId, String verifyUrl) {
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        String url = verifyUrl;

        String result = "";
        result = HttpUtil.createGet(url).form(params).execute().body();

        logger.info(result);
        Map mp = JSONObject.parseObject(result, Map.class);
        if (!(Boolean) mp.get("success")) {
            logger.info("当前时间：" + LocalDateTime.now());
            logger.info("当前用户id：" + uId);
            String wrongMsg = mp.get("msg").toString();
            logger.info(wrongMsg);
            if ("用户名或密码错误".equals(wrongMsg)) {
                userService.deleteJwcAccountAndPassword(uId);
            }
            return false;
        }

        // 创建新对象，并将新数据更新到数据库
        User newUser = new User();
        newUser.setUId(uId);
        newUser.setClassTable(mp.get("courseTable").toString());
        newUser.setScoreTable(mp.get("scoreTable").toString());
        newUser.setRealName(mp.get("realName").toString());
        Boolean saveSuccess = userService.updateUserInfo(newUser);
        if (!saveSuccess) {
            logger.info("当前时间：" + LocalDateTime.now());
            logger.info("当前用户id：" + uId);
            logger.info("错误信息：课表存入错误，请联系管理员");
            return false;
        }

        // 成功从教务处获取信息
        if ((Boolean) mp.get("success")) {
            Boolean success = userService.verifyUser(uId);
            if (success) {
                logger.info("当前时间：" + LocalDateTime.now());
                logger.info("当前用户id：" + uId);
                logger.info("信息：认证成功！");
                User user = new User();
                user.setUId(uId);
                user.setJwcUsername(username);
                user.setJwcPassword(password);
                userService.updateUserInfo(user);
            }
            else {
                logger.info("当前时间：" + LocalDateTime.now());
                logger.info("当前用户id：" + uId);
                logger.info("错误信息：成功从教务处取出数据，但是并没有保存到数据库中");
                return false;
            }
        }
        return true;
    }
}
