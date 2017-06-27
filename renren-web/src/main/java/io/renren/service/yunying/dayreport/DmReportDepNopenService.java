package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportDepNopenEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-26 14:36:17
 */
public interface DmReportDepNopenService {
	
	DmReportDepNopenEntity queryObject(String statPeriod);
	
	List<DmReportDepNopenEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportDepNopenEntity dmReportDepNopen);
	
	void update(DmReportDepNopenEntity dmReportDepNopen);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);
	Map<String, String> getExcelFields();
}
