package io.renren.service.yunying.dayreport.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportCashDataDao;
import io.renren.entity.yunying.dayreport.DmReportCashDataEntity;
import io.renren.service.yunying.dayreport.DmReportCashDataService;



@Service("dmReportCashDataService")
public class DmReportCashDataServiceImpl implements DmReportCashDataService {
	@Autowired
	private DmReportCashDataDao dmReportCashDataDao;
	
	@Override
	public DmReportCashDataEntity queryObject(BigDecimal statPeriod){
		return dmReportCashDataDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportCashDataEntity> queryList(Map<String, Object> map){
		
		List<DmReportCashDataEntity> list = dmReportCashDataDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportCashDataEntity entity = list.get(i);
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
		return dmReportCashDataDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportCashDataEntity dmReportCashData){
		dmReportCashDataDao.save(dmReportCashData);
	}
	
	@Override
	public void update(DmReportCashDataEntity dmReportCashData){
		dmReportCashDataDao.update(dmReportCashData);
	}
	
	@Override
	public void delete(BigDecimal statPeriod){
		dmReportCashDataDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(BigDecimal[] statPeriods){
		dmReportCashDataDao.deleteBatch(statPeriods);
	}
	
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		
		headMap.put("userId", "用户ID");
		headMap.put("cgUserId", "存管用户ID");
		headMap.put("username", "用户名称");
		
		headMap.put("phone", "手机号");
		headMap.put("cashMoney", "提现成功金额");
		headMap.put("frost", "账户资产权益额");
		
		headMap.put("balance", "账户余额");
		headMap.put("await", "待收金额");
		headMap.put("regTime", "注册时间");
		
		headMap.put("xmInvOneTime", "首投时间");
		headMap.put("xmStJg", "注册后首投间隔(分)");
		headMap.put("xmInvLastTime", "最近一次投资时间");
		
		headMap.put("xmTzJg", "首投到最后一次投资时间间隔(分)");
		headMap.put("xmInvMoney", "投资总金额");
		headMap.put("xmInvCou", "投资次数");
		
		headMap.put("useInvPackCou", "投资次数中使用奖励次数");
		headMap.put("zzFqCou", "债转次数");
		headMap.put("periodJq", "投资期限偏好");
		
		headMap.put("czMoney", "累计充值金额");
		headMap.put("txCgMoney", "累计提现金额");
		headMap.put("realname", "真实姓名");
	
		return headMap;
	}
}
