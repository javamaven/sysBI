package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ChannelRenewDataDao;
import io.renren.entity.ChannelRenewDataEntity;
import io.renren.service.ChannelRenewDataService;

@Service("ChannelRenewDataService")
public class ChannelRenewDataServiceImpl implements ChannelRenewDataService {
	@Autowired
	private ChannelRenewDataDao channelRenewDataDao;

	@Override
	public List<ChannelRenewDataEntity> queryChannelCost(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryChannelCost(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryOnlineTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryOnlineTime(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearAmount(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearRoi(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestYearRoi(map);
	}


}
