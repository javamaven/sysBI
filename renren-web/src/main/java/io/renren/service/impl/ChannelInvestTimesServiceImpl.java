package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ChannelInvestTimesDao;
import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.service.ChannelInvestTimesService;

@Service("ChannelInvestTimesService")
public class ChannelInvestTimesServiceImpl implements ChannelInvestTimesService {
	@Autowired
	private ChannelInvestTimesDao channelInvestTimesDao;

	@Override
	public List<ChannelInvestTimesEntity> queryRegisterUserNum(Map<String, Object> map) {
		List<ChannelInvestTimesEntity> list = channelInvestTimesDao.queryRegisterUserNum(map);
		return list;
	}

	@Override
	public List<ChannelInvestTimesEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryInvestTimes(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryInvestTimes(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryInvestUserNum(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryInvestAmount(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryInvestYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryInvestYearAmount(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryFirstInvestRedMoney(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryFirstInvestRedMoney(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryAllRedMoney(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryAllRedMoney(map);
	}

	@Override
	public List<ChannelInvestTimesEntity> queryDdzPerInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInvestTimesDao.queryDdzPerInvestAmount(map);
	}
}
