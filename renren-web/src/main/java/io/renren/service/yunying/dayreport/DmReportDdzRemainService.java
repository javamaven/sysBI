package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportDdzRemainEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-14 13:42:18
 */
public interface DmReportDdzRemainService {
	
	DmReportDdzRemainEntity queryObject(BigDecimal statPeriod);
	
	List<DmReportDdzRemainEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportDdzRemainEntity dmReportDdzRemain);
	
	void update(DmReportDdzRemainEntity dmReportDdzRemain);
	
	void delete(BigDecimal statPeriod);
	
	void deleteBatch(BigDecimal[] statPeriods);

	Map<String, String> getExcelFields();
}
