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

import io.renren.entity.ChannelRenewDataEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.ChannelRenewDataService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;

/**
 * 渠道续费数据提取汇总
 * 
 * @author liaodehui
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ChannelRenewReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	ChannelRenewDataService service = SpringBeanFactory.getBean(ChannelRenewDataService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "渠道续费数据提取汇总";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("++++++++++ChannelRenewReportJob++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);

			String date_offset_num = params.get("date_offset_num") + "";
			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				String date = params.get("date").toString();
				int days = Integer.valueOf(splitArr[0]);
				if ("day".equals(splitArr[1])) {
					date = DateUtil.getCurrDayBefore(date, -days, "yyyy-MM-dd");
				} else if ("hour".equals(splitArr[1])) {
					date = DateUtil.getHourBefore(date, -days, "yyyy-MM-dd");
				}
				params.put("date", date);
			}
			logVo.setParams(JSON.toJSONString(params));
			PageUtils pages = service.query(params);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < pages.getList().size(); i++) {
				ChannelRenewDataEntity entity = (ChannelRenewDataEntity) pages.getList().get(i);
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
