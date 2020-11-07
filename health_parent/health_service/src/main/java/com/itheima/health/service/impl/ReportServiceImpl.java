package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 获取运营统计数据
     *
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws HealthException {
        //创建一个map用来存储返回的数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //创建一个现在的时间
        Date today = new Date();
        //创建时间格式转换API
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        //使用工具类获取星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        //使用工具类获取星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        //使用工具类获取1号(每个月的1号)
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        //获取本月的最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //======会员数量======
        //格式化当前时间
        String reportDate = sdf.format(today);
        //todayNewMember 今日会员增加的数量
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember 总会员数量
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增的会员数量
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增的会员数量
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        //=======订单统计=======
        //todayOrderNumber 今日预约数
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber 今日到诊数
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber 本周预约数
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        //thisWeekVisitsNumber 本周到诊数
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //========热门套餐=======
        //调用dao查询热门套餐
        List<Map<String, Object>> hotSetmeal = orderDao.findHotSetmeal();

        //将参数都添加进map集合中返回
        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);

        return resultMap;
    }
	
	  @Override
    public Result getMemberReport() {
        Map<String, Object> map = null;
        try {
            //创建一个map集合,存放查询到的月份(x轴),和每个月份(y轴)的相对应的数量
            map = new HashMap<String, Object>();

            //创建集合存放月份
            List<String> months = new ArrayList<>();
            //创建集合存放每个月相对应的会员人数
            List<Integer> memberCounts = new ArrayList<>();

            //获取日历对象
            Calendar calendar = Calendar.getInstance();
            //设置日历往前推12个月
            calendar.add(Calendar.MONTH, -12);
            //遍历过去12个月的每一个月
            for (int i = 0; i < 12; i++) {
                //获取每个月的时间
                Date time = calendar.getTime();
                System.out.println(time+"---time---");
                //获取每个月的月份
                String date = new SimpleDateFormat("yyyy-MM").format(time);
                System.out.println(date+"----date---");
                //定义每个月开始日期
                String beginDate = date + "-1";
                //定义每个月结束时间
                String endDate = date + "-31";
                SimpleDateFormat slm = new SimpleDateFormat("yyyy-MM-dd");
                Date b = slm.parse(beginDate);
                Date e = slm.parse(endDate);
                //统计每个月人数
                int count = memberDao.findMemberCountByMonth(b, e);
                //添加每一个月
                months.add(date);
                //添加每一个月的会员数量
                memberCounts.add(count);
                //每次循环都在日历的月份上+1，如-11月,-10月，-9月，依次类推
                calendar.add(Calendar.MONTH, +1);
            }
            //将moths与memberCounts分别存入到map集合中
            map.put("months", months);
            map.put("memberCounts", memberCounts);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL, map);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }




    }

    /**
     * 展示
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Result findMemberByDate(String startDate, String endDate){
        try {
            //创建一个map集合,存放查询到的月份(x轴),和每个月份(y轴)的相对应的数量
            Map<String, Object> map = new HashMap<String, Object>();
            Date sdate = DateUtils.parseString2Date(startDate,"yyyy-MM-dd");
            Date edate = DateUtils.parseString2Date(endDate,"yyyy-MM-dd");
            System.out.println(sdate+"=前台数据===="+edate);

            System.out.println();

            //创建集合存放月份
            List<String> months = new ArrayList<>();
            //创建集合存放每个月相对应的会员人数
            List<Integer> memberCounts = new ArrayList<>();


            int eyear = Integer.parseInt(endDate.substring(0, 4));
            System.out.println(eyear+"年");
            int month1 = edate.getMonth();
            System.out.println(month1+"月");

            int syear = Integer.parseInt(startDate.substring(0, 4));
            System.out.println(syear+"年");
            int month2 = sdate.getMonth();
            System.out.println(month2+"月");
            //
            if (eyear != syear) {
                //年份不一样，则先循环前一年所有会员信息
                //将初始日期时间赋予变量date1
                int month = sdate.getMonth();
                for (int j = month; j < 12; j++) {
                    //定义每个月开始日期
                    String beginDate = syear + "-" + (j+1) + "-1";
                    //定义每个月结束时间
                    String endDatee = syear + "-" + (j+1) + "-31";
                    SimpleDateFormat slm = new SimpleDateFormat("yyyy-MM-dd");
                    Date b = slm.parse(beginDate);
                    Date e = slm.parse(endDatee);
                    //统计每个月会员数量
                    int count = memberDao.findMemberCountByMonth(b, e);
                    //向集合添加年月份
                    String monthh = syear +"-"+ (j+1);
                    months.add(monthh);
                    memberCounts.add(count);
                }
                System.out.println("------2019年-----");
                for (String s : months) {
                    System.out.println(s);
                }
                for (int j = 0; j <= edate.getMonth(); j++) {
                    //定义每个月开始日期
                    String beginDate = eyear + "-" + (j+1) + "-1";
                    //定义每个月结束时间
                    String endDatee = eyear + "-" + (j+1) + "-31";
                    SimpleDateFormat slm = new SimpleDateFormat("yyyy-MM-dd");
                    Date b = slm.parse(beginDate);
                    Date e = slm.parse(endDatee);
                    //统计每个月会员数量
                    int count = memberDao.findMemberCountByMonth(b,e);
                    //向集合添加年月份
                    String monthh = eyear + "-"+(j+1);
                    months.add(monthh);
                    memberCounts.add(count);
                }
                System.out.println("------2020年-----");
                for (String s : months) {
                    System.out.println(s);
                }
            } else {
                //年份相同
                int month = sdate.getMonth();
                for (int i = month; i <= edate.getMonth(); i++) {
                    //定义每个月开始日期
                    String beginDate = eyear+"-"+(i+1)+"-1";
                    //定义每个月结束时间
                    String endDatee = eyear+"-"+(i+1)+"-31";
                    SimpleDateFormat slm = new SimpleDateFormat("yyyy-MM-dd");
                    Date b = slm.parse(beginDate);
                    Date e = slm.parse(endDatee);
                    //统计每个月会员数量
                    int count = memberDao.findMemberCountByMonth(b, e);
                    //向集合添加年月份
                    String monthh = eyear+"-" + (i+1);

                    months.add(monthh);
                    memberCounts.add(count);
                }
            }
            //将moths与memberCounts分别存入到map集合中
            map.put("months", months);
            map.put("memberCounts", memberCounts);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }


    }
}
