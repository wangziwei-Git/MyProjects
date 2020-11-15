package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;

/**
 * 套餐:业务接口
 */
public interface SetmealService {

    /**
     * 套餐:分页查询
     * @param currentPage 页码
     * @param pageSize 每页个数
     * @param queryString 条件
     * @return 套餐集合
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 套餐:新增功能
     * @param setmeal 套餐对象
     * @param checkgroupIds 选中的检查组id数组
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 套餐:查询要修改的套餐对象
     * @param setmealId 套餐id
     * @return 套餐对象
     */
    Setmeal findById(Integer setmealId);

    /**
     * 套餐:查询套餐选中的检查组
     * @param setmealId 套餐id
     * @return 被选中的id集合
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    /**
     * * 套餐:修改功能
     *  @param setmeal 修改后的数据
     *  @param checkgroupIds 修改后的检查组id数组
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 套餐:删除功能
     * @param setmealId 被删除的id
     */
    void delete(Integer setmealId);

    /**
     * 微信端套餐:查询所有套餐
     * @return 套餐对象集合
     */
    List<Setmeal> findAll();
}
