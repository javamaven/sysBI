package io.renren.service.impl;

import io.renren.dao.InsideLxDao;
import io.renren.entity.InsideLxEntity;
import io.renren.service.InsideLxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Service("dmReportInsideLxService")
public class InsideLxServiceImpl implements InsideLxService {
	@Autowired
	private InsideLxDao insideLxDao;
	

	
	@Override
	public List<InsideLxEntity> queryList(Map<String, Object> map){
		return insideLxDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return insideLxDao.queryTotal(map);
	}

	@Override
	public List<InsideLxEntity> queryExport() { return insideLxDao.queryExport();//导出EX
	}
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod","日期");
		headMap.put("lxName","员工姓名");
		headMap.put("laxDep","部门");
		headMap.put("lxUserCou","当季度有效拉新人数");
		headMap.put("lxUserTg","当季度拉新目标人数");
		headMap.put("reach","当季度有效拉新人数指标达成率");
		headMap.put("lxDs","满足日均待收金额的人数");
		headMap.put("lxUserPw","当季度累计有效拉新人数排名");
		headMap.put("jf","季度度新增积分值");
		headMap.put("jfpw","季度度新增积分值排名");


		return headMap;
	}


}
