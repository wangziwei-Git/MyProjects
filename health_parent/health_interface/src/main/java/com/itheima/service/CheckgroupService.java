package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 业务接口
 *      Service接口
 */
public interface CheckgroupService {

    /**
     * 检查组:分页查询
     *
     * @param currentPage 页码
     * @param pageSize    每页数量,
     * @param queryString 条件
     * @return 查询的对象集合
     */
     PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 检查组:添加功能
     * @param checkGroup 添加的检查组对象
     * @param checkitemIds  关联的检查项id数组
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

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
     * @param checkitemIds 修改的关联检查项的数组
     */
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

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
