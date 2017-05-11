package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.DimChannelDao;
import io.renren.entity.DimChannelEntity;
import io.renren.service.DimChannelService;

@Service("DimChannelService")
public class DimChannelServiceImpl implements DimChannelService {
	@Autowired
	private DimChannelDao dimChannelDao;

	/**
	 * 创建渠道成本表：将渠道成本信息和渠道名称信息关联汇总
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public void createChanelCostTable(Map<String, Object> map) {
		dimChannelDao.createChanelCostTable(map);
	}
	
	@Override
	public List<DimChannelEntity> queryOnlineChannelCostList(Map<String, Object> map) {
		List<DimChannelEntity> queryChannel = dimChannelDao.queryOnlineChannelCostList();
		return queryChannel;
	}


	@Override
	public List<DimChannelEntity> queryChannelList(Map<String, Object> map) {
		List<DimChannelEntity> queryChannel = dimChannelDao.queryChannel();
		return queryChannel;
	}

	@Override
	public Map<String, String> queryListAsLabelMap(Map<String, Object> map) {
		List<DimChannelEntity> list = queryChannelList(map);
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			DimChannelEntity dimChannelEntity = list.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelNameBack());
		}
		return dataMap;
	}

	@Override
	public List<DimChannelEntity> queryChannelCostList() {
		List<DimChannelEntity> queryChannelCostList = dimChannelDao.queryChannelCostList();
		return queryChannelCostList;
	}

	@Override
	public Map<String, String> queryChanelTypeMap() {
		List<DimChannelEntity> list = queryChannelCostList();
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			DimChannelEntity dimChannelEntity = list.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getType());
		}
		return dataMap;
	}

	@Override
	public List<DimChannelEntity> queryChannelName() {
		// TODO Auto-generated method stub
		List<DimChannelEntity> nameList = dimChannelDao.queryChannelName();
		return nameList;
	}

}
