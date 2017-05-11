package io.renren.service.channelmanager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.channelmanager.DmReportUserActivateDailyDao;
import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;
import io.renren.service.channelmanager.DmReportUserActivateDailyService;

@Service("dmReportUserActivateDailyService")
public class DmReportUserActivateDailyServiceImpl implements DmReportUserActivateDailyService {
	@Autowired
	private DmReportUserActivateDailyDao dmReportUserActivateDailyDao;

	@Override
	public DmReportUserActivateDailyEntity queryObject(Integer statPeriod) {
		return dmReportUserActivateDailyDao.queryObject(statPeriod);
	}

	@Override
	public List<DmReportUserActivateDailyEntity> queryList(Map<String, Object> map) {
		return dmReportUserActivateDailyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return dmReportUserActivateDailyDao.queryTotal(map);
	}

	@Override
	public void save(DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyDao.save(dmReportUserActivateDaily);
	}

	@Override
	public void update(DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyDao.update(dmReportUserActivateDaily);
	}

	@Override
	public void delete(Integer statPeriod) {
		dmReportUserActivateDailyDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(Integer[] statPeriods) {
		dmReportUserActivateDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public UserActiveInfoEntity queryTotalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportUserActivateDailyDao.queryTotalList(map);
	}

}
