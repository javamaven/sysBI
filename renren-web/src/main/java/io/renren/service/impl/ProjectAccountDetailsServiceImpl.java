package io.renren.service.impl;

import io.renren.dao.ProjectAccountDetailsDao;
import io.renren.entity.ProjectAccountDetailsEntity;
import io.renren.service.ProjectAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("ProjectAccountDetailsService")
public class ProjectAccountDetailsServiceImpl implements ProjectAccountDetailsService {
	@Autowired
	private ProjectAccountDetailsDao  projectAccountDetailsDao;
	

	@Override
	public List<ProjectAccountDetailsEntity> queryList(Map<String, Object> map){
		return projectAccountDetailsDao.queryList(map);
	}


}
