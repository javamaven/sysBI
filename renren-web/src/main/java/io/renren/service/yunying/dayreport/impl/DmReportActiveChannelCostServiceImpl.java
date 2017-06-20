package io.renren.service.yunying.dayreport.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportActiveChannelCostDao;
import io.renren.entity.yunying.dayreport.DmReportActiveChannelCostEntity;
import io.renren.service.yunying.dayreport.DmReportActiveChannelCostService;



@Service("dmReportActiveChannelCostService")
public class DmReportActiveChannelCostServiceImpl implements DmReportActiveChannelCostService {
	@Autowired
	private DmReportActiveChannelCostDao dmReportActiveChannelCostDao;
	
	@Override
	public DmReportActiveChannelCostEntity queryObject(BigDecimal statPeriod){
		return dmReportActiveChannelCostDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportActiveChannelCostEntity> queryList(Map<String, Object> map){
		List<DmReportActiveChannelCostEntity> list = dmReportActiveChannelCostDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportActiveChannelCostEntity entity = list.get(i);
			String statPeriod = entity.getStatPeriod();
			if (statPeriod.length() == 8) {
				String year = statPeriod.substring(0, 4);
				String month = statPeriod.substring(4, 6);
				String day = statPeriod.substring(6, 8);
				entity.setStatPeriod(year + "-" + month + "-" + day);
			}
		}
		return list;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportActiveChannelCostDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportActiveChannelCostEntity dmReportActiveChannelCost){
		dmReportActiveChannelCostDao.save(dmReportActiveChannelCost);
	}
	
	@Override
	public void update(DmReportActiveChannelCostEntity dmReportActiveChannelCost){
		dmReportActiveChannelCostDao.update(dmReportActiveChannelCost);
	}
	
	@Override
	public void delete(BigDecimal statPeriod){
		dmReportActiveChannelCostDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(BigDecimal[] statPeriods){
		dmReportActiveChannelCostDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		
		headMap.put("code", "渠道标签");
		headMap.put("name", "渠道名称");
		headMap.put("cost", "推广成本");
		headMap.put("costSource", "成本归属部门");

		return headMap;
	}
	
}
