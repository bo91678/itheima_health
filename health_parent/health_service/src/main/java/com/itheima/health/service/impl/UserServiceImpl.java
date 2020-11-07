package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    /**
     * 根据登陆用户名称查询用户权限信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        //调用dao根据登陆用户名称查询用户权限信息
        return userDao.findByUsername(username);
    }

    /**
     * 分页查询用户
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<User> findPage(QueryPageBean queryPageBean) {
        //调用PageHelper插件启动分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有查询条件
        if (null != queryPageBean.getQueryString()) {
            //若有查询条件，拼接 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //调用持久层findPage方法，返回Page<User>方法
        Page<User> page = userDao.findPage(queryPageBean.getQueryString());
        //封装PageResult<User>并返回
        PageResult<User> pageResult = new PageResult<User>(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 新增用户
     * @param user
     */
    @Override
    public void addUser(User user) {
        //调用UserDao的addUser方法，参数传入User
        userDao.addUser(user);
    }

    /**
     * 根据用户id查询关联的角色id
     * @param id
     * @return
     */
    @Override
    public List<Integer> checkRoleIdByUserId(int id) {
        return userDao.checkRoleIdByUserId(id);
    }

    @Override
    public void UpdateRoleIdByUserId(Integer[] roleIds, Integer userId) {
        //调用UserDao的deleteRoleUser方法，传入用户id
        userDao.deleteRoleUser(userId);
        //判断是否有关联角色
        if (null != roleIds) {
            //若有关联角色，遍历角色id数组，调用UserDao的addRoleUser方法
            for (Integer roleId : roleIds) {
                userDao.addRoleUser(roleId, userId);
            }
        }
    }

    /**
     * 根据用户id查询用户数据
     * @param userId
     * @return
     */
    @Override
    public User findById(int userId) {
        //调用UserDao的findById方法，参数用户id，返回User
        return userDao.findById(userId);
    }

    /**
     * 更新用户数据
     * @param user
     */
    @Override
    public void updateUser(User user) {
        //调用UserDao的updateUser方法，传入User
        userDao.updateUser(user);
    }

    /**
     * 删除用户
     * @param userId
     */
    @Override
    public void deleteById(int userId) {
        //调用userDao的deleteRoleUser方法,参数传入用户id
        userDao.deleteRoleUser(userId);
        //调用UserDao的deleteById方法，参数传入用户id
        userDao.deleteById(userId);
    }
}
