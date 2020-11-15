package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置:业务接口
 */
public interface OrderSettingService {
    /**
     * 实现功能:添加功能
     * @param settingArrayList 对象集合
     */
    void add(List<OrderSetting> settingArrayList);

    /**
     *  根据指定日期修改可预约人数
     * @param orderSetting 接收日期和修改后的预约人数
     */
    void editNumberByDate(OrderSetting orderSetting);

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date 参数格式为：2019-03
     * @return List<Map>集合
     */
    List<Map> getOrderSettingByMonth(String date);
}
