package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;

import java.util.List;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
public interface UserService {

    /**
     * 根据登陆用户名称查询用户权限信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 分页查询用户
     *
     * @param queryPageBean
     * @return
     */
    PageResult<User> findPage(QueryPageBean queryPageBean);

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
     * 关联用户id和角色id
     * @param roleIds
     * @param id
     */
    void UpdateRoleIdByUserId(Integer[] roleIds, Integer id);

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
