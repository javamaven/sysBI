package io.renren.service.schedule.job.yunying;

import java.util.Date;
import java.util.HashMap;
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

import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.job.JobUtil;
import io.renren.service.yunying.dayreport.DmReportBasicDailyService;
import io.renren.system.common.ConfigProp;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

/**
 * 每日基本数据推送任务
 * 
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EveryDayBasicDataReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());
	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	DmReportBasicDailyService service = SpringBeanFactory.getBean(DmReportBasicDailyService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "每日基本数据";

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		for (int i = 0; i < 4; i++) {// 失败则重跑3次
			boolean success = run(ctx);
			if (success) {
				break;
			}
		}
		if (!ConfigProp.getIsSendEmail()) {
			return;
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
		log.info("+++++++++EveryDayBasicDataReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.putAll(params);
			String date_offset_num = params.get("date_offset_num") + "";
//			String[] splitArr = date_offset_num.split("-");
			String statPeriod = params.get("statPeriod") + "";
			String reg_begindate = params.get("reg_begindate") + "";
//			if (!"0".equals(splitArr[0])) {
//				if (StringUtils.isNotEmpty(statPeriod)) {
//					int days = Integer.valueOf(splitArr[0]);
//					if ("day".equals(splitArr[1])) {
//						statPeriod = DateUtil.getCurrDayBefore(statPeriod, -days, "yyyy-MM-dd");
//					} else if ("hour".equals(splitArr[1])) {
//						statPeriod = DateUtil.getHourBefore(statPeriod, -days, "yyyy-MM-dd");
//					}
//					if ("day".equals(splitArr[1])) {
//						reg_begindate = DateUtil.getCurrDayBefore(reg_begindate, -days, "yyyy-MM-dd");
//					} else if ("hour".equals(splitArr[1])) {
//						reg_begindate = DateUtil.getHourBefore(reg_begindate, -days, "yyyy-MM-dd");
//					}
//					params.put("statPeriod", statPeriod);
//					params.put("reg_begindate", reg_begindate);
//				}
//			}
			
			statPeriod = DateUtil.getCurrDayBefore(1);//yyyyMMdd
			reg_begindate = DateUtil.getCurrDayBefore(9);//yyyyMMdd
			queryParams.put("statPeriod", statPeriod);
			queryParams.put("reg_begindate", reg_begindate);
			params.put("statPeriod", statPeriod);
			params.put("reg_begindate", reg_begindate);
			logVo.setParams(JSON.toJSONString(params));

			List<DmReportBasicDailyEntity> queryList = service.queryList(queryParams);
			
			boolean yesterdayHasData = false;
			String yesterday = DateUtil.getCurrDayBefore(1);
			//如果昨天没有数据，则不发送邮件
			for (int i = 0; i < queryList.size(); i++) {
				DmReportBasicDailyEntity entity = queryList.get(i);
				if(entity.getStatPeriod().replace("-", "").equals(yesterday)){
					yesterdayHasData = true;
					break;
				}
			}
			
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < queryList.size(); i++) {
				DmReportBasicDailyEntity entity = queryList.get(i);
				dataArray.add(entity);
			}
			if (queryList.size() > 0 && yesterdayHasData) {
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
		entity.setTimeCost(logVo.getTimeCost());
		entity.setCondition(logVo.getParams());
		System.err.println("+++++++++timeCost+++++++++++++" + logVo.getTimeCost() + "  ;entity=" + entity);
		taskService.update(entity);

	}

}
