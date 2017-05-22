package io.renren.service;

import io.renren.entity.LogUserBehaviorEntity;

import java.util.List;
import java.util.Map;

/**
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
public interface LogUserBehaviorService {

	List<LogUserBehaviorEntity> queryAction();
	List<LogUserBehaviorEntity> queryActionPlatform();
	List<LogUserBehaviorEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	List<LogUserBehaviorEntity> queryExport();

}
