package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /**
     * 查询所有检查项数据
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项数据
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询数据
     * @param queryString
     * @return
     */
    Page<CheckItem> findPage(String queryString);

    /**
     * 查询该检查项是否被检查组使用了
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 根据id删除检查项数据
     * @param id
     */
    void deleteById(int id);

    /**
     * 修改检查项
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
