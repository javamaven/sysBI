package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 存管报备总表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-01 14:47:27
 */
public class DepositoryTotalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//统计日期
	private String statPeriod;
	//平台项目编号
	private String sourcecaseno;
	//部门
	private String department;
	//项目归属
	private String projectBelong;
	//项目类型
	private String projectType;
	//借款人
	private String customername;
	//金额
	private BigDecimal payformoney;
	//利率
	private BigDecimal loanrate;
	//期限-月
	private BigDecimal loanyearlimit;
	//期限-日
	private BigDecimal payforlimittime;
	//满标放款日
	private String giveoutmoneytime;
	//到期日
	private String willgetmoneydate;
	//资料签名、盖章是否完全
	private String iscompleted;
	//纸质文本最迟寄送日（发标5个工作日内）
	private Date sendDeadline;
	//纸文本是否盖章
	private String isstamp;
	//寄出日
	private String sendDate;

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
	 * 设置：平台项目编号
	 */
	public void setSourcecaseno(String sourcecaseno) {
		this.sourcecaseno = sourcecaseno;
	}
	/**
	 * 获取：平台项目编号
	 */
	public String getSourcecaseno() {
		return sourcecaseno;
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
	 * 设置：项目归属
	 */
	public void setProjectBelong(String projectBelong) {
		this.projectBelong = projectBelong;
	}
	/**
	 * 获取：项目归属
	 */
	public String getProjectBelong() {
		return projectBelong;
	}
	/**
	 * 设置：项目类型
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	/**
	 * 获取：项目类型
	 */
	public String getProjectType() {
		return projectType;
	}
	/**
	 * 设置：借款人
	 */
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	/**
	 * 获取：借款人
	 */
	public String getCustomername() {
		return customername;
	}
	/**
	 * 设置：金额
	 */
	public void setPayformoney(BigDecimal payformoney) {
		this.payformoney = payformoney;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getPayformoney() {
		return payformoney;
	}
	/**
	 * 设置：利率
	 */
	public void setLoanrate(BigDecimal loanrate) {
		this.loanrate = loanrate;
	}
	/**
	 * 获取：利率
	 */
	public BigDecimal getLoanrate() {
		return loanrate;
	}
	/**
	 * 设置：期限-月
	 */
	public void setLoanyearlimit(BigDecimal loanyearlimit) {
		this.loanyearlimit = loanyearlimit;
	}
	/**
	 * 获取：期限-月
	 */
	public BigDecimal getLoanyearlimit() {
		return loanyearlimit;
	}
	/**
	 * 设置：期限-日
	 */
	public void setPayforlimittime(BigDecimal payforlimittime) {
		this.payforlimittime = payforlimittime;
	}
	/**
	 * 获取：期限-日
	 */
	public BigDecimal getPayforlimittime() {
		return payforlimittime;
	}
	/**
	 * 设置：满标放款日
	 */
	public void setGiveoutmoneytime(String giveoutmoneytime) {
		this.giveoutmoneytime = giveoutmoneytime;
	}
	/**
	 * 获取：满标放款日
	 */
	public String getGiveoutmoneytime() {
		return giveoutmoneytime;
	}
	/**
	 * 设置：到期日
	 */
	public void setWillgetmoneydate(String willgetmoneydate) {
		this.willgetmoneydate = willgetmoneydate;
	}
	/**
	 * 获取：到期日
	 */
	public String getWillgetmoneydate() {
		return willgetmoneydate;
	}
	/**
	 * 设置：资料签名、盖章是否完全
	 */
	public void setIscompleted(String iscompleted) {
		this.iscompleted = iscompleted;
	}
	/**
	 * 获取：资料签名、盖章是否完全
	 */
	public String getIscompleted() {
		return iscompleted;
	}
	/**
	 * 设置：纸质文本最迟寄送日（发标5个工作日内）
	 */
	public void setSendDeadline(Date sendDeadline) {
		this.sendDeadline = sendDeadline;
	}
	/**
	 * 获取：纸质文本最迟寄送日（发标5个工作日内）
	 */
	public Date getSendDeadline() {
		return sendDeadline;
	}
	/**
	 * 设置：纸文本是否盖章
	 */
	public void setIsstamp(String isstamp) {
		this.isstamp = isstamp;
	}
	/**
	 * 获取：纸文本是否盖章
	 */
	public String getIsstamp() {
		return isstamp;
	}
	/**
	 * 设置：寄出日
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	/**
	 * 获取：寄出日
	 */
	public String getSendDate() {
		return sendDate;
	}
}
