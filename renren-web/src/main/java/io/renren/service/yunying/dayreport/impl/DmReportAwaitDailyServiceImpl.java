package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportAwaitDailyDao;
import io.renren.entity.yunying.dayreport.DmReportAwaitDailyEntity;
import io.renren.service.yunying.dayreport.DmReportAwaitDailyService;



@Service("dmReportAwaitDailyService")
public class DmReportAwaitDailyServiceImpl implements DmReportAwaitDailyService {
	@Autowired
	private DmReportAwaitDailyDao dmReportAwaitDailyDao;
	
	@Override
	public DmReportAwaitDailyEntity queryObject(String statPeriod){
		return dmReportAwaitDailyDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportAwaitDailyEntity> queryList(Map<String, Object> map){
		
		List<DmReportAwaitDailyEntity> list = dmReportAwaitDailyDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportAwaitDailyEntity entity = list.get(i);
			String statPeriod = entity.getStatPeriod();
			if(statPeriod.length() == 8){
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
		return dmReportAwaitDailyDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportAwaitDailyEntity dmReportAwaitDaily){
		dmReportAwaitDailyDao.save(dmReportAwaitDaily);
	}
	
	@Override
	public void update(DmReportAwaitDailyEntity dmReportAwaitDaily){
		dmReportAwaitDailyDao.update(dmReportAwaitDaily);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportAwaitDailyDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportAwaitDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");

		headMap.put("awaitCapital", "代收本金");
		headMap.put("awaitInterest", "代收利息");
		headMap.put("yesCapital", "已收本金");
		
		headMap.put("yesInterest", "已收利息");
		headMap.put("addAwaitCapital", "新增代收本金");
		headMap.put("addAwaitInteres", "新增代收利息");
		
		headMap.put("recoverCapital", "还款本金");
		headMap.put("recoverInterest", "还款利息");
		headMap.put("fromSys", "数据来源");
		
		return headMap;
	}
	
}
