package com.lee.xnxydev.service.impl;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.xnxydev.pojo.Life;
import com.lee.xnxydev.pojo.Trade;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.LifeVO;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.service.LifeService;
import com.lee.xnxydev.mapper.LifeMapper;
import com.lee.xnxydev.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author 晓龙coding
* @description 针对表【life】的数据库操作Service实现
* @createDate 2023-02-02 22:05:10
*/
@Service
@Slf4j
public class LifeServiceImpl extends ServiceImpl<LifeMapper, Life>
    implements LifeService {
    @Resource
    private UserService userService;
    @Resource
    private LifeMapper lifeMapper;
    @Override
    public ResponseResult listPost(Map<String, Object> data) {
        if (!"".equals(data.get("keyword").toString())) {
            List<Life> posts = lifeMapper.listPost(data);
            List<LifeVO> postVOs = lifeList2VO(posts);
            return ResponseResult.success(postVOs);
        }
        LambdaQueryWrapper<Life> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Life::getType, data.get("type"));
        // 未被删除的帖子
        queryWrapper.eq(Life::getHasDeleted, 0);
        queryWrapper.orderByDesc(Life::getCreateTime);
        queryWrapper.select(Life::getPId, Life::getTitle, Life::getUId, Life::getCreateTime, Life::getType, Life::getContent);

        Page<Life> page = new Page<>(Integer.parseInt(data.get("count").toString()), 30, false);
        Page<Life> lifePage = this.page(page, queryWrapper);
        List<Life> records = lifePage.getRecords();

        List<LifeVO> lifeVOList = lifeList2VO(records);

        return ResponseResult.success(lifeVOList);
    }

    private List<LifeVO> lifeList2VO(List<Life> postList) {
        List<LifeVO> lifeVOList = new ArrayList<>();

        for (Life post : postList) {
            LifeVO lifeVO = new LifeVO();
            lifeVO.setPId(post.getPId());
            lifeVO.setTitle(post.getTitle());
            lifeVO.setContent(post.getContent());
            lifeVO.setCreateTime(post.getCreateTime());
            lifeVO.setType(post.getType());

            // 通过uId查询author
            // 通过 uId 查询author
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUId, post.getUId());
            queryWrapper1.select(User::getUsername);
            User one = userService.getOne(queryWrapper1);
            if (one == null) {
                log.info("系统异常");
                log.info("当前帖子对应的用户id在用户表中无法找到，请检查代码逻辑");
                lifeVO.setAuthor("未知用户");
                lifeVOList.add(lifeVO);
                continue;
            }
            lifeVO.setAuthor(one.getUsername());
            lifeVOList.add(lifeVO);
        }

        return lifeVOList;
    }

    @Override
    public ResponseResult savePost(Life life) {
        this.save(life);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult listPostById(Long uId, String username) {
        LambdaQueryWrapper<Life> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Life::getUId, uId);
        // 未被删除的数据
        queryWrapper.eq(Life::getHasDeleted, 0);
        queryWrapper.orderByDesc(Life::getCreateTime);
        queryWrapper.select(Life::getPId, Life::getTitle, Life::getUId, Life::getCreateTime, Life::getType, Life::getContent);
        List<Life> lifeList = this.list(queryWrapper);
        List<LifeVO> lifeVOList = new ArrayList<>();

        for (Life post : lifeList) {
            LifeVO postVO = new LifeVO();

            postVO.setPId(post.getPId());
            postVO.setTitle(post.getTitle());
            postVO.setContent(post.getContent());
            postVO.setType(post.getType());
            postVO.setCreateTime(post.getCreateTime());

            postVO.setAuthor(username);

            lifeVOList.add(postVO);
        }

        return ResponseResult.success(lifeVOList);
    }

    @Override
    public ResponseResult deletePostById(Long pId, Long uId) {
        // 通过pId查询帖子的uId，判断uId是否属于该用户。如果属于，执行软删除。
        LambdaQueryWrapper<Life> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Life::getPId, pId);
        // 未被删除的帖子
        queryWrapper.eq(Life::getHasDeleted, 0);
        queryWrapper.select(Life::getUId);
        Life one = this.getOne(queryWrapper);
        if (one == null || one.getUId() == null || !one.getUId().equals(uId)) {
            return ResponseResult.fail("无权删除该帖子");
        }

        LambdaUpdateWrapper<Life> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Life::getPId, pId);
        updateWrapper.set(Life::getHasDeleted, 1);
        boolean hasDeleted = this.update(updateWrapper);
        if (hasDeleted) {
            return ResponseResult.success("删除成功，pId=" + pId);
        }

        return ResponseResult.fail("删除失败，请联系管理员");
    }
}




