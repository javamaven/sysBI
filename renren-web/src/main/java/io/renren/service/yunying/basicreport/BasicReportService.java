package io.renren.service.yunying.basicreport;

import java.util.List;
import java.util.Map;

import io.renren.utils.PageUtils;

/**
 * 
 * 
 */
public interface BasicReportService {

	PageUtils queryList(Integer page, Integer limit, String registerStartTime,String registerEndTime, int start, int end, String queryType);

	Map<String, String> getExcelFields();
	
	List<Map<String, Object>> queryRegisterThreeDaysNotInvestList(Map<String, Object> params);

	void getAmontByUserId(List<Map<String, Object>> retList);

	PageUtils queryFirstInvestNotMultiList(Map<String, Object> map);

	Map<String, String> getExcelFirstInvestNotMultiFields();

	void batchInsertPhoneSaleData(List<Map<String, String>> dataList);
	//筛选开通存管版用户
	void batchInsertPhoneSaleCgUser(List<Map<String, Object>> list);
	
	public List<Map<String, Object>> queryPhoneSaleCgUserList(Map<String, Object> map);

	int queryPhoneSaleCgUserTotal(Map<String, Object> map);

	void updatePhoneSaleCgUserList(Map<String, Object> map);
	
	void batchInsertPhoneSaleJobSendData(List<Map<String, String>> dataList);
}
