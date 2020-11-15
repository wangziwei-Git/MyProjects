package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置:映射接口
 */
public interface OrderSettingDao {
    /**
     * 查看数据库中否有一样的预约时间
     * @param orderDate 预约时间
     * @return 相同的总数
     */
    Integer findCountByOrderDate(Date orderDate);

    /**
     * 执行修改操作:根据预约时间修改该预约人数
     * @param orderSetting 修改的对象
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 执行添加操作
     * @param orderSetting 添加的对象
     */
    void add(OrderSetting orderSetting);

    /**
     * 查询当前月份的预约设置
     * @param map 查询时间范围
     * @return 对象集合
     */
    List<OrderSetting> getOrderSettingByMonth(Map map);
}
