package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportCashDataEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 11:27:30
 */
public interface DmReportCashDataService {
	
	DmReportCashDataEntity queryObject(BigDecimal statPeriod);
	
	List<DmReportCashDataEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportCashDataEntity dmReportCashData);
	
	void update(DmReportCashDataEntity dmReportCashData);
	
	void delete(BigDecimal statPeriod);
	
	void deleteBatch(BigDecimal[] statPeriods);

	Map<String, String> getExcelFields();
}
