package io.renren.service.schedule.job.yunying.basicreport;

import java.text.SimpleDateFormat;
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
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.job.JobUtil;
import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.service.yunying.dayreport.DmReportVipSituationService;
import io.renren.system.common.SpringBeanFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;

/**
 * 注册3天未投资用户
 * 
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FirstInvestThreeDayNotInvestReportJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());
	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	BasicReportService service = SpringBeanFactory.getBean(BasicReportService.class);
	DmReportVipSituationService serviceTotal = SpringBeanFactory.getBean(DmReportVipSituationService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "本月注册首投后三天未复投用户";
	SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

	/**
	 * 每天9点到16点，每个小时推送一遍 周六周日不推送
	 * 
	 * @param ctx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean run(JobExecutionContext ctx) {
		boolean flag = true;
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++FirstInvestThreeDayNotInvestReportJob+++++++++++++" + taskEntity);
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
			queryParams.put("statPeriod", statPeriod);
			logVo.setParams(JSON.toJSONString(params));
			
			PageUtils page = service.queryFirstInvestNotMultiList(queryParams);
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) page.getList();
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> entity = dataList.get(i);
				dataArray.add(entity);
			}
			if (dataList.size() > 0) {
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
		System.err.println("+++++++++timeCost+++++++++++++" + logVo.getTimeCost() + " ;entity=" + entity);
		taskService.update(entity);

	}

}
