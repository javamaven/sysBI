package io.renren.service;

import io.renren.entity.DmReportInvestmentDailyEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户投资情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-12 11:16:53
 */
public interface DmReportInvestmentDailyService {
	
	DmReportInvestmentDailyEntity queryObject(String statPeriod);
	
	List<DmReportInvestmentDailyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportInvestmentDailyEntity dmReportInvestmentDaily);
	
	void update(DmReportInvestmentDailyEntity dmReportInvestmentDaily);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);
	
	DmReportInvestmentDailyEntity queryTotalList(Map<String, Object> map);
}
