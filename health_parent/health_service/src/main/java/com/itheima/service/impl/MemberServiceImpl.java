package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员实现类
 * @Author: wzw
 * @Date: 2020/11/17 17:35
 * @version: 1.8
 */
@Service(interfaceClass = MemberService.class)
@Transactional //添加事务
public class MemberServiceImpl implements MemberService {
    //注入会员Dao
    @Autowired
    private MemberDao memberDao;

    /**
     * 会员:查询当前用户
     * @param telephone 手机号
     * @return 会员对象
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 会员:添加会员
     * @param member 会员对象
     */
    @Override
    public void add(Member member) {
        //判断是否有密码
         if (member.getPassword() != null){
             //有就加密
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
         //实现功能:添加会员
        memberDao.add(member);
    }
}
