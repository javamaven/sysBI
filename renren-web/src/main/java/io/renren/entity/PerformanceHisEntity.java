package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 历史绩效发放记录
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:54
 */
public class PerformanceHisEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计日期
	private String statPeriod;
	//总监
	private String developmanagername;
	//部门
	private String department;
	//本月应发绩效
	private BigDecimal expectedPerformance;
	//实发绩效
	private BigDecimal actualPerformance;

	/**
	 * 设置：统计日期
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：统计日期
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：总监
	 */
	public void setDevelopmanagername(String developmanagername) {
		this.developmanagername = developmanagername;
	}
	/**
	 * 获取：总监
	 */
	public String getDevelopmanagername() {
		return developmanagername;
	}
	/**
	 * 设置：部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：部门
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：本月应发绩效
	 */
	public void setExpectedPerformance(BigDecimal expectedPerformance) {
		this.expectedPerformance = expectedPerformance;
	}
	/**
	 * 获取：本月应发绩效
	 */
	public BigDecimal getExpectedPerformance() {
		return expectedPerformance;
	}
	/**
	 * 设置：实发绩效
	 */
	public void setActualPerformance(BigDecimal actualPerformance) {
		this.actualPerformance = actualPerformance;
	}
	/**
	 * 获取：实发绩效
	 */
	public BigDecimal getActualPerformance() {
		return actualPerformance;
	}
}
