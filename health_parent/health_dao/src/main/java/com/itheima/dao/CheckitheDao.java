package com.itheima.dao;


import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项映射接口
 */
public interface CheckitheDao {
    /**
     * 查询所有检查项列表
     * @return 查询结果
     */
    List<CheckItem> findAll();

    /**
     * 体检检查项管理:添加功能
     * @param checkItem 添加的数据
     */
    void add(CheckItem checkItem);

    /**
     * 体检检查项管理:分页查询所有功能
     * @param queryString   //查询条件
     * @return 总数和对象
     */
    List<CheckItem> selectByCondition(String queryString);

    /**
     * 查询当前检查项是否和检查组关联
     * @param id 删除的id
     * @return 关联的数量
     */
    long findCountByCheckItemId(Integer id);

    /**
     * 体检检查项管理:删除功能
     * @param id 删除的id
     */
    void delete(Integer id);

    /**
     * 体检检查项管理:查询修改对象功能
     * @param id 修改id
     * @return 修改对象
     */
    CheckItem findById(Integer id);

    /**
     * 体检检查项管理:修改功能
     * @param checkItem 修改对象
     */
    void edit(CheckItem checkItem);

    /**
     * 根据检查组id查询检查项信息
     * @param id
     * @return
     */
    List<CheckItem> findCheckItemListById(Integer id);
}
