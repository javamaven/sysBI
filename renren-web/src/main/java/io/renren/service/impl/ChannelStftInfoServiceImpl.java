package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ChannelStftInfoDao;
import io.renren.entity.ChannelStftInfoEntity;
import io.renren.service.ChannelStftInfoService;

@Service("ChannelStftInfoService")
public class ChannelStftInfoServiceImpl implements ChannelStftInfoService {
	@Autowired
	private ChannelStftInfoDao channelStftInfoDao;

	@Override
	public List<ChannelStftInfoEntity> queryRegisterUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryRegisterUserNum(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryInvestYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryInvestYearAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryProMultiInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryProMultiInvestAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryFirstInvestUserAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryFirstInvestUserAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryUserInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryUserInvestAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryFirstInvestPerTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryFirstInvestPerTime(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryMultiInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryMultiInvestUserNum(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryFirstInvestUserProInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryFirstInvestUserProInvestAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryProInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryProInvestAmount(map);
	}

	@Override
	public List<ChannelStftInfoEntity> queryUserInvestNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelStftInfoDao.queryUserInvestNum(map);
	}

}
