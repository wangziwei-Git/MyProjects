package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组web层,接收前端发过来的数据
 * @Author: wzw
 * @Date: 2020/11/10 0:01
 * @version: 1.8
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {

    @Reference  //订阅业务接口
    private CheckgroupService checkgroupService;

    /**
     * 检查组:查询所有
     * @return 检查组集合
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){
        //实现功能:查询所有检查组
        List<CheckGroup> checkGroupList = checkgroupService.findAll();
        //判断是否有值
        if(checkGroupList != null && checkGroupList.size() > 0){
            //失败
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        }
        //成功
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 检查组:分页查询
     * @param queryPageBean 页码,每页数量,条件
     * @return 查询的对象集合
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            //1.实现功能:分页查询
            PageResult pageResult = checkgroupService.findPage(
                    queryPageBean.getCurrentPage(),//页码
                    queryPageBean.getPageSize(),//每页大小
                    queryPageBean.getQueryString()//条件
            );
            //2.处理结果集
            return pageResult;
    }

    /**
     * 检查组:添加功能
     * @param checkGroup 添加的检查组对象
     * @param checkitemIds  关联的检查项id数组
     * @return 是否成功
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            //实现功能：调用业务接口service
            checkgroupService.add(checkGroup,checkitemIds);
            //处理结果集
            //成功
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查组：回显要编辑的检查组对象
     * @param checkgroupId 要编辑的检查组id
     * @return 回显要编辑的检查组对象
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer checkgroupId){
        try {
            //1.实现功能：查询一条检查组数据
            CheckGroup checkGroup = checkgroupService.findById(checkgroupId);
            //2.处理结果
            //成功
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查组：回显要编辑的关系检查项的id（中间表）
     * @param checkgroupId 要编辑的检查组id
     * @return  List<Integer> 会自动转成 Integer数组
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId",method = RequestMethod.GET)
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId){
        //实现功能
        return checkgroupService.findCheckItemIdsByCheckGroupId(checkgroupId);
    }

    /**
     * 检查组：修改功能
     * @param checkGroup 修改的检查组对象
     * @param checkitemIds 修改的关联检查项的数组
     * @return 是否成功
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            //实现功能：编辑数据
            checkgroupService.edit(checkGroup,checkitemIds);
            //处理结果集
            //成功
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 检查组:删除功能
     * @param checkgroupId 删除的id
     * @return 是否成功
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result deleteById(Integer checkgroupId){
        try {
            //实现功能：删除一条数据
            checkgroupService.deleteById(checkgroupId);
            //处理结果集
            //成功
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e){
            //因有关系导致删除失败的返回
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
}
