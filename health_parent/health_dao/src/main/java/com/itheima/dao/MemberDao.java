package com.itheima.dao;

import com.itheima.pojo.Member;

/**
 * 会员Dao层持久层
 */
public interface MemberDao {
    /**
     * 查询当前用户是否是会员
     * @param telephone 手机号
     * @return 对象
     */
    Member findByTelephone(String telephone);

    /**
     * 添加:自动注册会员
     * @param member 添加的会员
     */
    void add(Member member);
}
