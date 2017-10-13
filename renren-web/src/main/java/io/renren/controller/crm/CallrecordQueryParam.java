package io.renren.controller.crm;

import org.apache.commons.lang.StringUtils;

public class CallrecordQueryParam {
	String user_id;
	String belongs_to;
	String dep_user_id;
	String status;
	String call_time_type;
	String call_time_s;
	String call_time_e;
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBelongs_to() {
		return belongs_to;
	}

	public void setBelongs_to(String belongs_to) {
		this.belongs_to = belongs_to;
	}

	public String getDep_user_id() {
		return dep_user_id;
	}

	public void setDep_user_id(String dep_user_id) {
		this.dep_user_id = dep_user_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCall_time_type() {
		return call_time_type;
	}

	public void setCall_time_type(String call_time_type) {
		this.call_time_type = call_time_type;
	}

	public String getCall_time_s() {
		return call_time_s;
	}

	public void setCall_time_s(String call_time_s) {
		this.call_time_s = call_time_s;
	}

	public String getCall_time_e() {
		return call_time_e;
	}

	public void setCall_time_e(String call_time_e) {
		this.call_time_e = call_time_e;
	}

	String toWhereSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("where 1=1 ");
		if(StringUtils.isNotBlank(user_id)){
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
		}
		return sb.toString();
	}
}
