package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 项目总台帐
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 15:41:58
 */
public class ProjectSumEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//分公司/事业部
	private String orgsimplename;
	//产品分类
	private String producttype;
	//产品分类子类
	private String subproducttype;
	//项目合同编号
	private String sourcecaseno;
	//部门
	private String department;
	//总监
	private String developmanagername;
	//经办
	private String workername;
	//借款人
	private String customername;
	//借款金额
	private BigDecimal payformoney;
	//放款金额
	private BigDecimal payformoneyout;
	//贷款月数
	private BigDecimal loanyearlimit;
	//贷款天数
	private BigDecimal payforlimittime;
	//放款日期
	private String giveoutmoneytime;
	//到期时间
	private String willgetmoneydate;
	//年利率
	private BigDecimal loanrate;
	//发标利率
	private BigDecimal interestRate;
	//其它利率
	private BigDecimal otherRate;
	//总利率（金额）
	private BigDecimal totalRateAmount;
	//资金成本
	private BigDecimal capitalCost;
	//其他利率（金额）
	private BigDecimal otherRateAmount;
	//应还本金
	private BigDecimal remain;
	//应还利息
	private BigDecimal reinterest;
	//已还本金
	private BigDecimal rebackmain;
	//已还利息
	private BigDecimal rebackinterest;
	//待还本金
	private BigDecimal waitCapital;
	//待还利息
	private BigDecimal waitInterest;
	//提前还款违约金
	private BigDecimal reamercedmoney3;
	//罚息
	private BigDecimal reamercedmoney;
	//公私类型
	private String type;
	//资金来源
	private String capitalSource;
	//项目结清日
	private String realgetmoneydate;
	//手续费收入
	private BigDecimal rebackservice;
	//还款方式
	private String repaymentWay;
	//车牌上牌地
	private String carNoLocation;
	//资产摘牌公司
	private String capitalDelistCompany;
	//交易所1
	private String exchange1;
	//交易所2
	private String exchange2;
	//平台募集拆分对应
	private String borrowers;
	//统计日期
	private String statPeriod;

	/**
	 * 设置：分公司/事业部
	 */
	public void setOrgsimplename(String orgsimplename) {
		this.orgsimplename = orgsimplename;
	}
	/**
	 * 获取：分公司/事业部
	 */
	public String getOrgsimplename() {
		return orgsimplename;
	}
	/**
	 * 设置：产品分类
	 */
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	/**
	 * 获取：产品分类
	 */
	public String getProducttype() {
		return producttype;
	}
	/**
	 * 设置：产品分类子类
	 */
	public void setSubproducttype(String subproducttype) {
		this.subproducttype = subproducttype;
	}
	/**
	 * 获取：产品分类子类
	 */
	public String getSubproducttype() {
		return subproducttype;
	}
	/**
	 * 设置：项目合同编号
	 */
	public void setSourcecaseno(String sourcecaseno) {
		this.sourcecaseno = sourcecaseno;
	}
	/**
	 * 获取：项目合同编号
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
	 * 设置：经办
	 */
	public void setWorkername(String workername) {
		this.workername = workername;
	}
	/**
	 * 获取：经办
	 */
	public String getWorkername() {
		return workername;
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
	 * 设置：借款金额
	 */
	public void setPayformoney(BigDecimal payformoney) {
		this.payformoney = payformoney;
	}
	/**
	 * 获取：借款金额
	 */
	public BigDecimal getPayformoney() {
		return payformoney;
	}
	/**
	 * 设置：放款金额
	 */
	public void setPayformoneyout(BigDecimal payformoneyout) {
		this.payformoneyout = payformoneyout;
	}
	/**
	 * 获取：放款金额
	 */
	public BigDecimal getPayformoneyout() {
		return payformoneyout;
	}
	/**
	 * 设置：贷款月数
	 */
	public void setLoanyearlimit(BigDecimal loanyearlimit) {
		this.loanyearlimit = loanyearlimit;
	}
	/**
	 * 获取：贷款月数
	 */
	public BigDecimal getLoanyearlimit() {
		return loanyearlimit;
	}
	/**
	 * 设置：贷款天数
	 */
	public void setPayforlimittime(BigDecimal payforlimittime) {
		this.payforlimittime = payforlimittime;
	}
	/**
	 * 获取：贷款天数
	 */
	public BigDecimal getPayforlimittime() {
		return payforlimittime;
	}
	/**
	 * 设置：放款日期
	 */
	public void setGiveoutmoneytime(String giveoutmoneytime) {
		this.giveoutmoneytime = giveoutmoneytime;
	}
	/**
	 * 获取：放款日期
	 */
	public String getGiveoutmoneytime() {
		return giveoutmoneytime;
	}
	/**
	 * 设置：到期时间
	 */
	public void setWillgetmoneydate(String willgetmoneydate) {
		this.willgetmoneydate = willgetmoneydate;
	}
	/**
	 * 获取：到期时间
	 */
	public String getWillgetmoneydate() {
		return willgetmoneydate;
	}
	/**
	 * 设置：年利率
	 */
	public void setLoanrate(BigDecimal loanrate) {
		this.loanrate = loanrate;
	}
	/**
	 * 获取：年利率
	 */
	public BigDecimal getLoanrate() {
		return loanrate;
	}
	/**
	 * 设置：发标利率
	 */
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	/**
	 * 获取：发标利率
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	/**
	 * 设置：其它利率
	 */
	public void setOtherRate(BigDecimal otherRate) {
		this.otherRate = otherRate;
	}
	/**
	 * 获取：其它利率
	 */
	public BigDecimal getOtherRate() {
		return otherRate;
	}
	/**
	 * 设置：总利率（金额）
	 */
	public void setTotalRateAmount(BigDecimal totalRateAmount) {
		this.totalRateAmount = totalRateAmount;
	}
	/**
	 * 获取：总利率（金额）
	 */
	public BigDecimal getTotalRateAmount() {
		return totalRateAmount;
	}
	/**
	 * 设置：资金成本
	 */
	public void setCapitalCost(BigDecimal capitalCost) {
		this.capitalCost = capitalCost;
	}
	/**
	 * 获取：资金成本
	 */
	public BigDecimal getCapitalCost() {
		return capitalCost;
	}
	/**
	 * 设置：其他利率（金额）
	 */
	public void setOtherRateAmount(BigDecimal otherRateAmount) {
		this.otherRateAmount = otherRateAmount;
	}
	/**
	 * 获取：其他利率（金额）
	 */
	public BigDecimal getOtherRateAmount() {
		return otherRateAmount;
	}
	/**
	 * 设置：应还本金
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}
	/**
	 * 获取：应还本金
	 */
	public BigDecimal getRemain() {
		return remain;
	}
	/**
	 * 设置：应还利息
	 */
	public void setReinterest(BigDecimal reinterest) {
		this.reinterest = reinterest;
	}
	/**
	 * 获取：应还利息
	 */
	public BigDecimal getReinterest() {
		return reinterest;
	}
	/**
	 * 设置：已还本金
	 */
	public void setRebackmain(BigDecimal rebackmain) {
		this.rebackmain = rebackmain;
	}
	/**
	 * 获取：已还本金
	 */
	public BigDecimal getRebackmain() {
		return rebackmain;
	}
	/**
	 * 设置：已还利息
	 */
	public void setRebackinterest(BigDecimal rebackinterest) {
		this.rebackinterest = rebackinterest;
	}
	/**
	 * 获取：已还利息
	 */
	public BigDecimal getRebackinterest() {
		return rebackinterest;
	}
	/**
	 * 设置：待还本金
	 */
	public void setWaitCapital(BigDecimal waitCapital) {
		this.waitCapital = waitCapital;
	}
	/**
	 * 获取：待还本金
	 */
	public BigDecimal getWaitCapital() {
		return waitCapital;
	}
	/**
	 * 设置：待还利息
	 */
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}
	/**
	 * 获取：待还利息
	 */
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}
	/**
	 * 设置：提前还款违约金
	 */
	public void setReamercedmoney3(BigDecimal reamercedmoney3) {
		this.reamercedmoney3 = reamercedmoney3;
	}
	/**
	 * 获取：提前还款违约金
	 */
	public BigDecimal getReamercedmoney3() {
		return reamercedmoney3;
	}
	/**
	 * 设置：罚息
	 */
	public void setReamercedmoney(BigDecimal reamercedmoney) {
		this.reamercedmoney = reamercedmoney;
	}
	/**
	 * 获取：罚息
	 */
	public BigDecimal getReamercedmoney() {
		return reamercedmoney;
	}
	/**
	 * 设置：公私类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：公私类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：资金来源
	 */
	public void setCapitalSource(String capitalSource) {
		this.capitalSource = capitalSource;
	}
	/**
	 * 获取：资金来源
	 */
	public String getCapitalSource() {
		return capitalSource;
	}
	/**
	 * 设置：项目结清日
	 */
	public void setRealgetmoneydate(String realgetmoneydate) {
		this.realgetmoneydate = realgetmoneydate;
	}
	/**
	 * 获取：项目结清日
	 */
	public String getRealgetmoneydate() {
		return realgetmoneydate;
	}
	/**
	 * 设置：手续费收入
	 */
	public void setRebackservice(BigDecimal rebackservice) {
		this.rebackservice = rebackservice;
	}
	/**
	 * 获取：手续费收入
	 */
	public BigDecimal getRebackservice() {
		return rebackservice;
	}
	/**
	 * 设置：还款方式
	 */
	public void setRepaymentWay(String repaymentWay) {
		this.repaymentWay = repaymentWay;
	}
	/**
	 * 获取：还款方式
	 */
	public String getRepaymentWay() {
		return repaymentWay;
	}
	/**
	 * 设置：车牌上牌地
	 */
	public void setCarNoLocation(String carNoLocation) {
		this.carNoLocation = carNoLocation;
	}
	/**
	 * 获取：车牌上牌地
	 */
	public String getCarNoLocation() {
		return carNoLocation;
	}
	/**
	 * 设置：资产摘牌公司
	 */
	public void setCapitalDelistCompany(String capitalDelistCompany) {
		this.capitalDelistCompany = capitalDelistCompany;
	}
	/**
	 * 获取：资产摘牌公司
	 */
	public String getCapitalDelistCompany() {
		return capitalDelistCompany;
	}
	/**
	 * 设置：交易所1
	 */
	public void setExchange1(String exchange1) {
		this.exchange1 = exchange1;
	}
	/**
	 * 获取：交易所1
	 */
	public String getExchange1() {
		return exchange1;
	}
	/**
	 * 设置：交易所2
	 */
	public void setExchange2(String exchange2) {
		this.exchange2 = exchange2;
	}
	/**
	 * 获取：交易所2
	 */
	public String getExchange2() {
		return exchange2;
	}
	/**
	 * 设置：平台募集拆分对应
	 */
	public void setBorrowers(String borrowers) {
		this.borrowers = borrowers;
	}
	/**
	 * 获取：平台募集拆分对应
	 */
	public String getBorrowers() {
		return borrowers;
	}
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
}
