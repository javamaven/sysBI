package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.controller.yunying.dayreport.UserRedPacketController.QueryThread;
import io.renren.controller.yunying.dayreport.UserRedPacketController.QueryUserRedPackQkThread;
import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/userlayer")
public class UserLayerController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="用户分层";


	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit, String time, String userid) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		List<String> paramsList = new ArrayList<>();
		int start = (page - 1) * limit;
		int end = start + limit;
		long l1 = System.currentTimeMillis();
		
		if (StringUtils.isNotEmpty(time)) {
			time=time.replace("-", "");
			time=" AND STAT_PERIOD  ="+time+" ";
			paramsList.add(time);
		}else{
			time="";
		}
		
		if (StringUtils.isNotEmpty(userid)) {
			userid=" AND USER_ID  ="+userid+" ";
			paramsList.add(userid);
		}else{
			userid="";
		}
		
		

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryThread("list", dataSourceFactory, resultList, start, end, paramsList, time, userid));
			Thread thread2 = new Thread(new QueryThread("total", dataSourceFactory, totalList, 0,0, paramsList,time,userid));
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	class QueryThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;
		private String begin_time;
		private String end_time;
		private List<String> paramsList;

		public QueryThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String time,String userid) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start2;
			this.end = end2;
			this.begin_time = begin_time+"";
			this.end_time = end_time+"";
			this.paramsList = paramsList;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				String queryCond = "";
				for (int i = 0; i < paramsList.size(); i++) {
					queryCond += paramsList.get(i);
				}
				if("list".equals(statType)){
					
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/未流失用户_list.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, end, start));
				}else if("total".equals(statType)){
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/未流失用户_total.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping("/ddylist")
	public R daylist1(Integer page, Integer limit, String time, String userid) {
		List<String> paramsList = new ArrayList<>();
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		if (StringUtils.isNotEmpty(time)) {
			time=time.replace("-", "");
			time=" AND STAT_PERIOD  ="+time+" ";
			paramsList.add(time);
		}else{
			time="";
		}
		
		if (StringUtils.isNotEmpty(userid)) {
			userid=" AND USER_ID  ="+userid+" ";
			paramsList.add(userid);
		}else{
			userid="";
		}
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryUserRedPackQkThread("list", dataSourceFactory, resultList, start, end, paramsList,time,userid));
			Thread thread2 = new Thread(new QueryUserRedPackQkThread("total", dataSourceFactory, totalList, 0,0, paramsList,time,userid));
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++asdsad查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
		
		
	}

	
	class QueryUserRedPackQkThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;
		private String touzi_begin;
		private String yingxiao_begin;
		private String yingxiao_end;
		private List<String> paramsList;

		public QueryUserRedPackQkThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String time,String userid) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start2;
			this.end = end2;
			this.touzi_begin = touzi_begin+"";
			this.yingxiao_begin = yingxiao_begin+"";
			this.yingxiao_end = yingxiao_end+"";
			this.paramsList = paramsList;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				String queryCond = "";
				for (int i = 0; i < paramsList.size(); i++) {
					queryCond += paramsList.get(i);
				}

				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/流失用户_"+statType+".txt"));
				detail_sql = detail_sql.replace("${queryCond}", queryCond);
				
				if("list".equals(statType)){
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, end, start));
				}else {
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
				}

				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		
		String time = map.get("time") + "";
		String userid = map.get("userid") + "";

		
		R r = daylist(1, 1000000, time, userid);
		PageUtils pageUtil = (PageUtils) r.get("page");
	
		
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	
	// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "未流失用户数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportLossListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		
		String time = map.get("time") + "";
		String userid = map.get("userid") + "";

		
		R r = daylist1(1, 1000000, time, userid);
		PageUtils pageUtil = (PageUtils) r.get("page");
	
		
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	
	// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "流失用户数据";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STAT_PERIOD", "日期");
		headMap.put("USER_ID", "用户ID");
		headMap.put("LABEL", "用户标签");
		headMap.put("SUM_AMT", "周期内投资金额");
		headMap.put("CNT_ORDER", "周期内投资笔数");
		headMap.put("BORROW_PERIOD", "项目平均周期");
		headMap.put("HVNT_PUR", "已沉默时长");
		headMap.put("FIRST_DT", "距首投时长");
		headMap.put("COUPON", "周期内红包使用金额");
		headMap.put("MONEY_WAIT", "当前待收金额");
		headMap.put("BALANCE", "当前账户余额");
		headMap.put("MAX_ASSET", "总资产峰值(包含余额)");
		
		headMap.put("TIME_MAX_ASSET", "峰值日期");
		headMap.put("OUT_AMT_40", "40天内提现金额");
		headMap.put("OUT_AMT_90", "90天内提现金额");
		headMap.put("HOLD_CPON_AMT", "当前持有红包金额");
		headMap.put("CPON_NUM", "当前有效红包个数");
		return headMap;

	}

	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STAT_PERIOD", "日期");
		headMap.put("USER_ID", "用户ID");
		headMap.put("LABEL", "用户标签");
		headMap.put("SUM_AMT", "周期内投资金额");
		headMap.put("CNT_ORDER", "周期内投资笔数");
		headMap.put("BORROW_PERIOD", "项目平均周期");
		headMap.put("HVNT_PUR", "已沉默时长");
		headMap.put("FIRST_DT", "距首投时长");
		headMap.put("COUPON", "周期内红包使用金额");
		headMap.put("MONEY_WAIT", "当前待收金额");
		headMap.put("BALANCE", "当前账户余额");
		headMap.put("MAX_ASSET", "总资产峰值(包含余额)");
		
		headMap.put("TIME_MAX_ASSET", "峰值日期");
		headMap.put("OUT_AMT", "累计提现金额");
		headMap.put("OUT_CNT_ORDER", "累计提现次数");
		headMap.put("HOLD_CPON_AMT", "当前持有红包金额");
		headMap.put("CPON_NUM", "当前有效红包个数");
		return headMap;

	}






	


}
