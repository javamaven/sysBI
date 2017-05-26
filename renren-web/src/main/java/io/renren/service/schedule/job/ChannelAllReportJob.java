package io.renren.service.schedule.job;

import java.io.File;
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

import io.renren.entity.ChannelChannelAllEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.ChannelChannelAllService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.util.CreateChartImage;
import io.renren.system.common.ConfigProp;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

/**
 * 渠道分次投资情况
 * 
 * @author liaodehui
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ChannelAllReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	ChannelChannelAllService service = SpringBeanFactory.getBean(ChannelChannelAllService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	CreateChartImage createChartImage = SpringBeanFactory.getBean(CreateChartImage.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "渠道分次投资情况";

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++ChannelAllReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);

			String date_offset_num = params.get("date_offset_num") + "";
			String reg_begindate = params.get("reg_begindate") + "";
			String reg_enddate = params.get("reg_enddate") + "";
			String invest_enddate = params.get("invest_enddate") + "";

			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				int days = Integer.valueOf(splitArr[0]);
				if ("day".equals(splitArr[1])) {
					reg_begindate = DateUtil.getCurrDayBefore(reg_begindate, -days, "yyyyMMdd");
					reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, -days, "yyyyMMdd");
					invest_enddate = DateUtil.getCurrDayBefore(invest_enddate, -days, "yyyyMMdd");
				} else if ("hour".equals(splitArr[1])) {
					reg_begindate = DateUtil.getHourBefore(reg_begindate, -days, "yyyyMMdd");
					reg_enddate = DateUtil.getHourBefore(reg_enddate, -days, "yyyyMMdd");
					invest_enddate = DateUtil.getHourBefore(invest_enddate, -days, "yyyyMMdd");
				}
				params.put("reg_begindate", reg_begindate);
				params.put("reg_enddate", reg_enddate);
				params.put("invest_enddate", invest_enddate);
			}
			params.put("limit", 1000 * 10000);
			logVo.setParams(JSON.toJSONString(params));

			List<ChannelChannelAllEntity> queryList = service.queryList(params);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < queryList.size(); i++) {
				ChannelChannelAllEntity entity = (ChannelChannelAllEntity) queryList.get(i);
				dataArray.add(entity);
			}
			String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());

			// 生成图片
			String fileName = ConfigProp.getPrintscreenDir() + File.separator + createChartImage.getFileName();
			createChartImage.createChannelAllPicture(fileName, JSON.toJSONString(params));
			String[] imgPaths = new String[] { fileName };
			mailUtil.sendEmailWithAttachAndImg(imgPaths, "自动推送，请勿回复", title, taskEntity.getReceiveEmailList(),
					taskEntity.getChaosongEmailList(), attachFilePath);
			logVo.setSendResult("success");
			logVo.setEmailValue(attachFilePath + " ;" + fileName);
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
