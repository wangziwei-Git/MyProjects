package com.itheima.service;

import com.itheima.pojo.Member;

/**
 * 会员业务接口
 */
public interface MemberService {
    /**
     * 会员:查询当前用户
     * @param telephone 手机号
     * @return 会员对象
     */
    Member findByTelephone(String telephone);

    /**
     * 会员:添加会员
     * @param member 会员对象
     */
    void add(Member member);
}
