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
	private String actualCost;
	//新增注册人
	private String regCou;
	//新增首投人数
	private String firstinvestCou;
	//首投金额
	private String firstinvestMoney;
	//首投年化金额
	private String firstinvestYMoney;
	//投资总人数
	private String invCou;
	//投资总金额
	private String invMoney;
	//年化投资总金额
	private String invYMoney;
	//点点赚购买金额
	private String ddzMoney;
	//注册成本
	private String regCost;
	//首投成本
	private String firstinvestCost;
	//人均首投
	private String avgFirstinvestMoney;
	//注册人投资转化率
	private String regInvConversion;
	//首投ROI
	private String firstinvestRot;
	//累计ROI
	private String cumulativeRot;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getStatPeriod() {
		return statPeriod;
	}

	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}

	public String getChannelHead() {
		return channelHead;
	}

	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getActualCost() {
		return actualCost;
	}

	public void setActualCost(String actualCost) {
		this.actualCost = actualCost;
	}

	public String getRegCou() {
		return regCou;
	}

	public void setRegCou(String regCou) {
		this.regCou = regCou;
	}

	public String getFirstinvestCou() {
		return firstinvestCou;
	}

	public void setFirstinvestCou(String firstinvestCou) {
		this.firstinvestCou = firstinvestCou;
	}

	public String getFirstinvestMoney() {
		return firstinvestMoney;
	}

	public void setFirstinvestMoney(String firstinvestMoney) {
		this.firstinvestMoney = firstinvestMoney;
	}

	public String getFirstinvestYMoney() {
		return firstinvestYMoney;
	}

	public void setFirstinvestYMoney(String firstinvestYMoney) {
		this.firstinvestYMoney = firstinvestYMoney;
	}

	public String getInvCou() {
		return invCou;
	}

	public void setInvCou(String invCou) {
		this.invCou = invCou;
	}

	public String getInvMoney() {
		return invMoney;
	}

	public void setInvMoney(String invMoney) {
		this.invMoney = invMoney;
	}

	public String getInvYMoney() {
		return invYMoney;
	}

	public void setInvYMoney(String invYMoney) {
		this.invYMoney = invYMoney;
	}

	public String getDdzMoney() {
		return ddzMoney;
	}

	public void setDdzMoney(String ddzMoney) {
		this.ddzMoney = ddzMoney;
	}

	public String getRegCost() {
		return regCost;
	}

	public void setRegCost(String regCost) {
		this.regCost = regCost;
	}

	public String getFirstinvestCost() {
		return firstinvestCost;
	}

	public void setFirstinvestCost(String firstinvestCost) {
		this.firstinvestCost = firstinvestCost;
	}

	public String getAvgFirstinvestMoney() {
		return avgFirstinvestMoney;
	}

	public void setAvgFirstinvestMoney(String avgFirstinvestMoney) {
		this.avgFirstinvestMoney = avgFirstinvestMoney;
	}

	public String getRegInvConversion() {
		return regInvConversion;
	}

	public void setRegInvConversion(String regInvConversion) {
		this.regInvConversion = regInvConversion;
	}

	public String getFirstinvestRot() {
		return firstinvestRot;
	}

	public void setFirstinvestRot(String firstinvestRot) {
		this.firstinvestRot = firstinvestRot;
	}

	public String getCumulativeRot() {
		return cumulativeRot;
	}

	public void setCumulativeRot(String cumulativeRot) {
		this.cumulativeRot = cumulativeRot;
	}
}
