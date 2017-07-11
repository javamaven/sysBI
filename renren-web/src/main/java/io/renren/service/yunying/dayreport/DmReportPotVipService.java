package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportPotVipEntity;

/**
 * 潜力VIP数据表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-07 16:24:59
 */
public interface DmReportPotVipService {
	
	DmReportPotVipEntity queryObject(String statPeriod);
	
	List<DmReportPotVipEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportPotVipEntity dmReportPotVip);
	
	void update(DmReportPotVipEntity dmReportPotVip);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);
	Map<String, String> getExcelFields();
}
