package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportRecoverDataEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 14:33:45
 */
public interface DmReportRecoverDataService {
	
	DmReportRecoverDataEntity queryObject(BigDecimal statPeriod);
	
	List<DmReportRecoverDataEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportRecoverDataEntity dmReportRecoverData);
	
	void update(DmReportRecoverDataEntity dmReportRecoverData);
	
	void delete(BigDecimal statPeriod);
	
	void deleteBatch(BigDecimal[] statPeriods);

	Map<String, String> getExcelFields();
}
