package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
public interface RoleService {

    /**
     * 查询所有角色
     * @return
     */
    Set<Role> findAll();

    /**
     * 分页查询角色列表
     * @param queryPageBean
     * @return
     */
    PageResult<Role> findPage(QueryPageBean queryPageBean);

    /**
     * 添加角色信息
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    void add(Role role, Integer[] permissionIds, Integer[] menuIds)throws HealthException;

    /**
     * 根据ID删除角色信息
     * @param id
     */
    void deleteById(Integer id) throws HealthException;

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    Role findById(Integer id);

    void update(Role role, Integer[] permissionIds, Integer[] menuIds) throws HealthException;
}
