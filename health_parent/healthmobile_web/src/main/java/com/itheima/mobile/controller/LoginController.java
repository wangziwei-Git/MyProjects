package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 手机验证码快速登录
 * @Author: wzw
 * @Date: 2020/11/17 9:31
 * @version: 1.8
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    //订阅会员业务接口
    @Reference
    private MemberService memberService;

   //注入redis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 使用手机号和验证码登录
     * @param response 返回Cookie
     * @param map telephone: "18876891992"  validateCode: "8888"
     * @return
     */
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        //1.接收参数
        //1.1.获取前端手机号
        String telephone = (String) map.get("telephone");
        //1.2获取前端验证码
        String validateCode = (String) map.get("validateCode");

        //2.从Redis中获取缓存的验证码,判断验证码输入是否正确
        //2.1要连接池获取验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //2.2校验前端验证码和redis中的验证码是否相同
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //执行到这里说明验证码不相同
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2.3执行到这里:说明验证码输入正确,判断是否是会员
        //2.4实现功能:查询当前用户
        Member member = memberService.findByTelephone(telephone);
        //2.5如果会员不存在
        if(member == null){
            //2.6自动注册
            member = new Member();
            //手机号
            member.setPhoneNumber(telephone);
            //注册时间
            member.setRegTime(new Date());
            //实现功能:添加会员
            memberService.add(member);
        }

        //3如果用户存在:登录成功
        //3.1写入Cookie,跟踪用户,用于分布式系统单点登录
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        //3.2路径
        cookie.setPath("/");
        //3.3有效期30天(单位秒)
        cookie.setMaxAge(60*60*24*30);
        //3.4要response返回
        response.addCookie(cookie);
        //3.5返回结果
        return new Result(true,MessageConstant.LOGIN_SUCCESS);

    }
}
