package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportDepNopenDao;
import io.renren.entity.yunying.dayreport.DmReportDepNopenEntity;
import io.renren.service.yunying.dayreport.DmReportDepNopenService;



@Service("dmReportDepNopenService")
public class DmReportDepNopenServiceImpl implements DmReportDepNopenService {
	@Autowired
	private DmReportDepNopenDao dmReportDepNopenDao;
	
	@Override
	public DmReportDepNopenEntity queryObject(String statPeriod){
		return dmReportDepNopenDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportDepNopenEntity> queryList(Map<String, Object> map){
		return dmReportDepNopenDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportDepNopenDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportDepNopenEntity dmReportDepNopen){
		dmReportDepNopenDao.save(dmReportDepNopen);
	}
	
	@Override
	public void update(DmReportDepNopenEntity dmReportDepNopen){
		dmReportDepNopenDao.update(dmReportDepNopen);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportDepNopenDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportDepNopenDao.deleteBatch(statPeriods);
	}
	
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("username", "用户名");
		headMap.put("realname", "用户姓名");
		headMap.put("phone", "手机号");
		headMap.put("cgUserId", "存管ID");
		headMap.put("norWait", "普通版待收金额");
		headMap.put("tenderTime", "最后一次投资时间");
		headMap.put("recoverTime", "最后一次回款时间");
		headMap.put("invCou", "投资次数");
		headMap.put("tenderCapital", "累计投资金额");
		headMap.put("lv", "用户等级");
		headMap.put("norBalance", "普通版账户可用余额");
		headMap.put("reward", "可用红包金额");
		headMap.put("rewardInv", "投资次数中使用奖励比例");
		return headMap;
	}
}
