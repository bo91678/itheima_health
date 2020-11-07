package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项数据
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询检查项数据
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除检查项数据
     * @param id
     */
    void deleteById(int id)throws HealthException;

    /**
     * 修改检查项数据
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 根据id查询用户信息-编辑回显数据
     * @param id
     * @return
     */
    CheckItem findById(int id);
}
