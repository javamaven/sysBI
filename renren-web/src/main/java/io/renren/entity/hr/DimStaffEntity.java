package io.renren.entity.hr;

import java.io.Serializable;
import java.sql.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-28 10:12:47
 */
public class DimStaffEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String realname;
	//
	private String cardId;
	//
	private String phone;
	//
	private String department;
	//
	private String part;
	//
	private String post;
	//
	private String ifBoss;
	//
	private String workTime;
	//
	private String leaveTime;
	
	private Date workTimeDate;
	//
	private Date leaveTimeDate;
	
	
	
	public DimStaffEntity(String realname, String cardId, String phone, String department, String part, String post,
			String ifBoss, String workTime, String leaveTime, Date workTimeDate, Date leaveTimeDate) {
		super();
		this.realname = realname;
		this.cardId = cardId;
		this.phone = phone;
		this.department = department;
		this.part = part;
		this.post = post;
		this.ifBoss = ifBoss;
		this.workTime = workTime;
		this.leaveTime = leaveTime;
		this.workTimeDate = workTimeDate;
		this.leaveTimeDate = leaveTimeDate;
	}

	public DimStaffEntity(String realname, String cardId, String phone, String department, String part, String post,
			String ifBoss, String workTime, String leaveTime) {
		super();
		this.realname = realname;
		this.cardId = cardId;
		this.phone = phone;
		this.department = department;
		this.part = part;
		this.post = post;
		this.ifBoss = ifBoss;
		this.workTime = workTime;
		this.leaveTime = leaveTime;
	}
	
	public DimStaffEntity(String realname, String cardId, String phone, String department, String part, String post,
			String ifBoss, Date workTime, Date leaveTime) {
		super();
		this.realname = realname;
		this.cardId = cardId;
		this.phone = phone;
		this.department = department;
		this.part = part;
		this.post = post;
		this.ifBoss = ifBoss;
		this.workTimeDate = workTime;
		this.leaveTimeDate = leaveTime;
	}
	public void setLeaveTimeDate(Date leaveTimeDate) {
		this.leaveTimeDate = leaveTimeDate;
	}
	public void setWorkTimeDate(Date workTimeDate) {
		this.workTimeDate = workTimeDate;
	}
	public Date getLeaveTimeDate() {
		return leaveTimeDate;
	}
	public Date getWorkTimeDate() {
		return workTimeDate;
	}
	
	/**
	 * 设置：
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * 获取：
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 设置：
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	/**
	 * 获取：
	 */
	public String getCardId() {
		return cardId;
	}
	/**
	 * 设置：
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：
	 */
	public void setPart(String part) {
		this.part = part;
	}
	/**
	 * 获取：
	 */
	public String getPart() {
		return part;
	}
	/**
	 * 设置：
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * 获取：
	 */
	public String getPost() {
		return post;
	}
	/**
	 * 设置：
	 */
	public void setIfBoss(String ifBoss) {
		this.ifBoss = ifBoss;
	}
	/**
	 * 获取：
	 */
	public String getIfBoss() {
		return ifBoss;
	}
	/**
	 * 设置：
	 */
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	/**
	 * 获取：
	 */
	public String getWorkTime() {
		return workTime;
	}
	/**
	 * 设置：
	 */
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	/**
	 * 获取：
	 */
	public String getLeaveTime() {
		return leaveTime;
	}
}
