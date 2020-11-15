package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * 映射接口
 */
public interface CheckGroupDao {

    /**
     *
     * @param id
     * @return
     */
    List<CheckGroup> findCheckGroupListById(Integer id);

    /**
     * 检查组:分页查询
     * @param queryString 条件
     * @return 对象的集合
     */
    List<CheckGroup> findPageByCondition(String queryString);

    /**
     * 检查组:添加对象
     * @param checkGroup 添加的检查组对象
     */
    void add(CheckGroup checkGroup);

    /**
     * 检查组：往检查组检查项中间表插入数据
     * @param map 插入的关系值
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 检查组：回显要编辑的检查组对象
     * @param checkgroupId 要编辑的检查组
     * @return 回显要编辑的检查组对象
     */
    CheckGroup findById(Integer checkgroupId);

    /**
     * 检查组：回显要编辑的关系检查项的id（中间表）
     * @param checkgroupId 要编辑的检查组id
     * @return  List<Integer> 会自动转成 Integer数组
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId);

    /**
     * 检查组：修改功能
     * @param checkGroup 修改的检查组对象
     */
    void edit(CheckGroup checkGroup);

    /**
     * 1.根据检查组id删除中间表数据（清理原有关联关系）
     */
    void deleteAssociation(Integer checkgroupId);

    /**
     * 1.要检查组id查询是否有关联的检查项
     * @param checkgroupId 检查组的id
     * @return 关联检查项的数量
     */
    Integer findCheckGroupAndCheckItemCountByCheckGroupId(Integer checkgroupId);

    /**
     * 2.使用检查组id，查询 套餐 和 检查组 的 中间表
     * @param checkgroupId 检查组的id
     * @return 关联套餐的数量
     */
    Integer findSetmealAndCheckGroupCountByCheckGroupId(Integer checkgroupId);

    /**
     * 检查组:删除功能
     * @param checkgroupId 删除的id
     */
    void deleteById(Integer checkgroupId);

    /**
     * 检查组:查询所有
     * @return 检查组集合
     */
    List<CheckGroup> findAll();

}
