package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 绩效台帐-分配表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:58
 */
public class PerformanceParameterEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计日期
	private String statPeriod;
	//总监
	private String developmanagername;
	//部门
	private String department;
	//放款金额
	private BigDecimal payformoneyout;
	//毛利
	private BigDecimal grossProfit;
	//工资成本
	private BigDecimal salaryCost;
	//报销开支
	private BigDecimal reimbursement;
	//租金分摊
	private BigDecimal rentShare;
	//净利
	private BigDecimal netMargin;
	//提成系数
	private BigDecimal commissionRatio;
	//可发绩效
	private BigDecimal availablePerformance;
	//风险准备金
	private BigDecimal riskReserve;
	//结清金额
	private BigDecimal settledAmount;
	//结清金额占比
	private BigDecimal settledAmtRate;
	//本月应发绩效
	private BigDecimal expectedPerformance;

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
	 * 设置：总监
	 */
	public void setDevelopmanagername(String developmanagername) {
		this.developmanagername = developmanagername;
	}
	/**
	 * 获取：总监
	 */
	public String getDevelopmanagername() {
		return developmanagername;
	}
	/**
	 * 设置：部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：部门
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：放款金额
	 */
	public void setPayformoneyout(BigDecimal payformoneyout) {
		this.payformoneyout = payformoneyout;
	}
	/**
	 * 获取：放款金额
	 */
	public BigDecimal getPayformoneyout() {
		return payformoneyout;
	}
	/**
	 * 设置：毛利
	 */
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}
	/**
	 * 获取：毛利
	 */
	public BigDecimal getGrossProfit() {
		return grossProfit;
	}
	/**
	 * 设置：工资成本
	 */
	public void setSalaryCost(BigDecimal salaryCost) {
		this.salaryCost = salaryCost;
	}
	/**
	 * 获取：工资成本
	 */
	public BigDecimal getSalaryCost() {
		return salaryCost;
	}
	/**
	 * 设置：报销开支
	 */
	public void setReimbursement(BigDecimal reimbursement) {
		this.reimbursement = reimbursement;
	}
	/**
	 * 获取：报销开支
	 */
	public BigDecimal getReimbursement() {
		return reimbursement;
	}
	/**
	 * 设置：租金分摊
	 */
	public void setRentShare(BigDecimal rentShare) {
		this.rentShare = rentShare;
	}
	/**
	 * 获取：租金分摊
	 */
	public BigDecimal getRentShare() {
		return rentShare;
	}
	/**
	 * 设置：净利
	 */
	public void setNetMargin(BigDecimal netMargin) {
		this.netMargin = netMargin;
	}
	/**
	 * 获取：净利
	 */
	public BigDecimal getNetMargin() {
		return netMargin;
	}
	/**
	 * 设置：提成系数
	 */
	public void setCommissionRatio(BigDecimal commissionRatio) {
		this.commissionRatio = commissionRatio;
	}
	/**
	 * 获取：提成系数
	 */
	public BigDecimal getCommissionRatio() {
		return commissionRatio;
	}
	/**
	 * 设置：可发绩效
	 */
	public void setAvailablePerformance(BigDecimal availablePerformance) {
		this.availablePerformance = availablePerformance;
	}
	/**
	 * 获取：可发绩效
	 */
	public BigDecimal getAvailablePerformance() {
		return availablePerformance;
	}
	/**
	 * 设置：风险准备金
	 */
	public void setRiskReserve(BigDecimal riskReserve) {
		this.riskReserve = riskReserve;
	}
	/**
	 * 获取：风险准备金
	 */
	public BigDecimal getRiskReserve() {
		return riskReserve;
	}
	/**
	 * 设置：结清金额
	 */
	public void setSettledAmount(BigDecimal settledAmount) {
		this.settledAmount = settledAmount;
	}
	/**
	 * 获取：结清金额
	 */
	public BigDecimal getSettledAmount() {
		return settledAmount;
	}
	/**
	 * 设置：结清金额占比
	 */
	public void setSettledAmtRate(BigDecimal settledAmtRate) {
		this.settledAmtRate = settledAmtRate;
	}
	/**
	 * 获取：结清金额占比
	 */
	public BigDecimal getSettledAmtRate() {
		return settledAmtRate;
	}
	/**
	 * 设置：本月应发绩效
	 */
	public void setExpectedPerformance(BigDecimal expectedPerformance) {
		this.expectedPerformance = expectedPerformance;
	}
	/**
	 * 获取：本月应发绩效
	 */
	public BigDecimal getExpectedPerformance() {
		return expectedPerformance;
	}
}
