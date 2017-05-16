package io.renren.dao;

import java.util.Map;

import io.renren.entity.DmReportInvestmentDailyEntity;

/**
 * 用户投资情况表
 * 
 */
public interface DmReportInvestmentDailyDao extends BaseDao<DmReportInvestmentDailyEntity> {
	DmReportInvestmentDailyEntity queryTotalList(Map<String, Object> map);
}
