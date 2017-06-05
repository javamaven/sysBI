package io.renren.service.yunying.dayreport.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportRecoverDataDao;
import io.renren.entity.yunying.dayreport.DmReportRecoverDataEntity;
import io.renren.service.yunying.dayreport.DmReportRecoverDataService;



@Service("dmReportRecoverDataService")
public class DmReportRecoverDataServiceImpl implements DmReportRecoverDataService {
	@Autowired
	private DmReportRecoverDataDao dmReportRecoverDataDao;
	
	@Override
	public DmReportRecoverDataEntity queryObject(BigDecimal statPeriod){
		return dmReportRecoverDataDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportRecoverDataEntity> queryList(Map<String, Object> map){
		List<DmReportRecoverDataEntity> list = dmReportRecoverDataDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportRecoverDataEntity entity = list.get(i);
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
		return dmReportRecoverDataDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportRecoverDataEntity dmReportRecoverData){
		dmReportRecoverDataDao.save(dmReportRecoverData);
	}
	
	@Override
	public void update(DmReportRecoverDataEntity dmReportRecoverData){
		dmReportRecoverDataDao.update(dmReportRecoverData);
	}
	
	@Override
	public void delete(BigDecimal statPeriod){
		dmReportRecoverDataDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(BigDecimal[] statPeriods){
		dmReportRecoverDataDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		
		headMap.put("userId", "用户ID");
		headMap.put("cgUserId", "存管用户ID");
		headMap.put("username", "用户名称");
		
		headMap.put("phone", "手机号");
		headMap.put("projectId", "项目ID");
		headMap.put("money", "回款金额");
		
		headMap.put("borrowPeriod", "项目期限");
		headMap.put("regTime", "注册日期");
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
		headMap.put("dateCou", "本次推送记录数");
		
		return headMap;
	}
}
