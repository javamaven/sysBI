package io.renren.entity.yunying.phonesale;

import java.io.Serializable;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-15 16:41:23
 */
public class PhoneSaleMonthEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用户名
	private String userName;
	// 1：内部，2：外包
	private String mark;
	// 电销日期
	private String callDate;
	// 接通结果
	private String callResult;
	// 电销人员
	private String callPerson;
	// 电销月份
	private String statMonth;

	private String batchId;
	private String importUserName;
	private String importUserId;
	private String importTime;
	private String phoneType;
	private String userId;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public void setImportTime(String importTime) {
		this.importTime = importTime;
	}

	public void setImportUserId(String importUserId) {
		this.importUserId = importUserId;
	}

	public void setImportUserName(String importUserName) {
		this.importUserName = importUserName;
	}

	public String getBatchId() {
		return batchId;
	}

	public String getImportTime() {
		return importTime;
	}

	public String getImportUserId() {
		return importUserId;
	}

	public String getImportUserName() {
		return importUserName;
	}

	/**
	 * 设置：用户名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取：用户名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置：1：内部，2：外包
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}

	/**
	 * 获取：1：内部，2：外包
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * 设置：电销日期
	 */
	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	/**
	 * 获取：电销日期
	 */
	public String getCallDate() {
		return callDate;
	}

	/**
	 * 设置：接通结果
	 */
	public void setCallResult(String callResult) {
		this.callResult = callResult;
	}

	/**
	 * 获取：接通结果
	 */
	public String getCallResult() {
		return callResult;
	}

	/**
	 * 设置：电销人员
	 */
	public void setCallPerson(String callPerson) {
		this.callPerson = callPerson;
	}

	/**
	 * 获取：电销人员
	 */
	public String getCallPerson() {
		return callPerson;
	}

	/**
	 * 设置：电销月份
	 */
	public void setStatMonth(String statMonth) {
		this.statMonth = statMonth;
	}

	/**
	 * 获取：电销月份
	 */
	public String getStatMonth() {
		return statMonth;
	}
}
