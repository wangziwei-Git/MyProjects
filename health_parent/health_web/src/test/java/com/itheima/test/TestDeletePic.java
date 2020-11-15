package com.itheima.test;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author: wzw
 * @Date: 2020/11/12 21:19
 * @version: 1.8
 */

@ContextConfiguration(locations = "classpath:spring-redis.xml")
//@RunWith(value = SpringJUnit4ClassRunner.class)
public class TestDeletePic {

    @Autowired
    JedisPool jedisPool;

    // 删除Redis中2个不同key值存储的不同图片
    //RedisConstant.SETMEAL_PIC_RESOURCES是key1
    //RedisConstant.SETMEAL_PIC_DB_RESOURCES是key2
    //@Test
    public void test(){
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
