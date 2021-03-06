
package io.renren.entity.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import io.renren.service.schedule.Constants;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.job.ChannelAllReportJob;
import io.renren.service.schedule.job.ChannelInvestTimesReportJob;
import io.renren.service.schedule.job.ChannelLossReportJob;
import io.renren.service.schedule.job.ChannelRenewReportJob;
import io.renren.service.schedule.job.ChannelStFtReportJob;
import io.renren.service.schedule.job.DepositoryTotalJob;
import io.renren.service.schedule.job.MarketChannelReportJob;
import io.renren.service.schedule.job.PerformanceHisJob;
import io.renren.service.schedule.job.PerformanceParameterJob;
import io.renren.service.schedule.job.ProjectParameterJob;
import io.renren.service.schedule.job.ProjectSumJob;
import io.renren.service.schedule.job.UserActiveReportJob;
import io.renren.service.schedule.job.UserBehaviorReportJob;
import io.renren.service.schedule.job.UserInvestReportJob;
import io.renren.service.schedule.job.yunying.ChannelCostDataReportJob;
import io.renren.service.schedule.job.yunying.DailyReportDataJob;
import io.renren.service.schedule.job.yunying.DdzUserDataReportJob;
import io.renren.service.schedule.job.yunying.EveryDayAccTransferReportJob;
import io.renren.service.schedule.job.yunying.EveryDayAwaitDataReportJob;
import io.renren.service.schedule.job.yunying.EveryDayBasicDataReportJob;
import io.renren.service.schedule.job.yunying.EveryDayGetCashReportJob;
import io.renren.service.schedule.job.yunying.EveryDayRecoverDataReportJob;
import io.renren.service.schedule.job.yunying.LicaiPlanReportJob;
import io.renren.service.schedule.job.yunying.MonthlyReportJob;
import io.renren.service.schedule.job.yunying.OldDataJob;
import io.renren.service.schedule.job.yunying.VipPotData;
import io.renren.service.schedule.job.yunying.VipUserDataReportJob;
import io.renren.service.schedule.job.yunying.basicreport.FirstInvestThreeDayNotInvestReportJob;
import io.renren.service.schedule.job.yunying.basicreport.PhoneSaleCpsChannelSendJob;
import io.renren.service.schedule.job.yunying.basicreport.PhoneSaleInvitedChannelSendJob;
import io.renren.service.schedule.job.yunying.basicreport.PhoneSalePayChannelSendJob;
import io.renren.service.schedule.job.yunying.basicreport.RegisterOneHourNotInvestReportJob;
import io.renren.service.schedule.job.yunying.basicreport.RegisterThreeDayNotInvestReportJob;

/**
 * 报表推送任务配置表
 * 
 */
