package io.renren.entity.yunying.dayreport;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 09:48:41
 */
public class DmReportAccTransferEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//发起日期
	private String statPeriod;
	//待审核金额
	private BigDecimal awaitAuditM;
	//处理中金额
	private BigDecimal processingM;
	//成功金额
	private BigDecimal successfulM;
	//失败金额
	private BigDecimal failureM;
	//审核不通过金额
	private BigDecimal nthroughM;
	//发起人数
	private BigDecimal pCou;
	//回款金额
	private BigDecimal recoverM;
	//回款人数
	private BigDecimal recoverCou;

	/**
	 * 设置：发起日期
	 */
	public void setStatPeriod(String statPeriod) {
		this.statPeriod = statPeriod;
	}
	/**
	 * 获取：发起日期
	 */
	public String getStatPeriod() {
		return statPeriod;
	}
	/**
	 * 设置：待审核金额
	 */
	public void setAwaitAuditM(BigDecimal awaitAuditM) {
		this.awaitAuditM = awaitAuditM;
	}
	/**
	 * 获取：待审核金额
	 */
	public BigDecimal getAwaitAuditM() {
		return awaitAuditM;
	}
	/**
	 * 设置：处理中金额
	 */
	public void setProcessingM(BigDecimal processingM) {
		this.processingM = processingM;
	}
	/**
	 * 获取：处理中金额
	 */
	public BigDecimal getProcessingM() {
		return processingM;
	}
	/**
	 * 设置：成功金额
	 */
	public void setSuccessfulM(BigDecimal successfulM) {
		this.successfulM = successfulM;
	}
	/**
	 * 获取：成功金额
	 */
	public BigDecimal getSuccessfulM() {
		return successfulM;
	}
	/**
	 * 设置：失败金额
	 */
	public void setFailureM(BigDecimal failureM) {
		this.failureM = failureM;
	}
	/**
	 * 获取：失败金额
	 */
	public BigDecimal getFailureM() {
		return failureM;
	}
	/**
	 * 设置：审核不通过金额
	 */
	public void setNthroughM(BigDecimal nthroughM) {
		this.nthroughM = nthroughM;
	}
	/**
	 * 获取：审核不通过金额
	 */
	public BigDecimal getNthroughM() {
		return nthroughM;
	}
	/**
	 * 设置：发起人数
	 */
	public void setPCou(BigDecimal pCou) {
		this.pCou = pCou;
	}
	/**
	 * 获取：发起人数
	 */
	public BigDecimal getPCou() {
		return pCou;
	}
	/**
	 * 设置：回款金额
	 */
	public void setRecoverM(BigDecimal recoverM) {
		this.recoverM = recoverM;
	}
	/**
	 * 获取：回款金额
	 */
	public BigDecimal getRecoverM() {
		return recoverM;
	}
	/**
	 * 设置：回款人数
	 */
	public void setRecoverCou(BigDecimal recoverCou) {
		this.recoverCou = recoverCou;
	}
	/**
	 * 获取：回款人数
	 */
	public BigDecimal getRecoverCou() {
		return recoverCou;
	}
}
