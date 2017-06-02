package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportFcialPlanDailyEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 11:04:27
 */
public interface DmReportFcialPlanDailyService {
	
	DmReportFcialPlanDailyEntity queryObject(String statPeriod);
	
	List<DmReportFcialPlanDailyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportFcialPlanDailyEntity dmReportFcialPlanDaily);
	
	void update(DmReportFcialPlanDailyEntity dmReportFcialPlanDaily);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	Map<String, String> getExcelFields();
}
