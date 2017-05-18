package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用户投资情况表
 * 
 */
public class DmReportInvestmentDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计日期
	private String statPeriod;
	//
	private Integer userId;
	//
	private String username;
	//
	private String channelId;
	//
	private String channelName;
	//
	private String activityTag;
	//操作平台
	private String tenderFrom;
	//投资时间
	private Date addtime;
	//涉及项目类型
	private String borrowType;
	//投资记录ID
	private String pid;
	//涉及项目名称
	private String projectName;
	//涉及项目本金
	private BigDecimal tenderCapital;
	//涉及项目期限
	private Integer borrowPeriod;
	//目前状态
	private String stage;
	//当前持有总金额
	private BigDecimal recoverAccountWait;
	//
	private BigDecimal cia;

	/**
	 * 设置：统计日期
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：统计日期
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * 获取：
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * 设置：
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	/**
	 * 获取：
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * 设置：
	 */
	public void setActivityTag(String activityTag) {
		this.activityTag = activityTag;
	}
	/**
	 * 获取：
	 */
	public String getActivityTag() {
		return activityTag;
	}
	/**
	 * 设置：操作平台
	 */
	public void setTenderFrom(String tenderFrom) {
		this.tenderFrom = tenderFrom;
	}
	/**
	 * 获取：操作平台
	 */
	public String getTenderFrom() {
		return tenderFrom;
	}
	/**
	 * 设置：投资时间
	 */
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	/**
	 * 获取：投资时间
	 */
	public Date getAddtime() {
		return addtime;
	}
	/**
	 * 设置：涉及项目类型
	 */
	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}
	/**
	 * 获取：涉及项目类型
	 */
	public String getBorrowType() {
		return borrowType;
	}
	/**
	 * 设置：投资记录ID
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**
	 * 获取：投资记录ID
	 */
	public String getPid() {
		return pid;
	}
	/**
	 * 设置：涉及项目名称
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 获取：涉及项目名称
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * 设置：涉及项目本金
	 */
	public void setTenderCapital(BigDecimal tenderCapital) {
		this.tenderCapital = tenderCapital;
	}
	/**
	 * 获取：涉及项目本金
	 */
	public BigDecimal getTenderCapital() {
		return tenderCapital;
	}
	/**
	 * 设置：涉及项目期限
	 */
	public void setBorrowPeriod(Integer borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}
	/**
	 * 获取：涉及项目期限
	 */
	public Integer getBorrowPeriod() {
		return borrowPeriod;
	}
	/**
	 * 设置：目前状态
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	/**
	 * 获取：目前状态
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * 设置：当前持有总金额
	 */
	public void setRecoverAccountWait(BigDecimal recoverAccountWait) {
		this.recoverAccountWait = recoverAccountWait;
	}
	/**
	 * 获取：当前持有总金额
	 */
	public BigDecimal getRecoverAccountWait() {
		return recoverAccountWait;
	}
	/**
	 * 设置：
	 */
	public void setCia(BigDecimal cia) {
		this.cia = cia;
	}
	/**
	 * 获取：
	 */
	public BigDecimal getCia() {
		return cia;
	}
}
