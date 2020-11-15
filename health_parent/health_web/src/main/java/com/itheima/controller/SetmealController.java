package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐web层
 * @Author: wzw
 * @Date: 2020/11/11 23:40
 * @version: 1.8
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //注入jedis工具类
    @Autowired
    private JedisPool jedisPool;

    //订阅业务接口
    @Reference
    private SetmealService setmealService;

    /**
     * 套餐:分页查询
     * @param queryPageBean 页码/每页个数/条件
     * @return 套餐集合
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        //实现功能
        return setmealService.findPage(
                queryPageBean.getCurrentPage(),//页码
                queryPageBean.getPageSize(),//每页个数
                queryPageBean.getQueryString()//条件
        );
    }

    /**
     * 图片上传
     * @param imgFile 图片文件
     * @return 是否成功
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        try {
            //1.自定义图片名称
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取.后面的后多少位
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称,防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;

            //2.使用QiniuUtils工具类,将图片上传到七牛云中
            // (会出编译时异常)(图片文件转为字节数组,图片名称)
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            //4.将所有(不管成不成功)上传图片名称存入Redis,基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);

            //5.处理结果
            //成功(返回随机出来的文件名)
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 套餐:查询要修改的套餐对象
     * @param setmealId 套餐id
     * @return 套餐对象
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer setmealId){
        try {
            //实现功能
            Setmeal setmeal = setmealService.findById(setmealId);
            //处理结果
            //成功
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }

    }

    /**
     * 套餐:查询套餐选中的检查组
     * @param setmealId 套餐id
     * @return 被选中的id集合
     */
    @RequestMapping(value = "/findCheckGroupIdsBySetmealId",method = RequestMethod.GET)
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId){
       return setmealService.findCheckGroupIdsBySetmealId(setmealId);
    }

    /**
     * 套餐:新增功能
     * @param setmeal 套餐对象
     * @param checkgroupIds 选中的检查组id数组
     * @return 是否成功
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            //实现功能:添加功能
            setmealService.add(setmeal,checkgroupIds);

            //将确定后的图片上传名称存入Redis,基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

            //处理结果集
            //成功
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }

    /**
    * 套餐:修改功能
    * @param setmeal 修改后的数据
    * @param checkgroupIds 修改后的检查组id数组
    * @return 是否成功
    */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            //实现功能:修改套餐
            setmealService.edit(setmeal,checkgroupIds);

            //处理结果集
            //成功
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }

    /**
     * 套餐:删除功能
     * @param setmealId 被删除的id
     * @return 是否删除成功
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(Integer setmealId){
        try {
            //实现功能:删除套餐
            setmealService.delete(setmealId);
            //处理结果集
            //成功
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }




}
