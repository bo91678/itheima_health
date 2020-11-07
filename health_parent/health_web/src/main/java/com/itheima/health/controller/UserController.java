package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.apache.zookeeper.data.Id;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 获取用户名
     * @return
     */
    @GetMapping("/getUsername")
    public Result getUsername(){
        //从权限中获取
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //打印一下用户名
        System.out.println("登陆的用户名:" + user.getUsername());
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
    }

    /**
     * 登陆成功
     * @return
     *//*
    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        //返回响应
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    *//**
     * 登陆失败
     * @return
     *//*
    @RequestMapping("/loginFail")
    public Result loginFail(){
        //返回失败响应
        return new Result(false, "用户名或密码错误!!!");
    }*/

    /**
     * 分页查询用户
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        //调用服务findPage方法，传入QueryPageBean，返回PageResult<User>
        PageResult<User> pageResult = userService.findPage(queryPageBean);
        //封装结果集（flag、message、data）并返回
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, pageResult);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("addUser")
    public Result addUser(@RequestBody User user) {
        //调用业务层addUser方法，参数传入User
        userService.addUser(user);
        //封装结果集（flag，message）并返回
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 根据用户id查询关联的角色id
     * @param id
     * @return
     */
    @PostMapping("checkRoleIdByUserId")
    public Result checkRoleIdByUserId(int id) {
        //调用服务CheckRoleIdByUserId方法，参数传入用户id，返回角色id集合
        List<Integer> roleIds = userService.checkRoleIdByUserId(id);
        //封装结果集（flag，message，data）并返回
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleIds);
    }

    /**
     * 关联用户id与角色id
     * @param roleIds
     * @param user
     * @return
     */
    @PostMapping("/updateRoleIdByUserId")
    public Result updateRoleIdByUserId(Integer[] roleIds,@RequestBody User user) {
        int userId = user.getId();
        //调用服务UpdateRoleIdByUserId方法，传入角色id数组，用户id
        userService.UpdateRoleIdByUserId(roleIds, userId);
        //封装结果集并返回
        return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
    }

    /**
     * 根据用户id查询用户数据
     * @param userId
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int userId) {
        //调用服务findById方法，参数传入用户id，返回User
        User user = userService.findById(userId);
        //封装结果集（flag，message，data）并返回
        return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
    }

    /**
     * 更新用户数据
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        //调用服务updateUser方法，参数User
        userService.updateUser(user);
        //封装结果集（flag，message）
        return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id) {
        //调用服务deleteById方法，参数传入用户id
        userService.deleteById(id);
        //封装结果集（flag，message）并返回
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }
}
