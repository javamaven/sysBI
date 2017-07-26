package io.renren.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.ChannelChannelAllDao;
import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;

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
	@Override
	public List<ChannelChannelAllEntity>  queryChannelHead() {
		return channelChannelAllDao.queryChannelHead();
	}


	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("channelName", "渠道");
		headMap.put("investTimes", "投资次数");
		headMap.put("stayPer", "留存率");
		headMap.put("investUsers", "投资用户数");
		headMap.put("moneyVoucherPer", "次均红包金额");
		headMap.put("moneyInvestPer", "次均投资金额");
		headMap.put("moneyInvsetYearPer", "平均投资期限");
		headMap.put("borrowPeriodPer", "平均投资间隔");
		return headMap;
	}


}
