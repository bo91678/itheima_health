package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
public interface UserDao {
    /**
     * 根据登陆用户名称查询用户权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 分页查询用户信息
     * @param queryString
     * @return
     */
    Page<User> findPage(String queryString);

    /**
     * 新增用户
     * @param user
     */
    void addUser(User user);

    /**
     * 根据用户id查询关联的角色id
     * @param id
     * @return
     */
    List<Integer> checkRoleIdByUserId(int id);

    /**
     * 删除用户id与角色id的关系
     * @param userId
     */
    void deleteRoleUser(int userId);

    /**
     * 新增用户id与角色id的关系
     * @param roleId
     * @param userId
     */
    void addRoleUser(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

    /**
     * 根据用户id查询用户数据
     * @param userId
     * @return
     */
    User findById(int userId);

    /**
     * 更新用户数据
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteById(int userId);
}
