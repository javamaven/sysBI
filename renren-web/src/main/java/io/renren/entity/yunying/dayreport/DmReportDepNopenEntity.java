package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-26 14:36:17
 */
public class DmReportDepNopenEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String statPeriod;
	//
	private Integer userId;
	//用户名
	private String username;
	//用户姓名
	private String realname;
	//手机号
	private String phone;
	//存管ID
	private Integer cgUserId;
	//普通版待收金额
	private BigDecimal norWait;
	//最后一次投资时间
	private String tenderTime;
	//最后一次回款时间
	private String recoverTime;
	//投资次数
	private Integer invCou;
	//累计投资金额
	private BigDecimal tenderCapital;
	//用户等级
	private String lv;
	//普通版账户可用余额
	private BigDecimal norBalance;
	//可用红包金额
	private BigDecimal reward;
	//投资次数中使用奖励比例
	private BigDecimal rewardInv;

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
	 * 设置：
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Integer getUserId() {
		return userId;
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
	 * 设置：用户姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * 获取：用户姓名
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 设置：手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：存管ID
	 */
	public void setCgUserId(Integer cgUserId) {
		this.cgUserId = cgUserId;
	}
	/**
	 * 获取：存管ID
	 */
	public Integer getCgUserId() {
		return cgUserId;
	}
	/**
	 * 设置：普通版待收金额
	 */
	public void setNorWait(BigDecimal norWait) {
		this.norWait = norWait;
	}
	/**
	 * 获取：普通版待收金额
	 */
	public BigDecimal getNorWait() {
		return norWait;
	}
	/**
	 * 设置：最后一次投资时间
	 */
	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}
	/**
	 * 获取：最后一次投资时间
	 */
	public String getTenderTime() {
		return tenderTime;
	}
	/**
	 * 设置：最后一次回款时间
	 */
	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}
	/**
	 * 获取：最后一次回款时间
	 */
	public String getRecoverTime() {
		return recoverTime;
	}
	/**
	 * 设置：投资次数
	 */
	public void setInvCou(Integer invCou) {
		this.invCou = invCou;
	}
	/**
	 * 获取：投资次数
	 */
	public Integer getInvCou() {
		return invCou;
	}
	/**
	 * 设置：累计投资金额
	 */
	public void setTenderCapital(BigDecimal tenderCapital) {
		this.tenderCapital = tenderCapital;
	}
	/**
	 * 获取：累计投资金额
	 */
	public BigDecimal getTenderCapital() {
		return tenderCapital;
	}
	/**
	 * 设置：用户等级
	 */
	public void setLv(String lv) {
		this.lv = lv;
	}
	/**
	 * 获取：用户等级
	 */
	public String getLv() {
		return lv;
	}
	/**
	 * 设置：普通版账户可用余额
	 */
	public void setNorBalance(BigDecimal norBalance) {
		this.norBalance = norBalance;
	}
	/**
	 * 获取：普通版账户可用余额
	 */
	public BigDecimal getNorBalance() {
		return norBalance;
	}
	/**
	 * 设置：可用红包金额
	 */
	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
	/**
	 * 获取：可用红包金额
	 */
	public BigDecimal getReward() {
		return reward;
	}
	/**
	 * 设置：投资次数中使用奖励比例
	 */
	public void setRewardInv(BigDecimal rewardInv) {
		this.rewardInv = rewardInv;
	}
	/**
	 * 获取：投资次数中使用奖励比例
	 */
	public BigDecimal getRewardInv() {
		return rewardInv;
	}
}
