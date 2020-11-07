package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 根据电话号码判断是否为会员
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 查询过去12个月会员数据
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        //创建一个list集合用来存储过去一年中的会员数据
        List<Integer> memberCount = new ArrayList<>();
        //对传过来的年份进行判断
        if (months != null) {
            // 循环遍历12个月,每个月的数据都查询一次
            for (String month : months) {
                //调用dao查询每个月的会员数据
                Integer count = memberDao.findMemberCountBeforeDate(month + "-31");
                //将每个月的会员数据添加到集合中
                memberCount.add(count);
            }
        }
        //返回数据
        return memberCount;
    }
}
