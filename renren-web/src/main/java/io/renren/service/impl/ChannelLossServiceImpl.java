package io.renren.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ChannelLossDao;
import io.renren.entity.ChannelLossEntity;
import io.renren.service.ChannelLossService;

@Service("ChannelLossService")
public class ChannelLossServiceImpl implements ChannelLossService {
	@Autowired
	private ChannelLossDao channelLossDao;

	@Override
	public List<ChannelLossEntity> queryRegisterUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryRegisterUserNum(map);
	}

	@Override
	public List<ChannelLossEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelLossEntity> queryInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryInvestUserNum(map);
	}

	@Override
	public List<ChannelLossEntity> queryFirstInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryFirstInvestAmount(map);
	}

	@Override
	public List<ChannelLossEntity> queryInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<ChannelLossEntity> queryInvestAmount = channelLossDao.queryInvestAmount(map);
		return queryInvestAmount;
	}

	@Override
	public List<ChannelLossEntity> queryInvestYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryInvestYearAmount(map);
	}

	@Override
	public List<ChannelLossEntity> queryFirstInvestUseRedMoney(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryFirstInvestUseRedMoney(map);
	}

	@Override
	public List<ChannelLossEntity> queryTotalUseRedMoney(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryTotalUseRedMoney(map);
	}

	@Override
	public List<ChannelLossEntity> queryDdzPerInvestAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelLossDao.queryDdzPerInvestAmount(map);
	}

}
