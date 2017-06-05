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
	//用户ID
	private BigDecimal userId;
	//存管用户ID
	private BigDecimal cgUserId;
	//用户名称
	private String username;
	//手机号
	private String phone;
	//项目ID
	private BigDecimal projectId;
	//回款金额
	private BigDecimal money;
	//项目期限
	private BigDecimal borrowPeriod;
	//注册日期
	private String regTime;
	//首投时间
	private String xmInvOneTime;
	//注册后首投间隔(分)
	private BigDecimal xmStJg;
	//最近一次投资时间
	private String xmInvLastTime;
	//首投到最后一次投资时间间隔(分)
	private BigDecimal xmTzJg;
	//投资总金额
	private BigDecimal xmInvMoney;
	//投资次数
	private BigDecimal xmInvCou;
	//投资次数中使用奖励次数
	private BigDecimal useInvPackCou;
	//债转次数
	private BigDecimal zzFqCou;
	//投资期限偏好
	private BigDecimal periodJq;
	//累计充值金额
	private BigDecimal czMoney;
	//累计提现金额
	private BigDecimal txCgMoney;
	//本次推送记录数
	private BigDecimal dateCou;

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
	 * 设置：用户ID
	 */
	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public BigDecimal getUserId() {
		return userId;
	}
	/**
	 * 设置：存管用户ID
	 */
	public void setCgUserId(BigDecimal cgUserId) {
		this.cgUserId = cgUserId;
	}
	/**
	 * 获取：存管用户ID
	 */
	public BigDecimal getCgUserId() {
		return cgUserId;
	}
	/**
	 * 设置：用户名称
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名称
	 */
	public String getUsername() {
		return username;
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
	 * 设置：项目ID
	 */
	public void setProjectId(BigDecimal projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：项目ID
	 */
	public BigDecimal getProjectId() {
		return projectId;
	}
	/**
	 * 设置：回款金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：回款金额
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置：项目期限
	 */
	public void setBorrowPeriod(BigDecimal borrowPeriod) {
		this.borrowPeriod = borrowPeriod;
	}
	/**
	 * 获取：项目期限
	 */
	public BigDecimal getBorrowPeriod() {
		return borrowPeriod;
	}
	/**
	 * 设置：注册日期
	 */
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	/**
	 * 获取：注册日期
	 */
	public String getRegTime() {
		return regTime;
	}
	/**
	 * 设置：首投时间
	 */
	public void setXmInvOneTime(String xmInvOneTime) {
		this.xmInvOneTime = xmInvOneTime;
	}
	/**
	 * 获取：首投时间
	 */
	public String getXmInvOneTime() {
		return xmInvOneTime;
	}
	/**
	 * 设置：注册后首投间隔(分)
	 */
	public void setXmStJg(BigDecimal xmStJg) {
		this.xmStJg = xmStJg;
	}
	/**
	 * 获取：注册后首投间隔(分)
	 */
	public BigDecimal getXmStJg() {
		return xmStJg;
	}
	/**
	 * 设置：最近一次投资时间
	 */
	public void setXmInvLastTime(String xmInvLastTime) {
		this.xmInvLastTime = xmInvLastTime;
	}
	/**
	 * 获取：最近一次投资时间
	 */
	public String getXmInvLastTime() {
		return xmInvLastTime;
	}
	/**
	 * 设置：首投到最后一次投资时间间隔(分)
	 */
	public void setXmTzJg(BigDecimal xmTzJg) {
		this.xmTzJg = xmTzJg;
	}
	/**
	 * 获取：首投到最后一次投资时间间隔(分)
	 */
	public BigDecimal getXmTzJg() {
		return xmTzJg;
	}
	/**
	 * 设置：投资总金额
	 */
	public void setXmInvMoney(BigDecimal xmInvMoney) {
		this.xmInvMoney = xmInvMoney;
	}
	/**
	 * 获取：投资总金额
	 */
	public BigDecimal getXmInvMoney() {
		return xmInvMoney;
	}
	/**
	 * 设置：投资次数
	 */
	public void setXmInvCou(BigDecimal xmInvCou) {
		this.xmInvCou = xmInvCou;
	}
	/**
	 * 获取：投资次数
	 */
	public BigDecimal getXmInvCou() {
		return xmInvCou;
	}
	/**
	 * 设置：投资次数中使用奖励次数
	 */
	public void setUseInvPackCou(BigDecimal useInvPackCou) {
		this.useInvPackCou = useInvPackCou;
	}
	/**
	 * 获取：投资次数中使用奖励次数
	 */
	public BigDecimal getUseInvPackCou() {
		return useInvPackCou;
	}
	/**
	 * 设置：债转次数
	 */
	public void setZzFqCou(BigDecimal zzFqCou) {
		this.zzFqCou = zzFqCou;
	}
	/**
	 * 获取：债转次数
	 */
	public BigDecimal getZzFqCou() {
		return zzFqCou;
	}
	/**
	 * 设置：投资期限偏好
	 */
	public void setPeriodJq(BigDecimal periodJq) {
		this.periodJq = periodJq;
	}
	/**
	 * 获取：投资期限偏好
	 */
	public BigDecimal getPeriodJq() {
		return periodJq;
	}
	/**
	 * 设置：累计充值金额
	 */
	public void setCzMoney(BigDecimal czMoney) {
		this.czMoney = czMoney;
	}
	/**
	 * 获取：累计充值金额
	 */
	public BigDecimal getCzMoney() {
		return czMoney;
	}
	/**
	 * 设置：累计提现金额
	 */
	public void setTxCgMoney(BigDecimal txCgMoney) {
		this.txCgMoney = txCgMoney;
	}
	/**
	 * 获取：累计提现金额
	 */
	public BigDecimal getTxCgMoney() {
		return txCgMoney;
	}
	/**
	 * 设置：本次推送记录数
	 */
	public void setDateCou(BigDecimal dateCou) {
		this.dateCou = dateCou;
	}
	/**
	 * 获取：本次推送记录数
	 */
	public BigDecimal getDateCou() {
		return dateCou;
	}
}
