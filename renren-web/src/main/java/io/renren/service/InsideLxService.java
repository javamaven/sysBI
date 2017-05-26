package io.renren.service;

import io.renren.entity.InsideLxEntity;

import java.util.List;
import java.util.Map;

/**
 * 员工拉新统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-26 09:27:51
 */
public interface InsideLxService {

	
	List<InsideLxEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	List<InsideLxEntity> queryExport();

}
