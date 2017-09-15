package io.renren.service.shichang;

import java.util.List;
import java.util.Map;

import io.renren.entity.shichang.DmReportDalilyMarketingEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-31 14:16:57
 */
public interface DmReportDalilyMarketingService {
	
	DmReportDalilyMarketingEntity queryObject(String statPeriod);
	
	List<DmReportDalilyMarketingEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportDalilyMarketingEntity dmReportDalilyMarketing);
	
	void update(DmReportDalilyMarketingEntity dmReportDalilyMarketing);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	List<DmReportDalilyMarketingEntity> queryDayList(Map<String, Object> map);

	List<DmReportDalilyMarketingEntity> queryMonthList(Map<String, Object> map);
	
	List<DmReportDalilyMarketingEntity> queryTotalList(Map<String, Object> map);

}
