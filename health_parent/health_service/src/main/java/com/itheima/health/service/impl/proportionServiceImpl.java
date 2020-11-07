package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.ProportionDao;
import com.itheima.health.service.ProportionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = ProportionService.class)
public class proportionServiceImpl implements ProportionService {

    @Autowired
    private ProportionDao proportionDao;


    /**
     * 查询所有会员性别信息
     * @return
     */
    @Override
    public List<Map<String, Object>> findMenberCount() {
        return proportionDao.findMenberCount();
    }

    /**
     * 查询所有会员性别信息
     * @return
     */
    @Override
    public List<Map<String, Object>> findMemberAgeCount() {
        return proportionDao.findMemberAgeCount();
    }
}
