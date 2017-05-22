package io.renren.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.MarketChannelDao;
import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;

@Service("MarketChannelService")
public class MarketChannelServiceImpl implements MarketChannelService {
	@Autowired
	private MarketChannelDao marketChannelDao;

	@Override
	public List<MarketChannelEntity> queryList(Map<String, Object> map) {
		return marketChannelDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return marketChannelDao.queryTotal(map);
	}

	@Override
	public List<MarketChannelEntity> queryExport() {
		return marketChannelDao.queryExport();// 导出EX
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("channelHead", "主负责人");
		headMap.put("type", "渠道类型");
		headMap.put("channelName", "渠道名称");
		headMap.put("actualCost", "实际消费");
		headMap.put("regCou", "新增注册人");
		headMap.put("firstinvestCou", "新增首投人数");
		headMap.put("firstinvestMoney", "首投金额");
		headMap.put("firstinvestYMoney", "首投年化金额");
		headMap.put("invCou", "投资总人数");
		headMap.put("invMoney", "投资总金额");
		headMap.put("invYMoney", "年化投资总金额");
		headMap.put("ddzMoney", "点点赚购买金额");
		headMap.put("regCost", "注册成本");
		headMap.put("firstinvestCost", "首投成本");
		headMap.put("avgFirstinvestMoney", "人均首投");
		headMap.put("regInvConversion", "注册人投资转化率");
		headMap.put("firstinvestRot", "首投ROI");
		headMap.put("cumulativeRot", "累计ROI");
		return headMap;
	}

}
