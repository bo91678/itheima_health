package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据登陆用户名称查询用户权限信息
        com.itheima.health.pojo.User userInDB = userService.findByUsername(username);
        //判断查询是否有数据
        if (null != userInDB) {
            //创建权限集合
            List<GrantedAuthority> authorities = new ArrayList<>();
            //获取用户的角色
            Set<Role> roles = userInDB.getRoles();
            if (null != roles) {
                GrantedAuthority sga = null;
                //遍历用户的角色与拥有的权限
                for (Role role : roles) {
                    //角色名, 授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    //授予权限, 这个角色下所拥有的权限
                    Set<Permission> permissions = role.getPermissions();
                    if (null != permissions){
                        //遍历所有的权限
                        for (Permission permission : permissions) {
                            //授予权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }

            //创建权限用户
            User securityUser = new User(username, userInDB.getPassword(), authorities);
            //返回
            return securityUser;
        }
        return null;
    }
}
