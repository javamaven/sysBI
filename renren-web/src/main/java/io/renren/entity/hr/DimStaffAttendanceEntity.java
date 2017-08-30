package io.renren.entity.hr;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤信息记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-29 10:08:21
 */
public class DimStaffAttendanceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 员工姓名
	private String realname;
	// 职位
	private String post;
	// 考勤时间
	private Date attendTime;
	// 打卡时间
	private Date clockTime;
	// CLOCK_RESULT
	private String clockResult;
	// 考勤时间
	private String attendTimeString;
	// 打卡时间
	private String clockTimeString;
	

	public DimStaffAttendanceEntity(String realname, String post, Date attendTime, Date clockTime, String clockResult) {
		super();
		this.realname = realname;
		this.post = post;
		this.attendTime = attendTime;
		this.clockTime = clockTime;
		this.clockResult = clockResult;
	}

	public DimStaffAttendanceEntity(String realname, String post, String clockResult, String attendTimeString,
			String clockTimeString) {
		super();
		this.realname = realname;
		this.post = post;
		this.clockResult = clockResult;
		this.attendTimeString = attendTimeString;
		this.clockTimeString = clockTimeString;
	}

	public void setAttendTimeString(String attendTimeString) {
		this.attendTimeString = attendTimeString;
	}

	public void setClockTimeString(String clockTimeString) {
		this.clockTimeString = clockTimeString;
	}

	public String getAttendTimeString() {
		return attendTimeString;
	}

	public String getClockTimeString() {
		return clockTimeString;
	}

	/**
	 * 设置：员工姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * 获取：员工姓名
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * 设置：职位
	 */
	public void setPost(String post) {
		this.post = post;
	}

	/**
	 * 获取：职位
	 */
	public String getPost() {
		return post;
	}

	/**
	 * 设置：考勤时间
	 */
	public void setAttendTime(Date attendTime) {
		this.attendTime = attendTime;
	}

	/**
	 * 获取：考勤时间
	 */
	public Date getAttendTime() {
		return attendTime;
	}

	/**
	 * 设置：打卡时间
	 */
	public void setClockTime(Date clockTime) {
		this.clockTime = clockTime;
	}

	/**
	 * 获取：打卡时间
	 */
	public Date getClockTime() {
		return clockTime;
	}

	/**
	 * 设置：CLOCK_RESULT
	 */
	public void setClockResult(String clockResult) {
		this.clockResult = clockResult;
	}

	/**
	 * 获取：CLOCK_RESULT
	 */
	public String getClockResult() {
		return clockResult;
	}
}
