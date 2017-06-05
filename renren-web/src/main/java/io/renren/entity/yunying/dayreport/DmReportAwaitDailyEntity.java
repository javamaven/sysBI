package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 10:50:43
 */
public class DmReportAwaitDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String statPeriod;
	//代收本金
	private BigDecimal awaitCapital;
	//代收利息
	private BigDecimal awaitInterest;
	//已收本金
	private BigDecimal yesCapital;
	//已收利息
	private BigDecimal yesInterest;
	//新增代收本金
	private BigDecimal addAwaitCapital;
	//新增代收利息
	private BigDecimal addAwaitInteres;
	//还款本金
	private BigDecimal recoverCapital;
	//还款利息
	private BigDecimal recoverInterest;
	//数据来源：0 普通版 1存管版
	private String fromSys;

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
	 * 设置：代收本金
	 */
	public void setAwaitCapital(BigDecimal awaitCapital) {
		this.awaitCapital = awaitCapital;
	}
	/**
	 * 获取：代收本金
	 */
	public BigDecimal getAwaitCapital() {
		return awaitCapital;
	}
	/**
	 * 设置：代收利息
	 */
	public void setAwaitInterest(BigDecimal awaitInterest) {
		this.awaitInterest = awaitInterest;
	}
	/**
	 * 获取：代收利息
	 */
	public BigDecimal getAwaitInterest() {
		return awaitInterest;
	}
	/**
	 * 设置：已收本金
	 */
	public void setYesCapital(BigDecimal yesCapital) {
		this.yesCapital = yesCapital;
	}
	/**
	 * 获取：已收本金
	 */
	public BigDecimal getYesCapital() {
		return yesCapital;
	}
	/**
	 * 设置：已收利息
	 */
	public void setYesInterest(BigDecimal yesInterest) {
		this.yesInterest = yesInterest;
	}
	/**
	 * 获取：已收利息
	 */
	public BigDecimal getYesInterest() {
		return yesInterest;
	}
	/**
	 * 设置：新增代收本金
	 */
	public void setAddAwaitCapital(BigDecimal addAwaitCapital) {
		this.addAwaitCapital = addAwaitCapital;
	}
	/**
	 * 获取：新增代收本金
	 */
	public BigDecimal getAddAwaitCapital() {
		return addAwaitCapital;
	}
	/**
	 * 设置：新增代收利息
	 */
	public void setAddAwaitInteres(BigDecimal addAwaitInteres) {
		this.addAwaitInteres = addAwaitInteres;
	}
	/**
	 * 获取：新增代收利息
	 */
	public BigDecimal getAddAwaitInteres() {
		return addAwaitInteres;
	}
	/**
	 * 设置：还款本金
	 */
	public void setRecoverCapital(BigDecimal recoverCapital) {
		this.recoverCapital = recoverCapital;
	}
	/**
	 * 获取：还款本金
	 */
	public BigDecimal getRecoverCapital() {
		return recoverCapital;
	}
	/**
	 * 设置：还款利息
	 */
	public void setRecoverInterest(BigDecimal recoverInterest) {
		this.recoverInterest = recoverInterest;
	}
	/**
	 * 获取：还款利息
	 */
	public BigDecimal getRecoverInterest() {
		return recoverInterest;
	}
	/**
	 * 设置：数据来源：0 普通版 1存管版
	 */
	public void setFromSys(String fromSys) {
		this.fromSys = fromSys;
	}
	/**
	 * 获取：数据来源：0 普通版 1存管版
	 */
	public String getFromSys() {
		return fromSys;
	}
}
