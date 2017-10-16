package io.renren.controller.crm;

import org.apache.commons.lang.StringUtils;

//统计报表的日期选择条件
public class DateParam {
	String time_type;
	String time_s;
	String time_e;
	
	public String getTime_type() {
		return time_type;
	}

	public void setTime_type(String time_type) {
		this.time_type = time_type;
	}

	public String getTime_s() {
		return time_s;
	}

	public void setTime_s(String time_s) {
		this.time_s = time_s;
	}

	public String getTime_e() {
		return time_e;
	}

	public void setTime_e(String time_e) {
		this.time_e = time_e;
	}

	String toWhereSql(){
		return toWhereSql("data_date");
	}
	
	String toWhereSql(String dateField){
		StringBuilder sb = new StringBuilder();
		sb.append("where 1=1 ");
		if(StringUtils.isNotBlank(time_type)){
			int days = 0;
			if("4".equals(time_type)){
				if(StringUtils.isNotBlank(time_s)){
					sb.append(" and ").append(dateField).append(">='").append(time_s).append("'");
				}
				if(StringUtils.isNotBlank(time_e)){
					sb.append(" and ").append(dateField).append("<='").append(time_e).append("'");
				}
			}else {
				if("1".equals(time_type)){
					days = 60;
				}else if("2".equals(time_type)){
					days = 30;
				}else if("3".equals(time_type)){
					days = 7;
				}
				sb.append(" and to_days(DATE_ADD(STR_TO_DATE(t.").append(dateField).append(",'%Y%m%d'),INTERVAL ").append(days).append(" DAY)) >= to_days(now())");
			}
		}
		return sb.toString();
	}
	
	String toWhereSql1(String dateField){
		StringBuilder sb = new StringBuilder();
		sb.append("where 1=1 ");
		if(StringUtils.isNotBlank(time_type)){
			int days = 0;
			if("4".equals(time_type)){
				if(StringUtils.isNotBlank(time_s)){
					sb.append(" and ").append(dateField).append(">='").append(time_s).append("'");
				}
				if(StringUtils.isNotBlank(time_e)){
					sb.append(" and ").append(dateField).append("<='").append(time_e).append("'");
				}
			}else {
				if("1".equals(time_type)){
					days = 60;
				}else if("2".equals(time_type)){
					days = 30;
				}else if("3".equals(time_type)){
					days = 7;
				}
				sb.append(" and to_days(DATE_ADD(t.").append(dateField).append(",INTERVAL ").append(days).append(" DAY)) >= to_days(now())");
			}
		}
		return sb.toString();
	}
}
