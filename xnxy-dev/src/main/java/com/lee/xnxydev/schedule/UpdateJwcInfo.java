package com.lee.xnxydev.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lee.xnxydev.aop.LogAnnotation;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.service.VerifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时更新所有用户的教务处课程
 * @author 晓龙coding
 */
@Configuration
@EnableScheduling
public class UpdateJwcInfo {
    @Value("${verifyUrl}")
    private String verifyUrl;
    @Resource
    private UserService userService;

    @Resource
    private VerifyService verifyService;
    @Scheduled(cron = "0 0 0,6,12,18 * * ?")
//    @Scheduled(cron = "10 30 20 * * ?")
    @LogAnnotation(module="定时任务", operator="更新教务处信息")
    public void updateJwc() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        List<User> userList = userService.list(queryWrapper);
        for (User user : userList) {
            Long uId = user.getUId();
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(User::getUId, User::getJwcUsername, User::getJwcPassword);
            wrapper.eq(User::getUId, uId);
            User targetUser = userService.getOne(wrapper);
            if (targetUser.getJwcUsername() == null || targetUser.getJwcPassword() == null) {
                continue;
            }
            // 去教务处官网爬取登录过的用户的课表和成绩信息
            Map<String, Object> params = new HashMap<>();
            params.put("username", targetUser.getJwcUsername());
            params.put("password", targetUser.getJwcPassword());
            System.out.println("获取信息：" + targetUser.getJwcUsername() + " " + targetUser.getJwcPassword());
            Boolean success = verifyService.syncCrawlData(params, uId, verifyUrl);
        }
    }
}
