package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.DmReportInvestmentDailyDao;
import io.renren.entity.DmReportInvestmentDailyEntity;
import io.renren.service.DmReportInvestmentDailyService;



@Service("dmReportInvestmentDailyService")
public class DmReportInvestmentDailyServiceImpl implements DmReportInvestmentDailyService {
	@Autowired
	private DmReportInvestmentDailyDao dmReportInvestmentDailyDao;
	
	@Override
	public DmReportInvestmentDailyEntity queryObject(String statPeriod){
		return dmReportInvestmentDailyDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportInvestmentDailyEntity> queryList(Map<String, Object> map){
		return dmReportInvestmentDailyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportInvestmentDailyDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportInvestmentDailyEntity dmReportInvestmentDaily){
		dmReportInvestmentDailyDao.save(dmReportInvestmentDaily);
	}
	
	@Override
	public void update(DmReportInvestmentDailyEntity dmReportInvestmentDaily){
		dmReportInvestmentDailyDao.update(dmReportInvestmentDaily);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportInvestmentDailyDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportInvestmentDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public DmReportInvestmentDailyEntity queryTotalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportInvestmentDailyDao.queryTotalList(map);
	}
	
}
