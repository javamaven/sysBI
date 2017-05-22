package io.renren.service.schedule.entity;

import java.io.Serializable;

import io.renren.entity.schedule.ScheduleReportTaskEntity;

/**
 * 
 * @Description: 计划任务信息
 * @author liaodehui
 */
public class JobVo implements Serializable {
	private static final long serialVersionUID = -967581057015289440L;
	public static String JOB_GROUP_NAME = "report-group";

	/** 任务id **/
	private String jobId;
	/** 任务名称 **/
	private String jobName;
	/** 任务类型 **/
	private String jobType;
	/** 任务分组 **/
	private String jobGroup = JOB_GROUP_NAME;
	/** 任务状态 1启用 2禁用 **/
	private String jobStatus;
	/** 任务运行时间表达式 **/
	private String cronExpression;
	private ScheduleReportTaskEntity taskEntity;

	private Class<?> jobClass;

	/** 任务描述 **/
	private String desc;

	public JobVo() {
	}

	public void setJobClass(Class<?> jobClass) {
		this.jobClass = jobClass;
	}

	public Class<?> getJobClass() {
		return jobClass;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public ScheduleReportTaskEntity getTaskEntity() {
		return taskEntity;
	}

	public void setTaskEntity(ScheduleReportTaskEntity taskEntity) {
		this.taskEntity = taskEntity;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "JobVo [jobId=" + jobId + ", jobName=" + jobName + ", jobType=" + jobType + ", jobGroup=" + jobGroup
				+ ", jobStatus=" + jobStatus + ", cronExpression=" + cronExpression + ", taskEntity=" + taskEntity
				+ ", desc=" + desc + "]";
	}

}