package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 09:27:12
 */
public class DmReportActiveChannelCostEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//日期
	private String statPeriod;
	//渠道标签
	private String code;
	//渠道名称
	private String name;
	//推广成本
	private BigDecimal cost;
	//成本归属部门
	private String costSource;

	/**
	 * 设置：日期
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：日期
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：渠道标签
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：渠道标签
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：渠道名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：渠道名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：推广成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * 获取：推广成本
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * 设置：成本归属部门
	 */
	public void setCostSource(String costSource) {
		this.costSource = costSource;
	}
	/**
	 * 获取：成本归属部门
	 */
	public String getCostSource() {
		return costSource;
	}
}
