package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 查询所有角色
     * @return
     */
    @PostMapping("/findAll")
    public Result findAll() {
        //调用服务findAll方法，返回Set<Role>
        Set<Role> roleSet = roleService.findAll();
        //封装结果集（flag，message，data）并返回
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleSet);
    }

    /**
     * 分页查询角色列表
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Role> pageResult = roleService.findPage(queryPageBean);
        return new Result(true, "查询角色列表成功", pageResult);
    }

    /**
     * 添加角色信息
     *
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleService.add(role, permissionIds, menuIds);
        return new Result(true, "添加角色信息成功");
    }

    /**
     * 根据ID删除角色信息
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result deleteById(Integer id) {
        roleService.deleteById(id);
        return new Result(true, "删除角色成功");
    }

    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id) {
        Role role = roleService.findById(id);
        return new Result(true, "用户信息回显成功", role);
    }

    /**
     * 编辑角色信息
     *
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleService.update(role, permissionIds, menuIds);
        return new Result(true, "编辑角色信息成功");
    }
}
