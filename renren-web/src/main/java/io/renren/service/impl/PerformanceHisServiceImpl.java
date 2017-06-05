package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.PerformanceHisDao;
import io.renren.entity.PerformanceHisEntity;
import io.renren.service.PerformanceHisService;



@Service("dmReportPerformLedgerHisService")
public class PerformanceHisServiceImpl implements PerformanceHisService {
	@Autowired
	private PerformanceHisDao dmReportPerformLedgerHisDao;

	@Override
	public List<PerformanceHisEntity> queryExport() {
		return dmReportPerformLedgerHisDao.queryExport();// 导出EX
	}
	@Override
	public PerformanceHisEntity queryObject(String statPeriod){
		return dmReportPerformLedgerHisDao.queryObject(statPeriod);
	}
	
	@Override
	public List<PerformanceHisEntity> queryList(Map<String, Object> map){
		return dmReportPerformLedgerHisDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportPerformLedgerHisDao.queryTotal(map);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "业务日期");
		headMap.put("developmanagername", "总监");
		headMap.put("department", "部门");
		headMap.put("expectedPerformance", "本月应发绩效");
		headMap.put("actualPerformance", "实发绩效");


		return headMap;
	}

}
