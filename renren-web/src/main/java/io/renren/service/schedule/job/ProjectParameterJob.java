package io.renren.service.schedule.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.DmReportFinRepaymentdetailService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProjectParameterJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	DmReportFinRepaymentdetailService service = SpringBeanFactory.getBean(DmReportFinRepaymentdetailService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "项目台帐明细";

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		for (int i = 0; i < 4; i++) {// 失败则重跑3次
			boolean success = run(ctx);
			if (success) {
				break;
			}
		}
		logService.save(logVo);
		updateRunningTime();
	}

	@SuppressWarnings("unchecked")
	private boolean run(JobExecutionContext ctx) {
		boolean flag = true;
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++UserBehaviorReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);

			String date_offset_num = params.get("date_offset_num") + "";
			String STAT_PERIOD = params.get("STAT_PERIOD") + "";
			String PLANREPAYDATE = params.get("PLANREPAYDATE") + "";
			String REALREDATE = params.get("REALREDATE") + "";

			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				int days = Integer.valueOf(splitArr[0]);
				if ("day".equals(splitArr[1])) {
					STAT_PERIOD = DateUtil.getCurrDayBefore(STAT_PERIOD, -days, "yyyy-MM-dd");
					PLANREPAYDATE = DateUtil.getCurrDayBefore(PLANREPAYDATE, -days, "yyyy-MM-dd");
					REALREDATE = DateUtil.getCurrDayBefore(REALREDATE, -days, "yyyy-MM-dd");
				} else if ("hour".equals(splitArr[1])) {
					STAT_PERIOD = DateUtil.getHourBefore(STAT_PERIOD, -days, "yyyy-MM-dd");
					PLANREPAYDATE = DateUtil.getHourBefore(PLANREPAYDATE, -days, "yyyy-MM-dd");
					REALREDATE = DateUtil.getHourBefore(REALREDATE, -days, "yyyy-MM-dd");
				}
				params.put("STAT_PERIOD", STAT_PERIOD);
				params.put("PLANREPAYDATE", PLANREPAYDATE);
				params.put("REALREDATE", REALREDATE);
			}
			logVo.setParams(JSON.toJSONString(params));

			List<DmReportFinRepaymentdetailEntity> queryList = service.queryList(params);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < queryList.size(); i++) {
				DmReportFinRepaymentdetailEntity entity = (DmReportFinRepaymentdetailEntity) queryList.get(i);
				dataArray.add(entity);
			}
			if (queryList.size() > 0) {
				String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());

				mailUtil.sendWithAttach(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
						taskEntity.getChaosongEmailList(), attachFilePath);
				logVo.setEmailValue(attachFilePath);
			} else {
				logVo.setEmailValue("查询没有返回数据");
			}
			logVo.setSendResult("success");
		} catch (Exception e) {
			flag = false;
			logVo.setSendResult("fail");
			logVo.setDesc(JobUtil.getStackTrace(e));
			e.printStackTrace();
		} finally {
			long l2 = System.currentTimeMillis();
			logVo.setTaskId(taskEntity.getId());
			logVo.setChaosongEmail(taskEntity.getChaosongEmail());
			logVo.setReceiveEmal(taskEntity.getReceiveEmail());
			logVo.setTimeCost((int) (l2 - l1));
			logVo.setTime(new Date());
		}
		return flag;
	}

	/**
	 * 更新最后运行时间
	 * 
	 * @param id
	 * @param timeCost
	 */
	private void updateRunningTime() {
		ScheduleReportTaskEntity entity = new ScheduleReportTaskEntity();
		entity.setId(logVo.getTaskId());
		entity.setLastSendTime(new Date());
		System.err.println("+++++++++timeCost+++++++++++++" + logVo.getTimeCost());
		entity.setTimeCost(logVo.getTimeCost());
		entity.setCondition(logVo.getParams());
		taskService.update(entity);

	}

}
