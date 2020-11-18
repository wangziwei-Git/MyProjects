package com.itheima.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wzw
 * @Date: 2020/11/17 22:08
 * @version: 1.8
 */
@Component //权限控制注解:类似Controller
public class UserService implements UserDetailsService {

    //添加点:新建加密方式:bcryt==>可以不写:用于测试
    public static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //1.模拟数据库中的用户数据:这里user是pojo的
    public static Map<String, com.itheima.pojo.User> map = new HashMap<String,com.itheima.pojo.User>();

    //2.使用静态代码加载,模拟的账号密码:这里user是pojo的
    static{
        //用户1
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        //用户2
        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("wzw");
        user2.setPassword(passwordEncoder.encode("123"));

        //添加到模拟数据库map中
        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }


    /**
     * 根据用户名加载用户信息
     * @param username 页面用户输入的账号
     * @return 框架对象
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象
        System.out.println("username:"+ username);
        //2.模拟:根据用户名查询数据库==>因为有两个User,所以要全限定类名
        com.itheima.pojo.User userInDb = map.get(username);
        //3.判断是否有用户
        if (userInDb == null) {
            //根据用户名没有查询到用户，抛出异常，表示登录名输入有误
            return null;
        }

        //4.模拟:获取数据库中的密码
        //  ==>模拟数据库中的密码，后期需要查询数据库
        //  noop ==>开启明文形式
//        String passwordInDb = "{noop}"+ userInDb.getPassword();
        String passwordInDb = userInDb.getPassword();

        //5.授权==>后期需要改为查询数据库动态获取得用户拥有得权限和角色
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("add")); // 添加权限
        list.add(new SimpleGrantedAuthority("delete")); // 删除权限
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // 角色

        //6.将结果返回给springSecurity框架,返回User框架对象
        //参数一：存放登录名，
        //参数二：存放数据库查询的密码（数据库获取的密码，默认会和页面获取的密码进行比对，成功跳转到成功页面，失败回到登录页面，并抛出异常表示失败），
        //参数三:存放授权列表==>当前用户具有的角色(权限集合)
        return new User(username,passwordInDb,list);
    }
}
