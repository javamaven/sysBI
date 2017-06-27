package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用户标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-13 14:08:32
 */
public class LabeltagUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户主键
	private String userId;
	// 存管用户主键
	private String cgUserId;
	// 普通版用户主键
	private String oldUserId;
	// 手机号
	private String phone;
	// 用户账号
	private String username;
	// 用户姓名
	private String realname;
	//注册日期
	private String registerTime;
	//首投日期
	private String firstinvestTime;
	//渠道名称
	private String channelName;
	//性别
	private String sex;
	//用户年龄段
	private String age;
	//是否注册存管系统
	private String isInterflow;
	//是否开通存管账号
	private String isDepository;
	//投资时间段
	private String invPeriod;
	//投资间隔
	private String invInterval;
	//项目期限偏好
	private String normalPeriodPreference;
	//债转期限偏好
	private String changePeriodPreference;
	//累计投资金额
	private String cumulativeInvMoney;
	//当前待收金额
	private String totalAssets;	
	//累计投资年化金额
	private String cumulativeInvMoneyYear;


	//账户可用余额
	private String balance;
	//最近一笔投资金额
	private String lastInvMoney;
	//单笔投资最高金额
	private String invMaxMoney;
	//使用优惠投资的比例
	private String useVoucherProportion;
	//累计使用的红包及券的金额
	private String cumulativeUseVoucherMoney;
	//累计使用的红包及券的次数
	private String cumulativeUseVoucherCou;
	//红包券收益占比已收收益比例
	private String voucherEarningsProportion;
	//累计红包使用率
	private String cumulativeUsageVoucher;
	//次均红包金额
	private String avgUseMoney;
	//最近一次投资时间
	private String lastInvTime;
	//最近一次充值时间
	private String lastRechargeTime;
	//平均每笔项目投资金额
	private String avgNormalMoney;
	//平均每笔债转投资金额
	private String avgChangeMoney;
	//平均每笔综合投资金额
	private String avgInvMoney;
	//投资项目次数
	private String normalCou;
	//投资债转次数
	private String changeCou;
	//投资综合次数
	private String invCou;
	//账户可用优惠余额
	private String voucherBalance;
	//距离上次营销天数
	private String marketingDay;
	// 最后一次登录时间
	private String lastLoginTime;

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getFirstinvestTime() {
		return firstinvestTime;
	}

	public void setFirstinvestTime(String firstinvestTime) {
		this.firstinvestTime = firstinvestTime;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIsInterflow() {
		return isInterflow;
	}

	public void setIsInterflow(String isInterflow) {
		this.isInterflow = isInterflow;
	}

	public String getIsDepository() {
		return isDepository;
	}

	public void setIsDepository(String isDepository) {
		this.isDepository = isDepository;
	}

	public String getInvPeriod() {
		return invPeriod;
	}

	public void setInvPeriod(String invPeriod) {
		this.invPeriod = invPeriod;
	}

	public String getInvInterval() {
		return invInterval;
	}

	public void setInvInterval(String invInterval) {
		this.invInterval = invInterval;
	}

	public String getNormalPeriodPreference() {
		return normalPeriodPreference;
	}

	public void setNormalPeriodPreference(String normalPeriodPreference) {
		this.normalPeriodPreference = normalPeriodPreference;
	}

	public String getChangePeriodPreference() {
		return changePeriodPreference;
	}

	public void setChangePeriodPreference(String changePeriodPreference) {
		this.changePeriodPreference = changePeriodPreference;
	}

	public String getCumulativeInvMoney() {
		return cumulativeInvMoney;
	}

	public void setCumulativeInvMoney(String cumulativeInvMoney) {
		this.cumulativeInvMoney = cumulativeInvMoney;
	}

	public String getCumulativeInvMoneyYear() {
		return cumulativeInvMoneyYear;
	}

	public void setCumulativeInvMoneyYear(String cumulativeInvMoneyYear) {
		this.cumulativeInvMoneyYear = cumulativeInvMoneyYear;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLastInvMoney() {
		return lastInvMoney;
	}

	public void setLastInvMoney(String lastInvMoney) {
		this.lastInvMoney = lastInvMoney;
	}

	public String getInvMaxMoney() {
		return invMaxMoney;
	}

	public void setInvMaxMoney(String invMaxMoney) {
		this.invMaxMoney = invMaxMoney;
	}

	public String getUseVoucherProportion() {
		return useVoucherProportion;
	}

	public void setUseVoucherProportion(String useVoucherProportion) {
		this.useVoucherProportion = useVoucherProportion;
	}

	public String getCumulativeUseVoucherMoney() {
		return cumulativeUseVoucherMoney;
	}

	public void setCumulativeUseVoucherMoney(String cumulativeUseVoucherMoney) {
		this.cumulativeUseVoucherMoney = cumulativeUseVoucherMoney;
	}

	public String getCumulativeUseVoucherCou() {
		return cumulativeUseVoucherCou;
	}

	public void setCumulativeUseVoucherCou(String cumulativeUseVoucherCou) {
		this.cumulativeUseVoucherCou = cumulativeUseVoucherCou;
	}

	public String getVoucherEarningsProportion() {
		return voucherEarningsProportion;
	}

	public void setVoucherEarningsProportion(String voucherEarningsProportion) {
		this.voucherEarningsProportion = voucherEarningsProportion;
	}

	public String getCumulativeUsageVoucher() {
		return cumulativeUsageVoucher;
	}

	public void setCumulativeUsageVoucher(String cumulativeUsageVoucher) {
		this.cumulativeUsageVoucher = cumulativeUsageVoucher;
	}

	public String getAvgUseMoney() {
		return avgUseMoney;
	}

	public void setAvgUseMoney(String avgUseMoney) {
		this.avgUseMoney = avgUseMoney;
	}

	public String getLastInvTime() {
		return lastInvTime;
	}

	public void setLastInvTime(String lastInvTime) {
		this.lastInvTime = lastInvTime;
	}

	public String getLastRechargeTime() {
		return lastRechargeTime;
	}

	public void setLastRechargeTime(String lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}

	public String getAvgNormalMoney() {
		return avgNormalMoney;
	}

	public void setAvgNormalMoney(String avgNormalMoney) {
		this.avgNormalMoney = avgNormalMoney;
	}

	public String getAvgChangeMoney() {
		return avgChangeMoney;
	}

	public void setAvgChangeMoney(String avgChangeMoney) {
		this.avgChangeMoney = avgChangeMoney;
	}

	public String getAvgInvMoney() {
		return avgInvMoney;
	}

	public void setAvgInvMoney(String avgInvMoney) {
		this.avgInvMoney = avgInvMoney;
	}

	public String getNormalCou() {
		return normalCou;
	}

	public void setNormalCou(String normalCou) {
		this.normalCou = normalCou;
	}

	public String getChangeCou() {
		return changeCou;
	}

	public void setChangeCou(String changeCou) {
		this.changeCou = changeCou;
	}

	public String getInvCou() {
		return invCou;
	}

	public void setInvCou(String invCou) {
		this.invCou = invCou;
	}

	public String getVoucherBalance() {
		return voucherBalance;
	}

	public void setVoucherBalance(String voucherBalance) {
		this.voucherBalance = voucherBalance;
	}

	public String getMarketingDay() {
		return marketingDay;
	}

	public void setMarketingDay(String marketingDay) {
		this.marketingDay = marketingDay;
	}

	public String getCgUserId() {
		return cgUserId;
	}

	public void setCgUserId(String cgUserId) {
		this.cgUserId = cgUserId;
	}

	public String getOldUserId() {
		return oldUserId;
	}

	public void setOldUserId(String oldUserId) {
		this.oldUserId = oldUserId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
}
