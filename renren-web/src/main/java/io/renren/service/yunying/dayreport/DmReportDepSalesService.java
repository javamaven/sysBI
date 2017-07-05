package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-29 16:30:26
 */
public interface DmReportDepSalesService {
	
	DmReportDepSalesEntity queryObject(String statPeriod);
	
	List<DmReportDepSalesEntity> queryList(Map<String, Object> map);
	List<DmReportDepSalesEntity> queryLists(Map<String, Object> map);
	List<DmReportDepSalesEntity> queryListss(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
	Map<String, String> getExcelFields();
	Map<String, String> getExcelFields1();
	Map<String, String> getExcelFields2();



	
}
