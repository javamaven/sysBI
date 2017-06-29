package io.renren.entity.yunying.dayreport;

import java.io.Serializable;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 09:27:12
 */
public class MonthlyReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//日期
	private String statPeriod;
	//指标明细标签
	private String type;
	//指标
	private String nuM;
	public String getStatPeriod() {
		return statPeriod;
	}
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNuM() {
		return nuM;
	}
	public void setNuM(String nuM) {
		this.nuM = nuM;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


	
}
