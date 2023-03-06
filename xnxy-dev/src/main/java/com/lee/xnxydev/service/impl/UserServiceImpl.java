package com.lee.xnxydev.service.impl;
import java.time.LocalDateTime;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.UserService;
import com.lee.xnxydev.mapper.UserMapper;
import com.lee.xnxydev.service.VerifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-02-02 22:13:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Value("${verifyUrl}")
    private String verifyUrl;
    @Resource
    private VerifyService verifyService;
    @Override
    public Boolean verifyUser(Long uId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUId, uId);
        updateWrapper.set(User::getPower, 1);
        Boolean success = this.update(updateWrapper);
        return success;
    }

    @Override
    public Boolean updateUserInfo(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        Long uId = user.getUId();
        String username = user.getUsername();
        String jwcUsername = user.getJwcUsername();
        String jwcPassword = user.getJwcPassword();
        String photoImg = user.getPhotoImg();
        String classTable = user.getClassTable();
        String scoreTable = user.getScoreTable();
        Integer power = user.getPower();
        String realName = user.getRealName();

        if (username != null) {
            updateWrapper.set(User::getUsername, username);
        }
        if (jwcUsername != null) {
            updateWrapper.set(User::getJwcUsername, jwcUsername);
        }
        if (jwcPassword != null) {
            updateWrapper.set(User::getJwcPassword, jwcPassword);
        }
        if (photoImg != null) {
            updateWrapper.set(User::getPhotoImg, photoImg);
        }
        if (classTable != null) {
            updateWrapper.set(User::getClassTable, classTable);
        }
        if (scoreTable != null) {
            updateWrapper.set(User::getScoreTable, scoreTable);
        }
        if (power != null) {
            updateWrapper.set(User::getPower, power);
        }
        if (realName != null) {
            updateWrapper.set(User::getRealName, realName);
        }
        updateWrapper.eq(User::getUId, uId);
        boolean success = this.update(updateWrapper);
        return success;
    }

    @Override
    public ResponseResult getCourseTable(Long uId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getClassTable);
        queryWrapper.eq(User::getUId, uId);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            return ResponseResult.fail("信息获取异常，请联系管理员");
        }
        String classTable = user.getClassTable();
        // 课表信息转成json字符串
        ListIterator<Object> objectListIterator = JSON.parseArray(classTable).listIterator();
        Map<String, Object> mp = new HashMap<>();
        mp.put("courses", objectListIterator);
        // 重要，传入的值必须是从周一开始
        mp.put("startDate", "2023-02-20");
        return ResponseResult.success(mp);
    }

    @Override
    public ResponseResult getScoreTable(Long uId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getScoreTable);
        queryWrapper.eq(User::getUId, uId);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            return ResponseResult.fail("信息获取异常，请联系管理员");
        }
        String classTable = user.getScoreTable();
        // 课表信息转成json字符串
        Object parsedObj = JSON.parse(classTable);
        return ResponseResult.success(parsedObj);
    }

    @Override
    public Integer getPower(Long uId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getPower);
        queryWrapper.eq(User::getUId, uId);
        User user = this.getOne(queryWrapper);
        return user.getPower();
    }

    @Override
    public Boolean deleteJwcAccountAndPassword(Long uId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUId, uId);
        updateWrapper.set(User::getJwcUsername, null);
        updateWrapper.set(User::getJwcPassword, null);
        updateWrapper.set(User::getPower, 0);
        boolean success = this.update(updateWrapper);
        return success;
    }
}
