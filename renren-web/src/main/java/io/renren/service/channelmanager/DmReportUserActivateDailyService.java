package io.renren.service.channelmanager;


import java.util.List;
import java.util.Map;

import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;

/**
 * 用户激活情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-04 17:57:21
 */
public interface DmReportUserActivateDailyService {
	
	DmReportUserActivateDailyEntity queryObject(Integer statPeriod);
	
	List<DmReportUserActivateDailyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DmReportUserActivateDailyEntity dmReportUserActivateDaily);
	
	void update(DmReportUserActivateDailyEntity dmReportUserActivateDaily);
	
	void delete(Integer statPeriod);
	
	void deleteBatch(Integer[] statPeriods);
	
	UserActiveInfoEntity queryTotalList(Map<String, Object> map);
}
