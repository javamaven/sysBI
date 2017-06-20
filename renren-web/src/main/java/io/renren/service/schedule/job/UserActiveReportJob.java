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

import io.renren.entity.ChannelStftInfoEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.channelmanager.DmReportUserActivateDailyService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;

/**
 * 用户激活情况推送任务
 * 
 * @author liaodehui
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class UserActiveReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	DmReportUserActivateDailyService service = SpringBeanFactory.getBean(DmReportUserActivateDailyService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "用户激活情况";

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
		log.info("+++++++++UserActiveReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);

			String date_offset_num = params.get("date_offset_num") + "";
			// Integer page = Integer.parseInt(params.get("page") + "");
			// Integer limit = Integer.parseInt(params.get("limit") + "");
			String statPeriod = params.get("statPeriod") + "";
			String afterInvestBalance_start = params.get("afterInvestBalance_start") + "";
			String afterInvestBalance_end = params.get("afterInvestBalance_end") + "";
			String startFirstInvestTime = params.get("startFirstInvestTime") + "";
			String endFirstInvestTime = params.get("endFirstInvestTime") + "";
			String startTotalMoney = params.get("startTotalMoney") + "";
			String endTotalMoney = params.get("endTotalMoney") + "";
			String registerFrom = params.get("registerFrom") + "";
			String startTotalInvestAmount = params.get("startTotalInvestAmount") + "";
			String endTotalInvestAmount = params.get("endTotalInvestAmount") + "";
			String startFirstInvestAmount = params.get("startFirstInvestAmount") + "";
			String endFirstInvestAmount = params.get("endFirstInvestAmount") + "";
			String startRegisterTime = params.get("startRegisterTime") + "";
			String endRegisterTime = params.get("endRegisterTime") + "";
			String bangCard = params.get("bangCard") + "";
			String realName = params.get("realName") + "";
			String channelName = params.get("channelName") + "";

			String[] splitArr = date_offset_num.split("-");
			if (!"0".equals(splitArr[0])) {
				int days = Integer.valueOf(splitArr[0]);
				if ("day".equals(splitArr[1])) {
					statPeriod = DateUtil.getCurrDayBefore(statPeriod, -days, "yyyy-MM-dd");
				} else if ("hour".equals(splitArr[1])) {
					statPeriod = DateUtil.getHourBefore(statPeriod, -days, "yyyy-MM-dd");
				}
				params.put("statPeriod", statPeriod);
			}
			logVo.setParams(JSON.toJSONString(params));

			PageUtils pages = service.query(1, 1000 * 10000, statPeriod, afterInvestBalance_start,
					afterInvestBalance_end, startFirstInvestTime, endFirstInvestTime, startTotalMoney, endTotalMoney,
					startTotalInvestAmount, endTotalInvestAmount, startFirstInvestAmount, endFirstInvestAmount,
					startRegisterTime, endRegisterTime, bangCard, realName, channelName, registerFrom);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < pages.getList().size(); i++) {
				ChannelStftInfoEntity entity = (ChannelStftInfoEntity) pages.getList().get(i);
				dataArray.add(entity);
			}
			if (dataArray.size() > 0) {
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
