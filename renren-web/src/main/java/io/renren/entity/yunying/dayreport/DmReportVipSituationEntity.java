package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-16 13:41:01
 */
public class DmReportVipSituationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//时间
	private String statPeriod;
	//所属人
	private String owner;
	//当天新增投资人数
	private BigDecimal dayUserCou;
	//当天新增投资次数
	private BigDecimal dayTenderCou;
	//当天新增年化投资金额
	private BigDecimal dayTenderY;
	//当天新增投资金额
	private BigDecimal dayTender;

	/**
	 * 设置：时间
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：时间
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：所属人
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * 获取：所属人
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * 设置：当天新增投资人数
	 */
	public void setDayUserCou(BigDecimal dayUserCou) {
		this.dayUserCou = dayUserCou;
	}
	/**
	 * 获取：当天新增投资人数
	 */
	public BigDecimal getDayUserCou() {
		return dayUserCou;
	}
	/**
	 * 设置：当天新增投资次数
	 */
	public void setDayTenderCou(BigDecimal dayTenderCou) {
		this.dayTenderCou = dayTenderCou;
	}
	/**
	 * 获取：当天新增投资次数
	 */
	public BigDecimal getDayTenderCou() {
		return dayTenderCou;
	}
	/**
	 * 设置：当天新增年化投资金额
	 */
	public void setDayTenderY(BigDecimal dayTenderY) {
		this.dayTenderY = dayTenderY;
	}
	/**
	 * 获取：当天新增年化投资金额
	 */
	public BigDecimal getDayTenderY() {
		return dayTenderY;
	}
	/**
	 * 设置：当天新增投资金额
	 */
	public void setDayTender(BigDecimal dayTender) {
		this.dayTender = dayTender;
	}
	/**
	 * 获取：当天新增投资金额
	 */
	public BigDecimal getDayTender() {
		return dayTender;
	}
}
