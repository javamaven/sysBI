package io.renren.service.impl;

import io.renren.dao.MarketChannelDao;
import io.renren.entity.MarketChannelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.renren.service.MarketChannelService;
import java.util.List;
import java.util.Map;



@Service("MarketChannelService")
public class MarketChannelServiceImpl implements MarketChannelService {
	@Autowired
	private MarketChannelDao marketChannelDao;
	
	@Override
	public List<MarketChannelEntity> queryList(Map<String, Object> map){
		return marketChannelDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return marketChannelDao.queryTotal(map);
	}

	@Override
	public List<MarketChannelEntity> queryExport() { return marketChannelDao.queryExport();//导出EX
	}


}
