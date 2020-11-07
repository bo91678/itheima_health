package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
public interface RoleDao {

    /**
     * 查询所有角色
     * @return
     */
    Set<Role> findAll();

    /**
     * 分页查询角色列表
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);

    Role findByName(String name);

    void add(Role role);

    void addRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    void addRoleMenu(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId);

    Integer findUserRoleByRoleId(Integer id);

    void deleteRolePermissionByRoleId(Integer roleId);

    void deleteRoleMenuByRoleId(Integer roleId);

    void delete(Integer id);

    Role findById(Integer id);

    void update(Role role);
}
