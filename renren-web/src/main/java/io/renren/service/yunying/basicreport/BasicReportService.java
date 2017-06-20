package io.renren.service.yunying.basicreport;

import java.util.List;
import java.util.Map;

import io.renren.utils.PageUtils;

/**
 * 
 * 
 */
public interface BasicReportService {

	PageUtils queryList(Integer page, Integer limit, String registerStartTime,String registerEndTime, int start, int end);

	Map<String, String> getExcelFields();
	
	List<Map<String, Object>> queryRegisterThreeDaysNotInvestList(Map<String, Object> params);

	void getAmontByUserId(List<Map<String, Object>> retList);
}
