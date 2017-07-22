package io.renren.service.yunying.dayreport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.MonthlyReportEntity;
import io.renren.utils.PageUtils;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 09:27:12
 */
public interface MonthlyReportService {
	
	MonthlyReportEntity queryObject(BigDecimal statPeriod);
	
	List<MonthlyReportEntity> queryList(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
	
	Map<String, String> getExcelFields();
	Map<String, String> getExcelFields2();
	PageUtils queryList(Integer page, Integer limit, String statPeriod);

	PageUtils queryList1(Integer page, Integer limit,String statPeriod);
}
