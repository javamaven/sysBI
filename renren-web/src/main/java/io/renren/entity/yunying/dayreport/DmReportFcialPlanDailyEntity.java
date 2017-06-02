package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 11:04:27
 */
public class DmReportFcialPlanDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计时间
	private String statPeriod;
	//销售总额
	private BigDecimal tenderAmount;
	//15天期限
	private BigDecimal fifteenDay;
	//1个月期限
	private BigDecimal oneMonths;
	//2个月期限
	private BigDecimal twoMonths;
	//3个月期限
	private BigDecimal threeMonths;
	//6个月期限
	private BigDecimal sixMonths;
	//9个月期限
	private BigDecimal nineMonths;
	//12个月期限
	private BigDecimal twelveMonths;
	//其他期限
	private BigDecimal otherMonths;
	//明日锁定期结束（到期）计划金额
	private BigDecimal lockEndMoney;
	//截止今日结束锁定期计划累计金额
	private BigDecimal lockEndMoneyHistory;
	//昨日结束锁定期人数
	private Double lockEndP;
	//截止今日结束锁定期累计总人数
	private Double lockEndPHistory;
	//理财计划申请退出金额
	private BigDecimal quitMoney;
	//理财计划申请退出人数
	private Double quitP;
	//理财计划成功退出金额
	private BigDecimal quitSMoney;
	//理财计划成功退出人数
	private Double quitSP;

	/**
	 * 设置：统计时间
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：统计时间
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：销售总额
	 */
	public void setTenderAmount(BigDecimal tenderAmount) {
		this.tenderAmount = tenderAmount;
	}
	/**
	 * 获取：销售总额
	 */
	public BigDecimal getTenderAmount() {
		return tenderAmount;
	}
	/**
	 * 设置：15天期限
	 */
	public void setFifteenDay(BigDecimal fifteenDay) {
		this.fifteenDay = fifteenDay;
	}
	/**
	 * 获取：15天期限
	 */
	public BigDecimal getFifteenDay() {
		return fifteenDay;
	}
	/**
	 * 设置：1个月期限
	 */
	public void setOneMonths(BigDecimal oneMonths) {
		this.oneMonths = oneMonths;
	}
	/**
	 * 获取：1个月期限
	 */
	public BigDecimal getOneMonths() {
		return oneMonths;
	}
	/**
	 * 设置：2个月期限
	 */
	public void setTwoMonths(BigDecimal twoMonths) {
		this.twoMonths = twoMonths;
	}
	/**
	 * 获取：2个月期限
	 */
	public BigDecimal getTwoMonths() {
		return twoMonths;
	}
	/**
	 * 设置：3个月期限
	 */
	public void setThreeMonths(BigDecimal threeMonths) {
		this.threeMonths = threeMonths;
	}
	/**
	 * 获取：3个月期限
	 */
	public BigDecimal getThreeMonths() {
		return threeMonths;
	}
	/**
	 * 设置：6个月期限
	 */
	public void setSixMonths(BigDecimal sixMonths) {
		this.sixMonths = sixMonths;
	}
	/**
	 * 获取：6个月期限
	 */
	public BigDecimal getSixMonths() {
		return sixMonths;
	}
	/**
	 * 设置：9个月期限
	 */
	public void setNineMonths(BigDecimal nineMonths) {
		this.nineMonths = nineMonths;
	}
	/**
	 * 获取：9个月期限
	 */
	public BigDecimal getNineMonths() {
		return nineMonths;
	}
	/**
	 * 设置：12个月期限
	 */
	public void setTwelveMonths(BigDecimal twelveMonths) {
		this.twelveMonths = twelveMonths;
	}
	/**
	 * 获取：12个月期限
	 */
	public BigDecimal getTwelveMonths() {
		return twelveMonths;
	}
	/**
	 * 设置：其他期限
	 */
	public void setOtherMonths(BigDecimal otherMonths) {
		this.otherMonths = otherMonths;
	}
	/**
	 * 获取：其他期限
	 */
	public BigDecimal getOtherMonths() {
		return otherMonths;
	}
	/**
	 * 设置：明日锁定期结束（到期）计划金额
	 */
	public void setLockEndMoney(BigDecimal lockEndMoney) {
		this.lockEndMoney = lockEndMoney;
	}
	/**
	 * 获取：明日锁定期结束（到期）计划金额
	 */
	public BigDecimal getLockEndMoney() {
		return lockEndMoney;
	}
	/**
	 * 设置：截止今日结束锁定期计划累计金额
	 */
	public void setLockEndMoneyHistory(BigDecimal lockEndMoneyHistory) {
		this.lockEndMoneyHistory = lockEndMoneyHistory;
	}
	/**
	 * 获取：截止今日结束锁定期计划累计金额
	 */
	public BigDecimal getLockEndMoneyHistory() {
		return lockEndMoneyHistory;
	}
	/**
	 * 设置：昨日结束锁定期人数
	 */
	public void setLockEndP(Double lockEndP) {
		this.lockEndP = lockEndP;
	}
	/**
	 * 获取：昨日结束锁定期人数
	 */
	public Double getLockEndP() {
		return lockEndP;
	}
	/**
	 * 设置：截止今日结束锁定期累计总人数
	 */
	public void setLockEndPHistory(Double lockEndPHistory) {
		this.lockEndPHistory = lockEndPHistory;
	}
	/**
	 * 获取：截止今日结束锁定期累计总人数
	 */
	public Double getLockEndPHistory() {
		return lockEndPHistory;
	}
	/**
	 * 设置：理财计划申请退出金额
	 */
	public void setQuitMoney(BigDecimal quitMoney) {
		this.quitMoney = quitMoney;
	}
	/**
	 * 获取：理财计划申请退出金额
	 */
	public BigDecimal getQuitMoney() {
		return quitMoney;
	}
	/**
	 * 设置：理财计划申请退出人数
	 */
	public void setQuitP(Double quitP) {
		this.quitP = quitP;
	}
	/**
	 * 获取：理财计划申请退出人数
	 */
	public Double getQuitP() {
		return quitP;
	}
	/**
	 * 设置：理财计划成功退出金额
	 */
	public void setQuitSMoney(BigDecimal quitSMoney) {
		this.quitSMoney = quitSMoney;
	}
	/**
	 * 获取：理财计划成功退出金额
	 */
	public BigDecimal getQuitSMoney() {
		return quitSMoney;
	}
	/**
	 * 设置：理财计划成功退出人数
	 */
	public void setQuitSP(Double quitSP) {
		this.quitSP = quitSP;
	}
	/**
	 * 获取：理财计划成功退出人数
	 */
	public Double getQuitSP() {
		return quitSP;
	}
}
