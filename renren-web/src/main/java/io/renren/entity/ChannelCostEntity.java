package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
public class ChannelCostEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计时间
	private String statPeriod;
	//渠道ID
	private String channelId;
	//负责人
	private String channelHead;
	//类型
	private String type;
	//渠道名称
	private String channelName;
	//渠道标签
	private String channelLabel;
	//注册人数
	private String regCou;
	//首投人数
	private String firstinvestCou;
	//首投金额
	private String firstinvestMoney;
	//首投年化
	private String firstinvestyearamount;
	//累计投资人数
	private String countUser;
	//累计投资金额
	private String invMoney;
	//待收金额
	private String actualCost;
	//累计年化率
	private String yearamount;
	//点点赚投资金额
	private String ddzAmount;
	//注册成本
	private String regCost;
	//首投成本
	private String firstCost;
	//首投ROI
	private String firstROI;
	//累投ROI
	private String numROI;

	private String registerUserNum;
	private String investUserNum;
	private String firstInvestRate;
	private String totalInvestAmount;
	private String firstInvestAmount;
	private String changeInvestAmount;
	private String multiInvestUserNum;
	private String multiInvestAmount;
	private String multiInvestRate;

	public String getRegisterUserNum() {
		return registerUserNum;
	}

	public void setRegisterUserNum(String registerUserNum) {
		this.registerUserNum = registerUserNum;
	}

	public String getInvestUserNum() {
		return investUserNum;
	}

	public void setInvestUserNum(String investUserNum) {
		this.investUserNum = investUserNum;
	}

	public String getFirstInvestRate() {
		return firstInvestRate;
	}

	public void setFirstInvestRate(String firstInvestRate) {
		this.firstInvestRate = firstInvestRate;
	}

	public String getTotalInvestAmount() {
		return totalInvestAmount;
	}

	public void setTotalInvestAmount(String totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}

	public String getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setFirstInvestAmount(String firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public String getChangeInvestAmount() {
		return changeInvestAmount;
	}

	public void setChangeInvestAmount(String changeInvestAmount) {
		this.changeInvestAmount = changeInvestAmount;
	}

	public String getMultiInvestUserNum() {
		return multiInvestUserNum;
	}

	public void setMultiInvestUserNum(String multiInvestUserNum) {
		this.multiInvestUserNum = multiInvestUserNum;
	}

	public String getMultiInvestAmount() {
		return multiInvestAmount;
	}

	public void setMultiInvestAmount(String multiInvestAmount) {
		this.multiInvestAmount = multiInvestAmount;
	}

	public String getMultiInvestRate() {
		return multiInvestRate;
	}

	public void setMultiInvestRate(String multiInvestRate) {
		this.multiInvestRate = multiInvestRate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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

	public String getFirstinvestyearamount() {
		return firstinvestyearamount;
	}

	public void setFirstinvestyearamount(String firstinvestyearamount) {
		this.firstinvestyearamount = firstinvestyearamount;
	}

	public String getCountUser() {
		return countUser;
	}

	public void setCountUser(String countUser) {
		this.countUser = countUser;
	}

	public String getInvMoney() {
		return invMoney;
	}

	public void setInvMoney(String invMoney) {
		this.invMoney = invMoney;
	}

	public String getActualCost() {
		return actualCost;
	}

	public void setActualCost(String actualCost) {
		this.actualCost = actualCost;
	}

	public String getYearamount() {
		return yearamount;
	}

	public void setYearamount(String yearamount) {
		this.yearamount = yearamount;
	}

	public String getDdzAmount() {
		return ddzAmount;
	}

	public void setDdzAmount(String ddzAmount) {
		this.ddzAmount = ddzAmount;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getRegCost() {
		return regCost;
	}

	public void setRegCost(String regCost) {
		this.regCost = regCost;
	}

	public String getFirstCost() {
		return firstCost;
	}

	public void setFirstCost(String firstCost) {
		this.firstCost = firstCost;
	}

	public String getFirstROI() {
		return firstROI;
	}

	public void setFirstROI(String firstROI) {
		this.firstROI = firstROI;
	}

	public String getNumROI() {
		return numROI;
	}

	public void setNumROI(String numROI) {
		this.numROI = numROI;
	}

	/**
	 * 设置：统计时间
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：统计时间
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：渠道ID
	 */

	/**
	 * 设置：负责人
	 */
	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
	}
	/**
	 * 获取：负责人
	 */
	public String getChannelHead() {
		return channelHead;
	}
	/**
	 * 设置：类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型
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
	 * 设置：渠道标签
	 */
	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}
	/**
	 * 获取：渠道标签
	 */
	public String getChannelLabel() {
		return channelLabel;
	}
	/**
	 * 设置：注册人数
	 */

}
