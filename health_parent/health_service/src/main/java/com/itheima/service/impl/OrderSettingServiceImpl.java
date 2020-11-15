package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置:业务实现类
 *
 * @Author: wzw
 * @Date: 2020/11/13 21:16
 * @version: 1.8
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional //事务
public class OrderSettingServiceImpl implements OrderSettingService {
    //注入Dao层
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 实现功能:批量添加功能
     *
     * @param settingArrayList 对象集合
     */
    @Override
    public void add(List<OrderSetting> settingArrayList) {
        //1.判断对象集合是否为空
        if (settingArrayList != null && settingArrayList.size() > 0){
            //2.循环对象集合
            for (OrderSetting orderSetting : settingArrayList) {
                //预约设置表插入或编辑公共代码
                editAddOrderSetting(orderSetting);
            }
        }
    }

    /**
     * 预约设置表插入或编辑公共代码
     * @param orderSetting 预约设置对象
     */
    private void editAddOrderSetting(OrderSetting orderSetting) {
        //3.检查此数据(日期)是否存在数据库中
        Integer count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        //4.判断是否存在
        if (count>0){
            //存在:执行修改操作:根据预约时间修改该预约人数
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //不存在:执行添加操作
            orderSettingDao.add(orderSetting);
        }
    }

    /**
     * 根据指定日期修改可预约人数
     * @param orderSetting 接收日期和修改后的预约人数
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //预约设置表插入或编辑公共代码
        editAddOrderSetting(orderSetting);
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date 参数格式为：2019-03
     * @return List<Map>集合
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //1.组织查询Map,
        // dateBegin表示月份开始时间,
        String dateBegin = date + "-1";//2019-03-1
        //deteEnd月份结束时间
        String dateEnd = date + "-31";//2019-03-31
        //创建Map集合存放查询时间范围
        Map map = new HashMap();
        //添加开始时间
        map.put("dateBegin",dateBegin);
        //添加结束时间
        map.put("dateEnd",dateEnd);

        //2.实现功能:查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);

        //3.将List<OrderSetting>组织成List<Map>
        //创建List<Map>,存放日期/可预约人数/已预约人数
        List<Map> data = new ArrayList<>();
        //循环List<OrderSetting> 获取 其中的对象
        for (OrderSetting orderSetting : list) {
            //创建map存OrderSetting对象中的值
            Map orderSettingMap = new HashMap();
            //获得日期(几号)
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            //获取可预约人数
            orderSettingMap.put("number",orderSetting.getNumber());
            //获取已预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());
            //添加到List<Map>中
            data.add(orderSettingMap);
        }

        //4.返回List<Map>集合
        return data;
    }
}
