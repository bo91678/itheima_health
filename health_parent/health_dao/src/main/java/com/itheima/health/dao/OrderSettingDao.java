package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    /**
     * 更新可预约数量
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 查询数据库判断是否存在
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 批量添加新的订单
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据map集合查询当前月份的预约设置
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map map);

    /**
     * 通过日期更新已预约的人数
     * @param orderSetting
     * @return
     */
    int editReservationsByOrderDate(OrderSetting orderSetting);

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    Map<String, Object> findById4Detail(int id);

    /**
     *  根据当前系统时间去查绚历史预约数据
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
