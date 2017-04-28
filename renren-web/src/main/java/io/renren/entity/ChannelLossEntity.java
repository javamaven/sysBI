package io.renren.entity;

import java.io.Serializable;

/**
 * 渠道流失分析
 * 
 * @author liaodehui
 *
 */
public class ChannelLossEntity implements Serializable {

	private static final long serialVersionUID = 7596917974990705382L;

	private double channelId;
	private String channelName;
	private String channelLabel;
	private int registerUserNum;// 注册人数
	private int firstInvestUserNum;// 首投人数
	private int investUserNum;// 投资人数，传入次数
	private int investOneTimeUserNum;// 投资1次人数
	private int investTwoTimeUserNum;// 投资2次人数
	private int investThreeTimeUserNum;// 投资3次人数
	private int investFourTimeUserNum;// 投资4次人数
	private int investNTimeUserNum;// 投资N次人数
	private double firstInvestAmount;// 首投金额
	private double investAmount;// 累积投资金额
	private double investYearAmount;// 累计投资年化金额
	private double firstInvestUseRedMoney;// 首投使用红包金额
	private double perFirstInvestUseRedMoney;// 人均首投使用红包金额
	private double totalUseRedMoney;// 累计使用红包金额
	private int useRedMoneyUserNum;// 累计使用红包人数
	private double perTotalUseRedMoney;// 人均累计使用红包金额
	private int ddzInvestDays;// 点点赚投资天数
	private double ddzPerInvestAmount;// 点点赚平均投资金额

	public ChannelLossEntity() {

	}

	public void setUseRedMoneyUserNum(int useRedMoneyUserNum) {
		this.useRedMoneyUserNum = useRedMoneyUserNum;
	}

	public int getUseRedMoneyUserNum() {
		return useRedMoneyUserNum;
	}

	public ChannelLossEntity(double channelId, String channelName, String channelLabel, int registerUserNum,
			int firstInvestUserNum, int investUserNum, int investOneTimeUserNum, int investTwoTimeUserNum,
			int investThreeTimeUserNum, int investFourTimeUserNum, int investNTimeUserNum, double firstInvestAmount,
			double investAmount, double investYearAmount, double firstInvestUseRedMoney,
			double perFirstInvestUseRedMoney, double totalUseRedMoney, double perTotalUseRedMoney, int ddzInvestDays,
			double ddzPerInvestAmount) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.channelLabel = channelLabel;
		this.registerUserNum = registerUserNum;
		this.firstInvestUserNum = firstInvestUserNum;
		this.investUserNum = investUserNum;
		this.investOneTimeUserNum = investOneTimeUserNum;
		this.investTwoTimeUserNum = investTwoTimeUserNum;
		this.investThreeTimeUserNum = investThreeTimeUserNum;
		this.investFourTimeUserNum = investFourTimeUserNum;
		this.investNTimeUserNum = investNTimeUserNum;
		this.firstInvestAmount = firstInvestAmount;
		this.investAmount = investAmount;
		this.investYearAmount = investYearAmount;
		this.firstInvestUseRedMoney = firstInvestUseRedMoney;
		this.perFirstInvestUseRedMoney = perFirstInvestUseRedMoney;
		this.totalUseRedMoney = totalUseRedMoney;
		this.perTotalUseRedMoney = perTotalUseRedMoney;
		this.ddzInvestDays = ddzInvestDays;
		this.ddzPerInvestAmount = ddzPerInvestAmount;
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

	public int getInvestUserNum() {
		return investUserNum;
	}

	public void setInvestUserNum(int investUserNum) {
		this.investUserNum = investUserNum;
	}

	public int getInvestOneTimeUserNum() {
		return investOneTimeUserNum;
	}

	public void setInvestOneTimeUserNum(int investOneTimeUserNum) {
		this.investOneTimeUserNum = investOneTimeUserNum;
	}

	public int getInvestTwoTimeUserNum() {
		return investTwoTimeUserNum;
	}

