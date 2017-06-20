package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 14:33:45
 */
public class DmReportRecoverDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private String statPeriod;
	// 用户ID
	private BigDecimal userId;
	// 存管用户ID
	private BigDecimal cgUserId;
	// 用户名称
	private String username;
	// 手机号
	private String phone;
	// 项目ID
	private BigDecimal projectId;
	// 回款金额
	private BigDecimal money;
	// 项目期限
	private BigDecimal borrowPeriod;
	// 注册日期
	private String regTime;
	// 注册后首投间隔(分)(数据覆盖历史项目)
	private BigDecimal xmStJg;
	// 平均投资时间间隔(分)(数据覆盖历史项目)
	private BigDecimal avgXmTzJg;
	// 平均投资金额
	private BigDecimal avgXmInvMoney;

	// 投资次数
	private BigDecimal xmInvCou;
	// 投资次数中使用奖励次数(数据覆盖历史项目)
	private BigDecimal invPackBl;

	// 发起转让比例(数据覆盖历史项目)
	private BigDecimal zzBl;
	// 投资期限偏好(数据覆盖历史项目)
	private BigDecimal periodJq;

	// 当前是否持有红包
	private String rewardStatus;
	// 当前持有红包金额
	private BigDecimal rewardMoney;
	// 累计充值金额
	private BigDecimal czMoney;

	// 累计提现金额
	private BigDecimal txCgMoney;
	// 提现金额占比充值金额
	private BigDecimal txCgMoneyBl;
	// 本次推送记录数
	private BigDecimal dateCou;

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

	public BigDecimal getProjectId() {
		return projectId;
	}

	public void setProjectId(BigDecimal projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getBorrowPeriod() {
		return borrowPeriod;
	}

	public void setBorrowPeriod(BigDecimal borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
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

	public BigDecimal getDateCou() {
		return dateCou;
	}

	public void setDateCou(BigDecimal dateCou) {
		this.dateCou = dateCou;
	}

}
