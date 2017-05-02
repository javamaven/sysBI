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
public class DailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String  statPeriod;//日期
	private String indicatorsName;//指标名字
	private String indicatorsValue;//指标值
	private String sequential;//环比
	private String compared;//同比
	private String monthMeanValue;//30天均值
	private String monthMeanValueThan;//30天均值比

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getStatPeriod() {
		return statPeriod;
	}

	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}

	public String getIndicatorsName() {
		return indicatorsName;
	}

	public void setIndicatorsName(String indicatorsName) {
		this.indicatorsName = indicatorsName;
	}

	public String getIndicatorsValue() {
		return indicatorsValue;
	}

	public void setIndicatorsValue(String indicatorsValue) {
		this.indicatorsValue = indicatorsValue;
	}

	public String getSequential() {
		return sequential;
	}

	public void setSequential(String sequential) {
		this.sequential = sequential;
	}

	public String getCompared() {
		return compared;
	}

	public void setCompared(String compared) {
		this.compared = compared;
	}

	public String getMonthMeanValue() {
		return monthMeanValue;
	}

	public void setMonthMeanValue(String monthMeanValue) {
		this.monthMeanValue = monthMeanValue;
	}

	public String getMonthMeanValueThan() {
		return monthMeanValueThan;
	}

	public void setMonthMeanValueThan(String monthMeanValueThan) {
		this.monthMeanValueThan = monthMeanValueThan;
	}

	@Override
	public String toString() {
		return "DailyEntity{" +
				"indicatorsName='" + indicatorsName + '\'' +
				", indicatorsValue='" + indicatorsValue + '\'' +
				", sequential='" + sequential + '\'' +
				", compared='" + compared + '\'' +
				", monthMeanValue='" + monthMeanValue + '\'' +
				", monthMeanValueThan='" + monthMeanValueThan + '\'' +
				'}';
	}
}
