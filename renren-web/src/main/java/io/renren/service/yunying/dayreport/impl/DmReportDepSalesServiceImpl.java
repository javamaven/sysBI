package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportDepSalesDao;
import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;
import io.renren.service.yunying.dayreport.DmReportDepSalesService;



@Service("dmReportDepSalesService")
public class DmReportDepSalesServiceImpl implements DmReportDepSalesService {
	@Autowired
	private DmReportDepSalesDao dmReportDepSalesDao;
	
	@Override
	public DmReportDepSalesEntity queryObject(String statPeriod){
		return dmReportDepSalesDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportDepSalesEntity> queryList(Map<String, Object> map){
		return dmReportDepSalesDao.queryList(map);
	}
	@Override
	public List<DmReportDepSalesEntity> queryLists(Map<String, Object> map){
		return dmReportDepSalesDao.queryLists(map);
	}
	public List<DmReportDepSalesEntity> queryListss(Map<String, Object> map){
		return dmReportDepSalesDao.queryListss(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportDepSalesDao.queryTotal(map);
	}
	
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "产品");
		headMap.put("shiwutian", "15天");
		headMap.put("yiyue", "1个月");

		headMap.put("eryue", "2个月");
		headMap.put("sanyue", "3个月");
		headMap.put("liuyue", "6个月");
		headMap.put("bayue", "8个月");

		headMap.put("jiuyue", "9个月");
		headMap.put("shiyue", "10个月");
		headMap.put("shieryue", "12个月");
		
		headMap.put("shiwuyue", "15个月");
		headMap.put("shibayue", "18个月");
		headMap.put("ershisiyue", "24个月");

		headMap.put("sanshiwuyue", "35个月");
		headMap.put("sanshiliuyue", "36个月");
		headMap.put("sishibayue", "48个月");

		headMap.put("liushiyue", "60个月");
		headMap.put("jiushiliuyue", "96个月");
		headMap.put("zongji", "总计");
		headMap.put("zhanbi", "占比");
		return headMap;
	}
	@Override
	public Map<String, String> getExcelFields1() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("zichan", "期限");
		headMap.put("shiwutian", "15天");
		headMap.put("yiyue", "1个月");

		headMap.put("eryue", "2个月");
		headMap.put("sanyue", "3个月");
		headMap.put("liuyue", "6个月");
		headMap.put("bayue", "8个月");

		headMap.put("jiuyue", "9个月");
		headMap.put("shiyue", "10个月");
		headMap.put("shieryue", "12个月");
		
		headMap.put("shiwuyue", "15个月");
		headMap.put("shibayue", "18个月");
		headMap.put("ershisiyue", "24个月");

		headMap.put("sanshiwuyue", "35个月");
		headMap.put("sanshiliuyue", "36个月");
		headMap.put("sishibayue", "48个月");

		headMap.put("liushiyue", "60个月");
		headMap.put("jiushiliuyue", "96个月");
		headMap.put("zongji", "总计");
		return headMap;
	}
	@Override
	public Map<String, String> getExcelFields2() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "类型");
		headMap.put("zhanbi", "金额");

	

		return headMap;
	}
	
}
