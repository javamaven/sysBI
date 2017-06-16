package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportVipSituationEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-16 13:41:01
 */
public interface DmReportVipSituationService {
	
	DmReportVipSituationEntity queryObject(BigDecimal statPeriod);
	
	List<DmReportVipSituationEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportVipSituationEntity dmReportVipSituation);
	
	void update(DmReportVipSituationEntity dmReportVipSituation);
	
	void delete(BigDecimal statPeriod);
	
	void deleteBatch(BigDecimal[] statPeriods);

	Map<String, String> getExcelFields();
}
