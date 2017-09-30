package io.renren.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.MarketChannelDao;
import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;
import io.renren.util.MapUtil;
import io.renren.util.StringUtil;

@Service("MarketChannelService")
public class MarketChannelServiceImpl implements MarketChannelService {
	@Autowired
	private MarketChannelDao marketChannelDao;

	@Override
	public List<MarketChannelEntity> queryList(Map<String, Object> map) {
		//增加一行会总行 2017-09-30 ldh
		List<MarketChannelEntity> retList = new ArrayList<MarketChannelEntity>();
		List<MarketChannelEntity> dataList = marketChannelDao.queryList(map);
		double channelCostD = 0;
		double rechargeD = 0;
		int regUser = 0;
		int firstInvUser = 0;
		double firstInvMoney = 0;
		double yearFirstInvMoney = 0;
		int investUser = 0;
		double invMoney = 0;
		double yearInvestMoney = 0;
		for (int i = 0; i < dataList.size(); i++) {
			MarketChannelEntity en = dataList.get(i);
			channelCostD += StringUtil.getDoubleValue(en.getChannelCost());//渠道费用
			rechargeD += StringUtil.getDoubleValue(en.getChannelRecharge());//渠道充值
			regUser += StringUtil.getIntegerValue(en.getRegCou());//注册人数
			firstInvUser += StringUtil.getIntegerValue(en.getFirstinvestCou());//首投人数
			firstInvMoney += StringUtil.getDoubleValue(en.getFirstinvestMoney());//首投金额
			yearFirstInvMoney += StringUtil.getDoubleValue(en.getFirstinvestyearamount());//年化首投金额
//			investUser += StringUtil.getIntegerValue(en.getInvCou());//投资人数
			invMoney += StringUtil.getDoubleValue(en.getInvMoney());//投资金额
			yearInvestMoney += StringUtil.getDoubleValue(en.getYearamount());//年化投资金额
		}
		//汇总行
		MarketChannelEntity en = new MarketChannelEntity();
		en.setStatPeriod("汇总");
//		en.setChannelHead("所有负责人");
//		en.setType("所有类型");
//		en.setChannelName("所有渠道");
//		en.setChannelLabel("所有标记");
		en.setChannelCost(channelCostD + "");
		en.setChannelRecharge(rechargeD + "");
		en.setRegCou(regUser + "");
		en.setFirstinvestCou(firstInvUser + "");
		en.setFirstinvestMoney(firstInvMoney + "");
		en.setFirstinvestyearamount(yearFirstInvMoney + "");
//		en.setInvCou(investUser + "");
		en.setInvMoney(invMoney + "");
		en.setYearamount(yearInvestMoney + "");
		retList.add(en);
		retList.addAll(dataList);
		return retList;
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
		headMap.put("channelHead", "负责人");
		headMap.put("type", "渠道类型");
		headMap.put("channelName", "渠道名称");
		headMap.put("channelLabel", "渠道标记");
		
		
		headMap.put("regCou", "注册人数");
		
		headMap.put("firstinvestCou", "首投人数");
		headMap.put("firstinvestMoney", "首投金额");
		headMap.put("firstinvestyearamount", "年化首投金额");
		headMap.put("countUser", "投资人数");
		headMap.put("invMoney", "投资金额");
		headMap.put("yearamount", "年化投资金额");
		headMap.put("channelCost", "渠道费用");
		headMap.put("channelRecharge", "渠道充值");
//		headMap.put("ddzMoney", "点点赚购买金额");
//		headMap.put("regCost", "注册成本");
//		headMap.put("firstinvestCost", "首投成本");
//		headMap.put("avgFirstinvestMoney", "人均首投");
//		headMap.put("regInvConversion", "注册人投资转化率");
//		headMap.put("firstinvestRot", "首投ROI");
//		headMap.put("cumulativeRot", "累计ROI");
		return headMap;
	}

}
