package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 15:24:09
 */
public class DmReportBasicDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String statPeriod;
	//投资用户数
	private Double invCou;
	//投资使用红包金额
	private BigDecimal usePackMoney;
	//人均红包金额
	private BigDecimal avgUsePackMoney;
	//用户投资本金
	private BigDecimal accountMoney;
	//人均投资本金
	private BigDecimal avgMoney;
	//用户投资年化金额
	private BigDecimal yearMoney;
	//人均年化金额
	private BigDecimal avgYearMoney;
	//邀请人返利
	private BigDecimal spreadsMoney;
	//加息成本
	private BigDecimal discountCost;
	//人均加息成本
	private BigDecimal avgDiscountCost;
	//新增待收（预估）
	private BigDecimal forecastAwait;
	//新增代收（实际）
	private BigDecimal fullAwait;
	//新增待收（放款）
	private BigDecimal loanAwait;
	//回款金额
	private BigDecimal recoverMoney;
	//本月年化投资金额
	private BigDecimal monthNh;
	//新增且到12-31后还款的待收（考虑还款方式）（万元）
	private BigDecimal newEndYearAwait;
	//到12-31后还款的待收金额
	private BigDecimal endYearAwait;
	//总待收
	private BigDecimal allAwait;
	//测算累计待收(万元)
	private BigDecimal allWait;
	//理财计划预约金额(万元)
	private BigDecimal matchWait;
	//未匹配本金(万元)
	private BigDecimal matchCapitilWait;
	//未匹配测算利息(万元)
	private BigDecimal matchInterestlWait;
	public DmReportBasicDailyEntity(){
		
	}
	public DmReportBasicDailyEntity(String statPeriod, Double invCou, BigDecimal usePackMoney,
			BigDecimal avgUsePackMoney, BigDecimal accountMoney, BigDecimal avgMoney, BigDecimal yearMoney,
			BigDecimal avgYearMoney, BigDecimal spreadsMoney, BigDecimal discountCost, BigDecimal avgDiscountCost,
			BigDecimal forecastAwait, BigDecimal fullAwait, BigDecimal loanAwait, BigDecimal recoverMoney,
			BigDecimal monthNh, BigDecimal newEndYearAwait, BigDecimal endYearAwait, BigDecimal allAwait,
			BigDecimal allWait, BigDecimal matchWait, BigDecimal matchCapitilWait, BigDecimal matchInterestlWait) {
		super();
		this.statPeriod = statPeriod;
		this.invCou = invCou;
		this.usePackMoney = usePackMoney;
		this.avgUsePackMoney = avgUsePackMoney;
		this.accountMoney = accountMoney;
		this.avgMoney = avgMoney;
		this.yearMoney = yearMoney;
		this.avgYearMoney = avgYearMoney;
		this.spreadsMoney = spreadsMoney;
		this.discountCost = discountCost;
		this.avgDiscountCost = avgDiscountCost;
		this.forecastAwait = forecastAwait;
		this.fullAwait = fullAwait;
		this.loanAwait = loanAwait;
		this.recoverMoney = recoverMoney;
		this.monthNh = monthNh;
		this.newEndYearAwait = newEndYearAwait;
		this.endYearAwait = endYearAwait;
		this.allAwait = allAwait;
		this.allWait = allWait;
		this.matchWait = matchWait;
		this.matchCapitilWait = matchCapitilWait;
		this.matchInterestlWait = matchInterestlWait;
	}
	public BigDecimal getAllWait() {
		return allWait;
	}
	public void setAllWait(BigDecimal allWait) {
		this.allWait = allWait;
	}
	public BigDecimal getMatchWait() {
		return matchWait;
	}
	public void setMatchWait(BigDecimal matchWait) {
		this.matchWait = matchWait;
	}
	public BigDecimal getMatchCapitilWait() {
		return matchCapitilWait;
	}
	public void setMatchCapitilWait(BigDecimal matchCapitilWait) {
		this.matchCapitilWait = matchCapitilWait;
	}
	public BigDecimal getMatchInterestlWait() {
		return matchInterestlWait;
	}
	public void setMatchInterestlWait(BigDecimal matchInterestlWait) {
		this.matchInterestlWait = matchInterestlWait;
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
	 * 设置：投资用户数
	 */
	public void setInvCou(Double invCou) {
		this.invCou = invCou;
	}
	/**
	 * 获取：投资用户数
	 */
	public Double getInvCou() {
		return invCou;
	}
	/**
	 * 设置：投资使用红包金额
	 */
	public void setUsePackMoney(BigDecimal usePackMoney) {
		this.usePackMoney = usePackMoney;
	}
	/**
	 * 获取：投资使用红包金额
	 */
	public BigDecimal getUsePackMoney() {
		return usePackMoney;
	}
	/**
	 * 设置：人均红包金额
	 */
	public void setAvgUsePackMoney(BigDecimal avgUsePackMoney) {
		this.avgUsePackMoney = avgUsePackMoney;
	}
	/**
	 * 获取：人均红包金额
	 */
	public BigDecimal getAvgUsePackMoney() {
		return avgUsePackMoney;
	}
	/**
	 * 设置：用户投资本金
	 */
	public void setAccountMoney(BigDecimal accountMoney) {
		this.accountMoney = accountMoney;
	}
	/**
	 * 获取：用户投资本金
	 */
	public BigDecimal getAccountMoney() {
		return accountMoney;
	}
	/**
	 * 设置：人均投资本金
	 */
	public void setAvgMoney(BigDecimal avgMoney) {
		this.avgMoney = avgMoney;
	}
	/**
	 * 获取：人均投资本金
	 */
	public BigDecimal getAvgMoney() {
		return avgMoney;
	}
	/**
	 * 设置：用户投资年化金额
	 */
	public void setYearMoney(BigDecimal yearMoney) {
		this.yearMoney = yearMoney;
	}
	/**
	 * 获取：用户投资年化金额
	 */
	public BigDecimal getYearMoney() {
		return yearMoney;
	}
	/**
	 * 设置：人均年化金额
	 */
	public void setAvgYearMoney(BigDecimal avgYearMoney) {
		this.avgYearMoney = avgYearMoney;
	}
	/**
	 * 获取：人均年化金额
	 */
	public BigDecimal getAvgYearMoney() {
		return avgYearMoney;
	}
	/**
	 * 设置：邀请人返利
	 */
	public void setSpreadsMoney(BigDecimal spreadsMoney) {
		this.spreadsMoney = spreadsMoney;
	}
	/**
	 * 获取：邀请人返利
	 */
	public BigDecimal getSpreadsMoney() {
		return spreadsMoney;
	}
	/**
	 * 设置：加息成本
	 */
	public void setDiscountCost(BigDecimal discountCost) {
		this.discountCost = discountCost;
	}
	/**
	 * 获取：加息成本
	 */
	public BigDecimal getDiscountCost() {
		return discountCost;
	}
	/**
	 * 设置：人均加息成本
	 */
	public void setAvgDiscountCost(BigDecimal avgDiscountCost) {
		this.avgDiscountCost = avgDiscountCost;
	}
	/**
	 * 获取：人均加息成本
	 */
	public BigDecimal getAvgDiscountCost() {
		return avgDiscountCost;
	}
	/**
	 * 设置：新增待收（预估）
	 */
	public void setForecastAwait(BigDecimal forecastAwait) {
		this.forecastAwait = forecastAwait;
	}
	/**
	 * 获取：新增待收（预估）
	 */
	public BigDecimal getForecastAwait() {
		return forecastAwait;
	}
	/**
	 * 设置：新增代收（实际）
	 */
	public void setFullAwait(BigDecimal fullAwait) {
		this.fullAwait = fullAwait;
	}
	/**
	 * 获取：新增代收（实际）
	 */
	public BigDecimal getFullAwait() {
		return fullAwait;
	}
	/**
	 * 设置：新增待收（放款）
	 */
	public void setLoanAwait(BigDecimal loanAwait) {
		this.loanAwait = loanAwait;
	}
	/**
	 * 获取：新增待收（放款）
	 */
	public BigDecimal getLoanAwait() {
		return loanAwait;
	}
	/**
	 * 设置：回款金额
	 */
	public void setRecoverMoney(BigDecimal recoverMoney) {
		this.recoverMoney = recoverMoney;
	}
	/**
	 * 获取：回款金额
	 */
	public BigDecimal getRecoverMoney() {
		return recoverMoney;
	}
	/**
	 * 设置：本月年化投资金额
	 */
	public void setMonthNh(BigDecimal monthNh) {
		this.monthNh = monthNh;
	}
	/**
	 * 获取：本月年化投资金额
	 */
	public BigDecimal getMonthNh() {
		return monthNh;
	}
	/**
	 * 设置：新增且到12-31后还款的待收
（考虑还款方式）（万元）
	 */
	public void setNewEndYearAwait(BigDecimal newEndYearAwait) {
		this.newEndYearAwait = newEndYearAwait;
	}
	/**
	 * 获取：新增且到12-31后还款的待收
（考虑还款方式）（万元）
	 */
	public BigDecimal getNewEndYearAwait() {
		return newEndYearAwait;
	}
	/**
	 * 设置：到12-31后还款的待收金额
	 */
	public void setEndYearAwait(BigDecimal endYearAwait) {
		this.endYearAwait = endYearAwait;
	}
	/**
	 * 获取：到12-31后还款的待收金额
	 */
	public BigDecimal getEndYearAwait() {
		return endYearAwait;
	}
	/**
	 * 设置：总待收
	 */
	public void setAllAwait(BigDecimal allAwait) {
		this.allAwait = allAwait;
	}
	/**
	 * 获取：总待收
	 */
	public BigDecimal getAllAwait() {
		return allAwait;
	}
}
