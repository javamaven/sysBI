package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-14 13:42:18
 */
public class DmReportDdzRemainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private String statPeriod;
	//
	private String username;
	//
	private BigDecimal phone;
	//
	private String regTime;
	//
	private BigDecimal availableAmount;
	//
	private BigDecimal cou;
	//
	private BigDecimal xmInvMoney;
	//
	private String isInternal;
	//
	private String isInternalTuijian;
	//
	private String realname;

	/**
	 * 设置：
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}

	/**
	 * 获取：
	 */
	public String getStatPeriod() {
		return statPeriod;
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
	public void setPhone(BigDecimal phone) {
		this.phone = phone;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getPhone() {
		return phone;
	}

	/**
	 * 设置：
	 */
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	/**
	 * 获取：
	 */
	public String getRegTime() {
		return regTime;
	}

	/**
	 * 设置：
	 */
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	/**
	 * 设置：
	 */
	public void setCou(BigDecimal cou) {
		this.cou = cou;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getCou() {
		return cou;
	}

	/**
	 * 设置：
	 */
	public void setXmInvMoney(BigDecimal xmInvMoney) {
		this.xmInvMoney = xmInvMoney;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getXmInvMoney() {
		return xmInvMoney;
	}

	/**
	 * 设置：
	 */
	public void setIsInternal(String isInternal) {
		this.isInternal = isInternal;
	}

	/**
	 * 获取：
	 */
	public String getIsInternal() {
		return isInternal;
	}

	/**
	 * 设置：
	 */
	public void setIsInternalTuijian(String isInternalTuijian) {
		this.isInternalTuijian = isInternalTuijian;
	}

	/**
	 * 获取：
	 */
	public String getIsInternalTuijian() {
		return isInternalTuijian;
	}

	/**
	 * 设置：
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * 获取：
	 */
	public String getRealname() {
		return realname;
	}
}
