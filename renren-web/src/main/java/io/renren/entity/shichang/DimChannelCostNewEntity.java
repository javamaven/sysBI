package io.renren.entity.shichang;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-08 10:16:36
 */
public class DimChannelCostNewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private String statPeriod;
	//
	//
	private BigDecimal cost;
	//
	private BigDecimal recharge;

	private String channelName;
	private String channelLabel;
	private BigDecimal balance;
	private String lastRechargeTime;
	private String lastRegTime;

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setLastRechargeTime(String lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}

	public void setLastRegTime(String lastRegTime) {
		this.lastRegTime = lastRegTime;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getLastRechargeTime() {
		return lastRechargeTime;
	}

	public String getLastRegTime() {
		return lastRegTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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
	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	/**
	 * 获取：
	 */
	public String getChannelLabel() {
		return channelLabel;
	}

	/**
	 * 设置：
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * 设置：
	 */
	public void setRecharge(BigDecimal recharge) {
		this.recharge = recharge;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getRecharge() {
		return recharge;
	}
}
