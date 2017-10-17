package io.renren.controller.crm;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 构建VIP列表查询条件
 * @author HUGUANG 2017年10月16日下午4:13:51
 */
public class VIPQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String user_id;
	private String dep_user_id;
	private String remark;
	private String comm_remark;
	private String level;
	private String belongs_to;
	private String value_class;
	private String tags;
	private String vip_select;
	private String comm_count;// 通话次数

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDep_user_id() {
		return dep_user_id;
	}
	public void setDep_user_id(String dep_user_id) {
		this.dep_user_id = dep_user_id;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getBelongs_to() {
		return belongs_to;
	}
	public void setBelongs_to(String belongs_to) {
		this.belongs_to = belongs_to;
	}
	public String getValue_class() {
		return value_class;
	}
	public void setValue_class(String value_class) {
		this.value_class = value_class;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVip_select() {
		return vip_select;
	}
	public void setVip_select(String vip_select) {
		this.vip_select = vip_select;
	}
	public String getComm_count() {
		return comm_count;
	}
	public void setComm_count(String comm_count) {
		this.comm_count = comm_count;
	}
	
	/**
	 * VIP列表构建查询列表条件
	 * @author HUGUANG 2017年10月17日下午7:58:13
	 */
	String toWhereSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("where 1=1 ");
		if(StringUtils.isNotEmpty(user_id)){
			sb.append(" AND vu.user_id=").append(user_id);
		}
		if(StringUtils.isNotEmpty(dep_user_id)){
			sb.append(" AND vu.dep_user_id=").append(dep_user_id);
		}
		if(StringUtils.isNotEmpty(remark)){
			sb.append(" AND cr.remark LIKE").append('%' + remark.trim() + '%');
		}
		if(StringUtils.isNotEmpty(comm_remark)){
			sb.append(" AND cr.comm_remark LIKE").append('%' + comm_remark.trim() + '%');
		}
		if(StringUtils.isNotEmpty(level)){
			sb.append(" AND vui.level=").append(dep_user_id);
		}
		if(StringUtils.isNotEmpty(level)){
			sb.append(" AND vui.level=").append(dep_user_id);
		}
		if(StringUtils.isNotEmpty(belongs_to)){
			sb.append(" AND vub.belongs_to=").append(belongs_to);
		}
		if(StringUtils.isNotEmpty(value_class)){
			sb.append(" AND vub.value_type=").append(value_class);
		}
		if(StringUtils.isNotEmpty(tags)){
			sb.append(" AND vub.tags=").append(tags);
		}
		if(StringUtils.isNotEmpty(vip_select)){ // VIP筛选
			// <option value ="1">当前是VIP</option>
			// <option value ="2">流失VIP</option>
			// <option value ="3">预警用户</option>
			// <option value ="4">明日回款用户</option>	
			// 仅显示当前状态下仍然是VIP的用户
			// 流失VIP	仅现实之前是VIP，现在不是VIP的用户
			// 预警用户	参数设置中，达到预警条件的VIP用户
			// 明日回款用户	明日有回款的VIP用户
			if ("1".equals(vip_select)) {
				sb.append(" AND vui.vip_status=").append(vip_select).append(" AND vui.vip_status=0");
			} else if ("2".equals(vip_select)) {
				sb.append(" AND vui.vip_status=").append(vip_select);
			} else if ("3".equals(vip_select)) { // TODO
				sb.append(" AND vui.is_warning=1");
			} else if ("4".equals(vip_select)) {
				sb.append(" AND vui.vip_status=1").append(" AND vui.tomorow_recover_account > 0");
			}
		}
		if(StringUtils.isNotEmpty(comm_count)) {// 通话次数
		
		}
		return sb.toString();
	}
}
