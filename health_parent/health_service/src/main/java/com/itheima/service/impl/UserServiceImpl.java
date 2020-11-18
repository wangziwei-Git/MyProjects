package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口实现类
 * @Author: wzw
 * @Date: 2020/11/18 18:17
 * @version: 1.8
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{

    //注入dao层
    @Autowired
    private UserDao userDao;

    /**
     * 远程调用用户服务,根据用户名查询用户信息
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
