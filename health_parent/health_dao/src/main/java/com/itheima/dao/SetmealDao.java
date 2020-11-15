package com.itheima.dao;

import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐:dao层
 */
public interface SetmealDao {
    /**
     * 套餐:分页查询
     * @param queryString 查询条件
     * @return 所有符合条件的套餐
     */
    List<Setmeal> findPage(String queryString);

    /**
     * 套餐:新增功能
     * @param setmeal 套餐对象
     */
    void add(Setmeal setmeal);

    /**
     * 向套餐和检查组的 中间表 插入数据
     * @param map 一个套餐id和一个检查组id
     */
    void setSetmealAndCheckGroup(Map map);

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
     * 套餐:修改功能
     *  @param setmeal 修改后的数据
     */
    void edit(Setmeal setmeal);

    /**
     * 清理套餐在中间表原有的关系
     * @param setmealId 套餐id
     */
    void deleteAssociation(Integer setmealId);

    /**
     * 使用套餐id,查询套餐和检查组中间表
     * @param setmealId 套餐id
     * @return 总数
     */
    Integer findSetmealAndCheckGroupCountBySetmealId(Integer setmealId);

    /**
     * 如果没关联,删除套餐
     * @param setmealId 被删除的id
     */
    void deleteById(Integer setmealId);

    /**
     * 微信端套餐:查询所有套餐
     * @return 套餐对象集合
     */
    List<Setmeal> findAll();

}
