package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * 预约Dao
 */
public interface OrderDao {
    /**
     * 查询预约表中是否有重复的
     * @param order 预约的时间和会员id和套餐id
     * @return 可能会重复多个
     */
    List<Order> findByCondition(Order order);

    /**
     * 实现功能:添加预约信息
     * @param order 添加对象
     */
    void add(Order order);

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id 会员id
     * @return map集合:对应的信息
     */
    Map findById4Detail(Integer id);
}
