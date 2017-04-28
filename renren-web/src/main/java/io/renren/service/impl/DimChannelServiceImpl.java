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
	
	
	@Override
	public List<DimChannelEntity> queryList(Map<String, Object> map) {
		return dimChannelDao.queryList(map);
	}


	@Override
	public Map<String, String> queryListAsLabelMap(Map<String, Object> map) {
		List<DimChannelEntity> list = queryList(map);
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			DimChannelEntity dimChannelEntity = list.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelNameBack());
		}
		return dataMap;
	}
	

}
