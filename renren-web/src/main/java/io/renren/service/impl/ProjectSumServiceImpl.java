package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.ProjectSumDao;
import io.renren.entity.ProjectSumEntity;
import io.renren.service.ProjectSumService;



@Service("dmReportFinRepaymentsumService")
public class ProjectSumServiceImpl implements ProjectSumService {
	@Autowired
	private ProjectSumDao dmReportFinRepaymentsumDao;

	@Override
	public List<ProjectSumEntity> queryExport() {
		return dmReportFinRepaymentsumDao.queryExport();// 导出EX
	}
	
	@Override
	public List<ProjectSumEntity> queryList(Map<String, Object> map){
		return dmReportFinRepaymentsumDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportFinRepaymentsumDao.queryTotal(map);
	}
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "统计日期");
		headMap.put("orgsimplename", "事业部");
		headMap.put("producttype", "产品分类");
		headMap.put("subproducttype", "产品分类子类");
		headMap.put("sourcecaseno", "项目合同编号");
		headMap.put("department", "部门");
		headMap.put("developmanagername", "总监");
		headMap.put("workername", "经办");
		headMap.put("customername", "借款人");
		headMap.put("payformoney", "借款金额");
		headMap.put("payformoneyout", "放款金额");
		headMap.put("loanyearlimit", "贷款月数");
		headMap.put("payforlimittime", "贷款天数");
		headMap.put("giveoutmoneytime", "开始时间");
		headMap.put("willgetmoneydate", "到期时间");
		headMap.put("totalRateAmount", "总利率");
		headMap.put("interestRate", "发标利率");
		headMap.put("otherRate", "其它利率");
		headMap.put("capitalCost", "资金成本");
		headMap.put("otherRateAmount", "其他利率（金额）");
		headMap.put("remain", "应还本金");
		headMap.put("reinterest", "应还利息");
		headMap.put("rebackmain", "已还本金");
		headMap.put("rebackinterest", "已还利息");
		headMap.put("waitCapital", "待还本金");
		headMap.put("waitInterest", "待还利息");
		headMap.put("reamercedmoney3", "提前还款违约金");
		headMap.put("reamercedmoney", "罚息");
		headMap.put("type", "公私类型");
		headMap.put("capitalSource", "资金来源");
		headMap.put("realgetmoneydate", "项目结清日");
		headMap.put("rebackservice", "手续费收入");
		headMap.put("repaymentWay", "还款方式");
		headMap.put("carNoLocation", "车牌上牌地");
		headMap.put("capitalDelistCompany", "资产摘牌公司");
		headMap.put("exchange1", "交易所1");
		headMap.put("exchange2", "交易所2");
		headMap.put("borrowers", "平台募集拆分对应");
		return headMap;
	}

}
