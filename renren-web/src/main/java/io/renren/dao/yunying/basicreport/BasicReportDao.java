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

	public int queryFirstInvestNotMultiTotal(Map<String, Object> map);

	public void batchInsertPhoneSaleData(List<Map<String, String>> dataList);

}
