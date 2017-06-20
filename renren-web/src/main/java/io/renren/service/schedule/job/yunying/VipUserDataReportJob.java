package io.renren.service.schedule.job.yunying;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import io.renren.entity.yunying.dayreport.DmReportVipSituationEntity;
import io.renren.entity.yunying.dayreport.DmReportVipUserEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.job.JobUtil;
import io.renren.service.yunying.dayreport.DmReportVipSituationService;
import io.renren.service.yunying.dayreport.DmReportVipUserService;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

/**
 * 每日VIP用户数据报告
 * 
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class VipUserDataReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());
	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	DmReportVipUserService service = SpringBeanFactory.getBean(DmReportVipUserService.class);
	DmReportVipSituationService serviceTotal = SpringBeanFactory.getBean(DmReportVipSituationService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "每日VIP用户数据报告";
	String title2 = "VIP所属人汇总情况";

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
		log.info("+++++++++VipUserDataReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.putAll(params);
			String date_offset_num = params.get("date_offset_num") + "";
			String[] splitArr = date_offset_num.split("-");
			String statPeriod = params.get("statPeriod") + "";
			if (!"0".equals(splitArr[0])) {
				if (StringUtils.isNotEmpty(statPeriod)) {
					int days = Integer.valueOf(splitArr[0]);
					if ("day".equals(splitArr[1])) {
						statPeriod = DateUtil.getCurrDayBefore(statPeriod, -days, "yyyy-MM-dd");
					} else if ("hour".equals(splitArr[1])) {
						statPeriod = DateUtil.getHourBefore(statPeriod, -days, "yyyy-MM-dd");
					}
					params.put("statPeriod", statPeriod);
				}
			}
			queryParams.put("statPeriod", statPeriod.replace("-", ""));
			logVo.setParams(JSON.toJSONString(params));
			List<DmReportVipUserEntity> queryList = service.queryList(queryParams);
			// vip所属人汇总信息
			List<DmReportVipSituationEntity> totalList = serviceTotal.queryList(queryParams);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < queryList.size(); i++) {
				DmReportVipUserEntity entity = queryList.get(i);
				dataArray.add(entity);
			}
			JSONArray dataArray2 = new JSONArray();
			for (int i = 0; i < totalList.size(); i++) {
				DmReportVipSituationEntity entity = totalList.get(i);
				dataArray2.add(entity);
			}
			if (queryList.size() > 0) {
				String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());
				String attachFilePath2 = jobUtil.buildAttachFile(dataArray2, title2, title2,
						serviceTotal.getExcelFields());
				List<String> attachList = new ArrayList<String>();
				attachList.add(attachFilePath);
				attachList.add(attachFilePath2);
				mailUtil.sendWithAttachs(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
						taskEntity.getChaosongEmailList(), attachList);
				logVo.setEmailValue(attachFilePath + "," + attachFilePath2);
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
		System.err.println("+++++++++timeCost+++++++++++++" + logVo.getTimeCost() + " ;entity=" + entity);
		taskService.update(entity);

	}

}
