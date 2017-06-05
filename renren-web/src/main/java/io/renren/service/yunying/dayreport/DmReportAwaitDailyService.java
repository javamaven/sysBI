package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportAwaitDailyEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 10:50:43
 */
public interface DmReportAwaitDailyService {
	
	DmReportAwaitDailyEntity queryObject(String statPeriod);
	
	List<DmReportAwaitDailyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportAwaitDailyEntity dmReportAwaitDaily);
	
	void update(DmReportAwaitDailyEntity dmReportAwaitDaily);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	Map<String, String> getExcelFields();
}
