package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.service.ProportionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proportion")
public class ProportionController {


    @Reference
    private ProportionService proportionService;

    /**
     * 获得会员性别占比
     * @return
     */
    @GetMapping("/getMenberGenderProportion")
    public Result getMenberGenderProportion(){
        //调用服务查询会员总数量
        List<Map<String,Object>> genderCount = proportionService.findMenberCount();

        //性别名称集合
        List<String> gender = new ArrayList<String>();

        //抽取性别
        if (null != genderCount){
            for (Map<String, Object> map : genderCount) {
                //获取性别
                gender.add((String) map.get("name"));
            }
        }

        //封装返回的结果
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("gender",gender);
        resultMap.put("genderCount",genderCount);

        return new Result(true,"查询会员所有性别占比成功",resultMap);
    }

    /**
     * 获得会员年龄占比
     * @return
     */
    @GetMapping("/getMenberAgeProportion")
    public Result getMenberAgeProportion(){
        //调用服务层查询所有年龄的集合
        List<Map<String,Object>> ageCount = proportionService.findMemberAgeCount();

        //年龄名称集合
        List<String> age = new ArrayList<String>();

        //抽取各个年龄
        if (null != ageCount){
            for (Map<String, Object> map : ageCount) {
                //获取各个年龄展示在页面
                age.add((String) map.get("name"));
            }
        }

        //封装返回的结果
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("age",age);
        resultMap.put("ageCount",ageCount);

        return new Result(true,"查询所有年龄占比成功",resultMap);
    }
}
