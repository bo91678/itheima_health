package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入订单
     *
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) throws HealthException {
        //遍历所有的订单
        for (OrderSetting orderSetting : orderSettingList) {
            //查询数据库判断是否存在, 通过日期来查询
            OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            //判断订单是否为空
            if (null != os){
                //数据库存在这个预约设置, 已预约数量不能大于可预约数量
                //判断添加的订单是否大于可预约的数量
                if (os.getReservations() > orderSetting.getNumber()){
                    //订单数量大于可预约的数量,抛出异常
                    throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
                }
                //数据库中订单的数量小于可预约的数量,执行修改订单命令-更新可预约数量
                orderSettingDao.updateNumber(orderSetting);
            } else {
                //不存在订单就批量添加新的订单
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 获取当前月份预约设置信息
     *
     * @param month
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String month) {
        //定义开始的日期与结束的日期
        String dateBegin = month + "-1";//2019-03-1
        String dateEnd = month + "-31";//2019-03-31

        //创建一个map用来存储日期的开始时间和结束时间
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);

        //调用数据库根据map集合查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);

        //创建一个List集合用来存储返回的数据
        List<Map> data = new ArrayList<>();

        //将List<OrderSetting>，组织成List<Map>
        //遍历当前月份的预约设置
        for (OrderSetting orderSetting : list) {
            //创建一个map集合
            Map orderSettingMap = new HashMap();
            //获取到日期对象,添加进集合中
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            //获取可预约的人数,添加进集合中
            orderSettingMap.put("number", orderSetting.getNumber());
            //获取已预约人数,添加进集合中
            orderSettingMap.put("reservations", orderSetting.getReservations());
            //将map集合数据存放到data集合中返回
            data.add(orderSettingMap);
        }
        return data;
    }

    /**
     * 修改订单数量设置
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询数据库判断是否存在, 通过日期来查询
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //判断订单是否为空
        if (null != os){
            //数据库存在这个预约设置, 已预约数量不能大于可预约数量
            //判断添加的订单是否大于可预约的数量
            if (os.getReservations() > orderSetting.getNumber()){
                //订单数量大于可预约的数量,抛出异常
                throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
            }
            //数据库中订单的数量小于可预约的数量,执行修改订单命令-更新可预约数量
            orderSettingDao.updateNumber(orderSetting);
        } else {
            //不存在订单就批量添加新的订单
            orderSettingDao.add(orderSetting);
        }
    }
}
