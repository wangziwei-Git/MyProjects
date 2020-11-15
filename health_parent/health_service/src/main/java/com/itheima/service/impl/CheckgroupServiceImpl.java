package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务接口的实现类
 * 功能实现
 *
 * @Author: wzw
 * @Date: 2020/11/10 0:21
 * @version: 1.8
 */
@Service(interfaceClass = CheckgroupService.class)
@Transactional //开启事务（注意@Service的clss必须加）
public class CheckgroupServiceImpl implements CheckgroupService {

    //注入dao对象
    @Autowired
    private CheckGroupDao checkGroupDao;




    /**
     * 检查组:分页查询
     *
     * @param currentPage 页码
     * @param pageSize    每页数量,
     * @param queryString 条件
     * @return 对象的集合
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        /**
         * 要mybatis插件做
         */
        // 1：初始化
        PageHelper.startPage(currentPage, pageSize);
        // 2：定义条件查询
        List<CheckGroup> list = checkGroupDao.findPageByCondition(queryString);
        // 3：处理成PageHelper支持的javabean
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(list);
        //返回结果
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());

    }

    /**
     * 检查组:添加功能
     *
     * @param checkGroup   添加的检查组对象
     * @param checkitemIds 关联的检查项id数组
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //实现功能:添加检查组对象
        checkGroupDao.add(checkGroup);

        //实现功能：添加中间表的内容
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    /**
     * 检查组：回显要编辑的检查组对象
     * @param checkgroupId 要编辑的检查组
     * @return 回显要编辑的检查组对象
     */
    @Override
    public CheckGroup findById(Integer checkgroupId) {
        return checkGroupDao.findById(checkgroupId);
    }

    /**
     * 检查组：回显要编辑的关系检查项的id（中间表）
     * @param checkgroupId 要编辑的检查组id
     * @return  List<Integer> 会自动转成 Integer数组
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(checkgroupId);
    }

    /**
     * 检查组：修改功能
     * @param checkGroup 修改的检查组对象
     * @param checkitemIds 修改的关联检查项的数组
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1.根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //2.向中间表(t_checkgroup_checkitem)插入数据（重新建立检查组和检查项的关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //3.更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 检查组:删除功能
     * @param checkgroupId 删除的id
     * @return 是否成功
     */
    @Override
    public void deleteById(Integer checkgroupId) {
        //1.// 使用检查组id，查询 检查组 和 检查项 的 中间表
        Integer checkitemCount = checkGroupDao.findCheckGroupAndCheckItemCountByCheckGroupId(checkgroupId);
        if(checkitemCount>0){
            //和检查项有关系，抛出异常和提示
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_OR_CHECKGROUP__FAIL);
        }
        //2. 使用检查组id，查询 套餐 和 检查组 的 中间表
        Integer setmealCount = checkGroupDao.findSetmealAndCheckGroupCountByCheckGroupId(checkgroupId);
        if(setmealCount>0){
            //和套餐有关系，抛出异常和提示
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_OR_CHECKGROUP__FAIL);
        }

        //3.实现功能：删除功能
        checkGroupDao.deleteById(checkgroupId);

    }

    /**
     * 检查组:查询所有
     * @return 检查组集合
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 往检查组检查项中间表插入数据
     * @param checkGroupId 检查组id
     * @param checkitemIds 检查项id数组
     */
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        //判断是否有数据
        if (checkitemIds != null && checkitemIds.length > 0) {
            //循环遍历检查项ids（有多少关联添加多少次）
            for (Integer checkitemId : checkitemIds) {
                //往中间表插入数据t_checkgroup_checkitem     checkgroup_id checkitem_id
                Map<String, Integer> map = new HashMap<>();
                //检查组的id（checkgroup_id要和dao层的映射文件中的插入字段一致）
                map.put("checkgroup_id",checkGroupId);
                //检查项的id（checkitem_id要和dao层的映射文件中的插入字段一致）
                map.put("checkitem_id",checkitemId);
                //实现功能：插入数据到中间表
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }

        }
    }
}
