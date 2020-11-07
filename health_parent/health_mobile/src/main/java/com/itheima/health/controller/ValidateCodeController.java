package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    /**
     * 定义log日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送体检预约的验证码
     *
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        //定义一个key值,用来存储用户手机的验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //从redis中取出验证码,如果有值说明已经发送过了
        Jedis jedis = jedisPool.getResource();

        //取出redis中的key值(验证码)
        String codeInRedis = jedis.get(key);
        //判断验证码是否为空
        if (StringUtils.isEmpty(codeInRedis)) {
            //redis中的验证码为空,说明没有验证码
            //调用工具类生成随机的6位验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
            //调用SMSUtils发送验证码
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                //生成日志输出到后台
                log.debug("验证码发送成功, 手机: {}, 验证码: {}", telephone, code);
            } catch (ClientException e) {
                //e.printStackTrace();
                //使用日志输出异常信息
                log.debug("发送验证码失败", e);
            }

            //存入redis，加入有效时间，过期失效 -- 设置验证码的存活时间为十分钟
            jedis.setex(key, 10 * 60, code);
            //返回响应
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } else {
            //redis中有值,说明发送过了 -- 直接返回响应
            return new Result(false, "验证码已经发送过了，请注意查收");
        }
    }

    /**
     * 获取手机验证码登录
     *
     * @param telephone
     * @return
     */
    @PostMapping("/send4Login")
    public Result send4Login(String telephone) {
        //通过redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        //拼接存放redis中的前缀
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //获取redis中的key
        String codeInRedis = jedis.get(key);
        //判断是否有值
        if (null == codeInRedis) {
            //不存在，没发送，生成验证码，调用SMSUtils.发送验证码，把验证码存入redis(5,10,15分钟有效期)，value:验证码, key:手机号码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                //发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                //存入redis中, 有效时间为10分钟
                jedis.setex(key, 10*60, code +"");
                //返回响应
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                //发送失败
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //存在：验证码已经发送了，请注意查收
        return new Result(false, "验证码已发送,请注意查收");
    }
}
