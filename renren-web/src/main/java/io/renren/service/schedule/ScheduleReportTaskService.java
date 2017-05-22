package io.renren.service.schedule;

import java.util.List;
import java.util.Map;

import io.renren.entity.schedule.ScheduleReportTaskEntity;

/**
 * 报表推送任务配置表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-15 11:56:53
 */
public interface ScheduleReportTaskService {
	
	ScheduleReportTaskEntity queryObject(Integer id);
	
	List<ScheduleReportTaskEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ScheduleReportTaskEntity scheduleReportTask);
	
	void update(ScheduleReportTaskEntity scheduleReportTask);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	void startTask(ScheduleReportTaskEntity scheduleReportTask);

	void stopTask(ScheduleReportTaskEntity scheduleReportTask);
}
