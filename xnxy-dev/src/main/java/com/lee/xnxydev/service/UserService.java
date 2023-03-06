package com.lee.xnxydev.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.xnxydev.pojo.User;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
* @author 晓龙coding
* @description 针对表【user】的数据库操作Service
* @createDate 2023-02-02 22:13:38
*/
public interface UserService extends IService<User> {

    /**
     * 将该用户的权限设为1，表示已经过学生认证
     * @param uId
     * @return
     */
    Boolean verifyUser(Long uId);

    /**
     * 更新用户信息，如果新用户的某些属性为空，则不更新这些属性
     * 使用这个方法的时候一定要有uId属性
     * @param user
     * @return
     */
    Boolean updateUserInfo(User user);

    /**
     * 从数据库获取当前用户的课程表
     * @param uId
     * @return
     */
    ResponseResult getCourseTable(Long uId);

    /**
     * 从数据库获取当前用户的成绩
     * @param uId
     * @return
     */
    ResponseResult getScoreTable(Long uId);

    /**
     * 获取用户的权限
     * @param uId
     * @return
     */
    Integer getPower(Long uId);

    /**
     * 删除当前用户的教务处用户名和密码，并将他的权限置0（表示未认证）
     * @param uId
     * @return
     */
    Boolean deleteJwcAccountAndPassword(Long uId);
}
