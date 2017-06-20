package io.renren.dao.yunying.basicreport;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 */
public interface BasicReportDao {

	public List<Map<String, Object>> queryRegisterThreeDaysNotInvestList(Map<String, Object> params);

}
