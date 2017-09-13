package io.renren.service;

import io.renren.entity.DailyEntity;
import io.renren.entity.UserBehaviorEntity;

import java.util.List;
import java.util.Map;

/**
 * 市场部每日渠道数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
public interface DailyService {
	Map<String, String> getExcelFields();
	List<DailyEntity> queryList(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
	void insert(UserBehaviorEntity logUserBehavior);


}

