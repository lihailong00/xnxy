package com.lee.xnxydev.service;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.xnxydev.pojo.Life;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.LifeVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class LifeTest {
    @Resource
    private LifeService lifeService;

    @Resource
    private UserService userService;

    /**
     * 获取帖子
     */
    @Test
    void listPost() {
//        LambdaQueryWrapper<Life> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Life::getType, "study");
//        queryWrapper.select(Life::getPId, Life::getUId, Life::getTitle, Life::getCreateTime, Life::getType);
//
//        Page<Life> page = new Page<>(1, 10);
//        Page<Life> lifePage = lifeService.page(page, queryWrapper);
//        List<Life> records = lifePage.getRecords();
//
//        List<LifeVO> lifeVOList = new ArrayList<>();
//
//        for (Life record : records) {
//            LifeVO lifeVO = new LifeVO();
//            lifeVO.setPId(record.getPId());
//            lifeVO.setTitle(record.getTitle());
//            lifeVO.setContent(record.getContent());
//            lifeVO.setCreateTime(record.getCreateTime());
//
//            // 通过 uId 查询 username
//            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<User>();
//            queryWrapper1.eq(User::getUId, record.getUId());
//            queryWrapper1.select(User::getUsername);
//            User one = userService.getOne(queryWrapper1);
//            lifeVO.setAuthor(one.getUsername());
//
//            lifeVOList.add(lifeVO);
//        }
//
//        for (LifeVO lifeVO : lifeVOList) {
//            System.out.println(lifeVO);
//        }
    }

    /**
     * 创建帖子
     */
    @Test
    void savePost() {
        Life post = new Life();
        post.setUId(1L);
        post.setTitle("有组队参加icpc的同学吗");
        post.setContent("我的联系方式：jfliesdjf");
        post.setType("study");
        post.setCreateTime(LocalDateTime.now());
        post.setHasDeleted(0);
        post.setIp("");
        lifeService.save(post);
    }
}
