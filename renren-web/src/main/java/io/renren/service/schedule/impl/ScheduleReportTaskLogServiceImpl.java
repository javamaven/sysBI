package io.renren.service.schedule.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.schedule.ScheduleReportTaskLogDao;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;



@Service("scheduleReportTaskLogService")
public class ScheduleReportTaskLogServiceImpl implements ScheduleReportTaskLogService {
	@Autowired
	private ScheduleReportTaskLogDao scheduleReportTaskLogDao;
	
	@Override
	public ScheduleReportTaskLogEntity queryObject(Integer id){
		return scheduleReportTaskLogDao.queryObject(id);
	}
	
	@Override
	public List<ScheduleReportTaskLogEntity> queryList(Map<String, Object> map){
		return scheduleReportTaskLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return scheduleReportTaskLogDao.queryTotal(map);
	}
	
	@Override
	public void save(ScheduleReportTaskLogEntity scheduleReportTaskLog){
		scheduleReportTaskLogDao.save(scheduleReportTaskLog);
	}
	
	@Override
	public void update(ScheduleReportTaskLogEntity scheduleReportTaskLog){
		scheduleReportTaskLogDao.update(scheduleReportTaskLog);
	}
	
	@Override
	public void delete(Integer id){
		scheduleReportTaskLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		scheduleReportTaskLogDao.deleteBatch(ids);
	}
	
}
