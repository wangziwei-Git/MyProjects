package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 用户业务接口
 */
public interface UserService {
    /**
     * 远程调用用户服务,根据用户名查询用户信息
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);
}
