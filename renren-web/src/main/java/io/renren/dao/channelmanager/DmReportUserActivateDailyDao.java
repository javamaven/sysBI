package io.renren.dao.channelmanager;

import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;

/**
 * 用户激活情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-04 17:57:21
 */
public interface DmReportUserActivateDailyDao extends BaseDao<DmReportUserActivateDailyEntity> {

	
	
	UserActiveInfoEntity queryTotalList(Map<String, Object> map);
}