public class ScheduleReportTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Integer id;
	// 任务名称
	private String taskName;
	// 任务类型
	private String taskType;// 任务类型：1:channel_stft,2:channel_loss,3:channel_invest_times,
							// 4:channel_renew,5:user_active_info,6:user_invest_info
	// 任务内容
	private String taskConetent;
	// 上次推送时间
	private Date lastSendTime;
	// 发送方式
	private String sendType;
	// 调度频率
	private String sendRate;
	// 收件人,多个用英文逗号隔开
	private String receiveEmail;
	// 抄送人,多个用英文逗号隔开
	private String chaosongEmail;
	// 条件
	private String condition;
	// 耗时
	private Integer timeCost;
	// 备注
	private String description;
	// 下次执行时间
	private String nextRunTime;

	// 是否已经启动
	private String isRunning;

	public ScheduleReportTaskEntity() {

	}

	public JobVo toJobVo() {
		JobVo jobVo = new JobVo();
		jobVo.setJobId(this.id + "");
		jobVo.setDesc(this.description);
		jobVo.setJobName(this.taskName);
		jobVo.setJobStatus(this.isRunning);
		jobVo.setJobType(this.taskType);
		jobVo.setCronExpression(this.sendRate);
		if (Constants.TaskType.CHANNEL_INVEST_TIMES.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelInvestTimesReportJob.class);
		} else if (Constants.TaskType.CHANNEL_LOSS.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelLossReportJob.class);
		} else if (Constants.TaskType.CHANNEL_RENEW.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelRenewReportJob.class);
		} else if (Constants.TaskType.CHANNEL_STFT.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelStFtReportJob.class);
		} else if (Constants.TaskType.USER_ACTIVE_INFO.equals(jobVo.getJobType())) {
			jobVo.setJobClass(UserActiveReportJob.class);
		} else if (Constants.TaskType.USER_INVEST_INFO.equals(jobVo.getJobType())) {
			jobVo.setJobClass(UserInvestReportJob.class);
		}else if (Constants.TaskType.MARKET_CHANNEL.equals(jobVo.getJobType())) {
			jobVo.setJobClass(MarketChannelReportJob.class);
		}else if (Constants.TaskType.CHANNEL_ALL.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelAllReportJob.class);
		}else if (Constants.TaskType.USER_BEHAVIOR.equals(jobVo.getJobType())) {
			jobVo.setJobClass(UserBehaviorReportJob.class);
		}else if (Constants.TaskType.LICAI_PLAN.equals(jobVo.getJobType())) {
			jobVo.setJobClass(LicaiPlanReportJob.class);
		}else if (Constants.TaskType.PROJECT_PARAMETER.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ProjectParameterJob.class);
		}else if (Constants.TaskType.PROJECT_PARAMETER_SUM.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ProjectSumJob.class);
		}else if (Constants.TaskType.DEPOSITORY_TOTAL.equals(jobVo.getJobType())) {
			jobVo.setJobClass(DepositoryTotalJob.class);
		}else if (Constants.TaskType.PERFORMANCE_HIS.equals(jobVo.getJobType())) {
			jobVo.setJobClass(PerformanceHisJob.class);
		}else if (Constants.TaskType.PERFORMANCE_PARAMETER.equals(jobVo.getJobType())) {
			jobVo.setJobClass(PerformanceParameterJob.class);
		}else if (Constants.TaskType.EVERY_DAY_BASIC_DATA_PLAN.equals(jobVo.getJobType())) {
			jobVo.setJobClass(EveryDayBasicDataReportJob.class);
		}else if (Constants.TaskType.EVERY_DAY_ACC_TRANSFER.equals(jobVo.getJobType())) {
			jobVo.setJobClass(EveryDayAccTransferReportJob.class);
		}else if (Constants.TaskType.EVERY_DAY_AWAIT_DATA.equals(jobVo.getJobType())) {
			jobVo.setJobClass(EveryDayAwaitDataReportJob.class);
		}else if (Constants.TaskType.EVERY_DAY_GET_CASH.equals(jobVo.getJobType())) {
			jobVo.setJobClass(EveryDayGetCashReportJob.class);
		}else if (Constants.TaskType.EVERY_DAY_RECOVER_DATA.equals(jobVo.getJobType())) {
			jobVo.setJobClass(EveryDayRecoverDataReportJob.class);
		}else if (Constants.TaskType.DDZ_USER.equals(jobVo.getJobType())) {
			jobVo.setJobClass(DdzUserDataReportJob.class);
		}else if (Constants.TaskType.CHANNEL_COST.equals(jobVo.getJobType())) {
			jobVo.setJobClass(ChannelCostDataReportJob.class);
		}else if (Constants.TaskType.VIP_USER.equals(jobVo.getJobType())) {
			jobVo.setJobClass(VipUserDataReportJob.class);
		}else if (Constants.TaskType.REGISTER_ONE_HOUR_NOT_INVEST.equals(jobVo.getJobType())) {
			jobVo.setJobClass(RegisterOneHourNotInvestReportJob.class);
		}else if (Constants.TaskType.REGISTER_THREE_DAY_NOT_INVEST.equals(jobVo.getJobType())) {
			jobVo.setJobClass(RegisterThreeDayNotInvestReportJob.class);
		}else if (Constants.TaskType.FIRST_INVEST_THREE_DAY_NOT_INVEST.equals(jobVo.getJobType())) {
			jobVo.setJobClass(FirstInvestThreeDayNotInvestReportJob.class);
		}else if (Constants.TaskType.OLD_DATA.equals(jobVo.getJobType())) {
			jobVo.setJobClass(OldDataJob.class);
		}else if (Constants.TaskType.MONTHLY_REPORT.equals(jobVo.getJobType())) { 
			jobVo.setJobClass(MonthlyReportJob.class);
		}else if (Constants.TaskType.DAILY_REPORT_DATA.equals(jobVo.getJobType())) { 
			jobVo.setJobClass(DailyReportDataJob.class);
		}else if (Constants.TaskType.VIP_POT_DATA.equals(jobVo.getJobType())) { 
			jobVo.setJobClass(VipPotData.class);
		}else if (Constants.TaskType.PHONE_SALE_CPS_SEND.equals(jobVo.getJobType())){
			jobVo.setJobClass(PhoneSaleCpsChannelSendJob.class);
		}else if (Constants.TaskType.PHONE_SALE_PAY_SEND.equals(jobVo.getJobType())){
			jobVo.setJobClass(PhoneSalePayChannelSendJob.class);
		}else if (Constants.TaskType.PHONE_SALE_INVITED_SEND.equals(jobVo.getJobType())){
			jobVo.setJobClass(PhoneSaleInvitedChannelSendJob.class);
		}
		
		
		
		jobVo.setTaskEntity(this);
		return jobVo;
	}

	public void setNextRunTime(String nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

	public String getNextRunTime() {
		return nextRunTime;
	}

	public void setChaosongEmail(String chaosongEmail) {
		this.chaosongEmail = chaosongEmail;
	}

	public String getChaosongEmail() {
		return chaosongEmail;
	}

	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}

	public String getReceiveEmail() {
		return receiveEmail;
	}

	public void setIsRunning(String isRunning) {
		this.isRunning = isRunning;
	}

	public String getIsRunning() {
		return isRunning;
	}

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：任务名称
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 获取：任务名称
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * 设置：任务内容
	 */
	public void setTaskConetent(String taskConetent) {
		this.taskConetent = taskConetent;
	}

	/**
	 * 获取：任务内容
	 */
	public String getTaskConetent() {
		return taskConetent;
	}

	/**
	 * 设置：上次推送时间
	 */
	public void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	/**
	 * 获取：上次推送时间
	 */
	public Date getLastSendTime() {
		return lastSendTime;
	}

	/**
	 * 设置：发送方式
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * 获取：发送方式
	 */
	public String getSendType() {
		return sendType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskType() {
		return taskType;
	}

	/**
	 * 设置：调度频率
	 */
	public void setSendRate(String sendRate) {
		this.sendRate = sendRate;
	}

	/**
	 * 获取：调度频率
	 */
	public String getSendRate() {
		return sendRate;
	}

	/**
	 * 设置：条件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * 获取：条件
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * 设置：耗时
	 */
	public void setTimeCost(Integer timeCost) {
		this.timeCost = timeCost;
	}

	/**
	 * 获取：耗时
	 */
	public Integer getTimeCost() {
		return timeCost;
	}

	/**
	 * 设置：备注
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取：备注
	 */
	public String getDescription() {
		return description;
	}
	

	public List<String> getReceiveEmailList() {
		List<String> arrayList = new ArrayList<String>();
		if (receiveEmail != null && receiveEmail.contains(",")) {
			String[] split = receiveEmail.split(",");
			for (int i = 0; i < split.length; i++) {
				arrayList.add(split[i]);
			}
		} else {
			if (!StringUtils.isEmpty(receiveEmail)) {
				arrayList.add(receiveEmail);
			}
		}
		return arrayList;
	}

	public List<String> getChaosongEmailList() {
		List<String> arrayList = new ArrayList<String>();
		if (chaosongEmail != null && chaosongEmail.contains(",")) {
			String[] split = chaosongEmail.split(",");
			for (int i = 0; i < split.length; i++) {
				arrayList.add(split[i]);
			}
		} else {
			if (!StringUtils.isEmpty(chaosongEmail)) {
				arrayList.add(chaosongEmail);
			}
		}
		return arrayList;
	}

	@Override
	public String toString() {
		return "ScheduleReportTaskEntity [id=" + id + ", taskName=" + taskName + ", taskType=" + taskType
				+ ", taskConetent=" + taskConetent + ", lastSendTime=" + lastSendTime + ", sendType=" + sendType
				+ ", sendRate=" + sendRate + ", receiveEmail=" + receiveEmail + ", chaosongEmail=" + chaosongEmail
				+ ", condition=" + condition + ", timeCost=" + timeCost + ", description=" + description
				+ ", isRunning=" + isRunning + "]";
	}

}
