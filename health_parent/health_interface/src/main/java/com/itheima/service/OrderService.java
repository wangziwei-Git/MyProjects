package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * 体检预约服务
 * @Author: wzw
 * @Date: 2020/11/16 16:26
 * @version: 1.8
 */
public interface OrderService {
    /**
     * 提交预约
     * @param map 接收穿过来的预定信息
     * @return 是否成功
     */
    Result order(Map map) throws Exception;

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id 会员id
     * @return map集合:对应的信息
     */
    Map findById4Detail(Integer id) throws Exception;
}
