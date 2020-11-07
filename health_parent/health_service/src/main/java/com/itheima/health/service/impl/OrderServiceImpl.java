package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 提交预约信息
     *
     * @param orderInfo
     * @return
     */
    @Override
    @Transactional
    public Order submit(Map<String, String> orderInfo) {
        //创建SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //接收从前端传过来的日期
        String orderDateStr = orderInfo.get("orderDate");
        //将前端传过来的日期转换成指定格式
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderDateStr);
        } catch (ParseException e) {
//            e.printStackTrace();
            throw new HealthException("预约日期格式不正确");
        }
        //通过预约日期查询预约设置信息
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //判断该日期内是否可以预约
        if (null == orderSetting){
            //抛出异常
            throw new HealthException("所选日期不能预约，请选择其它日期");
        }
        //判断预约数量是否大于大于等于可预约数量
        if (orderSetting.getReservations() >= orderSetting.getNumber()){
            //抛出异常
            throw new HealthException("所选日期不能预约，请选择其它日期");
        }
        //获取从前端传过来的手机号码
        String telephone = orderInfo.get("telephone");
        //通过手机号码查询会员是否存在
        Member member = memberDao.findByTelephone(telephone);
        //创建一个新的订单
        Order order = new Order();
        //设置套餐id
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        //设置订单日期
        order.setOrderDate(orderDate);

        //判断该日是否已预约过-判断是否重复预约 通过member_id, orderDate, setmeal_id 查询订单表
        if (null != member){
            //存在才有可能重复预约
            //设置会员id
            order.setMemberId(member.getId());
            //根据订单数据查询数据是否有重复
            List<Order> orders = orderDao.findByCondition(order);
            //如果集合有数据就是 重复预约
            if (null != orders && orders.size() > 0){
                //则报错，已经预约过了，不能重复预约
                throw new HealthException("已经预约过了，不能重复预约");
            }
        } else {
            //会员不存在 是不可能重复预约
            //新加会员信息
            member = new Member();
            //设置会员名字
            member.setName(orderInfo.get("name"));
            //设置会员性别
            member.setSex(orderInfo.get("sex"));
            //获取身份证号码idCard属性
            String idCard = orderInfo.get("idCard");
            //赋值属性
            member.setIdCard(idCard);
            //设置会员手机号码
            member.setPhoneNumber(telephone);
            //设置创建的时间
            member.setRegTime(new Date());
            //设置密码-取身份证号码的后6位
            member.setPassword(idCard.substring(idCard.length()-6));
            //设置从什么平台创建的
            member.setRemark("微信公众号注册");
            //调用dao添加会员信息
            memberDao.add(member);

            //设置订单的会员id
            order.setMemberId(member.getId());
        }

        //设置订单类型
        order.setOrderType(orderInfo.get("orderType"));
        //初始值为未到诊
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //调勇dao添加订单
        orderDao.add(order);

        //通过日期更新已预约的人数
        int affectedRowCount = orderSettingDao.editReservationsByOrderDate(orderSetting);
        //判断受影响行数-防止出现超卖现象
        if (affectedRowCount == 0){
            //抛出异常提示预约已满
            throw new HealthException("所选日期预约已满,请选择其它日期");
        }

        //返回订单
        return order;
    }

    /**
     * 根据订单id查询订单信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findById4Detail(int id) {
        return orderSettingDao.findById4Detail(id);
    }

    /**
     *  根据当前系统时间去查绚历史预约数据
     * @Param [today]
     * @return java.util.List<java.lang.Integer>
     **/
    @Override
    public List<Integer> findByToday(Date today) {
        return orderSettingDao.findByToday(today);
    }

    /**
     *  删除历史预约数据
     * @Param [count]
     * @return void
     **/
    @Override
    public void delete(Integer count) {
        orderSettingDao.delete(count);
    }
}
