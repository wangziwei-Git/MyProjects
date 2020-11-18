package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * 权限持久层Dao接口
 */
public interface PermissionDao {
    /**
     * 查询指定角色的所有权限
     * @param roleId RoleDao.xml传过来的角色id
     * @return 权限集合
     */
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
