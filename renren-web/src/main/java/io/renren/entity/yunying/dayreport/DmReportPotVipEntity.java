package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 潜力VIP数据表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-07 16:24:59
 */
public class DmReportPotVipEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//日期
	private String statPeriod;
	//用户ID
	private String userId;
	//存管ID
	private String cgUserId;
	//用户名
	private String username;
	//电话
	private String phone;
	//姓名
	private String realname;
	//账户资产权益额
	private BigDecimal moneyAll;
	//账户余额
	private BigDecimal balance;
	//待收金额（≥5万元)
	private BigDecimal moneyWait;
	//投资次数(前3次)
	private BigDecimal sumInvest;
	//平均投资期限偏好(大于100天)
	private BigDecimal avgPeriod;
	//累计充值金额（≥5万元）
	private BigDecimal amount;
	//累计投资金额（≥5万元）
	private BigDecimal moneyInvest;
	//注册日期
	private String registerTime;

	/**
	 * 设置：日期
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：日期
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：存管ID
	 */
	public void setCgUserId(String cgUserId) {
		this.cgUserId = cgUserId;
	}
	/**
	 * 获取：存管ID
	 */
	public String getCgUserId() {
		return cgUserId;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * 获取：姓名
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 设置：账户资产权益额
	 */
	public void setMoneyAll(BigDecimal moneyAll) {
		this.moneyAll = moneyAll;
	}
	/**
	 * 获取：账户资产权益额
	 */
	public BigDecimal getMoneyAll() {
		return moneyAll;
	}
	/**
	 * 设置：账户余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	/**
	 * 获取：账户余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * 设置：待收金额（≥5万元)
	 */
	public void setMoneyWait(BigDecimal moneyWait) {
		this.moneyWait = moneyWait;
	}
	/**
	 * 获取：待收金额（≥5万元)
	 */
	public BigDecimal getMoneyWait() {
		return moneyWait;
	}
	/**
	 * 设置：投资次数(前3次)
	 */
	public void setSumInvest(BigDecimal sumInvest) {
		this.sumInvest = sumInvest;
	}
	/**
	 * 获取：投资次数(前3次)
	 */
	public BigDecimal getSumInvest() {
		return sumInvest;
	}
	/**
	 * 设置：平均投资期限偏好(大于100天)
	 */
	public void setAvgPeriod(BigDecimal avgPeriod) {
		this.avgPeriod = avgPeriod;
	}
	/**
	 * 获取：平均投资期限偏好(大于100天)
	 */
	public BigDecimal getAvgPeriod() {
		return avgPeriod;
	}
	/**
	 * 设置：累计充值金额（≥5万元）
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取：累计充值金额（≥5万元）
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * 设置：累计投资金额（≥5万元）
	 */
	public void setMoneyInvest(BigDecimal moneyInvest) {
		this.moneyInvest = moneyInvest;
	}
	/**
	 * 获取：累计投资金额（≥5万元）
	 */
	public BigDecimal getMoneyInvest() {
		return moneyInvest;
	}
	/**
	 * 设置：注册日期
	 */
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	/**
	 * 获取：注册日期
	 */
	public String getRegisterTime() {
		return registerTime;
	}
}
