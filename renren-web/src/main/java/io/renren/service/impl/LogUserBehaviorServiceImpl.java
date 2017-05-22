package io.renren.service.impl;

import io.renren.dao.LogUserBehaviorDao;
import io.renren.entity.LogUserBehaviorEntity;
import io.renren.service.LogUserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service(" LogUserBehaviorService")
public class LogUserBehaviorServiceImpl implements LogUserBehaviorService {
	@Autowired
	private LogUserBehaviorDao logUserBehaviorDao;

	@Override
	public List<LogUserBehaviorEntity>  queryAction() {
		return logUserBehaviorDao.queryAction();
	}

	@Override
	public List<LogUserBehaviorEntity>  queryActionPlatform() {
		return logUserBehaviorDao.queryActionPlatform();
	}

	@Override
	public List<LogUserBehaviorEntity> queryList(Map<String, Object> map){
		return logUserBehaviorDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return logUserBehaviorDao.queryTotal(map);
	}
	@Override
	public List<LogUserBehaviorEntity> queryExport() { return logUserBehaviorDao.queryExport();//导出EX
	}

	
}
