package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 14:07:28
 */
public class DmReportVipUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 时间
	private String statPeriod;
	// 用户ID
	private BigDecimal oldUserId;
	// 存管ID
	private BigDecimal cgUserId;
	// 名单用户名
	private String oldUsername;
	// 用户名
	private String username;
	// 姓名
	private String realname;
	// 名单电话号码
	private String oldPhone;
	// 电话号码
	private String phone;
	//
	private BigDecimal await;
	//
	private BigDecimal balance;
	// 注册时间
	private String regTime;
	// 最近登录时间
	private String loginTime;
	// 最近一次回款日期
	private String lastRecoverTime;
	// 最近一次回款金额
	private BigDecimal lastRecoverMoney;
	// 最近一次充值日期
	private String lastRechargeTime;
	// 最近一次充值金额
	private BigDecimal lastRechargeMoney;
	// 累计充值金额
	private BigDecimal rechargeMoneyC;
	// 账户红包
	private BigDecimal voucherMoney;
	// 最近一次提现日期
	private String lastCashTime;
	// 最近一次提现金额
	private BigDecimal lastCashMoney;
	// 累计提现金额
	private BigDecimal cashMoneyC;
	// 投资次数
	private BigDecimal invCou;
	// 平均投资期限
	private BigDecimal avgPeriod;
	// 当月投资金额
	private BigDecimal monthTender;
	// 当月年华金额
	private BigDecimal monthTenderY;
	// 当月投资次数
	private BigDecimal monthTenderCou;
	// 当天投资金额
	private BigDecimal dayTender;
	// 当天年华金额
	private BigDecimal dayTenderY;
	// 当天投资次数
	private BigDecimal dayTenderCou;
	//
	private BigDecimal lv;
	// 所属人
	private String owner;
	//
	private BigDecimal totalReceipt;

	private String isHighValue;

	private String sex;
	//200万以上客户分配时待收
	private String old200wAwait;
	
	private BigDecimal dcMonthTenderY;
	private BigDecimal dcDayTenderY;
	
	public void setDcDayTenderY(BigDecimal dcDayTenderY) {
		this.dcDayTenderY = dcDayTenderY;
	}
	
	public BigDecimal getDcDayTenderY() {
		return dcDayTenderY;
	}
	
	public BigDecimal getDcMonthTenderY() {
		return dcMonthTenderY;
	}
	
	public void setDcMonthTenderY(BigDecimal dcMonthTenderY) {
		this.dcMonthTenderY = dcMonthTenderY;
	}
	
	public void setOld200wAwait(String old200wAwait) {
		this.old200wAwait = old200wAwait;
	}
	
	public String getOld200wAwait() {
		return old200wAwait;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setIsHighValue(String isHighValue) {
		this.isHighValue = isHighValue;
	}

	public String getIsHighValue() {
		return isHighValue;
	}

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
	 * 设置：用户ID
	 */
	public void setOldUserId(BigDecimal oldUserId) {
		this.oldUserId = oldUserId;
	}

	/**
	 * 获取：用户ID
	 */
	public BigDecimal getOldUserId() {
		return oldUserId;
	}

	/**
	 * 设置：存管ID
	 */
	public void setCgUserId(BigDecimal cgUserId) {
		this.cgUserId = cgUserId;
	}

	/**
	 * 获取：存管ID
	 */
	public BigDecimal getCgUserId() {
		return cgUserId;
	}

	/**
	 * 设置：名单用户名
	 */
	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
	}

	/**
	 * 获取：名单用户名
	 */
	public String getOldUsername() {
		return oldUsername;
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
	 * 设置：名单电话号码
	 */
	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	/**
	 * 获取：名单电话号码
	 */
	public String getOldPhone() {
		return oldPhone;
	}

	/**
	 * 设置：电话号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取：电话号码
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置：
	 */
	public void setAwait(BigDecimal await) {
		this.await = await;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getAwait() {
		return await;
	}

	/**
	 * 设置：
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置：注册时间
	 */
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	/**
	 * 获取：注册时间
	 */
	public String getRegTime() {
		return regTime;
	}

	/**
	 * 设置：最近登录时间
	 */
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 获取：最近登录时间
	 */
	public String getLoginTime() {
		return loginTime;
	}

	/**
	 * 设置：最近一次回款日期
	 */
	public void setLastRecoverTime(String lastRecoverTime) {
		this.lastRecoverTime = lastRecoverTime;
	}

	/**
	 * 获取：最近一次回款日期
	 */
	public String getLastRecoverTime() {
		return lastRecoverTime;
	}

	/**
	 * 设置：最近一次回款金额
	 */
	public void setLastRecoverMoney(BigDecimal lastRecoverMoney) {
		this.lastRecoverMoney = lastRecoverMoney;
	}

	/**
	 * 获取：最近一次回款金额
	 */
	public BigDecimal getLastRecoverMoney() {
		return lastRecoverMoney;
	}

	/**
	 * 设置：最近一次充值日期
	 */
	public void setLastRechargeTime(String lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}

	/**
	 * 获取：最近一次充值日期
	 */
	public String getLastRechargeTime() {
		return lastRechargeTime;
	}

	/**
	 * 设置：最近一次充值金额
	 */
	public void setLastRechargeMoney(BigDecimal lastRechargeMoney) {
		this.lastRechargeMoney = lastRechargeMoney;
	}

	/**
	 * 获取：最近一次充值金额
	 */
	public BigDecimal getLastRechargeMoney() {
		return lastRechargeMoney;
	}

	/**
	 * 设置：累计充值金额
	 */
	public void setRechargeMoneyC(BigDecimal rechargeMoneyC) {
		this.rechargeMoneyC = rechargeMoneyC;
	}

	/**
	 * 获取：累计充值金额
	 */
	public BigDecimal getRechargeMoneyC() {
		return rechargeMoneyC;
	}

	/**
	 * 设置：账户红包
	 */
	public void setVoucherMoney(BigDecimal voucherMoney) {
		this.voucherMoney = voucherMoney;
	}

	/**
	 * 获取：账户红包
	 */
	public BigDecimal getVoucherMoney() {
		return voucherMoney;
	}

	/**
	 * 设置：最近一次提现日期
	 */
	public void setLastCashTime(String lastCashTime) {
		this.lastCashTime = lastCashTime;
	}

	/**
	 * 获取：最近一次提现日期
	 */
	public String getLastCashTime() {
		return lastCashTime;
	}

	/**
	 * 设置：最近一次提现金额
	 */
	public void setLastCashMoney(BigDecimal lastCashMoney) {
		this.lastCashMoney = lastCashMoney;
	}

	/**
	 * 获取：最近一次提现金额
	 */
	public BigDecimal getLastCashMoney() {
		return lastCashMoney;
	}

	/**
	 * 设置：累计提现金额
	 */
	public void setCashMoneyC(BigDecimal cashMoneyC) {
		this.cashMoneyC = cashMoneyC;
	}

	/**
	 * 获取：累计提现金额
	 */
	public BigDecimal getCashMoneyC() {
		return cashMoneyC;
	}

	/**
	 * 设置：投资次数
	 */
	public void setInvCou(BigDecimal invCou) {
		this.invCou = invCou;
	}

	/**
	 * 获取：投资次数
	 */
	public BigDecimal getInvCou() {
		return invCou;
	}

	/**
	 * 设置：平均投资期限
	 */
	public void setAvgPeriod(BigDecimal avgPeriod) {
		this.avgPeriod = avgPeriod;
	}

	/**
	 * 获取：平均投资期限
	 */
	public BigDecimal getAvgPeriod() {
		return avgPeriod;
	}

	/**
	 * 设置：当月投资金额
	 */
	public void setMonthTender(BigDecimal monthTender) {
		this.monthTender = monthTender;
	}

	/**
	 * 获取：当月投资金额
	 */
	public BigDecimal getMonthTender() {
		return monthTender;
	}

	/**
	 * 设置：当月年华金额
	 */
	public void setMonthTenderY(BigDecimal monthTenderY) {
		this.monthTenderY = monthTenderY;
	}

	/**
	 * 获取：当月年华金额
	 */
	public BigDecimal getMonthTenderY() {
		return monthTenderY;
	}

	/**
	 * 设置：当月投资次数
	 */
	public void setMonthTenderCou(BigDecimal monthTenderCou) {
		this.monthTenderCou = monthTenderCou;
	}

	/**
	 * 获取：当月投资次数
	 */
	public BigDecimal getMonthTenderCou() {
		return monthTenderCou;
	}

	/**
	 * 设置：当天投资金额
	 */
	public void setDayTender(BigDecimal dayTender) {
		this.dayTender = dayTender;
	}

	/**
	 * 获取：当天投资金额
	 */
	public BigDecimal getDayTender() {
		return dayTender;
	}

	/**
	 * 设置：当天年华金额
	 */
	public void setDayTenderY(BigDecimal dayTenderY) {
		this.dayTenderY = dayTenderY;
	}

	/**
	 * 获取：当天年华金额
	 */
	public BigDecimal getDayTenderY() {
		return dayTenderY;
	}

	/**
	 * 设置：当天投资次数
	 */
	public void setDayTenderCou(BigDecimal dayTenderCou) {
		this.dayTenderCou = dayTenderCou;
	}

	/**
	 * 获取：当天投资次数
	 */
	public BigDecimal getDayTenderCou() {
		return dayTenderCou;
	}

	/**
	 * 设置：
	 */
	public void setLv(BigDecimal lv) {
		this.lv = lv;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getLv() {
		return lv;
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
	 * 设置：
	 */
	public void setTotalReceipt(BigDecimal totalReceipt) {
		this.totalReceipt = totalReceipt;
	}

	/**
	 * 获取：
	 */
	public BigDecimal getTotalReceipt() {
		return totalReceipt;
	}
}
