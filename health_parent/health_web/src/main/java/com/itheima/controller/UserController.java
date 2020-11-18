package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐管理
 * @Author: wzw
 * @Date: 2020/11/18 21:06
 * @version: 1.8
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 获取登录人的姓名
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            //权限控制中的User
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                        //SpringSecurity对象.获取容器.获取认证管理器的对象.User对象
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //处理结果集
            //成功,返回名字
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

}
