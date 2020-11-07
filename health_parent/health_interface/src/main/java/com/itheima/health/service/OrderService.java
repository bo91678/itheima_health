package com.itheima.health.service;

import com.itheima.health.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    /**
     * 提交预约信息
     * @param orderInfo
     * @return
     */
    Order submit(Map<String, String> orderInfo);

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    Map<String, Object> findById4Detail(int id);

    /**
     * 根据当前系统时间去查绚历史预约数据
     * @Param [today]
     * @return java.util.List<java.lang.Integer>
    **/
    List<Integer> findByToday(Date today);

    /**
     *  删除历史预约数据
     * @Param [count]
     * @return void
    **/
    void delete(Integer count);

}
