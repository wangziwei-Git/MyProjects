package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户持久层Dao接口
 * @Author: wzw
 * @Date: 2020/11/18 18:17
 * @version: 1.8
 */
public interface UserDao {
    /**
     * 远程调用用户服务,根据用户名查询用户信息
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);
}
