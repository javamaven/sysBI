package io.renren.entity.channelmanager;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户激活情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-04 17:57:21
 */
public class DmReportUserActivateDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Integer statPeriod;
	//
	private Integer userId;
	//
	private String username;
	//
	private String channelId;
	//
	private String channelName;
	//
	private String channelMark;
	//
	private String registerTime;
	//
	private String isRealname;
	//
	private String isBinding;
	//
	private Integer activateInvestTime;
	//
	private String firstInvestTime;
	//
	private BigDecimal firstInvestBalance;
	//
	private Integer firstInvestPeriod;
	//
	private BigDecimal afterInvestBalance;
	//
	private Integer afterInvestNumber;
	//
	private BigDecimal totalInvestBalance;
	//
	private Integer totalInvestNumber;
	//
	private BigDecimal changeInvestBalance;
	//
	private BigDecimal totalCapital;

	private String registerFrom;

	public void setRegisterFrom(String registerFrom) {
		this.registerFrom = registerFrom;
	}

	public String getRegisterFrom() {
		return registerFrom;
	}

	/**
	 * 设置：
	 */
	public void setStatPeriod(Integer statPeriod) {
		this.statPeriod = statPeriod;
	}

	/**
	 * 获取：
	 */
	public Integer getStatPeriod() {
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
	public void setChannelMark(String channelMark) {
		this.channelMark = channelMark;
	}

	/**
	 * 获取：
	 */
	public String getChannelMark() {
		return channelMark;
	}

	/**
	 * 设置：
	 */
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	/**
	 * 获取：
	 */
	public String getRegisterTime() {
		return registerTime;
	}

	/**
	 * 设置：
	 */
	public void setIsRealname(String isRealname) {
		this.isRealname = isRealname;
	}

	/**
	 * 获取：
	 */
	public String getIsRealname() {
		return isRealname;
	}

	/**
	 * 设置：
	 */
	public void setIsBinding(String isBinding) {
		this.isBinding = isBinding;
	}

	/**
	 * 获取：
	 */
	public String getIsBinding() {
		return isBinding;
	}

	/**
	 * 设置：
	 */
	public void setActivateInvestTime(Integer activateInvestTime) {
		this.activateInvestTime = activateInvestTime;
	}

	/**
	 * 获取：
	 */
	public Integer getActivateInvestTime() {
		return activateInvestTime;
	}

	/**
	 * 设置：
	 */
	public void setFirstInvestTime(String firstInvestTime) {
		this.firstInvestTime = firstInvestTime;
	}

	/**
	 * 获取：
	 */
	public String getFirstInvestTime() {
		return firstInvestTime;
	}

	/**
	 * 设置：
	 */
	public void setFirstInvestBalance(BigDecimal firstInvestBalance) {
		this.firstInvestBalance = firstInvestBalance;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getFirstInvestBalance() {
		return firstInvestBalance;
	}

	/**
	 * 设置：
	 */
	public void setFirstInvestPeriod(Integer firstInvestPeriod) {
		this.firstInvestPeriod = firstInvestPeriod;
	}

	/**
	 * 获取：
	 */
	public Integer getFirstInvestPeriod() {
		return firstInvestPeriod;
	}

	/**
	 * 设置：
	 */
	public void setAfterInvestBalance(BigDecimal afterInvestBalance) {
		this.afterInvestBalance = afterInvestBalance;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getAfterInvestBalance() {
		return afterInvestBalance;
	}

	/**
	 * 设置：
	 */
	public void setAfterInvestNumber(Integer afterInvestNumber) {
		this.afterInvestNumber = afterInvestNumber;
	}

	/**
	 * 获取：
	 */
	public Integer getAfterInvestNumber() {
		return afterInvestNumber;
	}

	/**
	 * 设置：
	 */
	public void setTotalInvestBalance(BigDecimal totalInvestBalance) {
		this.totalInvestBalance = totalInvestBalance;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getTotalInvestBalance() {
		return totalInvestBalance;
	}

	/**
	 * 设置：
	 */
	public void setTotalInvestNumber(Integer totalInvestNumber) {
		this.totalInvestNumber = totalInvestNumber;
	}

	/**
	 * 获取：
	 */
	public Integer getTotalInvestNumber() {
		return totalInvestNumber;
	}

	/**
	 * 设置：
	 */
	public void setChangeInvestBalance(BigDecimal changeInvestBalance) {
		this.changeInvestBalance = changeInvestBalance;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getChangeInvestBalance() {
		return changeInvestBalance;
	}

	/**
	 * 设置：
	 */
	public void setTotalCapital(BigDecimal totalCapital) {
		this.totalCapital = totalCapital;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getTotalCapital() {
		return totalCapital;
	}
}
