package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 15:24:09
 */
public interface DmReportBasicDailyService {
	
	DmReportBasicDailyEntity queryObject(String statPeriod);
	
	List<DmReportBasicDailyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportBasicDailyEntity dmReportBasicDaily);
	
	void update(DmReportBasicDailyEntity dmReportBasicDaily);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	Map<String, String> getExcelFields();
}
