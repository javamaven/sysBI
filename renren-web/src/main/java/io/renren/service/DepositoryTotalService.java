package io.renren.service;

import io.renren.entity.DepositoryTotalEntity;

import java.util.List;
import java.util.Map;

/**
 * 存管报备总表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-01 14:47:27
 */
public interface DepositoryTotalService {


	List<DepositoryTotalEntity> queryExport();
	List<DepositoryTotalEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	Map<String, String> getExcelFields();
}
