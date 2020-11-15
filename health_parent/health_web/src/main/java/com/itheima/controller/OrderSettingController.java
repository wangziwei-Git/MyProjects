package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 * @Author: wzw
 * @Date: 2020/11/13 20:42
 * @version: 1.8
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    //订阅业务接口
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param date 参数格式为：2019-03
     * @return List<Map>集合
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//参数格式为：2019-03
        try {
            //1.实现功能:根据年月查询预约信息
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //2.处理结果
            //成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }


    }

    /**
     * Excel文件上传,并解析文件内容批量保存到数据库
     * @param excelFile 提交上来的文件
     * @return 是否成功
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try {
            //1.读取Excel文件数据:这个工具类有异常,要try
            List<String[]> list = POIUtils.readExcel(excelFile);

            //2.将List<String[]>转为List<OrderSetting>
            if (list != null && list.size()>0) {
                //2.1创建List<OrderSetting>
                List<OrderSetting> settingArrayList = new ArrayList<>();
                //2.2循环List<String[]>中的内容
                for (String[] strings : list) {
                    //2.3创建实现类:strings == excel 每一行数据[2020-11-15,500]==>(时间,预约人数)
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                    //2.4将每行数据放入集合中(这样就转成List<OrderSetting>集合了)
                    settingArrayList.add(orderSetting);
                }
                //3.实现功能:批量添加功能功能==>>传入List<OrderSetting>参数
                orderSettingService.add(settingArrayList);
            }

            //4.处理结果集
            //成功
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     *  根据指定日期修改可预约人数
     * @param orderSetting 接收日期和修改后的预约人数
     * @return 是否成功
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            //1.实现功能:单个预约设置
            orderSettingService.editNumberByDate(orderSetting);
            //2.处理结果集
            //成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
