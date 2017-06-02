package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.renren.dao.DepositoryTotalDao;
import io.renren.entity.DepositoryTotalEntity;
import io.renren.service.DepositoryTotalService;



@Service("dmReportCgReportService")
public class DepositoryTotalServiceImpl implements DepositoryTotalService {
	@Autowired
	private DepositoryTotalDao dmReportCgReportDao;

	@Override
	public List<DepositoryTotalEntity> queryExport() {
		return dmReportCgReportDao.queryExport();// 导出EX
	}
	@Override
	public List<DepositoryTotalEntity> queryList(Map<String, Object> map){
		return dmReportCgReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportCgReportDao.queryTotal(map);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "业务日期");
		headMap.put("sourcecaseno", "平台项目编号");
		headMap.put("department", "部门");
		headMap.put("projectBelong", "项目归属");
		headMap.put("projectType", "项目类型");
		headMap.put("customername", "借款人");
		headMap.put("payformoney", "金额");
		headMap.put("loanyearlimit", "期限-月");
		headMap.put("payforlimittime", "期限-日");
		headMap.put("giveoutmoneytime", "满标放款日");
		headMap.put("willgetmoneydate", "到期日");
		headMap.put("iscompleted", "资料签名、盖章是否完全");
		headMap.put("sendDeadline", "纸质文本最迟寄送日（发标5个工作日内）");
		headMap.put("isstamp", "纸文本是否盖章");

		return headMap;
	}
}
