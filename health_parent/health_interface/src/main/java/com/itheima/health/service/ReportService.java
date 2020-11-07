package com.itheima.health.service;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;

import java.util.Map;

public interface ReportService {
    /**
     * 获取运营统计数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws HealthException;
	
	/**
     * 查询日期
     * @param startDate
     * @param endDate
     * @return
     */
    Result findMemberByDate(String startDate, String endDate);

    /**
     * 展示
     * @return
     */
    Result getMemberReport();
}
