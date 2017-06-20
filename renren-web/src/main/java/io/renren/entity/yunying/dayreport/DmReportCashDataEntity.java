package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 11:27:30
 */
public class DmReportCashDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private String statPeriod;
	//
	private BigDecimal userId;
	//存管用户ID
	private BigDecimal cgUserId;
	//用户名称
	private String username;
	//手机号
	private String phone;
	
	private String realname;
	private BigDecimal money;
	private BigDecimal total;
	
	private BigDecimal balance;
	private BigDecimal await;
	
	private String regTime;
	private BigDecimal xmStJg;
	private BigDecimal avgXmTzJg;
	
	private BigDecimal avgXmInvMoney;
	private BigDecimal xmInvCou;
	private BigDecimal invPackBl;
	
	private BigDecimal zzBl;
	private BigDecimal periodJq;
	private String rewardStatus;

	private BigDecimal rewardMoney;
	private BigDecimal czMoney;
	private BigDecimal txCgMoney;
	
	private BigDecimal txCgMoneyBl;

	public String getStatPeriod() {
		return statPeriod;
	}

	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}

	public BigDecimal getUserId() {
		return userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	public BigDecimal getCgUserId() {
		return cgUserId;
	}

	public void setCgUserId(BigDecimal cgUserId) {
		this.cgUserId = cgUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAwait() {
		return await;
	}

	public void setAwait(BigDecimal await) {
		this.await = await;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public BigDecimal getXmStJg() {
		return xmStJg;
	}

	public void setXmStJg(BigDecimal xmStJg) {
		this.xmStJg = xmStJg;
	}

	public BigDecimal getAvgXmTzJg() {
		return avgXmTzJg;
	}

	public void setAvgXmTzJg(BigDecimal avgXmTzJg) {
		this.avgXmTzJg = avgXmTzJg;
	}

	public BigDecimal getAvgXmInvMoney() {
		return avgXmInvMoney;
	}

	public void setAvgXmInvMoney(BigDecimal avgXmInvMoney) {
		this.avgXmInvMoney = avgXmInvMoney;
	}

	public BigDecimal getXmInvCou() {
		return xmInvCou;
	}

	public void setXmInvCou(BigDecimal xmInvCou) {
		this.xmInvCou = xmInvCou;
	}

	public BigDecimal getInvPackBl() {
		return invPackBl;
	}

	public void setInvPackBl(BigDecimal invPackBl) {
		this.invPackBl = invPackBl;
	}

	public BigDecimal getZzBl() {
		return zzBl;
	}

	public void setZzBl(BigDecimal zzBl) {
		this.zzBl = zzBl;
	}

	public BigDecimal getPeriodJq() {
		return periodJq;
	}

	public void setPeriodJq(BigDecimal periodJq) {
		this.periodJq = periodJq;
	}

	public String getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(String rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public BigDecimal getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(BigDecimal rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public BigDecimal getCzMoney() {
		return czMoney;
	}

	public void setCzMoney(BigDecimal czMoney) {
		this.czMoney = czMoney;
	}

	public BigDecimal getTxCgMoney() {
		return txCgMoney;
	}

	public void setTxCgMoney(BigDecimal txCgMoney) {
		this.txCgMoney = txCgMoney;
	}

	public BigDecimal getTxCgMoneyBl() {
		return txCgMoneyBl;
	}

	public void setTxCgMoneyBl(BigDecimal txCgMoneyBl) {
		this.txCgMoneyBl = txCgMoneyBl;
	}
	
	
}
