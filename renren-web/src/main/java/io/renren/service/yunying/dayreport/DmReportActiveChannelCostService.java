package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportActiveChannelCostEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 09:27:12
 */
public interface DmReportActiveChannelCostService {
	
	DmReportActiveChannelCostEntity queryObject(BigDecimal statPeriod);
	
	List<DmReportActiveChannelCostEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportActiveChannelCostEntity dmReportActiveChannelCost);
	
	void update(DmReportActiveChannelCostEntity dmReportActiveChannelCost);
	
	void delete(BigDecimal statPeriod);
	
	void deleteBatch(BigDecimal[] statPeriods);

	Map<String, String> getExcelFields();

	List<Map<String, Object>> queryCostList(Map<String, Object> map);
}
