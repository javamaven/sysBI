package io.renren.dao.yunying.dayreport;
import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-29 16:30:26
 */
public interface DmReportDepSalesDao extends BaseDao<DmReportDepSalesEntity> {

	List<DmReportDepSalesEntity> queryLists(Map<String, Object> map);

	List<DmReportDepSalesEntity> queryListss(Map<String, Object> map);
	
}
