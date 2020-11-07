package com.itheima.health.dao;

import java.util.List;
import java.util.Map;

public interface ProportionDao {
    /**
     * 查询所有会员性别信息
     * @return
     */
    List<Map<String, Object>> findMenberCount();

    /**
     * 查询所有会员性别信息
     * @return
     */
    List<Map<String, Object>> findMemberAgeCount();
}
