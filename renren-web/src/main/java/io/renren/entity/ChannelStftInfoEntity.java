package io.renren.entity;

import java.io.Serializable;

/**
 * 渠道首投复情况
 * 
 * @author liaodehui
 *
 */
public class ChannelStftInfoEntity implements Serializable {

	private static final long serialVersionUID = 7596917974990705382L;

	private double channelId;
	private String channelName;
	private String channelLabel;
	private String channelType;// 渠道分类
	private int registerUserNum;// 注册人数
	private int firstInvestUserNum;// 首投人数
	private double firstInvestAmount;// 首投金额
	private double firstInvestYearAmount;// 首投年化投资金额
	private double firstInvestPer;// 人均首投=首投金额/首投人数
	private double conversionRate;// 转化率=首投人数/注册人数
	private String conversionRateText;// 转化率 格式化，0.986 ==> 98.6%
	private int multipleUser;// 复投人数
	private double multipleRate;// 复投率
	private String multipleRateText;// 复投率 格式化，0.986 ==> 98.6%
	private double firstInvestProAmount;// 首投用户项目投资金额
	private double proInvestAmount;// 项目投资金额
	private int proInvestUser;// 项目投资人数
	private double proMultiInvestAmount;// 项目复投金额
	private int proMultiInvestUser;// 项目复投人数
	private double firstInvestUserAmount;// 首投用户投资金额
	private double userInvestAmount;// 用户投资金额
	private int userInvestNum;// 用户投资人数
	private double userInvestYearAmount;// 用户年化投资金额
	private double firstInvestPerTime;// 首投平均期限
	private double firstInvestUserPerProAmount;// 首投用户平均项目投资金额
	private double perProInvestAmont;// 平均项目投资金额
	private double perProMultiInvestAmount;// 平均项目复投金额
	private double firstInvestUserPerAmount;// 首投用户平均投资金额
	private double perInvestAmount;// 平均投资金额
	private double amountMultiRate;// 金额复投率
	private String amountMultiRateText;// 金额复投率

	public ChannelStftInfoEntity() {

	}

