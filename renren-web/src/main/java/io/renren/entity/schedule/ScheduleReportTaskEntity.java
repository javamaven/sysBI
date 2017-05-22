package io.renren.entity.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.renren.service.schedule.Constants;
import io.renren.service.schedule.entity.JobVo;
import io.renren.service.schedule.job.ChannelInvestTimesReportJob;
import io.renren.service.schedule.job.ChannelLossReportJob;
import io.renren.service.schedule.job.ChannelRenewReportJob;
import io.renren.service.schedule.job.ChannelStFtReportJob;
import io.renren.service.schedule.job.UserActiveReportJob;
import io.renren.service.schedule.job.UserInvestReportJob;

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
		if (receiveEmail != null && receiveEmail.contains(",")) {
			String[] split = receiveEmail.split(",");
			return Arrays.asList(split);
		}
		List<String> arrayList = new ArrayList<String>();
		arrayList.add(receiveEmail);
		return arrayList;
	}

	public List<String> getChaosongEmailList() {
		if (chaosongEmail != null && chaosongEmail.contains(",")) {
			String[] split = chaosongEmail.split(",");
			return Arrays.asList(split);
		}
		List<String> arrayList = new ArrayList<String>();
		arrayList.add(chaosongEmail);
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
