package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportFcialPlanDailyDao;
import io.renren.entity.yunying.dayreport.DmReportFcialPlanDailyEntity;
import io.renren.service.yunying.dayreport.DmReportFcialPlanDailyService;



@Service("dmReportFcialPlanDailyService")
public class DmReportFcialPlanDailyServiceImpl implements DmReportFcialPlanDailyService {
	@Autowired
	private DmReportFcialPlanDailyDao dmReportFcialPlanDailyDao;
	
	@Override
	public DmReportFcialPlanDailyEntity queryObject(String statPeriod){
		return dmReportFcialPlanDailyDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportFcialPlanDailyEntity> queryList(Map<String, Object> map){
		return dmReportFcialPlanDailyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportFcialPlanDailyDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportFcialPlanDailyEntity dmReportFcialPlanDaily){
		dmReportFcialPlanDailyDao.save(dmReportFcialPlanDaily);
	}
	
	@Override
	public void update(DmReportFcialPlanDailyEntity dmReportFcialPlanDaily){
		dmReportFcialPlanDailyDao.update(dmReportFcialPlanDaily);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportFcialPlanDailyDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportFcialPlanDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("tenderAmount", "销售总额");
		headMap.put("fifteenDay", "15天期限");
		headMap.put("oneMonths", "1个月期限");
		headMap.put("twoMonths", "2个月期限");
		headMap.put("threeMonths", "3个月期限");
		headMap.put("sixMonths", "6个月期限");
		headMap.put("nineMonths", "9个月期限");
		headMap.put("twelveMonths", "12个月期限");
		headMap.put("otherMonths", "其他期限");
		headMap.put("lockEndMoney", "明日锁定期结束（到期）计划金额");
		headMap.put("lockEndMoneyHistory", "截止今日结束锁定期计划累计金额");
		headMap.put("lockEndP", "昨日结束锁定期人数");
		headMap.put("lockEndPHistory", "截止今日结束锁定期累计总人数");
		headMap.put("quitMoney", "理财计划申请退出金额");
		headMap.put("quitP", "理财计划申请退出人数");
		headMap.put("quitSMoney", "理财计划成功退出金额");
		headMap.put("quitSP", "理财计划成功退出人数");
		return headMap;
	}
	
}
