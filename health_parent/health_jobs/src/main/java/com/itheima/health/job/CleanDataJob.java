package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  定时清理预约的历史数据
 * @Param
 * @return
**/
@Component
public class CleanDataJob {

    //订阅服务
    @Reference
    private OrderService orderService;

    /**
     * 定时清理历史预约数据
     *
     * @return
     * @Param
     **/

    //@Scheduled(cron = "0 0 5 L * ?")   每月的最后一天凌晨五点执行

    //测试 每过一个小时执行一次,执行后 延迟三秒后启动
    @Scheduled(fixedDelay = 3600000, initialDelay = 3000)
    public void cleanData() throws ParseException {

        //获取当前系统时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = sdf.format(date);
        Date today = sdf.parse(todayString);
        //先根据当前日期去数据库查绚历史的数据的id值
        List<Integer> countList = orderService.findByToday(today);
        //再根据查绚到的历史数据的id去删除
        if (countList != null) {
            countList.forEach(count -> orderService.delete(count));
        }
    }

}
