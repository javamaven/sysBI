package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportBasicDailyDao;
import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;
import io.renren.service.yunying.dayreport.DmReportBasicDailyService;



@Service("dmReportBasicDailyService")
public class DmReportBasicDailyServiceImpl implements DmReportBasicDailyService {
	@Autowired
	private DmReportBasicDailyDao dmReportBasicDailyDao;
	
	@Override
	public DmReportBasicDailyEntity queryObject(String statPeriod){
		return dmReportBasicDailyDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportBasicDailyEntity> queryList(Map<String, Object> map){
		List<DmReportBasicDailyEntity> list = dmReportBasicDailyDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportBasicDailyEntity entity = list.get(i);
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
		return dmReportBasicDailyDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportBasicDailyEntity dmReportBasicDaily){
		dmReportBasicDailyDao.save(dmReportBasicDaily);
	}
	
	@Override
	public void update(DmReportBasicDailyEntity dmReportBasicDaily){
		dmReportBasicDailyDao.update(dmReportBasicDaily);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportBasicDailyDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportBasicDailyDao.deleteBatch(statPeriods);
	}
	
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("invCou", "投资用户数");
		headMap.put("usePackMoney", "投资使用红包金额");
		
		headMap.put("avgUsePackMoney", "人均红包金额");
		headMap.put("accountMoney", "用户投资本金");
		headMap.put("avgMoney", "人均投资本金");
		
		headMap.put("yearMoney", "用户投资年化金额");
		headMap.put("avgYearMoney", "人均年化金额");
		headMap.put("spreadsMoney", "邀请人返利");
		
		headMap.put("discountCost", "加息成本");
		headMap.put("avgDiscountCost", "人均加息成本");
		headMap.put("forecastAwait", "新增待收（预估）");
		
		headMap.put("fullAwait", "新增代收（实际）");
		headMap.put("loanAwait", "新增待收（放款）");
		headMap.put("recoverMoney", "回款金额");
		
		headMap.put("monthNh", "本月年化投资金额");
		headMap.put("newEndYearAwait", "新增且到12-31后还款的待收（考虑还款方式）（万元）");
		headMap.put("endYearAwait", "到12-31后还款的待收金额");
		headMap.put("allAwait", "总待收");
		
		headMap.put("allWait", "测算累计待收(万元)");
		headMap.put("matchWait", "理财计划预约金额(万元)");
		headMap.put("matchCapitilWait", "未匹配本金(万元)");
		headMap.put("matchInterestlWait", "未匹配测算利息(万元)");
		return headMap;
	}
	
	
}
