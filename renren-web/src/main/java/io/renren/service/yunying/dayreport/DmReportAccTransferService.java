package io.renren.service.yunying.dayreport;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.dayreport.DmReportAccTransferEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 09:48:41
 */
public interface DmReportAccTransferService {
	
	DmReportAccTransferEntity queryObject(String statPeriod);
	
	List<DmReportAccTransferEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportAccTransferEntity dmReportAccTransfer);
	
	void update(DmReportAccTransferEntity dmReportAccTransfer);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	Map<String, String> getExcelFields();
}
