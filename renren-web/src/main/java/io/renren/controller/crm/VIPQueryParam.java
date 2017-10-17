package io.renren.controller.crm;

import java.io.Serializable;

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



	String toWhereSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("where 1=1 ");
/*		if(StringUtils.isNotBlank(user_id)){
			sb.append(" and t.user_id=").append(user_id);
		}
		if(StringUtils.isNotBlank(dep_user_id)){
			sb.append(" and v.dep_user_id=").append(dep_user_id);
		}
		if(StringUtils.isNotBlank(belongs_to)){
			sb.append(" and vb.belongs_to=").append(belongs_to);
		}
		if(StringUtils.isNotBlank(status)){
			sb.append(" and t.status=").append(status);
		}
		if(StringUtils.isNotBlank(call_time_type)){
			int days = 0;
			if("4".equals(call_time_type)){
				if(StringUtils.isNotBlank(call_time_s)){
					sb.append(" and DATE_FORMAT(t.`call_time`,'%Y-%m-%d')>='").append(call_time_s).append("'");
				}
				if(StringUtils.isNotBlank(call_time_e)){
					sb.append(" and DATE_FORMAT(t.`call_time`,'%Y-%m-%d')<='").append(call_time_e).append("'");
				}
			}else {
				if("1".equals(call_time_type)){
					days = 60;
				}else if("2".equals(call_time_type)){
					days = 30;
				}else if("3".equals(call_time_type)){
					days = 7;
				}
				sb.append(" and to_days(DATE_ADD(t.`call_time`,INTERVAL ").append(days).append(" DAY)) >= to_days(now())");
			}
		}*/
		return sb.toString();
	}
}
