package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入订单
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //调用工具类POIUtils读取excel内容
            List<String[]> readExcel = POIUtils.readExcel(excelFile);

            //转成List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //使用SimpleDateFormat转换日期格式
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

            //创建一个空的日期对象
            Date orderDate = null;
            //创建一个空的orderSetting对象
            OrderSetting os = null;

            //遍历excel的内容
            for (String[] dataArr : readExcel) {
                //转换日期对象
                orderDate = sdf.parse(dataArr[0]);
                //转换成int类型
                int number = Integer.valueOf(dataArr[1]);
                //创建一个订单
                os = new OrderSetting(orderDate, number);
                //将订单存放到list集合中
                orderSettingList.add(os);
            }
            //调用服务导入订单
            orderSettingService.add(orderSettingList);
            //返回响应
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    /**
     * 获取当前月份预约设置信息
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        //调用服务查询预约设置的信息
        List<Map> mapList = orderSettingService.getOrderSettingByMonth(month);
        //返回响应
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, mapList);
    }

    /**
     * 修改订单数量设置
     * @param orderSetting
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        //调用服务执行修改订单数量
        orderSettingService.editNumberByDate(orderSetting);
        //返回响应
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
