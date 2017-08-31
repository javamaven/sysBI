package io.renren.service.shichang.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.shichang.DmReportDalilyMarketingDao;
import io.renren.entity.shichang.DmReportDalilyMarketingEntity;
import io.renren.service.shichang.DmReportDalilyMarketingService;



@Service("dmReportDalilyMarketingService")
public class DmReportDalilyMarketingServiceImpl implements DmReportDalilyMarketingService {
	@Autowired
	private DmReportDalilyMarketingDao dmReportDalilyMarketingDao;
	
	@Override
	public DmReportDalilyMarketingEntity queryObject(String statPeriod){
		return dmReportDalilyMarketingDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportDalilyMarketingEntity> queryList(Map<String, Object> map){
		return dmReportDalilyMarketingDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportDalilyMarketingDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingDao.save(dmReportDalilyMarketing);
	}
	
	@Override
	public void update(DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingDao.update(dmReportDalilyMarketing);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportDalilyMarketingDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportDalilyMarketingDao.deleteBatch(statPeriods);
	}

	@Override
	public List<DmReportDalilyMarketingEntity> queryDayList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportDalilyMarketingDao.queryDayList(map);
	}

	@Override
	public List<DmReportDalilyMarketingEntity> queryMonthList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportDalilyMarketingDao.queryMonthList(map);
	}

	@Override
	public List<DmReportDalilyMarketingEntity> queryTotalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportDalilyMarketingDao.queryTotalList(map);
	}
	
}
