package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.ChannelCostDao;
import io.renren.entity.ChannelCostEntity;
import io.renren.service.ChannelCostService;



@Service("ChannelCostService")
public class ChannelCostServiceImpl implements ChannelCostService {
	@Autowired
	private ChannelCostDao ChannelCostDao;
	
	@Override
	public ChannelCostEntity queryObject(String statPeriod){
		return ChannelCostDao.queryObject(statPeriod);
	}
	
	@Override
	public List<ChannelCostEntity> queryList(Map<String, Object> map){
		return ChannelCostDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return ChannelCostDao.queryTotal(map);
	}

	@Override
	public List<ChannelCostEntity>  queryChannel() {
		return ChannelCostDao.queryChannel();
	}

	@Override
	public ChannelCostEntity queryTotalList(Map<String, Object> map) {

		return ChannelCostDao.queryTotalList(map);
	}
	@Override
	public List<ChannelCostEntity> queryExport() { return ChannelCostDao.queryExport();//导出EX
	}
}
