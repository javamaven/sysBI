package io.renren.service.schedule.job;

import java.util.Date;
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

import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.ChannelInvestTimesService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;

/**
 * 渠道投资次数分析
 * 
 * @author liaodehui
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ChannelInvestTimesReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	ChannelInvestTimesService service = SpringBeanFactory.getBean(ChannelInvestTimesService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "渠道投资次数分析";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++ChannelInvestTimesReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);
			String date_offset_num = params.get("date_offset_num") + "";
			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				String invBeginDate = params.get("invBeginDate").toString();
				String invEndDate = params.get("invEndDate").toString();
				String regBeginDate = params.get("regBeginDate").toString();
				String regEndDate = params.get("regEndDate").toString();
				int days = Integer.valueOf(splitArr[0]);
				if ("day".equals(splitArr[1])) {
					invBeginDate = DateUtil.getCurrDayBefore(invBeginDate, -days, "yyyyMMdd");
					invEndDate = DateUtil.getCurrDayBefore(invEndDate, -days, "yyyyMMdd");
					regBeginDate = DateUtil.getCurrDayBefore(regBeginDate, -days, "yyyyMMdd");
					regEndDate = DateUtil.getCurrDayBefore(regEndDate, -days, "yyyyMMdd");
				} else if ("hour".equals(splitArr[1])) {
					invBeginDate = DateUtil.getHourBefore(invBeginDate, -days, "yyyyMMdd");
					invEndDate = DateUtil.getCurrDayBefore(invEndDate, -days, "yyyyMMdd");
					regBeginDate = DateUtil.getHourBefore(regBeginDate, -days, "yyyyMMdd");
					regEndDate = DateUtil.getHourBefore(regEndDate, -days, "yyyyMMdd");
				}
				params.put("invBeginDate", invBeginDate);
				params.put("invEndDate", invEndDate);
				params.put("regBeginDate", regBeginDate);
				params.put("regEndDate", regEndDate);
			}
			logVo.setParams(JSON.toJSONString(params));
			
			PageUtils pages = service.query(params);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < pages.getList().size(); i++) {
				ChannelInvestTimesEntity entity = (ChannelInvestTimesEntity) pages.getList().get(i);
				dataArray.add(entity);
			}
			String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());

			mailUtil.sendWithAttach(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
					taskEntity.getChaosongEmailList(), attachFilePath);
			logVo.setEmailValue(attachFilePath);
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