	public void setInvestTwoTimeUserNum(int investTwoTimeUserNum) {
		this.investTwoTimeUserNum = investTwoTimeUserNum;
	}

	public int getInvestThreeTimeUserNum() {
		return investThreeTimeUserNum;
	}

	public void setInvestThreeTimeUserNum(int investThreeTimeUserNum) {
		this.investThreeTimeUserNum = investThreeTimeUserNum;
	}

	public int getInvestFourTimeUserNum() {
		return investFourTimeUserNum;
	}

	public void setInvestFourTimeUserNum(int investFourTimeUserNum) {
		this.investFourTimeUserNum = investFourTimeUserNum;
	}

	public int getInvestNTimeUserNum() {
		return investNTimeUserNum;
	}

	public void setInvestNTimeUserNum(int investNTimeUserNum) {
		this.investNTimeUserNum = investNTimeUserNum;
	}

	public double getFirstInvestAmount() {
		return firstInvestAmount;
	}

	public void setFirstInvestAmount(double firstInvestAmount) {
		this.firstInvestAmount = firstInvestAmount;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public double getInvestYearAmount() {
		return investYearAmount;
	}

	public void setInvestYearAmount(double investYearAmount) {
		this.investYearAmount = investYearAmount;
	}

	public double getFirstInvestUseRedMoney() {
		return firstInvestUseRedMoney;
	}

	public void setFirstInvestUseRedMoney(double firstInvestUseRedMoney) {
		this.firstInvestUseRedMoney = firstInvestUseRedMoney;
	}

	public double getPerFirstInvestUseRedMoney() {
		return perFirstInvestUseRedMoney;
	}

	public void setPerFirstInvestUseRedMoney(double perFirstInvestUseRedMoney) {
		this.perFirstInvestUseRedMoney = perFirstInvestUseRedMoney;
	}

	public double getTotalUseRedMoney() {
		return totalUseRedMoney;
	}

	public void setTotalUseRedMoney(double totalUseRedMoney) {
		this.totalUseRedMoney = totalUseRedMoney;
	}

	public double getPerTotalUseRedMoney() {
		return perTotalUseRedMoney;
	}

	public void setPerTotalUseRedMoney(double perTotalUseRedMoney) {
		this.perTotalUseRedMoney = perTotalUseRedMoney;
	}

	public int getDdzInvestDays() {
		return ddzInvestDays;
	}

	public void setDdzInvestDays(int ddzInvestDays) {
		this.ddzInvestDays = ddzInvestDays;
	}

	public double getDdzPerInvestAmount() {
		return ddzPerInvestAmount;
	}

	public void setDdzPerInvestAmount(double ddzPerInvestAmount) {
		this.ddzPerInvestAmount = ddzPerInvestAmount;
	}

	@Override
	public String toString() {
		return "ChannelLossEntity [channelId=" + channelId + ", channelName=" + channelName + ", channelLabel="
				+ channelLabel + ", registerUserNum=" + registerUserNum + ", firstInvestUserNum=" + firstInvestUserNum
				+ ", investUserNum=" + investUserNum + ", investOneTimeUserNum=" + investOneTimeUserNum
				+ ", investTwoTimeUserNum=" + investTwoTimeUserNum + ", investThreeTimeUserNum="
				+ investThreeTimeUserNum + ", investFourTimeUserNum=" + investFourTimeUserNum + ", investNTimeUserNum="
				+ investNTimeUserNum + ", firstInvestAmount=" + firstInvestAmount + ", investAmount=" + investAmount
				+ ", investYearAmount=" + investYearAmount + ", firstInvestUseRedMoney=" + firstInvestUseRedMoney
				+ ", perFirstInvestUseRedMoney=" + perFirstInvestUseRedMoney + ", totalUseRedMoney=" + totalUseRedMoney
				+ ", perTotalUseRedMoney=" + perTotalUseRedMoney + ", ddzInvestDays=" + ddzInvestDays
				+ ", ddzPerInvestAmount=" + ddzPerInvestAmount + "]";
	}

}
