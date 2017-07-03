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

import io.renren.entity.DmReportInvestmentDailyEntity;
import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.DmReportInvestmentDailyService;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.system.common.ConfigProp;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;

/**
 * 用户投资推送任务
 * 
 * @author liaodehui
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class UserInvestReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());

	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	DmReportInvestmentDailyService service = SpringBeanFactory.getBean(DmReportInvestmentDailyService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "用户投资情况";

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
		log.info("+++++++++UserInvestReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			// String condition = taskEntity.getCondition();
			// Map<String, Object> params = JSON.parseObject(condition,
			// Map.class);
			ScheduleReportTaskEntity queryObject = taskService.queryObject(taskEntity.getId());
			Map<String, Object> params = JSON.parseObject(queryObject.getCondition(), Map.class);
			// Integer page = params.get("page") == null ? 0 :
			// Integer.parseInt(params.get("page").toString());
			// Integer limit = params.get("limit") == null ? 0 :
			// Integer.parseInt(params.get("limit").toString());
			String userId = params.get("userId") + "";
			String userName = params.get("userName") + "";
			String channelId = params.get("channelId") + "";
			String channelName = params.get("channelName") + "";
			String investStartTime = params.get("investStartTime") + "";
			String investEndTime = params.get("investEndTime") + "";
			String operPlatform = params.get("operPlatform") + "";
			String withProType = params.get("withProType") + "";
			PageUtils pages = service.query(1, 1000 * 10000, userId, userName, channelId, channelName, investStartTime,
					investEndTime, operPlatform, withProType);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < pages.getList().size(); i++) {
				DmReportInvestmentDailyEntity entity = (DmReportInvestmentDailyEntity) pages.getList().get(i);
				dataArray.add(entity);
			}
			if (dataArray.size() > 0) {
				String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());

				mailUtil.sendWithAttach(title + "-每日报表", "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
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
