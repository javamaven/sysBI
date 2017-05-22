package io.renren.service.schedule;

import java.util.List;
import java.util.Map;

import io.renren.entity.schedule.ScheduleReportTaskLogEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-19 10:19:30
 */
public interface ScheduleReportTaskLogService {
	
	ScheduleReportTaskLogEntity queryObject(Integer id);
	
	List<ScheduleReportTaskLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ScheduleReportTaskLogEntity scheduleReportTaskLog);
	
	void update(ScheduleReportTaskLogEntity scheduleReportTaskLog);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
