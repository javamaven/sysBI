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

import io.renren.entity.MarketChannelEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.MarketChannelService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MarketChannelReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());
	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	MarketChannelService service = SpringBeanFactory.getBean(MarketChannelService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "渠道负责人情况";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++MarketChannelReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);

			String date_offset_num = params.get("date_offset_num") + "";
			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				String reg_begindate = params.get("reg_begindate").toString();
				String reg_enddate = params.get("reg_enddate").toString();
				int days = Integer.valueOf(splitArr[0]);
				if("day".equals(splitArr[1])){
					reg_begindate = DateUtil.getCurrDayBefore(reg_begindate, -days, "yyyyMMdd");
					reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, -days, "yyyyMMdd");
				}else if("hour".equals(splitArr[1])){
					reg_begindate = DateUtil.getHourBefore(reg_begindate, -days, "yyyyMMdd");
					reg_enddate = DateUtil.getHourBefore(reg_enddate, -days, "yyyyMMdd");
				}
				params.put("reg_begindate", reg_begindate);
				params.put("reg_enddate", reg_enddate);
			}
			logVo.setParams(JSON.toJSONString(params));
			
			List<MarketChannelEntity> queryList = service.queryList(params);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < queryList.size(); i++) {
				MarketChannelEntity entity = queryList.get(i);
				dataArray.add(entity);
			}
			String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());

			mailUtil.sendWithAttach(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
					taskEntity.getChaosongEmailList(), attachFilePath);

			logVo.setSendResult("success");
		} catch (Exception e) {
			logVo.setSendResult("fail");
			logVo.setDesc(JobUtil.getStackTrace(e));
			e.printStackTrace();
		} finally {
			long l2 = System.currentTimeMillis();
			updateRunningTime(taskEntity.getId(), l2 - l1, logVo.getParams());

			logVo.setTaskId(taskEntity.getId());
			logVo.setChaosongEmail(taskEntity.getChaosongEmail());
			logVo.setReceiveEmal(taskEntity.getReceiveEmail());
			logVo.setTimeCost((int) (l2 - l1));
			logVo.setTime(new Date());
			logService.save(logVo);
		}
	}

	/**
	 * 更新最后运行时间
	 * 
	 * @param id
	 * @param timeCost
	 */
	private void updateRunningTime(int id, long timeCost, String params) {
		ScheduleReportTaskEntity entity = new ScheduleReportTaskEntity();
		entity.setId(id);
		entity.setLastSendTime(new Date());
		System.err.println("+++++++++timeCost+++++++++++++" + timeCost);
		entity.setTimeCost((int) timeCost);
		entity.setCondition(params);
		taskService.update(entity);

	}


}
