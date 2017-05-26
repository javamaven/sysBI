package io.renren.service.impl;

import io.renren.dao.InsideLxDao;
import io.renren.entity.InsideLxEntity;
import io.renren.service.InsideLxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("dmReportInsideLxService")
public class InsideLxServiceImpl implements InsideLxService {
	@Autowired
	private InsideLxDao insideLxDao;
	

	
	@Override
	public List<InsideLxEntity> queryList(Map<String, Object> map){
		return insideLxDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return insideLxDao.queryTotal(map);
	}

	@Override
	public List<InsideLxEntity> queryExport() { return insideLxDao.queryExport();//导出EX
	}
	
}
