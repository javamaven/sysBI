package io.renren.service.schedule.impl;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.schedule.ScheduleReportTaskDao;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.service.schedule.JobService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;

@Service("scheduleReportTaskService")
public class ScheduleReportTaskServiceImpl implements ScheduleReportTaskService {
	@Autowired
	private ScheduleReportTaskDao scheduleReportTaskDao;

	@Autowired
	private JobService jobService;

	@Override
	public ScheduleReportTaskEntity queryObject(Integer id) {
		return scheduleReportTaskDao.queryObject(id);
	}

	@Override
	public List<ScheduleReportTaskEntity> queryList(Map<String, Object> map) {
		return scheduleReportTaskDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return scheduleReportTaskDao.queryTotal(map);
	}

	@Override
	public void save(ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTaskDao.save(scheduleReportTask);
	}

	@Override
	public void update(ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTaskDao.update(scheduleReportTask);
	}

	@Override
	public void delete(Integer id) {
		ScheduleReportTaskEntity entity = scheduleReportTaskDao.queryObject(id);
		deleteScheduleJob(entity);
		scheduleReportTaskDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		for (int i = 0; i < ids.length; i++) {
			ScheduleReportTaskEntity entity = scheduleReportTaskDao.queryObject(ids[i]);
			deleteScheduleJob(entity);
		}
		scheduleReportTaskDao.deleteBatch(ids);
	}

	/**
	 * 启动任务
	 */
	@Override
	public void startTask(ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTask.setIsRunning("1");
		scheduleReportTaskDao.update(scheduleReportTask);
		ScheduleReportTaskEntity entity = scheduleReportTaskDao.queryObject(scheduleReportTask.getId());
		try {
			JobVo jobVo = entity.toJobVo();
			jobService.deleteJob(jobVo);
			jobService.addJob(jobVo, jobVo.getJobClass());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopTask(ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTask.setIsRunning("0");
		scheduleReportTaskDao.update(scheduleReportTask);
		ScheduleReportTaskEntity entity = scheduleReportTaskDao.queryObject(scheduleReportTask.getId());
		deleteScheduleJob(entity);
	}
	
	public void deleteScheduleJob(ScheduleReportTaskEntity entity){
		try {
			JobVo jobVo = entity.toJobVo();
			jobService.pauseJob(jobVo);
			jobService.deleteJob(jobVo);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
