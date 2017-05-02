package io.renren.service.impl;

import io.renren.dao.DailyDao;
import io.renren.entity.DailyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.renren.service.DailyService;
import java.util.List;
import java.util.Map;



@Service("DailyService")
public class DailyServiceImpl implements DailyService {
	@Autowired
	private DailyDao dailyDao;

	@Override
	public List<DailyEntity> queryList(Map<String, Object> map){
		return dailyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {return dailyDao.queryTotal(map);
	}
	@Override
	public List<DailyEntity> queryExports() { return dailyDao.queryExports();
	}


}
