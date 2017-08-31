package io.renren.dao.shichang;

import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.shichang.DmReportDalilyMarketingEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-31 14:16:57
 */
public interface DmReportDalilyMarketingDao extends BaseDao<DmReportDalilyMarketingEntity> {

	List<DmReportDalilyMarketingEntity> queryDayList(Map<String, Object> map);
	List<DmReportDalilyMarketingEntity> queryMonthList(Map<String, Object> map);
	List<DmReportDalilyMarketingEntity> queryTotalList(Map<String, Object> map);
	
}
