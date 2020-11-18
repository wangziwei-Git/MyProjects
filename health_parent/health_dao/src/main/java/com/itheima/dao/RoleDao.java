package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * 角色持久层Dao接口
 */
public interface RoleDao {
    /**
     * 查询指定用户的所有角色
     * @param userId UserDao.xml传过来的用户id
     * @return 角色集合
     */
    Set<Role> findRolesByUserId(Integer userId);
}
