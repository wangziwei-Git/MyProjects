package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 权限自定义类
 * @Author: wzw
 * @Date: 2020/11/18 17:50
 * @version: 1.8
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    //订阅服务
    @Reference
    private UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 过滤器的User
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.实现功能:远程调用用户服务,根据用户名查询用户信息
        com.itheima.pojo.User user = userService.findUserByUsername(username);
        //2.判断是否存在
        if (user == null) {
            //用户不存在,抛出异常UsernameNotFoundException
            return null;
        }
        //3.存在,获取数据库密文密码
        String password = user.getPassword();

        //4.给当前用户授权==>GrantedAuthority:权限接口
        List<GrantedAuthority> list = new ArrayList<>();
        //获取当前用户的权限
        Set<Role> roles = user.getRoles();
        //循环用户获取该有的角色
        for (Role role : roles) {
            //获得每个角色的所有权限
            Set<Permission> permissions = role.getPermissions();
            //循环所有权限
            for (Permission permission : permissions) {
                //添加权限关键字到权限过滤器中(授权,针对权限)，用于权限控制
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        //5.返回结果
        /**
         * User()   security
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限
         */
        return new User(username,password,list);
    }
}
