package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;

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

    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();
}
