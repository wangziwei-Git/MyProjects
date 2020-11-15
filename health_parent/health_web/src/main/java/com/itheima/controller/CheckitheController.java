package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 体检检查项管理web层
 * @Author: wzw
 * @Date: 2020/11/9 11:16
 * @version: 1.8
 */
@ResponseBody
@RestController
@RequestMapping("/checkitem")
public class CheckitheController {

    @Reference //订阅服务
    private CheckitheService checkItemService;

    /**
     * 查询所有检查项列表
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll() {
        try {
            List<CheckItem> checkItemList = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 体检检查项管理:分页查询所有功能
     * @param queryPageBean 接收参数并封装数据
     * @return 总数和对象
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        //1.实现功能:调用service
        PageResult pageResult = checkItemService.findPage(
                queryPageBean.getCurrentPage(), //页码
                queryPageBean.getPageSize(),    //每页显示的记录数
                queryPageBean.getQueryString()  //查询条件
        );

        //2.返回参数
        return pageResult;
    }

    /**
     * 体检检查项管理:添加功能
     * @param checkItem 接收的参数并封装实体
     * @return 提示对象
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem) {//1.接收参数并封装实体
        try {
            //2.实现功能:调用service业务接口对象
            checkItemService.add(checkItem);
            //3.处理结果集
            //3.1成功,公共类的提示
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //3.2失败,公共类的提示
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 体检检查项管理:删除功能
     * @param id 自动封装要删除的id
     * @return 是否成功
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(Integer id){
        try {
            //1.实现功能:调用service
            checkItemService.delete(id);
            //2.处理结果:
            //2.1成功
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //2.2失败
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

    }

    /**
     * 体检检查项管理:查询修改对象功能
     * @param id 自动接收封装数据:修改id
     * @return   修改的对象
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){
        try {
            //1.实现功能
            CheckItem checkItem = checkItemService.findById(id);
            //2,处理结果集
            //成功
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 体检检查项管理:修改功能
     * @param checkItem 修改后的对象
     * @return 是否成功
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            //1.实现功能:调用service
            checkItemService.edit(checkItem);
            //2.处理结果
            //成功
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);

        }

    }



}