	public ChannelStftInfoEntity(double channelId, String channelName, String channelLabel, String channelType,
			int registerUserNum, int firstInvestUserNum, double firstInvestAmount, double firstInvestYearAmount,
			double firstInvestPer, double conversionRate, String conversionRateText, int multipleUser,
			double multipleRate, double firstInvestProAmount, double proInvestAmount, int proInvestUser,
			double proMultiInvestAmount, int proMultiInvestUser, double firstInvestUserAmount, double userInvestAmount,
			int userInvestNum, double userInvestYearAmount, double firstInvestPerTime,
			double firstInvestUserPerProAmount, double perProInvestAmont, double perProMultiInvestAmount,
			double firstInvestUserPerAmount, double perInvestAmount, double amountMultiRate) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.channelLabel = channelLabel;
		this.channelType = channelType;
		this.registerUserNum = registerUserNum;
		this.firstInvestUserNum = firstInvestUserNum;
		this.firstInvestAmount = firstInvestAmount;
		this.firstInvestYearAmount = firstInvestYearAmount;
		this.firstInvestPer = firstInvestPer;
		this.conversionRate = conversionRate;
		this.conversionRateText = conversionRateText;
		this.multipleUser = multipleUser;
		this.multipleRate = multipleRate;
		this.firstInvestProAmount = firstInvestProAmount;
		this.proInvestAmount = proInvestAmount;
		this.proInvestUser = proInvestUser;
		this.proMultiInvestAmount = proMultiInvestAmount;
		this.proMultiInvestUser = proMultiInvestUser;
		this.firstInvestUserAmount = firstInvestUserAmount;
		this.userInvestAmount = userInvestAmount;
		this.userInvestNum = userInvestNum;
		this.userInvestYearAmount = userInvestYearAmount;
		this.firstInvestPerTime = firstInvestPerTime;
		this.firstInvestUserPerProAmount = firstInvestUserPerProAmount;
		this.perProInvestAmont = perProInvestAmont;
		this.perProMultiInvestAmount = perProMultiInvestAmount;
		this.firstInvestUserPerAmount = firstInvestUserPerAmount;
		this.perInvestAmount = perInvestAmount;
		this.amountMultiRate = amountMultiRate;
	}

	public void setAmountMultiRateText(String amountMultiRateText) {
		this.amountMultiRateText = amountMultiRateText;
	}

	public String getAmountMultiRateText() {
		return amountMultiRateText;
	}

	public void setMultipleRateText(String multipleRateText) {
		this.multipleRateText = multipleRateText;
	}

	public String getMultipleRateText() {
		return multipleRateText;
	}

	public double getChannelId() {
		return channelId;
	}

	public void setChannelId(double channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelLabel() {
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public int getRegisterUserNum() {
		return registerUserNum;
	}

	public void setRegisterUserNum(int registerUserNum) {
		this.registerUserNum = registerUserNum;
	}

	public int getFirstInvestUserNum() {
		return firstInvestUserNum;
	}

	public void setFirstInvestUserNum(int firstInvestUserNum) {
		this.firstInvestUserNum = firstInvestUserNum;
	}

	public double getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setFirstInvestAmount(double firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public double getFirstInvestYearAmount() {
		return firstInvestYearAmount;
	}

	public void setFirstInvestYearAmount(double firstInvestYearAmount) {
		this.firstInvestYearAmount = firstInvestYearAmount;
	}

	public double getFirstInvestPer() {
		return firstInvestPer;
	}

	public void setFirstInvestPer(double firstInvestPer) {
		this.firstInvestPer = firstInvestPer;
	}

	public double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getConversionRateText() {
		return conversionRateText;
	}

	public void setConversionRateText(String conversionRateText) {
		this.conversionRateText = conversionRateText;
	}

	public int getMultipleUser() {
		return multipleUser;
	}

	public void setMultipleUser(int multipleUser) {
		this.multipleUser = multipleUser;
	}

	public double getMultipleRate() {
		return multipleRate;
	}

	public void setMultipleRate(double multipleRate) {
		this.multipleRate = multipleRate;
	}

	public double getFirstInvestProAmount() {
		return firstInvestProAmount;
	}

	public void setFirstInvestProAmount(double firstInvestProAmount) {
		this.firstInvestProAmount = firstInvestProAmount;
	}

	public double getProInvestAmount() {
		return proInvestAmount;
	}

	public void setProInvestAmount(double proInvestAmount) {
		this.proInvestAmount = proInvestAmount;
	}

	public int getProInvestUser() {
		return proInvestUser;
	}

	public void setProInvestUser(int proInvestUser) {
		this.proInvestUser = proInvestUser;
	}

	public double getProMultiInvestAmount() {
		return proMultiInvestAmount;
	}

	public void setProMultiInvestAmount(double proMultiInvestAmount) {
		this.proMultiInvestAmount = proMultiInvestAmount;
	}

	public int getProMultiInvestUser() {
		return proMultiInvestUser;
	}

	public void setProMultiInvestUser(int proMultiInvestUser) {
		this.proMultiInvestUser = proMultiInvestUser;
	}

	public double getFirstInvestUserAmount() {
		return firstInvestUserAmount;
	}

	public void setFirstInvestUserAmount(double firstInvestUserAmount) {
		this.firstInvestUserAmount = firstInvestUserAmount;
	}

	public double getUserInvestAmount() {
		return userInvestAmount;
	}

	public void setUserInvestAmount(double userInvestAmount) {
		this.userInvestAmount = userInvestAmount;
	}

	public int getUserInvestNum() {
		return userInvestNum;
	}

	public void setUserInvestNum(int userInvestNum) {
		this.userInvestNum = userInvestNum;
	}

	public double getUserInvestYearAmount() {
		return userInvestYearAmount;
	}

	public void setUserInvestYearAmount(double userInvestYearAmount) {
		this.userInvestYearAmount = userInvestYearAmount;
	}

	public double getFirstInvestPerTime() {
		return firstInvestPerTime;
	}

	public void setFirstInvestPerTime(double firstInvestPerTime) {
		this.firstInvestPerTime = firstInvestPerTime;
	}

	public double getFirstInvestUserPerProAmount() {
		return firstInvestUserPerProAmount;
	}

	public void setFirstInvestUserPerProAmount(double firstInvestUserPerProAmount) {
		this.firstInvestUserPerProAmount = firstInvestUserPerProAmount;
	}

	public double getPerProInvestAmont() {
		return perProInvestAmont;
	}

	public void setPerProInvestAmont(double perProInvestAmont) {
		this.perProInvestAmont = perProInvestAmont;
	}

	public double getPerProMultiInvestAmount() {
		return perProMultiInvestAmount;
	}

	public void setPerProMultiInvestAmount(double perProMultiInvestAmount) {
		this.perProMultiInvestAmount = perProMultiInvestAmount;
	}

	public double getFirstInvestUserPerAmount() {
		return firstInvestUserPerAmount;
	}

	public void setFirstInvestUserPerAmount(double firstInvestUserPerAmount) {
		this.firstInvestUserPerAmount = firstInvestUserPerAmount;
	}

	public double getPerInvestAmount() {
		return perInvestAmount;
	}

	public void setPerInvestAmount(double perInvestAmount) {
		this.perInvestAmount = perInvestAmount;
	}

	public double getAmountMultiRate() {
		return amountMultiRate;
	}

	public void setAmountMultiRate(double amountMultiRate) {
		this.amountMultiRate = amountMultiRate;
	}

	@Override
	public String toString() {
		return "ChannelStftInfoEntity [channelId=" + channelId + ", channelName=" + channelName + ", channelLabel="
				+ channelLabel + ", channelType=" + channelType + ", registerUserNum=" + registerUserNum
				+ ", firstInvestUserNum=" + firstInvestUserNum + ", firstInvestAmount=" + firstInvestAmount
				+ ", firstInvestYearAmount=" + firstInvestYearAmount + ", firstInvestPer=" + firstInvestPer
				+ ", conversionRate=" + conversionRate + ", conversionRateText=" + conversionRateText
				+ ", multipleUser=" + multipleUser + ", multipleRate=" + multipleRate + ", firstInvestProAmount="
				+ firstInvestProAmount + ", proInvestAmount=" + proInvestAmount + ", proInvestUser=" + proInvestUser
				+ ", proMultiInvestAmount=" + proMultiInvestAmount + ", proMultiInvestUser=" + proMultiInvestUser
				+ ", firstInvestUserAmount=" + firstInvestUserAmount + ", userInvestAmount=" + userInvestAmount
				+ ", userInvestNum=" + userInvestNum + ", userInvestYearAmount=" + userInvestYearAmount
				+ ", firstInvestPerTime=" + firstInvestPerTime + ", firstInvestUserPerProAmount="
				+ firstInvestUserPerProAmount + ", perProInvestAmont=" + perProInvestAmont
				+ ", perProMultiInvestAmount=" + perProMultiInvestAmount + ", firstInvestUserPerAmount="
				+ firstInvestUserPerAmount + ", perInvestAmount=" + perInvestAmount + ", amountMultiRate="
				+ amountMultiRate + "]";
	}

}
