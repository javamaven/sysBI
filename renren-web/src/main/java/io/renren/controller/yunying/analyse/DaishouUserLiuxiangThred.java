package io.renren.controller.yunying.analyse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.aspectj.util.FileUtil;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;

public class DaishouUserLiuxiangThred implements Runnable {
	private DataSourceFactory dataSourceFactory;
	private List<Map<String, Object>> list;
	private String statType;
	private String statPeriod;
	private String weekAgo;
	private String userLevel;
	private boolean isCurrWeek;

	public DaishouUserLiuxiangThred(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, String statPeriod, String userLevel, boolean isCurrWeek) {
		this.statType = statType;
		this.dataSourceFactory = dataSourceFactory;
		this.list = list;
		this.statPeriod = statPeriod;
		this.weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
		this.userLevel = userLevel;
		this.isCurrWeek = isCurrWeek;
	}

	@Override
	public void run() {
		String path = this.getClass().getResource("/").getPath();
		String detail_sql = null;
		try {
			
			if("daishou".equals(statType)){
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/待收用户动态流动/待收用户动态流动-待收资金.txt"));
				list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, statPeriod, weekAgo));
			}else if("liuru".equals(statType)){
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/待收用户动态流动/待收用户动态流动-增量流入.txt"));
				detail_sql = detail_sql.replace("${currDay}", statPeriod);
				detail_sql = detail_sql.replace("${weekAgo}", weekAgo);
				list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			}else if("liuchu".equals(statType)){
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/待收用户动态流动/待收用户动态流动-减量流出.txt"));
				detail_sql = detail_sql.replace("${currDay}", statPeriod);
				detail_sql = detail_sql.replace("${weekAgo}", weekAgo);
				list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			}else if("mingxi".equals(statType)){
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/待收用户动态流动/待收用户动态流动-明细.txt"));
				if(isCurrWeek){
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, weekAgo,statPeriod, userLevel));
				}else{
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, statPeriod,weekAgo, userLevel));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

