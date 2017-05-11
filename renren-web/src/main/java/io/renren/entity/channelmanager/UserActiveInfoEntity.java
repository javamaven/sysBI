package io.renren.entity.channelmanager;

import java.io.Serializable;

public class UserActiveInfoEntity implements Serializable {

	private static final long serialVersionUID = 8041617253408925756L;
	private double registerUserNum;
	private double investUserNum;
	private double firstInvestRate;
	private double totalInvestAmount;
	private double firstInvestAmount;
	private double changeInvestAmount;
	private double multiInvestUserNum;
	private double multiInvestAmount;
	private double multiInvestRate;

	public UserActiveInfoEntity() {

	}

	public double getRegisterUserNum() {
		return registerUserNum;
	}

	public void setRegisterUserNum(double registerUserNum) {
		this.registerUserNum = registerUserNum;
	}

	public double getInvestUserNum() {
		return investUserNum;
	}

	public void setInvestUserNum(double investUserNum) {
		this.investUserNum = investUserNum;
	}

	public double getFirstInvestRate() {
		return firstInvestRate;
	}

	public void setFirstInvestRate(double firstInvestRate) {
		this.firstInvestRate = firstInvestRate;
	}

	public double getTotalInvestAmount() {
		return totalInvestAmount;
	}

	public void setTotalInvestAmount(double totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}

	public double getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setFirstInvestAmount(double firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public double getChangeInvestAmount() {
		return changeInvestAmount;
	}

	public void setChangeInvestAmount(double changeInvestAmount) {
		this.changeInvestAmount = changeInvestAmount;
	}

	public double getMultiInvestUserNum() {
		return multiInvestUserNum;
	}

	public void setMultiInvestUserNum(double multiInvestUserNum) {
		this.multiInvestUserNum = multiInvestUserNum;
	}

	public double getMultiInvestAmount() {
		return multiInvestAmount;
	}

	public void setMultiInvestAmount(double multiInvestAmount) {
		this.multiInvestAmount = multiInvestAmount;
	}

	public double getMultiInvestRate() {
		return multiInvestRate;
	}

	public void setMultiInvestRate(double multiInvestRate) {
		this.multiInvestRate = multiInvestRate;
	}

	@Override
	public String toString() {
		return "UserActiveInfoEntity [registerUserNum=" + registerUserNum + ", investUserNum=" + investUserNum
				+ ", firstInvestRate=" + firstInvestRate + ", totalInvestAmount=" + totalInvestAmount
				+ ", firstInvestAmount=" + firstInvestAmount + ", changeInvestAmount=" + changeInvestAmount
				+ ", multiInvestUserNum=" + multiInvestUserNum + ", multiInvestAmount=" + multiInvestAmount
				+ ", multiInvestRate=" + multiInvestRate + "]";
	}

}
