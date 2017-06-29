package io.renren.service.schedule.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.dao.schedule.ScheduleReportTaskLogDao;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.system.jdbc.JdbcHelper;



@Service("scheduleReportTaskLogService")
public class ScheduleReportTaskLogServiceImpl implements ScheduleReportTaskLogService {
	@Autowired
	private DruidDataSource dataSource;
	
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
	public void save(ScheduleReportTaskLogEntity vo) {

		// scheduleReportTaskLogDao.save(scheduleReportTaskLog);
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String filePath = vo.getEmailValue();
		int coloumnIndex = 1;// 文件字段在表中，属于第二个字段
		String insertSql = "INSERT INTO `schedule_report_task_log` ( `FILE`, `TASK_ID`, `TIME_COST`, `SEND_RESULT`, `PARAMS`, `RECEIVE_EMAL`, `CHAOSONG_EMAIL`, `EMAIL_VALUE`, `TIME`, `DESC`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, now(), ?);";
		try {
			jdbcHelper.storeImg(filePath, coloumnIndex, insertSql, null, vo.getTaskId(), vo.getTimeCost(),
					vo.getSendResult(), vo.getParams(), vo.getReceiveEmal(), vo.getChaosongEmail(), vo.getEmailValue(), vo.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
		}

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
