package io.renren.service;

import io.renren.entity.PerformanceHisEntity;

import java.util.List;
import java.util.Map;

/**
 * 历史绩效发放记录
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:54
 */
public interface PerformanceHisService {
	List<PerformanceHisEntity> queryExport();
	Map<String, String> getExcelFields();
	PerformanceHisEntity queryObject(String statPeriod);
	
	List<PerformanceHisEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);


}
