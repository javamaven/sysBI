package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 市场部每日渠道数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
public class MarketChannelEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String statPeriod;
	//主负责人
	private String channelHead;
	//渠道类型
	private String type;
	//渠道名称
	private String channelName;
	//日期
	private String ctime;
	//实际消费
	private Integer actualCost;
	//新增注册人
	private Integer regCou;
	//新增首投人数
	private Integer firstinvestCou;
	//首投金额
	private BigDecimal firstinvestMoney;
	//首投年化金额
	private BigDecimal firstinvestYMoney;
	//投资总人数
	private Integer invCou;
	//投资总金额
	private BigDecimal invMoney;
	//年化投资总金额
	private BigDecimal invYMoney;
	//点点赚购买金额
	private BigDecimal ddzMoney;
	//注册成本
	private BigDecimal regCost;
	//首投成本
	private BigDecimal firstinvestCost;
	//人均首投
	private BigDecimal avgFirstinvestMoney;
	//注册人投资转化率
	private BigDecimal regInvConversion;
	//首投ROI
	private BigDecimal firstinvestRot;
	//累计ROI
	private BigDecimal cumulativeRot;

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
	 * 设置：主负责人
	 */
	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
	}
	/**
	 * 获取：主负责人
	 */
	public String getChannelHead() {
		return channelHead;
	}
	/**
	 * 设置：渠道类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：渠道类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：渠道名称
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	/**
	 * 获取：渠道名称
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * 设置：日期
	 */
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	/**
	 * 获取：日期
	 */
	public String getCtime() {
		return ctime;
	}
	/**
	 * 设置：实际消费
	 */
	public void setActualCost(Integer actualCost) {
		this.actualCost = actualCost;
	}
	/**
	 * 获取：实际消费
	 */
	public Integer getActualCost() {
		return actualCost;
	}
	/**
	 * 设置：新增注册人
	 */
	public void setRegCou(Integer regCou) {
		this.regCou = regCou;
	}
	/**
	 * 获取：新增注册人
	 */
	public Integer getRegCou() {
		return regCou;
	}
	/**
	 * 设置：新增首投人数
	 */
	public void setFirstinvestCou(Integer firstinvestCou) {
		this.firstinvestCou = firstinvestCou;
	}
	/**
	 * 获取：新增首投人数
	 */
	public Integer getFirstinvestCou() {
		return firstinvestCou;
	}
	/**
	 * 设置：首投金额
	 */
	public void setFirstinvestMoney(BigDecimal firstinvestMoney) {
		this.firstinvestMoney = firstinvestMoney;
	}
	/**
	 * 获取：首投金额
	 */
	public BigDecimal getFirstinvestMoney() {
		return firstinvestMoney;
	}
	/**
	 * 设置：首投年化金额
	 */
	public void setFirstinvestYMoney(BigDecimal firstinvestYMoney) {
		this.firstinvestYMoney = firstinvestYMoney;
	}
	/**
	 * 获取：首投年化金额
	 */
	public BigDecimal getFirstinvestYMoney() {
		return firstinvestYMoney;
	}
	/**
	 * 设置：投资总人数
	 */
	public void setInvCou(Integer invCou) {
		this.invCou = invCou;
	}
	/**
	 * 获取：投资总人数
	 */
	public Integer getInvCou() {
		return invCou;
	}
	/**
	 * 设置：投资总金额
	 */
	public void setInvMoney(BigDecimal invMoney) {
		this.invMoney = invMoney;
	}
	/**
	 * 获取：投资总金额
	 */
	public BigDecimal getInvMoney() {
		return invMoney;
	}
	/**
	 * 设置：年化投资总金额
	 */
	public void setInvYMoney(BigDecimal invYMoney) {
		this.invYMoney = invYMoney;
	}
	/**
	 * 获取：年化投资总金额
	 */
	public BigDecimal getInvYMoney() {
		return invYMoney;
	}
	/**
	 * 设置：点点赚购买金额
	 */
	public void setDdzMoney(BigDecimal ddzMoney) {
		this.ddzMoney = ddzMoney;
	}
	/**
	 * 获取：点点赚购买金额
	 */
	public BigDecimal getDdzMoney() {
		return ddzMoney;
	}
	/**
	 * 设置：注册成本
	 */
	public void setRegCost(BigDecimal regCost) {
		this.regCost = regCost;
	}
	/**
	 * 获取：注册成本
	 */
	public BigDecimal getRegCost() {
		return regCost;
	}
	/**
	 * 设置：首投成本
	 */
	public void setFirstinvestCost(BigDecimal firstinvestCost) {
		this.firstinvestCost = firstinvestCost;
	}
	/**
	 * 获取：首投成本
	 */
	public BigDecimal getFirstinvestCost() {
		return firstinvestCost;
	}
	/**
	 * 设置：人均首投
	 */
	public void setAvgFirstinvestMoney(BigDecimal avgFirstinvestMoney) {
		this.avgFirstinvestMoney = avgFirstinvestMoney;
	}
	/**
	 * 获取：人均首投
	 */
	public BigDecimal getAvgFirstinvestMoney() {
		return avgFirstinvestMoney;
	}
	/**
	 * 设置：注册人投资转化率
	 */
	public void setRegInvConversion(BigDecimal regInvConversion) {
		this.regInvConversion = regInvConversion;
	}
	/**
	 * 获取：注册人投资转化率
	 */
	public BigDecimal getRegInvConversion() {
		return regInvConversion;
	}
	/**
	 * 设置：首投ROI
	 */
	public void setFirstinvestRot(BigDecimal firstinvestRot) {
		this.firstinvestRot = firstinvestRot;
	}
	/**
	 * 获取：首投ROI
	 */
	public BigDecimal getFirstinvestRot() {
		return firstinvestRot;
	}
	/**
	 * 设置：累计ROI
	 */
	public void setCumulativeRot(BigDecimal cumulativeRot) {
		this.cumulativeRot = cumulativeRot;
	}
	/**
	 * 获取：累计ROI
	 */
	public BigDecimal getCumulativeRot() {
		return cumulativeRot;
	}
}
