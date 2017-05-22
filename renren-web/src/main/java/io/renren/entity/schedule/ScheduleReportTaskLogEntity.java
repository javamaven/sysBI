package io.renren.entity.schedule;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-19 10:19:30
 */
public class ScheduleReportTaskLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//任务ID,schedule_report_task.id
	private Integer taskId;
	//发送耗时
	private Integer timeCost;
	//发送结果
	private String sendResult;
	//该次任务查询参数
	private String params;
	//该次任务收件人
	private String receiveEmal;
	//该次任务抄送人
	private String chaosongEmail;
	//邮件简单内容介绍
	private String emailValue;
	//发送邮件结束，记录日志的时间
	private Date time;
	//备注
	private String desc;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：任务ID,schedule_report_task.id
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：任务ID,schedule_report_task.id
	 */
	public Integer getTaskId() {
		return taskId;
	}
	/**
	 * 设置：发送耗时
	 */
	public void setTimeCost(Integer timeCost) {
		this.timeCost = timeCost;
	}
	/**
	 * 获取：发送耗时
	 */
	public Integer getTimeCost() {
		return timeCost;
	}
	/**
	 * 设置：发送结果
	 */
	public void setSendResult(String sendResult) {
		this.sendResult = sendResult;
	}
	/**
	 * 获取：发送结果
	 */
	public String getSendResult() {
		return sendResult;
	}
	/**
	 * 设置：该次任务查询参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：该次任务查询参数
	 */
	public String getParams() {
		return params;
	}
	/**
	 * 设置：该次任务收件人
	 */
	public void setReceiveEmal(String receiveEmal) {
		this.receiveEmal = receiveEmal;
	}
	/**
	 * 获取：该次任务收件人
	 */
	public String getReceiveEmal() {
		return receiveEmal;
	}
	/**
	 * 设置：该次任务抄送人
	 */
	public void setChaosongEmail(String chaosongEmail) {
		this.chaosongEmail = chaosongEmail;
	}
	/**
	 * 获取：该次任务抄送人
	 */
	public String getChaosongEmail() {
		return chaosongEmail;
	}
	/**
	 * 设置：邮件简单内容介绍
	 */
	public void setEmailValue(String emailValue) {
		this.emailValue = emailValue;
	}
	/**
	 * 获取：邮件简单内容介绍
	 */
	public String getEmailValue() {
		return emailValue;
	}
	/**
	 * 设置：发送邮件结束，记录日志的时间
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * 获取：发送邮件结束，记录日志的时间
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * 设置：备注
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：备注
	 */
	public String getDesc() {
		return desc;
	}
}
