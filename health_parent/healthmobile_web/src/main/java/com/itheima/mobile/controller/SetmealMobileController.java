package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wzw
 * @Date: 2020/11/14 19:56
 * @version: 1.8
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    //订阅service
    @Reference
    private SetmealService setmealService;

    /**
     * 微信端套餐:查询所有套餐
     * @return 套餐对象集合
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            //1.实现功能:查询所有
            List<Setmeal> list = setmealService.findAll();
            //2.处理结果集
            //成功
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 微信端套餐:查询一条套餐
     * @param id 查询条件:套餐id
     * @return 套餐对象
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            //1.实现功能:查询一条数据
            Setmeal setmeal = setmealService.findById(id);
            //2.处理结果集
            //成功
            return new Result(true,MessageConstant.GET_SETMEAL_DETAIL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.GET_SETMEAL_DETAIL_FAIL);
        }

    }
}
