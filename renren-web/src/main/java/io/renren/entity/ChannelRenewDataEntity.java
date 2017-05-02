package io.renren.entity;

import java.io.Serializable;

/**
 * 渠道续费数据提取汇总
 * 
 * @author liaodehui
 *
 */
public class ChannelRenewDataEntity implements Serializable {

	private static final long serialVersionUID = 666567400919208674L;
	private String channelType;
	private String channelName;
	private String channelLabel;

	private double day30Cost;// 30天费用
	private double day60Cost;// 60天费用
	private double day90Cost;// 90天费用

	private String onlineTime;// 上线时间

	private double yearAmount;// 年化金额
	private double amount;// 投资金额
	private double day30YearAmount;// 30日年化投资金额
	private double day60YearAmount;// 60日年化投资金额
	private double day90YearAmount;// 90日年化投资金额
	private double day30Amount;// 30日投资金额
	private double day60Amount;// 60日投资金额
	private double day90Amount;// 90日投资金额

	private double day30YearRoi;// 30日年化ROI
	private double day60YearRoi;// 60日年化ROI
	private double day90YearRoi;// 90日年化ROI

	private double firstInvestUserNum;
	private double firstInvestAmount;
	private double day30FirstInvestAmount;
	private double day60FirstInvestAmount;
	private double day90FirstInvestAmount;
	private double day30MultiInvestAmount;
	private double day60MultiInvestAmount;
	private double day90MultiInvestAmount;
	private double day30FirstInvestUserNum;// 30日首投用户数
	private double day60FirstInvestUserNum;// 60日首投用户数
	private double day90FirstInvestUserNum;// 90日首投用户数

	private double day30MultiInvestUserNum;// 30日复投用户数
	private double day60MultiInvestUserNum;// 60日复投用户数
	private double day90MultiInvestUserNum;// 90日复投用户数

	private double day30MultiRate;// 30日复投率
	private double day60MultiRate;// 60日复投率
	private double day90MultiRate;// 90日复投率
	private String day30MultiRateText;// 30日复投率
	private String day60MultiRateText;// 60日复投率
	private String day90MultiRateText;// 90日复投率

	private double day30MultiInvestAmountRate;// 30日复投金额比
	private double day60MultiInvestAmountRate;// 60日复投金额比
	private double day90MultiInvestAmountRate;// 90日复投金额比
	private String day30MultiInvestAmountRateText;// 30日复投金额比
	private String day60MultiInvestAmountRateText;// 60日复投金额比
	private String day90MultiInvestAmountRateText;// 90日复投金额比

	private double firstInvestYearAmount;
	private double day30FirstInvestYearAmount;// 30日首投年化金额
	private double day60FirstInvestYearAmount;// 60日首投年化金额
	private double day90FirstInvestYearAmount;// 90日首投年化金额

	private double day30perFirstInvestYearAmount;// 30日人均首投年化金额
	private double day60perFirstInvestYearAmount;// 60日人均首投年化金额
	private double day90perFirstInvestYearAmount;// 90日人均首投年化金额

	private double firstInvestYearRoi;//
	private double day30FirstInvestYearRoi;// 30日首投年化ROI
	private double day60FirstInvestYearRoi;// 60日首投年化ROI
	private double day90FirstInvestYearRoi;// 90日首投年化ROI

	public ChannelRenewDataEntity() {

	}

