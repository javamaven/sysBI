package io.renren.entity;

import java.io.Serializable;

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
	// 主负责人
	private String channelHead;
	// 渠道类型
	private String type;
	// 渠道名称
	private String channelName;
	// 渠道标签
	private String channelLabel;

	// 日期
	private String ctime;
	// 实际消费
	private String actualCost;
	// 新增注册人
	private String regCou;
	// 首投金额
	private String firstinvestMoney;
	// 首投年化金额
	private String firstinvestyearamount;
	// 投资总人数
	private String countUser;
	// 投资总金额
	private String invMoney;
	// 年化投资总金额
	private String yearamount;
	
	private String channelCost;
	private String channelRecharge;
	//首投人数
	private String firstinvestCou;
	
	public void setFirstinvestCou(String firstinvestCou) {
		this.firstinvestCou = firstinvestCou;
	}
	
	public String getFirstinvestCou() {
		return firstinvestCou;
	}
	
	public void setFirstinvestyearamount(String firstinvestyearamount) {
		this.firstinvestyearamount = firstinvestyearamount;
	}
	
	public String getFirstinvestyearamount() {
		return firstinvestyearamount;
	}
	
	public void setYearamount(String yearamount) {
		this.yearamount = yearamount;
	}
	
	public String getYearamount() {
		return yearamount;
	}
	
	public void setCountUser(String countUser) {
		this.countUser = countUser;
	}
	
	public String getCountUser() {
		return countUser;
	}
	
	public void setChannelCost(String channelCost) {
		this.channelCost = channelCost;
	}
	
	public String getChannelCost() {
		return channelCost;
	}
	
	public void setChannelRecharge(String channelRecharge) {
		this.channelRecharge = channelRecharge;
	}
	
	public String getChannelRecharge() {
		return channelRecharge;
	}

	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	public String getChannelLabel() {
		return channelLabel;
	}

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

	public String getFirstinvestMoney() {
		return firstinvestMoney;
	}

	public void setFirstinvestMoney(String firstinvestMoney) {
		this.firstinvestMoney = firstinvestMoney;
	}

	public String getInvCou() {
		return countUser;
	}

	public void setInvCou(String invCou) {
		this.countUser = invCou;
	}

	public String getInvMoney() {
		return invMoney;
	}

	public void setInvMoney(String invMoney) {
		this.invMoney = invMoney;
	}

	@Override
	public String toString() {
		return "MarketChannelEntity [statPeriod=" + statPeriod + ", channelHead=" + channelHead + ", type=" + type
				+ ", channelName=" + channelName + ", channelLabel=" + channelLabel + ", ctime=" + ctime
				+ ", actualCost=" + actualCost + ", regCou=" + regCou + ", firstinvestMoney=" + firstinvestMoney
				+ ", firstinvestyearamount=" + firstinvestyearamount + ", countUser=" + countUser + ", invMoney="
				+ invMoney + ", yearamount=" + yearamount + ", channelCost=" + channelCost + ", channelRecharge="
				+ channelRecharge + "]";
	}
	

}
