package io.renren.entity;


import java.io.Serializable;

/**
 * 菜单管理
 * 
 * @author lujianfeng
 * @email lujianfeng@mindai.com
 * @date 2017年3月29日17:41:37
 */
public class ChannelChannelAllEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 投资次数
	 */
	private int investTimes;

	/**
	 * 留存率
	 */
	private String stayPer;

	/**
	 * 投资用户数
	 */
	private int investUsers;

	/**
	 * 次均红包金额
	 */
	private String moneyVoucherPer;

	/**
	 * 次均投资金额
	 */
	private String moneyInvestPer;

	/**
	 * 次均年化金额
	 */
	private String moneyInvsetYearPer;

	/**
	 * 平均投资期限
	 */
	private String borrowPeriodPer;

	/**
	 * 平均投资间隔
	 */
	private String intervalsPer;
	
	
	//渠道负责人
	
	private String channelHead;
	
	

	public String getChannelHead() {
		return channelHead;
	}

	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getInvestTimes() {
		return investTimes;
	}

	public void setInvestTimes(int investTimes) {
		this.investTimes = investTimes;
	}

	public String getStayPer() {
		return stayPer;
	}

	public void setStayPer(String stayPer) {
		this.stayPer = stayPer;
	}

	public int getInvestUsers() {
		return investUsers;
	}

	public void setInvestUsers(int investUsers) {
		this.investUsers = investUsers;
	}

	public String getMoneyVoucherPer() {
		return moneyVoucherPer;
	}

	public void setMoneyVoucherPer(String moneyVoucherPer) {
		this.moneyVoucherPer = moneyVoucherPer;
	}

	public String getMoneyInvestPer() {
		return moneyInvestPer;
	}

	public void setMoneyInvestPer(String moneyInvestPer) {
		this.moneyInvestPer = moneyInvestPer;
	}

	public String getMoneyInvsetYearPer() {
		return moneyInvsetYearPer;
	}

	public void setMoneyInvsetYearPer(String moneyInvsetYearPer) {
		this.moneyInvsetYearPer = moneyInvsetYearPer;
	}

	public String getBorrowPeriodPer() {
		return borrowPeriodPer;
	}

	public void setBorrowPeriodPer(String borrowPeriodPer) {
		this.borrowPeriodPer = borrowPeriodPer;
	}

	public String getIntervalsPer() {
		return intervalsPer;
	}

	public void setIntervalsPer(String intervalsPer) {
		this.intervalsPer = intervalsPer;
	}
}
