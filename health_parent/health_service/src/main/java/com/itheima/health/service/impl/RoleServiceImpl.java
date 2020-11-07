package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Description：用户数据维护
 * User：JuZhao
 * Date：2020-11-06
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public Set<Role> findAll() {
        //调用RoleDao的findAll方法，返回Set<Role>
        return roleDao.findAll();
    }

    /**
     * 分页查询角色列表
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        //PageHelper对象的静态方法
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //有条件，模糊查询，拼接%
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //不为空，有条件，则拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //调用Dao，查询语句会被分页（拦截，底层自动截取，拼接sql语句）,配置文件根据查询条件拼接sql
        //page 是PageHelper对象，包装成PageResult对象返回
        //1、解耦 2、page里的total是基本数据类型，序列化时会丢失 3、page对象内容过多
        Page<Role> page = roleDao.findByCondition(queryPageBean.getQueryString());
        //采用线程本地化 threadlocal,把page放入容器
        return new PageResult<Role>(page.getTotal(), page.getResult());
    }

    /**
     * 添加角色信息
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    @Override
    @Transactional
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) throws HealthException {
//根据角色名查询，判断该角色是否存在
        if (null!=roleDao.findByName(role.getName())) {
            //该角色名存在，添加失败
            throw new HealthException("该角色名已存在");
        }
        //添加角色信息
        roleDao.add(role);
        //获取该角色自增长ID
        Integer roleId = role.getId();
        //维护权限角色中间表
        for (Integer permissionId : permissionIds) {
            roleDao.addRolePermission(roleId,permissionId);
        }
        //维护菜单角色中间表
        for (Integer menuId : menuIds) {
            roleDao.addRoleMenu(roleId,menuId);
        }

        //事务控制
    }

    /**
     * 根据ID删除角色信息
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Integer id) throws HealthException {
        //判断该角色是否被用户使用
        Integer count = roleDao.findUserRoleByRoleId(id);
        if (count>0) {
            throw new HealthException("该角色已被用户使用,不能删除");
        }
        //先删除角色权限关系
        roleDao.deleteRolePermissionByRoleId(id);
        //在删除角色菜单关系
        roleDao.deleteRoleMenuByRoleId(id);
        //最后删除角色信息
        roleDao.delete(id);
        //事务控制
    }

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 编辑角色信息
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    @Override
    @Transactional
    public void update(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //添加角色信息
        roleDao.update(role);
        //获取该角色ID
        Integer roleId = role.getId();
        //删除旧的角色权限关系
        roleDao.deleteRolePermissionByRoleId(roleId);
        for (Integer permissionId : permissionIds) {
            //添加新的角色权限关系
            roleDao.addRolePermission(roleId,permissionId);
        }
        //删除旧的角色菜单关系
        roleDao.deleteRoleMenuByRoleId(roleId);
        for (Integer menuId : menuIds) {
            //添加新的角色菜单关系
            roleDao.addRoleMenu(roleId,menuId);
        }
        //事务控制
    }
}
