package com.itheima.job;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * job执行类
 *      每10秒根据redis的记录清除在七牛云上的垃圾图片
 * @Author: wzw
 * @Date: 2020/11/13 17:12
 * @version: 1.8
 */
public class ClearImgJob {
    //注入
    @Autowired
    JedisPool jedisPool;

    // 删除Redis中2个不同key值存储的不同图片
    //RedisConstant.SETMEAL_PIC_RESOURCES是key1
    //RedisConstant.SETMEAL_PIC_DB_RESOURCES是key2
    public void clearImg(){
        //提示定时任务被调用了
        System.out.println("定时任务调度执行了...."+ new Date());
        //====================以上是测试定时任务调度框架,以下是清理七牛云垃圾图片========================
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("删除的图片："+pic);
            // 删除七牛云上的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除Redis中key值为SETMEAL_PIC_RESOURCE的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
