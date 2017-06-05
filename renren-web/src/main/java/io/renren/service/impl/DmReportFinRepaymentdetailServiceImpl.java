package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.DmReportFinRepaymentdetailDao;
import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.service.DmReportFinRepaymentdetailService;



@Service("dmReportFinRepaymentdetailService")
public class DmReportFinRepaymentdetailServiceImpl implements DmReportFinRepaymentdetailService {
	@Autowired
	private DmReportFinRepaymentdetailDao dmReportFinRepaymentdetailDao;

	
	@Override
	public List<DmReportFinRepaymentdetailEntity> queryList(Map<String, Object> map){
		return dmReportFinRepaymentdetailDao.queryList(map);
	}

	@Override
	public List<DmReportFinRepaymentdetailEntity> queryExport() {
		return dmReportFinRepaymentdetailDao.queryExport();// 导出EX
	}
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportFinRepaymentdetailDao.queryTotal(map);
	}


	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("sourcecaseno", "项目合同编号");
		headMap.put("customername", "借款人");
		headMap.put("payformoney", "借款金额");
		headMap.put("payformoneyout", "放款金额");
		headMap.put("totpmts", "总期数");
		headMap.put("reindex", "还款期数");
		headMap.put("planrepaydate", "计划还款日");
		headMap.put("realredate", "实际还款日期");
		headMap.put("remain", "应还本金");
		headMap.put("reinterest", "应还利息");
		headMap.put("rebackmain", "已还本金");
		headMap.put("rebackinterest", "已还利息");
		headMap.put("reamercedmoney", "已还罚息");
		headMap.put("reamercedmoney3", "已还违约金");
		headMap.put("realgetmoneydate", "项目结清日");
		return headMap;
	}

}
