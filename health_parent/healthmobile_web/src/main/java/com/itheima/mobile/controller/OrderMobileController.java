package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约
 *
 * @Author: wzw
 * @Date: 2020/11/16 15:41
 * @version: 1.8
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    //订阅接口
    @Reference
    private OrderService orderService;

    //注入redis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约
     * @param map 接收穿过来的预定信息
     * @return 是否成功
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //1.获取预定的手机号码
        String telephone = (String) map.get("telephone");
        //2.从Redis中获取缓存的验证码,key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //3.获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //4.校验手机验证码(不为空说明用户输入的值且redis的验证码没过期,比较验证码内容)
        if (validateCode == null || codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //如果满足上面任意条件,就说明校验失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //5.调用体检预约服务
        Result result=null;

        try {
            //5,1存入预约方式==>微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //5.2实现功能:在service中返回结果
            result = orderService.order(map);
            //6.处理结果集
            //6.1预约成功,发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
            if(result.isFlag()){
                //6.2获取前台传过来的预约时间
                String orderDate = (String) map.get("orderDate");
                //6.3发送短信工具类(发送通知,手机,预约时间)
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            }
            //6.4返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //6.5预约失败:直接返回结果
            return result;
        }
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id 会员id
     * @return map集合:对应的信息
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            //1.实现功能:查询预约信息
            Map map = orderService.findById4Detail(id);
            //2.处理结果集
            //成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }

    }


}
