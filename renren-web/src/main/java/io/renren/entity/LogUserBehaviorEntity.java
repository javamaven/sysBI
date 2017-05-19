package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
public class LogUserBehaviorEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户ID
	private String  userID;
	//用户名
	private String  userName;
	//渠道名称
	private String  channlName;
	//渠道标记
	private String  channlMark;
	//操作时间
	private String  actionTime;
	//操作平台
	private String  actionPlatform;
	//行为
	private String  action;
	//涉及项目类型
	private String  projectType;
	//涉及项目本金
	private String  projectAmount;

	private String num;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChannlName() {
		return channlName;
	}

	public void setChannlName(String channlName) {
		this.channlName = channlName;
	}

	public String getChannlMark() {
		return channlMark;
	}

	public void setChannlMark(String channlMark) {
		this.channlMark = channlMark;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public String getActionPlatform() {
		return actionPlatform;
	}

	public void setActionPlatform(String actionPlatform) {
		this.actionPlatform = actionPlatform;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(String projectAmount) {
		this.projectAmount = projectAmount;
	}
}
