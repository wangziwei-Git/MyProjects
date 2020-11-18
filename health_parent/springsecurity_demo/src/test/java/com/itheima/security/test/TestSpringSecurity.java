package com.itheima.security.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: wzw
 * @Date: 2020/11/18 10:27
 * @version: 1.8
 */
public class TestSpringSecurity {
    //@Test
    public void testSpringSecurity(){
        // $2a$10$dyIf5fOjCRZs/pYXiBYy8uOiTa1z7I.mpqWlK5B/0icpAKijKCgxe
        // $2a$10$OphM.agzJ55McriN2BzCFeoLZh/z8uL.8dcGx.VCnjLq01vav7qEm

        //加密
        //1.创建BCrypt自动加盐加密对象
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //加密
        String s = encoder.encode("abc");
        System.out.println(s);
        //加密
        String s1 = encoder.encode("abc");
        System.out.println(s1);

        // 校验:进行判断(加密后判读)
        boolean b = encoder.matches("abc", "$2a$10$dyIf5fOjCRZs/pYXiBYy8uOiTa1z7I.mpqWlK5B/0icpAKijKCgxe");
        System.out.println(b);
    }
}
