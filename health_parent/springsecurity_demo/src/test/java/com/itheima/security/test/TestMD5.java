package com.itheima.security.test;

import com.itheima.utils.MD5Utils;
import org.junit.Test;

/**
 * @Author: wzw
 * @Date: 2020/11/18 10:23
 * @version: 1.8
 */
public class TestMD5 {
   // @Test
    public void testMD5(){
        // 密码同样是1234却变成了密码不相同
        System.out.println(MD5Utils.md5("1234xiaowang")); //a8231077b3d5b40ffadee7f4c8f66cb7
        System.out.println(MD5Utils.md5("1234xiaoli")); //7d5250d8620fcdb53b25f96a1c7be591
        System.out.println(MD5Utils.md5("wzw"));//b91f7b5679833f44750e8b450c2e8002
    }
}
