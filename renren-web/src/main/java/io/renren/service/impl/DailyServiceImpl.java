package io.renren.service.impl;

import io.renren.dao.DailyDao;
import io.renren.entity.DailyEntity;
import io.renren.entity.UserBehaviorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.renren.service.DailyService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Service("DailyService")
public class DailyServiceImpl implements DailyService {
	@Autowired
	private DailyDao dailyDao;


	@Override
	public void insert(UserBehaviorEntity UserBehavior) {dailyDao.insert(UserBehavior);
	}

	@Override
	public List<DailyEntity> queryList(Map<String, Object> map){return dailyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {return dailyDao.queryTotal(map);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod","日期");
		headMap.put("indicatorsName","指标名字");
		headMap.put("indicatorsValue","指标值");
		headMap.put("sequential","环比");
		headMap.put("compared","同比");
		headMap.put("monthMeanValue","30天均值");
		headMap.put("monthMeanValueThan","30天均值比");

		return headMap;
	}



}
