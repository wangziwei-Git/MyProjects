package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 * @Author: wzw
 * @Date: 2020/11/16 16:26
 * @version: 1.8
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    //注入:预约设置Dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    //注入:会员Dao]
    @Autowired
    private MemberDao memberDao;

    //注入:预约的dao
    @Autowired
    private OrderDao orderDao;

    /**
     * 提交预约
     * @param map 接收穿过来的预定信息
     * @return 是否成功
     */
    @Override
    public Result order(Map map) throws Exception {
        try {
            //1.检查当前日期是否可以预约设置
            //1.1获取用户预约时间
            String orderDate = (String) map.get("orderDate");
            //1.2调用工具类:日期转换-  String -> Date
            Date date = DateUtils.parseString2Date(orderDate);
            //1.3实现功能:根据预约日期查询预约设置信息
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
            //1.4判断是否能预约
            if (orderSetting == null) {
                //返回结果(false,当前日期不能预约)
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }

            //2.可预约,看下是否已经预约满了
            //2.1获取已预约人数
            int reservations = orderSetting.getReservations();
            //2.2获取可预约人数
            int number = orderSetting.getNumber();
            //2.3判断是否已预约满
            if(reservations>=number){
                //返回结果(false,预约满了)
                return new Result(false,MessageConstant.ORDER_FULL);
            }

            //3.预约未满,根据手机号,查询当前用户是否是会员
            //3.1获取查询条件:手机号
            String telephone = (String) map.get("telephone");
            //3.2实现功能:查询当前用户是否是会员
            Member member = memberDao.findByTelephone(telephone);

            //3.3如果是会员,防止重复预约(一个会员,一个时间,一个套餐不能重复,否则是重复预定)
            if (member != null) {
                //3.3.1查询参数1:会员id
                Integer memberId = member.getId();
                //3.3.2查询参数2:套餐id
                Integer setmealId = Integer.parseInt((String)map.get("setmealId"));
                //3.3.3封装到order对象中(会员id,预约时间,套餐id)
                Order order = new Order(memberId, date, null, null, setmealId);
                //3.3.4实现功能:查询预约表中是否有重复的
                List<Order> list = orderDao.findByCondition(order);
                //3.3.5判断是否有重复的
                if (list != null && list.size()>0) {
                    //已完成了预约,不能重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }

            //4.当前用户不是会员,自动注册会员
            if (member == null){
                //4.1创建新的会员对象
                member = new Member();
                //4.2添加姓名
                member.setName((String) map.get("name"));
                //4.3添加手机号
                member.setPhoneNumber((String) map.get("telephone"));
                //4.4添加身份证
                member.setIdCard((String) map.get("idCard"));
                //4.5添加性别
                member.setSex((String) map.get("sex"));
                //4.6注册时间(这里不是预约时间)
                member.setRegTime((Date) map.get(new Date()));
                //4.7实现功能:自动注册会员
                memberDao.add(member);
            }

            //5.实现功能:可以预约,更新设置预约人数加一
            orderSettingDao.editReservationsByOrderDate(date);

            //6.保存预约信息到预约表
            //6.1创建一个新的对象
            Order order = new Order(
                    member.getId(),//会员id
                    date,//时间
                    (String) map.get("orderType"),//预约类型:
                    Order.ORDERSTATUS_NO,//预约状态:未到诊
                    Integer.parseInt((String) map.get("setmealId"))
                    );
            //6.2实现功能:添加预约信息
            orderDao.add(order);

            //7.处理结果:执行到这里就是成功了
            return new Result(true,MessageConstant.ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.SYSTEM_FAIL);
        }

    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id 会员id
     * @return map集合:对应的信息
     */
    @Override
    public Map findById4Detail(Integer id) throws Exception {
        //1.实现功能:查询预约信息
        Map map = orderDao.findById4Detail(id);
        //2.判断是否有值
        if (map != null) {
            //3.处理日期格式
            //3.1先拿出时间的value
            Date orderDate = (Date) map.get("orderDate");
            //3.2转换后覆盖到map集合中
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        //4.返回map
        return map;
    }
}
