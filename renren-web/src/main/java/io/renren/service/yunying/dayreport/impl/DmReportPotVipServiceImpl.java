package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportPotVipDao;
import io.renren.entity.yunying.dayreport.DmReportPotVipEntity;
import io.renren.service.yunying.dayreport.DmReportPotVipService;



@Service("dmReportPotVipService")
public class DmReportPotVipServiceImpl implements DmReportPotVipService {
	@Autowired
	private DmReportPotVipDao dmReportPotVipDao;
	
	@Override
	public DmReportPotVipEntity queryObject(String statPeriod){
		return dmReportPotVipDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportPotVipEntity> queryList(Map<String, Object> map){
		return dmReportPotVipDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportPotVipDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportPotVipEntity dmReportPotVip){
		dmReportPotVipDao.save(dmReportPotVip);
	}
	
	@Override
	public void update(DmReportPotVipEntity dmReportPotVip){
		dmReportPotVipDao.update(dmReportPotVip);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportPotVipDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportPotVipDao.deleteBatch(statPeriods);
	}
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("userId", "用户ID");
		headMap.put("cgUserId", "存管ID");
		
		headMap.put("username", "用户名");
		headMap.put("phone", "电话");
		headMap.put("realname", "姓名");
		
		headMap.put("moneyAll", "账户资产权益额");
		headMap.put("balance", "账户余额");
		headMap.put("moneyWait", "待收金额（≥5万元");
		
		headMap.put("sumInvest", "投资次数(前3次)");
		headMap.put("avgPeriod", "平均投资期限偏好(大于100天)");
		headMap.put("amount", "累计充值金额（≥5万元）");
		
		headMap.put("moneyInvest", "累计投资金额（≥5万元）");
		headMap.put("registerTime", "注册日期");
		
		

		return headMap;
	}
	
	
}
