package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.PerformanceParameterDao;
import io.renren.entity.PerformanceParameterEntity;
import io.renren.service.PerformanceParameterService;



@Service("dmReportPerformanceLedgerService")
public class PerformanceParameterServiceImpl implements PerformanceParameterService {
	@Autowired
	private PerformanceParameterDao dmReportPerformanceLedgerDao;
	
	@Override
	public PerformanceParameterEntity queryObject(String statPeriod){
		return dmReportPerformanceLedgerDao.queryObject(statPeriod);
	}
	
	@Override
	public List<PerformanceParameterEntity> queryList(Map<String, Object> map){
		return dmReportPerformanceLedgerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportPerformanceLedgerDao.queryTotal(map);
	}

	@Override
	public List<PerformanceParameterEntity> queryExport() {
		return dmReportPerformanceLedgerDao.queryExport();// 导出EX
	}
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "业务日期");
		headMap.put("developmanagername", "总监");
		headMap.put("department", "部门");
		headMap.put("payformoneyout", "放款金额");
		headMap.put("grossProfit", "毛利");

		headMap.put("salaryCost", "工资成本");
		headMap.put("reimbursement", "报销开支");
		headMap.put("rentShare", "租金分摊");
		headMap.put("netMargin", "净利");
		headMap.put("commissionRatio", "提成系数");

		headMap.put("availablePerformance", "可发绩效");
		headMap.put("riskReserve", "风险准备金");
		headMap.put("settledAmount", "结清金额");
		headMap.put("settledAmtRate", "结清金额占比");
		headMap.put("expectedPerformance", "本月应发绩效");



		return headMap;
	}

}
