package io.renren.service;

import io.renren.entity.PerformanceParameterEntity;

import java.util.List;
import java.util.Map;

/**
 * 绩效台帐-分配表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:58
 */
public interface PerformanceParameterService {
	
	PerformanceParameterEntity queryObject(String statPeriod);
	
	List<PerformanceParameterEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	List<PerformanceParameterEntity> queryExport();
	Map<String, String> getExcelFields();
}
