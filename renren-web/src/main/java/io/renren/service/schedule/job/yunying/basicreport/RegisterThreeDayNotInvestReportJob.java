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
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.MailUtil;

/**
 * 注册3天未投资用户
 * 
 * @author Administrator
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RegisterThreeDayNotInvestReportJob implements Job {
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
	 * 每天9点到16点，每个小时推送一遍 周六周日不推送
	 * 
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
		log.info("+++++++++RegisterThreeDayNotInvestReportJob+++++++++++++" + taskEntity);
		MailUtil mailUtil = new MailUtil();
		JobUtil jobUtil = new JobUtil();
		try {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			String registerStartTime = "";
			String registerEndTime = "";

			registerStartTime = DateUtil.getCurrDayBefore(3, "yyyy-MM-dd") + " 00:00:00";
			registerEndTime = DateUtil.getCurrDayBefore(3, "yyyy-MM-dd") + " 23:59:59";
			queryParams.put("registerStartTime", registerStartTime);
			queryParams.put("registerEndTime", registerEndTime);
			logVo.setParams(JSON.toJSONString(queryParams));
			List<Map<String, Object>> dataList = service.queryRegisterThreeDaysNotInvestList(queryParams);
			JSONArray dataArray = new JSONArray();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> entity = dataList.get(i);
				dataArray.add(entity);
			}
			String year = registerEndTime.substring(0 , 4);
			String month = registerEndTime.substring(5 , 7);
			String day = registerEndTime.substring(8 , 10);
			List<String> attachFilePathList = new ArrayList<>();
			//每小时推送数据补数
			JSONArray oneHoursArray = getOneHourHasConfirmData();//查询已经确定渠道是否收费数据
			String oneHourAttachFilePath = null;
			if(oneHoursArray != null && oneHoursArray.size()  > 0){
				
				String hourTitle = "注册一小时未投资用户-W-" + month + day + "-" + oneHoursArray.size();
				oneHourAttachFilePath = jobUtil.buildAttachFile(oneHoursArray, hourTitle, hourTitle, service.getExcelFields());
			}
//			if (dataList.size() > 0) {
				title = "注册3天未投资用户-W-" + month + day + "-" + dataList.size();
				
				String attachFilePath = jobUtil.buildAttachFile(dataArray, title, title, service.getExcelFields());
				
				attachFilePathList.add(attachFilePath);
				if(oneHoursArray != null && oneHoursArray.size()  > 0){
					attachFilePathList.add(oneHourAttachFilePath);
				}
				mailUtil.sendWithAttachs(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(), taskEntity.getChaosongEmailList(), attachFilePathList );
				
//				mailUtil.sendWithAttach(title, "自动推送，请勿回复", taskEntity.getReceiveEmailList(),
//						taskEntity.getChaosongEmailList(), attachFilePath);
				if(oneHoursArray != null && oneHoursArray.size()  > 0){
					logVo.setEmailValue(attachFilePath + "," + oneHourAttachFilePath);
				}else{
					logVo.setEmailValue(attachFilePath);
				}
//			} else {
//				logVo.setEmailValue("查询没有返回数据");
//			}
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
				insert_map.put("type", "day");
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

	String query_sql = "select " +
						"p.* " +
						"from  " +
						"phone_sale_hour_not_send p " +
						"where 1=1 " +
						"and p.CHANNEL_TYPE like '%免费%' " +
						"and p.insert_time >= CONCAT(DATE_ADD(curdate(),interval -day(curdate()) + 1 day),' 00:00:00') ";
	
	String query_one_hour_sql = 
			"SELECT " +
			"	u.user_id 用户ID, " +
			"	u2.user_name 用户名, " +
			"	u2.phone 手机号, " +
			"	u.register_time 注册时间, " +
			"	case when u.channel_id is null or u.channel_id = '' then u.activity_tag else u.channel_id end 用户来源, " +
			"	case when u3.real_name is null then '否' else '是' end 实名认证, " +
			"	u3.real_name 真实姓名, " +
			"	'否' 是否投资, " +
			"  0  投资金额, " +
			"	0  投资次数, " +
			"	'' 最近一次投资时间, " +
			"	'' 最近一次投资期限, " +
			"	ifnull(c.amount,0)/100 账户余额 " +
			"FROM " +
			"	mdtx_user.user_ext_info u " +
			"LEFT JOIN mdtx_user.user_basic_info u2 ON (u.user_id = u2.id) " +
			"LEFT JOIN mdtx_user.user_auth_info u3 ON (u.user_id = u3.user_id) " +
			"left join mdtx_business.account_tender c on (u.user_id=c.user_id) " +
			"WHERE " +
			"	1 = 1 " +
			"AND u2.is_borrower = 0  " +
			"and u2.phone is not null and u2.phone <> '' " +
			"and ifnull(c.amount,0)/100 <= 100 ";

	
	/**
	 * 每小时推送数据
	 * @return
	 */
	private JSONArray getOneHourHasConfirmData() {
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		
		String update_channel_type = "update phone_sale_hour_not_send s set s.channel_type = (select t.channel_type from dim_channel_type t where t.channel_label=s.channel_label)";
		List<Map<String, Object>> dataList = null;
		JSONArray dataArray = new JSONArray();
		try {
			//更新所有渠道收费信息
			jdbcHelper.execute(update_channel_type);
			//查询所有电销数据,未投资的
			List<Map<String, Object>> bushuList = new JdbcHelper(dataSource).query(query_sql);
			if(bushuList.size() == 0){
				return null;
			}
			String idCond = " and u.user_id in (";
			for (int i = 0; i < bushuList.size(); i++) {
				Map<String, Object> map = bushuList.get(i);
				if(i == bushuList.size() - 1){
					idCond += map.get("cg_user_id");
				}else{
					idCond += map.get("cg_user_id") + ",";
				}
				
			}
			idCond += ")";
			
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			dataList = util.query(query_one_hour_sql + idCond);
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> entity = dataList.get(i);
				dataArray.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return dataArray;
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
