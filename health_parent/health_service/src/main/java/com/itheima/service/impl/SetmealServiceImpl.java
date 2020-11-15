package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐:service实现类
 * @Author: wzw
 * @Date: 2020/11/11 23:51
 * @version: 1.8
 */
@Service(interfaceClass = SetmealService.class)
@Transactional //事务
public class SetmealServiceImpl implements SetmealService {

    //注入dao层
    @Autowired
    private SetmealDao setmealDao;

    //注入FreemarkerConfigurer
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    //从属性文件读取输出目录的路径
    @Value("${out_put_path}")
    private String outputpath;



    /**
     * 套餐:分页查询
     * @param currentPage 页码
     * @param pageSize 每页个数
     * @param queryString 条件
     * @return 套餐集合
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        /**
         * 要mybatis做分页查询
         */
        //1.初始化
        PageHelper.startPage(currentPage,pageSize);
        //2.定义条件查询
        List<Setmeal> list = setmealDao.findPage(queryString);
        //3.处理成PageHelper支持的JavaBean
        PageInfo<Setmeal> pageInfo = new PageInfo<>(list);
        //4.返回结果集
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 套餐:新增功能
     * @param setmeal 套餐对象
     * @param checkgroupIds 选中的检查组id数组
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.实现功能:往套餐表插入数据
        setmealDao.add(setmeal);
        //2.向套餐和检查组的 中间表 插入数据
        if(checkgroupIds != null && checkgroupIds.length>0){
            //绑定套餐和检查组的多对多关系(套餐id,检查组id数组)
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
        //Freemarker添加点：新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    private void generateMobileStaticHtml() {
        //1.准备模板文件需要的数据
        //调用本类的：查询所有方法（包括新增的那条）
        List<Setmeal> setmealList = this.findAll();
        //2.生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //3.生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 生成套餐列表静态页面
     */
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<>();
        //存所有套餐信息
        dataMap.put("setmealList",setmealList);
        //调用创建静态页面化方法（模板，静态页面名，需要生成页面的数据）
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    /**
     * 生成套餐详情静态页面（多个）
     * @param setmealList
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        //循环所有套餐列表
        for (Setmeal setmeal : setmealList) {
            //存套餐对象：内有检查组和检查项信息
            HashMap<String, Object> dataMap = new HashMap<>();
            //要查询一个套餐的方法，循环查询关联的setmeal的id的套餐详情（获得检查组和检查项信息）
            dataMap.put("setmeal",this.findById(setmeal.getId()));
            //调用创建静态页面化方法（模板，静态页面名(保证页面不重复)，需要生成页面的数据）
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);


        }
    }

    /**
     * 调用公共方法生成静态页面
     * @param templateName 模板名称
     * @param htmlPageName 生成静态页面文件名称（m_setmeal.html）或(setmeal_datail_${setmeal.id}.html)
     * @param dataMap 生成静态页面需要的数据
     */
    private void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap) {
        //1.模板配置类对象(底层方法)
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //2.声明写（在关流的时候用）
        Writer out = null;
        try {
            //3.加载模板文件
            Template template = configuration.getTemplate(templateName);
            //4.生成数据（properties配置文件中的路径+静态页面.html）
            File docFile = new File(outputpath + "/" + htmlPageName);
            //5.FileWrite操作磁盘的 BufferedWriter操作缓存的【效率高|推荐使用】
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //6.输出文件(页面需要的数据，静态页面存放的路径)
            template.process(dataMap,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关流
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 套餐:查询要修改的套餐对象
     * @param setmealId 套餐id
     * @return 套餐对象
     */
    @Override
    public Setmeal findById(Integer setmealId) {
        return setmealDao.findById(setmealId);
    }

    /**
     * 套餐:查询套餐选中的检查组
     * @param setmealId 套餐id
     * @return 被选中的id集合
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId) {
        return setmealDao.findCheckGroupIdsBySetmealId(setmealId);
    }

    /**
     * * 套餐:修改功能
     *  @param setmeal 修改后的数据
     *  @param checkgroupIds 修改后的检查组id数组
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //实现功能:修改套餐
        setmealDao.edit(setmeal);

        //实现功能:清理套餐在中间表原有的关系
        setmealDao.deleteAssociation(setmeal.getId());

        //实现功能:修改中间表
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
    }

    /**
     * 套餐:删除功能
     * @param setmealId 被删除的id
     */
    @Override
    public void delete(Integer setmealId) {
        //使用套餐id,查询数据库对应的套餐,数据库存放的img名称
        Setmeal setmeal_db = setmealDao.findById(setmealId);
        //使用套餐id,查询套餐和检查组中间表
        Integer count =setmealDao.findSetmealAndCheckGroupCountBySetmealId(setmealId);
        //判断是否有关联
        if(count>0){
            //抛出异常
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_OR_CHECKGROUP__FAIL);
        }
        //如果没关联,删除套餐
        setmealDao.deleteById(setmealId);
        //获取存放的图片名称
        String img = setmeal_db.getImg();
        //需要先删除七牛云之前数据库的图片
        if(img != null && !"".equals(img)){
            //调用七牛云工具类删除数据
            QiniuUtils.deleteFileFromQiniu(img);
        }

    }

    /**
     * 微信端套餐:查询所有套餐
     * @return 套餐对象集合
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }


    /**
     * 向套餐和检查组的 中间表 插入数据
     * @param setmealId 套餐id,
     * @param checkgroupIds 检查组id数组
     */
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        //选中多少个检查组就要插入多少次
        for (Integer checkgroupId : checkgroupIds) {
            //将要插入中表的中存到Map中传给dao
            Map map = new HashMap();
            //添加套餐id
            map.put("setmeal_id",setmealId);
            //添加选中的检查组id
            map.put("checkgroup_id",checkgroupId);
            //实现功能:向中间表插入数据
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
