package com.itheima.health.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;
    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        List<Setmeal> setmealList =  setmealService.findAll();
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmealList);
    }
    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        Setmeal setmeal =setmealService.findDetailById(id);
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 根据id查询套餐详情
     * @param id
     * @return
     */
    @PostMapping("/findById")
    public Result findById(int id){
        //调用服务查询套餐详情
        Setmeal setmeal = setmealService.findById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
