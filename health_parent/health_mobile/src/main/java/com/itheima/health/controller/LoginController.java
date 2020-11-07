package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录认证
     *
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody Map<String, String> loginInfo, HttpServletResponse response) {
        //获取手机号码
        String telephone = loginInfo.get("telephone");
        //获取验证码
        String validateCode = loginInfo.get("validateCode");
        //拼接验证码的前缀到redis中查找key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        //获取redis中的验证码
        String codeInRedis = jedis.get(key);
        //判断redis中是否有key
        if (null == codeInRedis) {
            //redis中没有验证码, 失效获取没有还发送
            return new Result(false, "请点击发送验证码");
        }
        //如果前端传过来的验证码与redis中的验证码不相同
        if (!codeInRedis.equals(validateCode)) {
            //返回错误信息
            return new Result(false, "验证码错误");
        }
        //清除验证码，已经使用过了
        jedis.del(key);

        //根据电话号码判断是否为会员
        Member member = memberService.findByTelephone(telephone);
        //如果不是会员就直接添加会员信息
        if (null == member) {
            //添加会员
            member = new Member();
            //注册时间
            member.setRegTime(new Date());
            //手机号码
            member.setPhoneNumber(telephone);
            //注册的方式
            member.setRemark("手机快速注册");

            //调用服务添加会员
            memberService.add(member);
        }

        //将会员信息添加进cookie中
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        //设置cookie的有效时间为1个月
        cookie.setMaxAge(30 * 24 * 60 * 60);
        //访问的路径 根路径下时 网站的所有路径 都会发送这个cookie
        cookie.setPath("/");
        //存入cookie
        response.addCookie(cookie);

        //返回响应
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
