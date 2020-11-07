package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

public interface MemberService {
    /**
     * 根据电话号码判断是否为会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);

    /**
     * 查询过去12个月会员数据
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
