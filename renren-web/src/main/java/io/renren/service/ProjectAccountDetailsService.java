package io.renren.service;

import io.renren.entity.ProjectAccountDetailsEntity;

import java.util.List;
import java.util.Map;

/**
 * 项目台帐明细
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 10:07:16
 */
public interface ProjectAccountDetailsService {

	
	List<ProjectAccountDetailsEntity> queryList(Map<String, Object> map);
	


}
