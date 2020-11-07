package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 提交预约信息
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo){
        //校验验证码
        //获取手机号码
        String telephone = orderInfo.get("telephone");
        //通过手机号码获取redis中的验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //如果codeInRedis是空值,说明redis中没有key值,通知用户重新获取key
        if (StringUtils.isEmpty(codeInRedis)){
            return new Result(false, "请重新获取验证码");
        }
        //redis中有key值, 获取从前端传过来的key值(验证码)
        String validateCode = orderInfo.get("validateCode");
        //比较redis中的验证码与前端的验证是否相同
        if (!codeInRedis.equals(validateCode)){
            //两个验证码不同,说明验证不通过-返回false
            return new Result(false, "验证码错误");
        }
        //验证码相同, 删除redis中的验证码,防止重复使用
        jedis.del(key);
        //设置预约类型
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用服务，返回结果给页面,要返回order对象是因为页面中需要用到它的id
        Order order = orderService.submit(orderInfo);
        //返回响应
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }


    /**
     * 根据订单id查询订单状态
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        //调用服务根据订单id查询订单信息
        Map<String, Object> resultMap = orderService.findById4Detail(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, resultMap);
    }
}
