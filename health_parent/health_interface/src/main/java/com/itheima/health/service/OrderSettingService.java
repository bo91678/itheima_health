package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    /**
     * 批量导入订单
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList) throws HealthException;

    /**
     * 获取当前月份预约设置信息
     * @param month
     * @return
     */
    List<Map> getOrderSettingByMonth(String month);

    /**
     * 修改订单数量设置
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
