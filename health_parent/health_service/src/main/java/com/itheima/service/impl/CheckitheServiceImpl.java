package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckitheDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 业务接口实现类
 * @Author: wzw
 * @Date: 2020/11/9 11:37
 * @version: 1.8
 */
@Service
public class CheckitheServiceImpl implements CheckitheService {

    //注入dao层
    @Autowired
    private CheckitheDao checkItemDao;

    /**
     * 查询所有检查项列表
     * @return 查询结果
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 体检检查项管理:添加功能
     * @param checkItem 添加的参数
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 体检检查项管理:分页查询所有功能
     * @param currentPage   //页码
     * @param pageSize  //每页显示的记录数
     * @param queryString   //查询条件
     * @return 总数和对象
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        /**
         * 采用mybatis的分页插件
         */
        // 1：完成对分页初始化工作
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        List<CheckItem> list = checkItemDao.selectByCondition(queryString);
        // 3：后处理，PageHelper会根据查询的结果再封装成PageHealper对应的实体类
        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
        // 组织PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 体检检查项管理:删除功能
     * @param id 删除的id
     */
    @Override
    public void delete(Integer id) {
        //查询当前检查项是否和检查组管理
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            //当前检查项被引用不能删除
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_OR_CHECKGROUP__FAIL);
        }
        //实现功能调用dao层
        checkItemDao.delete(id);
    }

    /**
     * 体检检查项管理:查询修改对象功能
     * @param id   修改id
     * @return 修改对象
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 体检检查项管理:修改功能
     * @param checkItem 修改后的对象
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }


}
