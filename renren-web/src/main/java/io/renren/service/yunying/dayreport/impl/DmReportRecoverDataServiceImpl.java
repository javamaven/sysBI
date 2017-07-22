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
		headMap.put("cgUserId", "存管ID");
		headMap.put("username", "用户名");
		
		headMap.put("phone", "电话");
		headMap.put("projectId", "回款项目号");
		headMap.put("money", "回款金额");
		headMap.put("moneyWait", "待收金额");
		
		headMap.put("borrowPeriod", "项目期限");
		headMap.put("regTime", "注册日期");
		headMap.put("xmStJg", "注册后首投间隔(分)(数据覆盖历史项目)");
		
		headMap.put("avgXmTzJg", "平均投资时间间隔(分)(数据覆盖历史项目)");
		headMap.put("avgXmInvMoney", "平均投资金额(数据覆盖历史项目)");
		headMap.put("xmInvCou", "投资次数(数据覆盖历史项目)");
		
		headMap.put("invPackBl", "投资次数中使用奖励比列(数据覆盖历史项目)");
		headMap.put("zzBl", "发起转让比例(数据覆盖历史项目)");
		headMap.put("periodJq", "投资期限偏好(数据覆盖历史项目)");
		
		headMap.put("rewardStatus", "账户是否有红包");
		headMap.put("rewardMoney", "红包金额");
		headMap.put("czMoney", "累计充值金额");
		
		headMap.put("txCgMoney", "累计提现金额");
		headMap.put("txCgMoneyBl", "提现金额占比充值金额");
		headMap.put("dateCou", "本次推送记录数");
		
		return headMap;
	}
}
