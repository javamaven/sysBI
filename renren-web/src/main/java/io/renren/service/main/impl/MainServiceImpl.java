package io.renren.service.main.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.service.main.MainService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;

@Service("mainService")
public class MainServiceImpl implements MainService {
	@Autowired
	private DataSourceFactory dataSourceFactory;


	@Override
	public Map<String, Object> queryUserWait(String oldUserId, String cgUserId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> waitMapPt = new HashMap<String, Object>();
		Map<String, Object> waitMapCg = new HashMap<String, Object>();
		if(StringUtils.isEmpty(oldUserId) && StringUtils.isEmpty(cgUserId)){
			retMap.put("code", "error");
			retMap.put("reason", "请传入普通版ID：oldUserId 和 存管版ID：cgUserId");
			return retMap;
		}
		try {
			Thread t1 = null;
			Thread t2 = null;
			if(StringUtils.isNotEmpty(oldUserId)){
				t1 = new Thread(new QueryWaitThread(oldUserId, "pt", waitMapPt, dataSourceFactory));
				t1.start();
			}
			if(StringUtils.isNotEmpty(cgUserId)){
				t2 = new Thread(new QueryWaitThread(cgUserId, "cg", waitMapCg, dataSourceFactory));
				t2.start();
			}
			if(t1 != null){
				t1.join();
			}
			if(t2 != null){
				t2.join();
			}
			System.err.println(waitMapPt);
			System.err.println(waitMapCg);
			double waitPt = 0;
			double waitCg = 0;
			if(waitMapPt.containsKey("WAIT")){
				Object object = waitMapPt.get("WAIT");
				if(object != null){
					waitPt = Double.parseDouble(object + "");
				}
			}
			if(waitMapCg.containsKey("WAIT")){
				Object object = waitMapCg.get("WAIT");
				if(object != null){
					waitCg = Double.parseDouble(object + "");
				}
			}
			retMap.put("OLD_USER_ID", oldUserId);
			retMap.put("CG_USER_ID", cgUserId);
			retMap.put("WAIT", waitPt+waitCg);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("code", "error");
			retMap.put("reason", printStackTraceToString(e));
			return retMap;
		}
		return retMap;
	}


	public String printStackTraceToString(Throwable t) {
	    StringWriter sw = new StringWriter();
	    t.printStackTrace(new PrintWriter(sw, true));
	    return sw.getBuffer().toString();
	}

	class QueryWaitThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private Map<String,Object> waitMap;
		private String userId;
		private String systemType;//1普通版2存管版
		public QueryWaitThread(String userId, String systemType, Map<String,Object> waitMap, DataSourceFactory dataSourceFactory) {
			this.dataSourceFactory = dataSourceFactory;
			this.userId = userId;
			this.systemType = systemType;
			this.waitMap = waitMap;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String querySql = null;
			List<Map<String, Object>> dataList = null;
			try {
				if(systemType.equals("pt")){
					querySql = FileUtil.readAsString(new File(path + File.separator + "sql/CRM/普通版实时待收.txt"));
					querySql = querySql.replace("${userId}", userId);
					dataList = new JdbcUtil(dataSourceFactory, "oracle").query(querySql);
				}else if(systemType.equals("cg")){
					querySql = FileUtil.readAsString(new File(path + File.separator + "sql/CRM/存管版实时待收.txt"));
					querySql = querySql.replace("${userId}", userId);
					dataList = new JdbcUtil(dataSourceFactory, "mysql").query(querySql);

				}
				if(dataList != null && dataList.size() > 0){
					this.waitMap.putAll(dataList.get(0));
				}
			} catch (IOException e) {
				e.printStackTrace();
				dataSourceFactory.reInitConnectionPoll();
			} catch (SQLException e) {
				e.printStackTrace();
				dataSourceFactory.reInitConnectionPoll();
			}
		}
	}
	
}
