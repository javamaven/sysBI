package io.renren.service;

import io.renren.entity.ProjectSumEntity;

import java.util.List;
import java.util.Map;

/**
 * 项目总台帐
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 15:41:58
 */
public interface ProjectSumService {

	List<ProjectSumEntity> queryExport();
	
	List<ProjectSumEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	Map<String, String> getExcelFields();
}