	public void setFirstInvestAmount(double firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public double getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setDay30MultiInvestAmount(double day30MultiInvestAmount) {
		this.day30MultiInvestAmount = day30MultiInvestAmount;
	}

	public void setDay60MultiInvestAmount(double day60MultiInvestAmount) {
		this.day60MultiInvestAmount = day60MultiInvestAmount;
	}

	public void setDay90MultiInvestAmount(double day90MultiInvestAmount) {
		this.day90MultiInvestAmount = day90MultiInvestAmount;
	}

	public double getDay30MultiInvestAmount() {
		return day30MultiInvestAmount;
	}

	public double getDay60MultiInvestAmount() {
		return day60MultiInvestAmount;
	}

	public double getDay90MultiInvestAmount() {
		return day90MultiInvestAmount;
	}

	public ChannelRenewDataEntity(String channelType, String channelName, String channelLabel, double day30Cost,
			double day60Cost, double day90Cost, String onlineTime, double yearAmount, double day30YearAmount,
			double day60YearAmount, double day90YearAmount, double day30YearRoi, double day60YearRoi,
			double day90YearRoi, double firstInvestUserNum, double day30FirstInvestUserNum,
			double day60FirstInvestUserNum, double day90FirstInvestUserNum, double day30MultiInvestUserNum,
			double day60MultiInvestUserNum, double day90MultiInvestUserNum, double day30MultiRate,
			double day60MultiRate, double day90MultiRate, String day30MultiRateText, String day60MultiRateText,
			String day90MultiRateText, double day30MultiInvestAmountRate, double day60MultiInvestAmountRate,
			double day90MultiInvestAmountRate, String day30MultiInvestAmountRateText,
			String day60MultiInvestAmountRateText, String day90MultiInvestAmountRateText, double firstInvestYearAmount,
			double day30FirstInvestYearAmount, double day60FirstInvestYearAmount, double day90FirstInvestYearAmount,
			double day30perFirstInvestYearAmount, double day60perFirstInvestYearAmount,
			double day90perFirstInvestYearAmount, double firstInvestYearRoi, double day30FirstInvestYearRoi,
			double day60FirstInvestYearRoi, double day90FirstInvestYearRoi) {
		super();
		this.channelType = channelType;
		this.channelName = channelName;
		this.channelLabel = channelLabel;
		this.day30Cost = day30Cost;
		this.day60Cost = day60Cost;
		this.day90Cost = day90Cost;
		this.onlineTime = onlineTime;
		this.yearAmount = yearAmount;
		this.day30YearAmount = day30YearAmount;
		this.day60YearAmount = day60YearAmount;
		this.day90YearAmount = day90YearAmount;
		this.day30YearRoi = day30YearRoi;
		this.day60YearRoi = day60YearRoi;
		this.day90YearRoi = day90YearRoi;
		this.firstInvestUserNum = firstInvestUserNum;
		this.day30FirstInvestUserNum = day30FirstInvestUserNum;
		this.day60FirstInvestUserNum = day60FirstInvestUserNum;
		this.day90FirstInvestUserNum = day90FirstInvestUserNum;
		this.day30MultiInvestUserNum = day30MultiInvestUserNum;
		this.day60MultiInvestUserNum = day60MultiInvestUserNum;
		this.day90MultiInvestUserNum = day90MultiInvestUserNum;
		this.day30MultiRate = day30MultiRate;
		this.day60MultiRate = day60MultiRate;
		this.day90MultiRate = day90MultiRate;
		this.day30MultiRateText = day30MultiRateText;
		this.day60MultiRateText = day60MultiRateText;
		this.day90MultiRateText = day90MultiRateText;
		this.day30MultiInvestAmountRate = day30MultiInvestAmountRate;
		this.day60MultiInvestAmountRate = day60MultiInvestAmountRate;
		this.day90MultiInvestAmountRate = day90MultiInvestAmountRate;
		this.day30MultiInvestAmountRateText = day30MultiInvestAmountRateText;
		this.day60MultiInvestAmountRateText = day60MultiInvestAmountRateText;
		this.day90MultiInvestAmountRateText = day90MultiInvestAmountRateText;
		this.firstInvestYearAmount = firstInvestYearAmount;
		this.day30FirstInvestYearAmount = day30FirstInvestYearAmount;
		this.day60FirstInvestYearAmount = day60FirstInvestYearAmount;
		this.day90FirstInvestYearAmount = day90FirstInvestYearAmount;
		this.day30perFirstInvestYearAmount = day30perFirstInvestYearAmount;
		this.day60perFirstInvestYearAmount = day60perFirstInvestYearAmount;
		this.day90perFirstInvestYearAmount = day90perFirstInvestYearAmount;
		this.firstInvestYearRoi = firstInvestYearRoi;
		this.day30FirstInvestYearRoi = day30FirstInvestYearRoi;
		this.day60FirstInvestYearRoi = day60FirstInvestYearRoi;
		this.day90FirstInvestYearRoi = day90FirstInvestYearRoi;
	}

	public void setDay30FirstInvestAmount(double day30FirstInvestAmount) {
		this.day30FirstInvestAmount = day30FirstInvestAmount;
	}

	public void setDay60FirstInvestAmount(double day60FirstInvestAmount) {
		this.day60FirstInvestAmount = day60FirstInvestAmount;
	}

	public void setDay90FirstInvestAmount(double day90FirstInvestAmount) {
		this.day90FirstInvestAmount = day90FirstInvestAmount;
	}

	public double getDay30FirstInvestAmount() {
		return day30FirstInvestAmount;
	}

	public double getDay60FirstInvestAmount() {
		return day60FirstInvestAmount;
	}

	public double getDay90FirstInvestAmount() {
		return day90FirstInvestAmount;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
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

	public double getDay30Cost() {
		return day30Cost;
	}

	public void setDay30Cost(double day30Cost) {
		this.day30Cost = day30Cost;
	}

	public double getDay60Cost() {
		return day60Cost;
	}

	public void setDay60Cost(double day60Cost) {
		this.day60Cost = day60Cost;
	}

	public double getDay90Cost() {
		return day90Cost;
	}

	public void setDay90Cost(double day90Cost) {
		this.day90Cost = day90Cost;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setDay30Amount(double day30Amount) {
		this.day30Amount = day30Amount;
	}

	public void setDay90Amount(double day90Amount) {
		this.day90Amount = day90Amount;
	}

	public void setDay60Amount(double day60Amount) {
		this.day60Amount = day60Amount;
	}

	public double getAmount() {
		return amount;
	}

	public double getDay30Amount() {
		return day30Amount;
	}

	public double getDay60Amount() {
		return day60Amount;
	}

	public double getDay90Amount() {
		return day90Amount;
	}

	public double getYearAmount() {
		return yearAmount;
	}

	public void setYearAmount(double yearAmount) {
		this.yearAmount = yearAmount;
	}

	public double getDay30YearAmount() {
		return day30YearAmount;
	}

	public void setDay30YearAmount(double day30YearAmount) {
		this.day30YearAmount = day30YearAmount;
	}

	public double getDay60YearAmount() {
		return day60YearAmount;
	}

	public void setDay60YearAmount(double day60YearAmount) {
		this.day60YearAmount = day60YearAmount;
	}

	public double getDay90YearAmount() {
		return day90YearAmount;
	}

	public void setDay90YearAmount(double day90YearAmount) {
		this.day90YearAmount = day90YearAmount;
	}

	public double getDay30YearRoi() {
		return day30YearRoi;
	}

	public void setDay30YearRoi(double day30YearRoi) {
		this.day30YearRoi = day30YearRoi;
	}

	public double getDay60YearRoi() {
		return day60YearRoi;
	}

	public void setDay60YearRoi(double day60YearRoi) {
		this.day60YearRoi = day60YearRoi;
	}

	public double getDay90YearRoi() {
		return day90YearRoi;
	}

	public void setDay90YearRoi(double day90YearRoi) {
		this.day90YearRoi = day90YearRoi;
	}

	public double getFirstInvestUserNum() {
		return firstInvestUserNum;
	}

	public void setFirstInvestUserNum(double firstInvestUserNum) {
		this.firstInvestUserNum = firstInvestUserNum;
	}

	public double getDay30FirstInvestUserNum() {
		return day30FirstInvestUserNum;
	}

	public void setDay30FirstInvestUserNum(double day30FirstInvestUserNum) {
		this.day30FirstInvestUserNum = day30FirstInvestUserNum;
	}

	public double getDay60FirstInvestUserNum() {
		return day60FirstInvestUserNum;
	}

	public void setDay60FirstInvestUserNum(double day60FirstInvestUserNum) {
		this.day60FirstInvestUserNum = day60FirstInvestUserNum;
	}

	public double getDay90FirstInvestUserNum() {
		return day90FirstInvestUserNum;
	}

	public void setDay90FirstInvestUserNum(double day90FirstInvestUserNum) {
		this.day90FirstInvestUserNum = day90FirstInvestUserNum;
	}

	public double getDay30MultiInvestUserNum() {
		return day30MultiInvestUserNum;
	}

	public void setDay30MultiInvestUserNum(double day30MultiInvestUserNum) {
		this.day30MultiInvestUserNum = day30MultiInvestUserNum;
	}

	public double getDay60MultiInvestUserNum() {
		return day60MultiInvestUserNum;
	}

	public void setDay60MultiInvestUserNum(double day60MultiInvestUserNum) {
		this.day60MultiInvestUserNum = day60MultiInvestUserNum;
	}

	public double getDay90MultiInvestUserNum() {
		return day90MultiInvestUserNum;
	}

	public void setDay90MultiInvestUserNum(double day90MultiInvestUserNum) {
		this.day90MultiInvestUserNum = day90MultiInvestUserNum;
	}

	public double getDay30MultiRate() {
		return day30MultiRate;
	}

	public void setDay30MultiRate(double day30MultiRate) {
		this.day30MultiRate = day30MultiRate;
	}

	public double getDay60MultiRate() {
		return day60MultiRate;
	}

	public void setDay60MultiRate(double day60MultiRate) {
		this.day60MultiRate = day60MultiRate;
	}

	public double getDay90MultiRate() {
		return day90MultiRate;
	}

	public void setDay90MultiRate(double day90MultiRate) {
		this.day90MultiRate = day90MultiRate;
	}

	public String getDay30MultiRateText() {
		return day30MultiRateText;
	}

	public void setDay30MultiRateText(String day30MultiRateText) {
		this.day30MultiRateText = day30MultiRateText;
	}

	public String getDay60MultiRateText() {
		return day60MultiRateText;
	}

	public void setDay60MultiRateText(String day60MultiRateText) {
		this.day60MultiRateText = day60MultiRateText;
	}

	public String getDay90MultiRateText() {
		return day90MultiRateText;
	}

	public void setDay90MultiRateText(String day90MultiRateText) {
		this.day90MultiRateText = day90MultiRateText;
	}

	public double getDay30MultiInvestAmountRate() {
		return day30MultiInvestAmountRate;
	}

	public void setDay30MultiInvestAmountRate(double day30MultiInvestAmountRate) {
		this.day30MultiInvestAmountRate = day30MultiInvestAmountRate;
	}

	public double getDay60MultiInvestAmountRate() {
		return day60MultiInvestAmountRate;
	}

	public void setDay60MultiInvestAmountRate(double day60MultiInvestAmountRate) {
		this.day60MultiInvestAmountRate = day60MultiInvestAmountRate;
	}

	public double getDay90MultiInvestAmountRate() {
		return day90MultiInvestAmountRate;
	}

	public void setDay90MultiInvestAmountRate(double day90MultiInvestAmountRate) {
		this.day90MultiInvestAmountRate = day90MultiInvestAmountRate;
	}

	public String getDay30MultiInvestAmountRateText() {
		return day30MultiInvestAmountRateText;
	}

	public void setDay30MultiInvestAmountRateText(String day30MultiInvestAmountRateText) {
		this.day30MultiInvestAmountRateText = day30MultiInvestAmountRateText;
	}

	public String getDay60MultiInvestAmountRateText() {
		return day60MultiInvestAmountRateText;
	}

	public void setDay60MultiInvestAmountRateText(String day60MultiInvestAmountRateText) {
		this.day60MultiInvestAmountRateText = day60MultiInvestAmountRateText;
	}

	public String getDay90MultiInvestAmountRateText() {
		return day90MultiInvestAmountRateText;
	}

	public void setDay90MultiInvestAmountRateText(String day90MultiInvestAmountRateText) {
		this.day90MultiInvestAmountRateText = day90MultiInvestAmountRateText;
	}

	public double getFirstInvestYearAmount() {
		return firstInvestYearAmount;
	}

	public void setFirstInvestYearAmount(double firstInvestYearAmount) {
		this.firstInvestYearAmount = firstInvestYearAmount;
	}

	public double getDay30FirstInvestYearAmount() {
		return day30FirstInvestYearAmount;
	}

	public void setDay30FirstInvestYearAmount(double day30FirstInvestYearAmount) {
		this.day30FirstInvestYearAmount = day30FirstInvestYearAmount;
	}

	public double getDay60FirstInvestYearAmount() {
		return day60FirstInvestYearAmount;
	}

	public void setDay60FirstInvestYearAmount(double day60FirstInvestYearAmount) {
		this.day60FirstInvestYearAmount = day60FirstInvestYearAmount;
	}

	public double getDay90FirstInvestYearAmount() {
		return day90FirstInvestYearAmount;
	}

	public void setDay90FirstInvestYearAmount(double day90FirstInvestYearAmount) {
		this.day90FirstInvestYearAmount = day90FirstInvestYearAmount;
	}

	public double getDay30perFirstInvestYearAmount() {
		return day30perFirstInvestYearAmount;
	}

	public void setDay30perFirstInvestYearAmount(double day30perFirstInvestYearAmount) {
		this.day30perFirstInvestYearAmount = day30perFirstInvestYearAmount;
	}

	public double getDay60perFirstInvestYearAmount() {
		return day60perFirstInvestYearAmount;
	}

	public void setDay60perFirstInvestYearAmount(double day60perFirstInvestYearAmount) {
		this.day60perFirstInvestYearAmount = day60perFirstInvestYearAmount;
	}

	public double getDay90perFirstInvestYearAmount() {
		return day90perFirstInvestYearAmount;
	}

	public void setDay90perFirstInvestYearAmount(double day90perFirstInvestYearAmount) {
		this.day90perFirstInvestYearAmount = day90perFirstInvestYearAmount;
	}

	public double getFirstInvestYearRoi() {
		return firstInvestYearRoi;
	}

	public void setFirstInvestYearRoi(double firstInvestYearRoi) {
		this.firstInvestYearRoi = firstInvestYearRoi;
	}

	public double getDay30FirstInvestYearRoi() {
		return day30FirstInvestYearRoi;
	}

	public void setDay30FirstInvestYearRoi(double day30FirstInvestYearRoi) {
		this.day30FirstInvestYearRoi = day30FirstInvestYearRoi;
	}

	public double getDay60FirstInvestYearRoi() {
		return day60FirstInvestYearRoi;
	}

	public void setDay60FirstInvestYearRoi(double day60FirstInvestYearRoi) {
		this.day60FirstInvestYearRoi = day60FirstInvestYearRoi;
	}

	public double getDay90FirstInvestYearRoi() {
		return day90FirstInvestYearRoi;
	}

	public void setDay90FirstInvestYearRoi(double day90FirstInvestYearRoi) {
		this.day90FirstInvestYearRoi = day90FirstInvestYearRoi;
	}

	@Override
	public String toString() {
		return "ChannelRenewDataEntity [channelType=" + channelType + ", channelName=" + channelName + ", channelLabel="
				+ channelLabel + ", day30Cost=" + day30Cost + ", day60Cost=" + day60Cost + ", day90Cost=" + day90Cost
				+ ", onlineTime=" + onlineTime + ", yearAmount=" + yearAmount + ", day30YearAmount=" + day30YearAmount
				+ ", day60YearAmount=" + day60YearAmount + ", day90YearAmount=" + day90YearAmount + ", day30YearRoi="
				+ day30YearRoi + ", day60YearRoi=" + day60YearRoi + ", day90YearRoi=" + day90YearRoi
				+ ", firstInvestUserNum=" + firstInvestUserNum + ", day30FirstInvestUserNum=" + day30FirstInvestUserNum
				+ ", day60FirstInvestUserNum=" + day60FirstInvestUserNum + ", day90FirstInvestUserNum="
				+ day90FirstInvestUserNum + ", day30MultiInvestUserNum=" + day30MultiInvestUserNum
				+ ", day60MultiInvestUserNum=" + day60MultiInvestUserNum + ", day90MultiInvestUserNum="
				+ day90MultiInvestUserNum + ", day30MultiRate=" + day30MultiRate + ", day60MultiRate=" + day60MultiRate
				+ ", day90MultiRate=" + day90MultiRate + ", day30MultiRateText=" + day30MultiRateText
				+ ", day60MultiRateText=" + day60MultiRateText + ", day90MultiRateText=" + day90MultiRateText
				+ ", day30MultiInvestAmountRate=" + day30MultiInvestAmountRate + ", day60MultiInvestAmountRate="
				+ day60MultiInvestAmountRate + ", day90MultiInvestAmountRate=" + day90MultiInvestAmountRate
				+ ", day30MultiInvestAmountRateText=" + day30MultiInvestAmountRateText
				+ ", day60MultiInvestAmountRateText=" + day60MultiInvestAmountRateText
				+ ", day90MultiInvestAmountRateText=" + day90MultiInvestAmountRateText + ", firstInvestYearAmount="
				+ firstInvestYearAmount + ", day30FirstInvestYearAmount=" + day30FirstInvestYearAmount
				+ ", day60FirstInvestYearAmount=" + day60FirstInvestYearAmount + ", day90FirstInvestYearAmount="
				+ day90FirstInvestYearAmount + ", day30perFirstInvestYearAmount=" + day30perFirstInvestYearAmount
				+ ", day60perFirstInvestYearAmount=" + day60perFirstInvestYearAmount
				+ ", day90perFirstInvestYearAmount=" + day90perFirstInvestYearAmount + ", firstInvestYearRoi="
				+ firstInvestYearRoi + ", day30FirstInvestYearRoi=" + day30FirstInvestYearRoi
				+ ", day60FirstInvestYearRoi=" + day60FirstInvestYearRoi + ", day90FirstInvestYearRoi="
				+ day90FirstInvestYearRoi + "]";
	}

}
