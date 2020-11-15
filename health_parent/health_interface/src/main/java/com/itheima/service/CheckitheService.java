package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 查项列表业务接口
 * @Author: wzw
 * @Date: 2020/11/9 11:36
 * @version: 1.8
 */
public interface CheckitheService {
    /**
     * 查询所有检查项列表
     * @return 查询结果
     */
    List<CheckItem> findAll();

    /**
     * 体检检查项管理:添加功能
     * @param checkItem 添加的参数
     */
    void add(CheckItem checkItem);

    /**
     * 体检检查项管理:分页查询所有功能
     * @param currentPage   //页码
     * @param pageSize  //每页显示的记录数
     * @param queryString   //查询条件
     * @return 总数和对象
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     *  体检检查项管理:删除功能
     * @param id 删除的id
     */
    void delete(Integer id);

    /**
     * 体检检查项管理:查询修改对象功能
     * @param id   修改id:
     * @return  修改的对象
     */
    CheckItem findById(Integer id);

    /**
     * 体检检查项管理:修改功能
     * @param checkItem 修改后的对象
     */
    void edit(CheckItem checkItem);
}
