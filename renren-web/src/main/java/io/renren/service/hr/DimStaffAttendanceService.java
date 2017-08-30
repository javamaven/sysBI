package io.renren.service.hr;

import java.util.List;
import java.util.Map;

import io.renren.entity.hr.DimStaffAttendanceEntity;

/**
 * 考勤信息记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-29 10:08:21
 */
public interface DimStaffAttendanceService {
	
	DimStaffAttendanceEntity queryObject(DimStaffAttendanceEntity dimStaffAttendance);
	
	List<DimStaffAttendanceEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DimStaffAttendanceEntity dimStaffAttendance);
	
	void update(DimStaffAttendanceEntity dimStaffAttendance);
	
	void delete(String realname);
	
	void deleteBatch(String[] realnames);

	void batchInsert(List<Map<String, Object>> list);
}
