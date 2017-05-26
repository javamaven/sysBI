package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 员工拉新统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-26 09:27:51
 */
public class InsideLxEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计时间
	private String statPeriod;
	//员工姓名
	private String lxName;
	//部门
	private String laxDep;
	//在职月份
	private Integer lxZmonth;
	//当季度有效拉新人数
	private Integer lxUserCou;
	//当季度拉新目标人数
	private Integer lxUserTg;
	//当季度有效拉新人数排名
	private Integer lxUserPw;
	//当季度有效拉新人数指标达成率
	private BigDecimal reach;
	//满足日均待收金额的人数
	private BigDecimal lxDs;
	//季度度新增积分值
	private BigDecimal jf;
	//季度度新增积分值排名
	private BigDecimal jfpw;


	public static long getSerialVersionUID() {
		return serialVersionUID;
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
	 * 设置：员工姓名
	 */
	public void setLxName(String lxName) {
		this.lxName = lxName;
	}
	/**
	 * 获取：员工姓名
	 */
	public String getLxName() {
		return lxName;
	}
	/**
	 * 设置：部门
	 */
	public void setLaxDep(String laxDep) {
		this.laxDep = laxDep;
	}
	/**
	 * 获取：部门
	 */
	public String getLaxDep() {
		return laxDep;
	}
	/**
	 * 设置：在职月份
	 */
	public void setLxZmonth(Integer lxZmonth) {
		this.lxZmonth = lxZmonth;
	}
	/**
	 * 获取：在职月份
	 */
	public Integer getLxZmonth() {
		return lxZmonth;
	}
	/**
	 * 设置：当季度有效拉新人数
	 */
	public void setLxUserCou(Integer lxUserCou) {
		this.lxUserCou = lxUserCou;
	}
	/**
	 * 获取：当季度有效拉新人数
	 */
	public Integer getLxUserCou() {
		return lxUserCou;
	}
	/**
	 * 设置：当季度拉新目标人数
	 */
	public void setLxUserTg(Integer lxUserTg) {
		this.lxUserTg = lxUserTg;
	}
	/**
	 * 获取：当季度拉新目标人数
	 */
	public Integer getLxUserTg() {
		return lxUserTg;
	}
	/**
	 * 设置：当季度有效拉新人数排名
	 */
	public void setLxUserPw(Integer lxUserPw) {
		this.lxUserPw = lxUserPw;
	}
	/**
	 * 获取：当季度有效拉新人数排名
	 */
	public Integer getLxUserPw() {
		return lxUserPw;
	}
	/**
	 * 设置：当季度有效拉新人数指标达成率
	 */
	public void setReach(BigDecimal reach) {
		this.reach = reach;
	}
	/**
	 * 获取：当季度有效拉新人数指标达成率
	 */
	public BigDecimal getReach() {
		return reach;
	}
	/**
	 * 设置：满足日均待收金额的人数
	 */
	public void setLxDs(BigDecimal lxDs) {
		this.lxDs = lxDs;
	}
	/**
	 * 获取：满足日均待收金额的人数
	 */
	public BigDecimal getLxDs() {
		return lxDs;
	}
	/**
	 * 设置：季度度新增积分值
	 */
	public void setJf(BigDecimal jf) {
		this.jf = jf;
	}
	/**
	 * 获取：季度度新增积分值
	 */
	public BigDecimal getJf() {
		return jf;
	}
	/**
	 * 设置：季度度新增积分值排名
	 */
	public void setJfpw(BigDecimal jfpw) {
		this.jfpw = jfpw;
	}
	/**
	 * 获取：季度度新增积分值排名
	 */
	public BigDecimal getJfpw() {
		return jfpw;
	}
}
