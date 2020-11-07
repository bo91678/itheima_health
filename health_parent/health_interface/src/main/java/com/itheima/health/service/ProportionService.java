package com.itheima.health.service;

import java.util.List;
import java.util.Map;

public interface ProportionService {

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
