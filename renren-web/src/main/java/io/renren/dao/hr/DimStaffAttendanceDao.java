package io.renren.dao.hr;

import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.hr.DimStaffAttendanceEntity;

/**
 * 考勤信息记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-29 10:08:21
 */
public interface DimStaffAttendanceDao extends BaseDao<DimStaffAttendanceEntity> {

	void batchInsert(List<Map<String, Object>> list);
	
}
