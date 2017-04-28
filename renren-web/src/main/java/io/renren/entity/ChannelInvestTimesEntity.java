package io.renren.entity;

import java.io.Serializable;

/**
 * 渠道投资次数分析
 * 
 * @author liaodehui
 *
 */
public class ChannelInvestTimesEntity implements Serializable {

	private static final long serialVersionUID = 7596917974990705382L;

	private double channelId;
	private String channelName;
	private String channelLabel;
	private int registerUserNum;// 注册人数
	private int firstInvestUserNum;// 首投人数
	private double firstInvestAmount;// 首投金额
	private int investTimes;// 投资次数
	private int investUserNum;// 投资人数
	private double investAmount;// 累积投资金额
	private double investYearAmount;// 累计投资年化金额
	private double firstInvestRedMoney;// 首投使用红包金额
	private double perFirstInvestRedMoney;// 人均首投使用红包金额
	private double allRedMoney;// 累计使用红包金额
	private double perAllRedMoney;// 人均累计使用红包金额
	private int ddzInvestDays;// 点点赚投资天数
	private double ddzPerInvestAmount;// 点点赚平均投资金额

	public ChannelInvestTimesEntity() {

	}

	public ChannelInvestTimesEntity(double channelId, String channelName, String channelLabel, int registerUser,
			int firstInvestUser, double firstInvestAmount, int investTimes, int investUserNum, double investAmount,
			double investYearAmount, double firstInvestRedMoney, double perFirstInvestRedMoney, double allRedMoney,
			double perAllRedMoney, int ddzInvestDays, double ddzPerInvestAmount) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.channelLabel = channelLabel;
		this.registerUserNum = registerUser;
		this.firstInvestUserNum = firstInvestUser;
		this.firstInvestAmount = firstInvestAmount;
		this.investTimes = investTimes;
		this.investUserNum = investUserNum;
		this.investAmount = investAmount;
		this.investYearAmount = investYearAmount;
		this.firstInvestRedMoney = firstInvestRedMoney;
		this.perFirstInvestRedMoney = perFirstInvestRedMoney;
		this.allRedMoney = allRedMoney;
		this.perAllRedMoney = perAllRedMoney;
		this.ddzInvestDays = ddzInvestDays;
		this.ddzPerInvestAmount = ddzPerInvestAmount;
	}

	public double getChannelId() {
		return channelId;
	}

	public void setChannelId(double channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelLabel() {
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	public int getRegisterUserNum() {
		return registerUserNum;
	}

	public void setRegisterUserNum(int registerUser) {
		this.registerUserNum = registerUser;
	}

	public int getFirstInvestUserNum() {
		return firstInvestUserNum;
	}

	public void setFirstInvestUserNum(int firstInvestUser) {
		this.firstInvestUserNum = firstInvestUser;
	}

	public double getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setFirstInvestAmount(double firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public int getInvestTimes() {
		return investTimes;
	}

	public void setInvestTimes(int investTimes) {
		this.investTimes = investTimes;
	}

	public int getInvestUserNum() {
		return investUserNum;
	}

	public void setInvestUserNum(int investUserNum) {
		this.investUserNum = investUserNum;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public double getInvestYearAmount() {
		return investYearAmount;
	}

	public void setInvestYearAmount(double investYearAmount) {
		this.investYearAmount = investYearAmount;
	}

	public double getFirstInvestRedMoney() {
		return firstInvestRedMoney;
	}

	public void setFirstInvestRedMoney(double firstInvestRedMoney) {
		this.firstInvestRedMoney = firstInvestRedMoney;
	}

	public double getPerFirstInvestRedMoney() {
		return perFirstInvestRedMoney;
	}

	public void setPerFirstInvestRedMoney(double perFirstInvestRedMoney) {
		this.perFirstInvestRedMoney = perFirstInvestRedMoney;
	}

	public double getAllRedMoney() {
		return allRedMoney;
	}

	public void setAllRedMoney(double allRedMoney) {
		this.allRedMoney = allRedMoney;
	}

	public double getPerAllRedMoney() {
		return perAllRedMoney;
	}

	public void setPerAllRedMoney(double perAllRedMoney) {
		this.perAllRedMoney = perAllRedMoney;
	}

	public int getDdzInvestDays() {
		return ddzInvestDays;
	}

	public void setDdzInvestDays(int ddzInvestDays) {
		this.ddzInvestDays = ddzInvestDays;
	}

	public double getDdzPerInvestAmount() {
		return ddzPerInvestAmount;
	}

	public void setDdzPerInvestAmount(double ddzPerInvestAmount) {
		this.ddzPerInvestAmount = ddzPerInvestAmount;
	}

	@Override
	public String toString() {
		return "ChannelInvestTimesEntity [channelId=" + channelId + ", channelName=" + channelName + ", channelLabel="
				+ channelLabel + ", registerUserNum=" + registerUserNum + ", firstInvestUserNum=" + firstInvestUserNum
				+ ", firstInvestAmount=" + firstInvestAmount + ", investTimes=" + investTimes + ", investUserNum="
				+ investUserNum + ", investAmount=" + investAmount + ", investYearAmount=" + investYearAmount
				+ ", firstInvestRedMoney=" + firstInvestRedMoney + ", perFirstInvestRedMoney=" + perFirstInvestRedMoney
				+ ", allRedMoney=" + allRedMoney + ", perAllRedMoney=" + perAllRedMoney + ", ddzInvestDays="
				+ ddzInvestDays + ", ddzPerInvestAmount=" + ddzPerInvestAmount + "]";
	}

}
