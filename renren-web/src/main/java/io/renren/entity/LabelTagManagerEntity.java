package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-11 19:46:06
 */
public class LabelTagManagerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//标签类型
	private String type;
	//创建用户
	private String sysUser;
	//标签名字
	private String mainlabelName;
	// 创建时间
	private Date createDate;
    // 标签表主键
	private Integer mainID;
	// 标签ID
	private Integer labelID;
	// 类型(维度/指标)
	private String labelType;
	// 内容
	private String labelContene;
	// 逻辑
	private String labelLogic;
	// 条件
	private String labelCondition;

	private  String strSql;

	public String getStrSql() {
		return strSql;
	}

	public void setStrSql(String strSql) {
		this.strSql = strSql;
	}

	public Integer getMainID() {
		return mainID;
	}

	public void setMainID(Integer mainID) {
		this.mainID = mainID;
	}

	public Integer getLabelID() {
		return labelID;
	}

	public void setLabelID(Integer labelID) {
		this.labelID = labelID;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getLabelContene() {
		return labelContene;
	}

	public void setLabelContene(String labelContene) {
		this.labelContene = labelContene;
	}

	public String getLabelLogic() {
		return labelLogic;
	}

	public void setLabelLogic(String labelLogic) {
		this.labelLogic = labelLogic;
	}

	public String getLabelCondition() {
		return labelCondition;
	}

	public void setLabelCondition(String labelCondition) {
		this.labelCondition = labelCondition;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：标签类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：标签类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：创建用户
	 */
	public void setSysUser(String sysUser) {
		this.sysUser = sysUser;
	}
	/**
	 * 获取：创建用户
	 */
	public String getSysUser() {
		return sysUser;
	}
	/**
	 * 设置：标签名字
	 */
	public void setMainlabelName(String mainlabelName) {
		this.mainlabelName = mainlabelName;
	}
	/**
	 * 获取：标签名字
	 */
	public String getMainlabelName() {
		return mainlabelName;
	}
}
