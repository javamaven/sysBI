package io.renren.service.impl;

import io.renren.dao.ChannelChannelAllDao;
import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("ChannelChannelAllService")
public class ChannelChannelAllServiceImpl implements ChannelChannelAllService {
	@Autowired
	private ChannelChannelAllDao channelChannelAllDao;

	@Override
	public List<ChannelChannelAllEntity> queryList(Map<String, Object> map) {
		return channelChannelAllDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return channelChannelAllDao.queryTotal(map);
	}

	@Override
	public List<ChannelChannelAllEntity>  queryMainChart(Map<String, Object> map){
		return channelChannelAllDao.queryMainChart(map);
	}

	@Override
	public List<ChannelChannelAllEntity>  queryChannel() {
		return channelChannelAllDao.queryChannel();
	}


}
