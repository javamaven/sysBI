package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 项目台帐明细
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 10:07:16
 */
public class DmReportFinRepaymentdetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计日期
	private String statPeriod;
	//项目合同编号
	private String sourcecaseno;
	//借款人
	private String customername;
	//借款金额
	private BigDecimal payformoney;
	//放款金额
	private BigDecimal payformoneyout;
	//计划还款日
	private String planrepaydate;
	//实际还款日期
	private String realredate;
	//应还本金
	private BigDecimal remain;
	//应还利息
	private BigDecimal reinterest;
	//已还本金
	private BigDecimal rebackmain;
	//已还利息
	private BigDecimal rebackinterest;
	//已还罚息
	private BigDecimal reamercedmoney;
	//已还违约金
	private BigDecimal reamercedmoney3;
	//项目结清日
	private Date realgetmoneydate;
	//期次
	private BigDecimal reindex;
	//总期数
	private BigDecimal totpmts;
	//手续费收入
	private BigDecimal rebackservice;

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
	 * 设置：项目合同编号
	 */
	public void setSourcecaseno(String sourcecaseno) {
		this.sourcecaseno = sourcecaseno;
	}
	/**
	 * 获取：项目合同编号
	 */
	public String getSourcecaseno() {
		return sourcecaseno;
	}
	/**
	 * 设置：借款人
	 */
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	/**
	 * 获取：借款人
	 */
	public String getCustomername() {
		return customername;
	}
	/**
	 * 设置：借款金额
	 */
	public void setPayformoney(BigDecimal payformoney) {
		this.payformoney = payformoney;
	}
	/**
	 * 获取：借款金额
	 */
	public BigDecimal getPayformoney() {
		return payformoney;
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
	 * 设置：计划还款日
	 */
	public void setPlanrepaydate(String planrepaydate) {
		this.planrepaydate = planrepaydate;
	}
	/**
	 * 获取：计划还款日
	 */
	public String getPlanrepaydate() {
		return planrepaydate;
	}
	/**
	 * 设置：实际还款日期
	 */
	public void setRealredate(String realredate) {
		this.realredate = realredate;
	}
	/**
	 * 获取：实际还款日期
	 */
	public String getRealredate() {
		return realredate;
	}
	/**
	 * 设置：应还本金
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}
	/**
	 * 获取：应还本金
	 */
	public BigDecimal getRemain() {
		return remain;
	}
	/**
	 * 设置：应还利息
	 */
	public void setReinterest(BigDecimal reinterest) {
		this.reinterest = reinterest;
	}
	/**
	 * 获取：应还利息
	 */
	public BigDecimal getReinterest() {
		return reinterest;
	}
	/**
	 * 设置：已还本金
	 */
	public void setRebackmain(BigDecimal rebackmain) {
		this.rebackmain = rebackmain;
	}
	/**
	 * 获取：已还本金
	 */
	public BigDecimal getRebackmain() {
		return rebackmain;
	}
	/**
	 * 设置：已还利息
	 */
	public void setRebackinterest(BigDecimal rebackinterest) {
		this.rebackinterest = rebackinterest;
	}
	/**
	 * 获取：已还利息
	 */
	public BigDecimal getRebackinterest() {
		return rebackinterest;
	}
	/**
	 * 设置：已还罚息
	 */
	public void setReamercedmoney(BigDecimal reamercedmoney) {
		this.reamercedmoney = reamercedmoney;
	}
	/**
	 * 获取：已还罚息
	 */
	public BigDecimal getReamercedmoney() {
		return reamercedmoney;
	}
	/**
	 * 设置：已还违约金
	 */
	public void setReamercedmoney3(BigDecimal reamercedmoney3) {
		this.reamercedmoney3 = reamercedmoney3;
	}
	/**
	 * 获取：已还违约金
	 */
	public BigDecimal getReamercedmoney3() {
		return reamercedmoney3;
	}
	/**
	 * 设置：项目结清日
	 */
	public void setRealgetmoneydate(Date realgetmoneydate) {
		this.realgetmoneydate = realgetmoneydate;
	}
	/**
	 * 获取：项目结清日
	 */
	public Date getRealgetmoneydate() {
		return realgetmoneydate;
	}
	/**
	 * 设置：期次
	 */
	public void setReindex(BigDecimal reindex) {
		this.reindex = reindex;
	}
	/**
	 * 获取：期次
	 */
	public BigDecimal getReindex() {
		return reindex;
	}
	/**
	 * 设置：总期数
	 */
	public void setTotpmts(BigDecimal totpmts) {
		this.totpmts = totpmts;
	}
	/**
	 * 获取：总期数
	 */
	public BigDecimal getTotpmts() {
		return totpmts;
	}
	/**
	 * 设置：手续费收入
	 */
	public void setRebackservice(BigDecimal rebackservice) {
		this.rebackservice = rebackservice;
	}
	/**
	 * 获取：手续费收入
	 */
	public BigDecimal getRebackservice() {
		return rebackservice;
	}
}
