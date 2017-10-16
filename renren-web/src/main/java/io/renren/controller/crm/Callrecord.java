package io.renren.controller.crm;

import java.util.Date;

public class Callrecord {

	private Integer user_id;
	private Date call_time;
	private Byte status;
	private Long account_wait;
	private Long account_wait_inc_rate;
	private Byte call_type;
	private Integer call_duration;
	
	private String question_item_json;
	private String remark;
	private String comm_remark;
	private Integer create_by;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Date getCall_time() {
		return call_time;
	}
	public void setCall_time(Date call_time) {
		this.call_time = call_time;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getAccount_wait() {
		return account_wait;
	}
	public void setAccount_wait(Long account_wait) {
		this.account_wait = account_wait;
	}
	public Long getAccount_wait_inc_rate() {
		return account_wait_inc_rate;
	}
	public void setAccount_wait_inc_rate(Long account_wait_inc_rate) {
		this.account_wait_inc_rate = account_wait_inc_rate;
	}
	public Byte getCall_type() {
		return call_type;
	}
	public void setCall_type(Byte call_type) {
		this.call_type = call_type;
	}
	public Integer getCall_duration() {
		return call_duration;
	}
	public void setCall_duration(Integer call_duration) {
		this.call_duration = call_duration;
	}
	public String getQuestion_item_json() {
		return question_item_json;
	}
	public void setQuestion_item_json(String question_item_json) {
		this.question_item_json = question_item_json;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getComm_remark() {
		return comm_remark;
	}
	public void setComm_remark(String comm_remark) {
		this.comm_remark = comm_remark;
	}
	public Integer getCreate_by() {
		return create_by;
	}
	public void setCreate_by(Integer create_by) {
		this.create_by = create_by;
	}
}
