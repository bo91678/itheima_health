package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;



    /**
     * 获取过去一年内的会员数据
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        return reportService.getMemberReport();
    }


    /**
     * 查询
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("/findMemberByDate")
    public Result findMemberByDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
       return reportService.findMemberByDate(startDate,endDate);
    }


    /**
     * 获取套餐预约占比
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //调用服务查询套餐数量
        List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();

        //创建一个list集合用来存储套餐的名称
        List<String> setmealNames = new ArrayList<>();

        //判断返回的套餐集合数据
        if (null != setmealCount){
            //遍历套餐集合获取套餐名称
            for (Map<String, Object> map : setmealCount) {
                //获取套餐名称添加进 setmealNames 集合中
                setmealNames.add((String) map.get("name"));
            }
        }

        //创建一个map集合用来存储套餐的返回数据
        Map<String, Object> resultMap = new HashMap<>();
        //添加套餐名称
        resultMap.put("setmealNames", setmealNames);
        //添加套餐集合
        resultMap.put("setmealCount", setmealCount);

        //返回响应
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }

    /**
     * 获取运营统计数据
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        //调用服务获取运营统计数据
        Map<String, Object> businessReport = reportService.getBusinessReportData();
        //返回响应
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, businessReport);
    }

    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        //获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");

        try {
            //创建工作簿
            XSSFWorkbook wk = new XSSFWorkbook(template);
            // 响应获取输出流对象
            OutputStream os = response.getOutputStream();

            //获取工作簿
            XSSFSheet sht = wk.getSheetAt(0);
            //调用服务获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReportData();
            //日期  坐标 2,5
            sht.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());

            //======================== 会员 ===========================
            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));

            //=================== 预约 ============================
            sht.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>> )reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String)setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long)setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String)setmealMap.get("remark"));
                row++;
            }

            // 工作簿写给reponse输出流
            response.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            // 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            response.setHeader("Content-Disposition","attachement;filename=" + filename);
            wk.write(os);
            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/exportBusinessReportPdf")
    public void exportBusinessReportPdf(HttpServletRequest request, HttpServletResponse response){
        //获取模板的路径 getRealPath("/") 相当于到webapp目录下
        String basePath = request.getSession().getServletContext().getRealPath("/template");

        //设置jrcml的路径
        String jrxml = basePath + File.separator + "report_business.jrxml";
        //设置jasper路径
        String jasper = basePath + File.separator + "report_business.jasper";

        try {
            //编译模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            //调用服务查询数据库数据
            Map<String, Object> reportData = reportService.getBusinessReportData();
            //构建给field使用的数据 热门套餐
            List<Map<String, Object>> hotSetmealList = (List<Map<String, Object>>)reportData.get("hotSetmeal");
            //创建JRBeanCollectionDataSource对象
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(hotSetmealList);

            //填充数据到print
            JasperPrint print = JasperFillManager.fillReport(jasper, reportData, dataSource);
            //设置内容体格式
            response.setContentType("application/pdf");
            //响应头信息
            response.setHeader("Content-Disposition","attachment;filename=reportBusiness.pdf");

            //使用response的输出流导出
            JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

       // return new Result(false, "导出Pdf文件失败");
    }
}
