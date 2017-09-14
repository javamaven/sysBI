package io.renren.service.schedule.job.yunying.basicreport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.alibaba.druid.pool.DruidDataSource;
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
import io.renren.system.common.ConfigProp;
import io.renren.system.common.SpringBeanFactory;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

/**
 * 注册未投资用户，
 * 
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PhoneSaleCpsChannelSendJob implements Job {
	public final Logger log = Logger.getLogger(this.getClass());
	private ScheduleReportTaskService taskService = SpringBeanFactory.getBean(ScheduleReportTaskService.class);
	
	DruidDataSource dataSource = SpringBeanFactory.getBean(DruidDataSource.class);
	DataSourceFactory dataSourceFactory = SpringBeanFactory.getBean(DataSourceFactory.class);
	BasicReportService service = SpringBeanFactory.getBean(BasicReportService.class);
	DmReportVipSituationService serviceTotal = SpringBeanFactory.getBean(DmReportVipSituationService.class);
	private ScheduleReportTaskLogService logService = SpringBeanFactory.getBean(ScheduleReportTaskLogService.class);

	private ScheduleReportTaskLogEntity logVo;
	String title = "";
	SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	String type = "cps_channel";

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

	/**
	 * @param ctx
	 * @return
	 */
	private boolean run(JobExecutionContext ctx) {
		boolean flag = true;
		logVo = new ScheduleReportTaskLogEntity();
		long l1 = System.currentTimeMillis();
		JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
		JobVo jobVo = (JobVo) jobDataMap.get("jobVo");
		ScheduleReportTaskEntity taskEntity = jobVo.getTaskEntity();
		log.info("+++++++++PhoneSaleCpsChannelSendJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			String registerStartTime = "";
			String registerEndTime = "";

			registerStartTime = DateUtil.getCurrDayBefore(7, "yyyy-MM-dd") + " 00:00:00";
			registerEndTime = DateUtil.getCurrDayBefore(7, "yyyy-MM-dd") + " 23:59:59";
			queryParams.put("registerStartTime", registerStartTime);
			queryParams.put("registerEndTime", registerEndTime);
			queryParams.put("type", type);
			logVo.setParams(JSON.toJSONString(queryParams));
//			List<Map<String, Object>> dataList = service.queryRegisterThreeDaysNotInvestList(queryParams);
			List<Map<String, Object>> dataList = service.queryPayOrCpsChannelList(queryParams);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> entity = dataList.get(i);
				dataArray.add(entity);
			}
			String year = registerEndTime.substring(0 , 4);
			String month = registerEndTime.substring(5 , 7);
			String day = registerEndTime.substring(8 , 10);
			List<String> attachFilePathList = new ArrayList<>();
			title = "注册7天未投资用户(CPS渠道)-W-" + month + day + "-" + dataList.size();
//			title = "注册3天未投资用户-W-" + month + day + "-" + dataList.size();
			
			String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());
			
			attachFilePathList.add(attachFilePath);
			mailUtil.sendWithAttachs(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(), taskEntity.getChaosongEmailList(), attachFilePathList );
			
			logVo.setEmailValue(attachFilePath);
			//将电销的数据，入库保存
			if (ConfigProp.getIsSendEmail()) {
				insertPhoneSaleData(year+month+day, dataList);
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

	private void insertPhoneSaleData(String dataTime, List<Map<String, Object>> dataList) {
		try {
			List<Map<String, String>> insert_data = new ArrayList<Map<String,String>>();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> insert_map = new HashMap<String, String>();
				Map<String, Object> map = dataList.get(i);
				insert_map.put("data", JSON.toJSONString(map));
				insert_map.put("cg_user_id", map.get("用户ID") + "");
				insert_map.put("type", type);
				insert_map.put("data_time", dataTime);
				insert_data.add(insert_map);
			}
			if(insert_data.size() > 0){
				service.batchInsertPhoneSaleJobSendData(insert_data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
