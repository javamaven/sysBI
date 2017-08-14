package io.renren.dao.yunying.basicreport;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 */
public interface BasicReportDao {

	public List<Map<String, Object>> queryRegisterThreeDaysNotInvestList(Map<String, Object> params);

	public List<Map<String, Object>> queryFirstInvestNotMultiList(Map<String, Object> map);
	
	public List<Map<String, Object>> queryPhoneSaleCgUserList(Map<String, Object> map);
	
	public int queryFirstInvestNotMultiTotal(Map<String, Object> map);

	public void batchInsertPhoneSaleData(List<Map<String, String>> dataList);

	public void batchInsertPhoneSaleCgUser(List<Map<String, Object>> dataList);

	public int queryPhoneSaleCgUserTotal(Map<String, Object> map);

	public void updatePhoneSaleCgUserList(Map<String, Object> map);
	
	public void batchInsertPhoneSaleJobSendData(List<Map<String, String>> dataList);

}
