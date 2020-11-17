package com.itheima.mobile.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 发送验证码控制层
 * @Author: wzw
 * @Date: 2020/11/16 12:13
 * @version: 1.8
 */
@RestController
@RequestMapping("/validateCode")
public class MbileValidateCodeController {

    //注入redis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约-发送验证码
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        try {
            //1.生成4位数验证码
            String code = ValidateCodeUtils.generateValidateCode(4)+"";
            //设置倒数30秒
            if(false){
                //2.发送短信=>有异常要try
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
            }
            //3.将验证码打印到控制台
            System.out.println("发送的手机验证码为：" + code);
            //4.将验证码存到redis中,设置时间为5分钟(key,过期时间,value)
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code);
            //5.处理结果
            //成功
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            //失败
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
    }
}
