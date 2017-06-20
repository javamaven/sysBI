package io.renren.service.yunying.basicreport;

import java.util.Map;

import io.renren.utils.PageUtils;

/**
 * 
 * 
 */
public interface BasicReportService {

	PageUtils queryList(Integer page, Integer limit, String registerStartTime,String registerEndTime, int start, int end);

	Map<String, String> getExcelFields();
}
