package io.renren.controller.yunying.dayreport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/userredeq")
public class UserRedPacketEqController {


	@Autowired
	private DataSourceFactory dataSourceFactory;

	

	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit, String begin_time,String end_time,String huodong_name,
			String hongbao_name,String hongbao_id, String user_type,String channelName, 
			String userName,String userId,String touzi_begin, String touzi_end) {
//		Map<String,Object> params = new HashMap<String, Object>();
		List<String> paramsList = new ArrayList<>();
		
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
			
		if (StringUtils.isNotEmpty(touzi_begin)) {
			touzi_begin=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin+"' and '"+touzi_end+"'";
		}else {
			touzi_begin="";
		}
		
		if (StringUtils.isNotEmpty(userId)) {
			List<String> idList = new ArrayList<>();
			String[] split = userId.split("\n");
			for (int i = 0; i < split.length; i++) {
				String[] idsArr = split[i].split(" ");
				idList.addAll(Arrays.asList(idsArr));
			}
			String idCond = "(";
			for (int i = 0; i < idList.size(); i++) {
				if(i == idList.size() - 1){
					idCond += idList.get(i);
				}else {
					idCond += idList.get(i) + ",";
				}
			}
			idCond += ")";
			userId=" AND hd.user_id in "+idCond+" ";
			paramsList.add(userId);
		}else{
			userId="";
		}

		if (StringUtils.isNotEmpty(huodong_name)) {
			huodong_name=" AND hd.PURPOSE in ("+huodong_name+") ";
			paramsList.add(huodong_name);
		}else{
			huodong_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name)) {
			hongbao_name=" AND hd.NAME in ("+hongbao_name+") ";
			paramsList.add(hongbao_name);
		}else{
			hongbao_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id)) {
			hongbao_id=" AND hd.prize_template_id = "+hongbao_id+" ";
			paramsList.add(hongbao_id);
		}else{
			hongbao_id="";
		}
		if (StringUtils.isNotEmpty(user_type)) {
			user_type=" AND ub.USER_LEVEL in ("+user_type+")";
			paramsList.add(user_type);
		}else{
			user_type="";
		}
		if (StringUtils.isNotEmpty(channelName)) {
			channelName=" AND ub.channel_name in ("+channelName+") ";
			paramsList.add(channelName);
		}else{
			channelName="";
		}
		
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryThread("list", dataSourceFactory, resultList, start, end, paramsList,begin_time,end_time,touzi_begin));
			Thread thread2 = new Thread(new QueryThread("total", dataSourceFactory, totalList, 0,0, paramsList,begin_time,end_time,touzi_begin));
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
		private String touzi_begin;
		private List<String> paramsList;

		public QueryThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String begin_time,String end_time , String touzi_begin) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start2;
			this.end = end2;
			this.begin_time = begin_time+"";
			this.end_time = end_time+"";
			this.touzi_begin = touzi_begin+"";
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
					
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/qudaohongbao_list.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					detail_sql = detail_sql.replace("${begin_time}", begin_time);
					detail_sql = detail_sql.replace("${end_time}", end_time);
					detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin);
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
				}else if("total".equals(statType)){
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/qudaohongbao_total.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					detail_sql = detail_sql.replace("${begin_time}", begin_time);
					detail_sql = detail_sql.replace("${end_time}", end_time);
					detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin);
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
	public R daylist1(Integer page, Integer limit, String begin_time2,String end_time2,String huodong_name2,
			String hongbao_name2,String hongbao_id2, String user_type2,String channelName2, 
			String userName,String userId2,String touzi_begin2, String touzi_end2) {
		List<String> paramsList = new ArrayList<>();
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
	
		
		if (StringUtils.isNotEmpty(touzi_begin2)) {
			touzi_begin2=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin2+"' and '"+touzi_end2+"'";
		}else {
			touzi_begin2="";
		}
		
		if (StringUtils.isNotEmpty(userId2)) {
			List<String> idList = new ArrayList<>();
			String[] split = userId2.split("\n");
			for (int i = 0; i < split.length; i++) {
				String[] idsArr = split[i].split(" ");
				idList.addAll(Arrays.asList(idsArr));
			}
			String idCond = "(";
			for (int i = 0; i < idList.size(); i++) {
				if(i == idList.size() - 1){
					idCond += idList.get(i);
				}else {
					idCond += idList.get(i) + ",";
				}
			}
			idCond += ")";
			userId2=" AND hd.user_id in "+idCond+" ";
			paramsList.add(userId2);
		}else{
			userId2="";
		}

		if (StringUtils.isNotEmpty(huodong_name2)) {
			huodong_name2=" AND hd.PURPOSE in ("+huodong_name2+") ";
			paramsList.add(huodong_name2);
		}else{
			huodong_name2="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name2)) {
			hongbao_name2=" AND hd.NAME in ("+hongbao_name2+") ";
			paramsList.add(hongbao_name2);
		}else{
			hongbao_name2="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id2)) {
			hongbao_id2=" AND hd.prize_template_id = "+hongbao_id2+" ";
			paramsList.add(hongbao_id2);
		}else{
			hongbao_id2="";
		}
		if (StringUtils.isNotEmpty(user_type2)) {
			user_type2=" AND ub.USER_LEVEL in ("+user_type2+")";
			paramsList.add(user_type2);
		}else{
			user_type2="";
		}
		if (StringUtils.isNotEmpty(channelName2)) {
			channelName2=" AND ub.channel_name in ("+channelName2+") ";
			paramsList.add(channelName2);
		}else{
			channelName2="";
		}
		
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryUserRedPackQkThread("list", dataSourceFactory, resultList, start, end, paramsList, begin_time2,end_time2,touzi_begin2));
			Thread thread2 = new Thread(new QueryUserRedPackQkThread("total", dataSourceFactory, totalList, 0,0, paramsList,begin_time2,end_time2,touzi_begin2));
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
		private String begin_time2;
		private String end_time2;
		private String touzi_begin2;
		private List<String> paramsList;

		public QueryUserRedPackQkThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String begin_time2,String end_time2 , String touzi_begin2) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start2;
			this.end = end2;
			this.touzi_begin2 = touzi_begin2+"";
			this.end_time2 = end_time2+"";
			this.begin_time2 = begin_time2+"";
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

				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/hbpurpose_"+statType+".txt"));
				detail_sql = detail_sql.replace("${queryCond}", queryCond);
				detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin2);
				detail_sql = detail_sql.replace("${begin_time}", begin_time2);
				detail_sql = detail_sql.replace("${end_time}", end_time2);
				
				if("list".equals(statType)){
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
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
	

	@ResponseBody
	@RequestMapping("/ddylist2")
	public R daylist2(Integer page, Integer limit, String begin_time3,String end_time3,String huodong_name3,
			String hongbao_name3,String hongbao_id3, String rate,String hongbaoType, 
			String money,String touzi_begin3, String touzi_end3) {
		List<String> paramsList = new ArrayList<>();
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
		if (StringUtils.isNotEmpty(touzi_begin3)) {
			touzi_begin3=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin3+"' and '"+touzi_end3+"'";
		}else {
			touzi_begin3="";
		}
		
	
		if (StringUtils.isNotEmpty(huodong_name3)) {
			huodong_name3=" AND hd.PURPOSE in ("+huodong_name3+") ";
			paramsList.add(huodong_name3);
		}else{
			huodong_name3="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name3)) {
			hongbao_name3=" AND hd.NAME in ("+hongbao_name3+") ";
			paramsList.add(hongbao_name3);
		}else{
			hongbao_name3="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id3)) {
			hongbao_id3=" AND hd.prize_template_id = "+hongbao_id3+" ";
			paramsList.add(hongbao_id3);
		}else{
			hongbao_id3="";
		}
		if (StringUtils.isNotEmpty(hongbaoType)) {
			hongbaoType=" AND hd.annualized = "+hongbaoType+"";
			paramsList.add(hongbaoType);
		}else{
			hongbaoType="";
		}
		if (StringUtils.isNotEmpty(money)) {
			money=" AND get_money = "+money+" ";
			paramsList.add(money);
		}else{
			money="";
		}
		
		if (StringUtils.isNotEmpty(rate)) {
			rate=" AND hd.rate in ( "+rate+") ";
			paramsList.add(rate);
		}else{
			rate="";
		}
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryUserRedPackEQThread("list", dataSourceFactory, resultList, start, end, paramsList,begin_time3,end_time3,touzi_begin3));
			Thread thread2 = new Thread(new QueryUserRedPackEQThread("total", dataSourceFactory, totalList, 0,0, paramsList,begin_time3,end_time3,touzi_begin3));
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

		System.err.println("++++++++ddddddddddd查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
		
		
	}

	
	class QueryUserRedPackEQThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;
		private String touzi_begin3;
		private String begin_time3;
		private String end_time3;
		private List<String> paramsList;

		public QueryUserRedPackEQThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String begin_time3,String end_time3 , String touzi_begin3) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start2;
			this.end = end2;
			this.touzi_begin3 = touzi_begin3+"";
			this.begin_time3 = begin_time3+"";
			this.end_time3 = end_time3+"";
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

				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/hongbaorate_"+statType+".txt"));
				detail_sql = detail_sql.replace("${queryCond}", queryCond);
				detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin3);
				detail_sql = detail_sql.replace("${begin_time}", begin_time3);
				detail_sql = detail_sql.replace("${end_time}", end_time3);
				
				if("list".equals(statType)){
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
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
	public void exportHbChannelExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
//		String end_time = map.get("end_time") + "";
//		String userId = map.get("userId") + "";
//		String begin_time = map.get("begin_time") + "";
//		String huodong_name = map.get("huodong_name") + "";
//		String userName = map.get("userName") + "";
//		String channelName = map.get("channelName") + "";
//		String user_type = map.get("user_type") + "";
//		String hongbao_id = map.get("hongbao_id") + "";
//		String hongbao_name = map.get("hongbao_name") + "";
		
		String begin_time=map.get("begin_time") + "";
		String end_time=map.get("end_time") + "";
		String huodong_name=map.get("huodong_name") + "";
		String hongbao_name=map.get("hongbao_name") + "";
		String hongbao_id=map.get("hongbao_id") + "";
		String user_type=map.get("user_type") + "";
		String channelName=map.get("channelName") + "";
		String userName=map.get("userName") + "";
		String userId=map.get("userId") + "";
		String touzi_begin=map.get("touzi_begin") + "";
		String touzi_end=map.get("touzi_end") + "";		
		R r =daylist(1, 1000000, begin_time, end_time, huodong_name, hongbao_name, hongbao_id, user_type, channelName, userName, userId, touzi_begin, touzi_end);
		PageUtils pageUtil = (PageUtils) r.get("page");	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
		
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = getDayListExcelFields();
		String title = "按渠道汇总";
		

		List<String> titleList = new ArrayList<>();
		titleList.add(title);

		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);

		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);

		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
		
	}

	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportQingKuangListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String begin_time2=map.get("begin_time2") + "";
		String end_time2=map.get("end_time2") + "";
		String huodong_name2=map.get("huodong_name2") + "";
		String hongbao_name2=map.get("hongbao_name2") + "";
		String hongbao_id2=map.get("hongbao_id2") + "";
		String user_type2=map.get("user_type2") + "";
		String channelName2=map.get("channelName2") + "";
		String userName2=map.get("userName2") + "";
		String userId2=map.get("userId2") + "";
		String touzi_begin2=map.get("touzi_begin2") + "";
		String touzi_end2=map.get("touzi_end2") + "";	
	
		R r=daylist1(1, 1000000, begin_time2, end_time2, huodong_name2, hongbao_name2, hongbao_id2, user_type2, channelName2, userName2, userId2, touzi_begin2, touzi_end2);
		PageUtils pageUtil = (PageUtils) r.get("page");
	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
	
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "按活动目的总汇";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel3")
	public void exportRateListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String begin_time3=map.get("begin_time3") + "";
		String end_time3=map.get("end_time3") + "";
		String huodong_name3=map.get("huodong_name3") + "";
		String hongbao_name3=map.get("hongbao_name3") + "";
		String hongbao_id3=map.get("hongbao_id3") + "";
		String rate=map.get("rate") + "";
		String hongbaoType=map.get("hongbaoType") + "";
		String money=map.get("money") + "";
		String touzi_begin3=map.get("touzi_begin3") + "";
		String touzi_end3 =map.get("touzi_end3") + "";
	
		R r=daylist2(1, 1000000, begin_time3, end_time3, huodong_name3, hongbao_name3, hongbao_id3, rate, hongbaoType, money, touzi_begin3, touzi_end3);
		PageUtils pageUtil = (PageUtils) r.get("page");
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
	
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "按红包杠杆总汇";
		headMap = getDayListExcelFields3();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "查询日期");
		headMap.put("CHANNELNAME", "渠道名称");
		headMap.put("BIAOJI", "渠道标记");
		headMap.put("USERNUM", "获得红包用户数");
		headMap.put("HONGBAO", "获取红包个数");
		headMap.put("SHIYONG", "使用红包的个数");
		headMap.put("HDMONEY", "获得红包总金额");
		headMap.put("SYMONEY", "使用红包总金额");
		headMap.put("SYMONEYZ", "剩余可使用红包金额");
		headMap.put("HBZHANBI", "使用红包金额占比");
		headMap.put("HONGBAOGS", "使用红包个数占比");
		headMap.put("VOUCHETIME", "投资次数中红包使用次数占比");
		
		headMap.put("CAPITAL", "使用红包投资的总金额");
		headMap.put("TENDER", "使用红包投资的年化投资金额");
		headMap.put("ROI", "使用红包的ROI");
		headMap.put("NROI", "使用红包的年化ROI");
		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "系统日期");
		headMap.put("TITIME", "活动时间");
		headMap.put("PURPOSE", "活动目的");
		headMap.put("NAME", "红包名称");
		headMap.put("PTID", "红包模板ID");
		headMap.put("RATE", "杠杆");
		headMap.put("AN", "红包类型");
		headMap.put("MONEY", "红包金额");
		headMap.put("USERNUM", "获得红包用户数");
		headMap.put("HBCOUNT", "获取红包个数");
		headMap.put("COUNTHB", "使用红包的个数");
		headMap.put("HBMONEY", "获得红包总金额");
		headMap.put("USEMONEY", "使用红包总金额");
		headMap.put("SYMONEY", "剩余可使用红包金额");
		headMap.put("SYMONEYZ", "使用红包金额占比");
		headMap.put("SYGESHU", "使用红包个数占比");
		headMap.put("TZZHANBI", "投资次数中红包使用次数占比");
		headMap.put("CAPITAL", "使用红包投资的总金额");
		headMap.put("SUMTENDER", "使用红包投资的年化投资金额");
		headMap.put("ROI", "使用红包的ROI");
		headMap.put("NROI", "使用红包的年化ROI");
		return headMap;
	}
	
	private Map<String, String> getDayListExcelFields3() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "系统日期");
		headMap.put("MONEY", "获得红包的金额");
		headMap.put("RATE", "杠杆");
		headMap.put("AN", "红包类型");
		headMap.put("HDHONGBAO", "获得红包用户数");
		headMap.put("HBGESHU", "获取红包个数");
		
		headMap.put("SYHONGBAO", "使用红包的个数");
		headMap.put("HDMONEY", "获得红包总金额");
		headMap.put("SYMONEY", "使用红包总金额");
		headMap.put("SYYMONEY", "剩余可使用红包金额");
		headMap.put("USEMONEY", "使用红包金额占比");
		headMap.put("SYZHANBI", "使用红包个数占比");
		headMap.put("TIMEI", "投资次数中红包使用次数占比");
		headMap.put("CAPITAL", "使用红包投资的总金额");
		headMap.put("NSUM", "使用红包投资的年化投资金额");
		headMap.put("ROI", "使用红包的ROI");
		headMap.put("NROI", "使用红包的年化ROI");
		return headMap;
	}
	@ResponseBody
	@RequestMapping("/hongbaohuodong")
	public R getChannelHead(){

	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/红包活动.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包活动查询耗时：" + (l2 - l1));
		return R.ok().put("hongbao", resultList);
	}
	@ResponseBody
	@RequestMapping("/hongbaoname")
	public R getHbName(){

	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/红包名称.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包活动查询耗时：" + (l2 - l1));
		return R.ok().put("hongbaoname", resultList);
	}
	@ResponseBody
	@RequestMapping("/userbiaoqian")
	public R getUserBiaoQian(){

	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/用户分类标签.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包活动查询耗时：" + (l2 - l1));
		return R.ok().put("userbiaoqian", resultList);
	}
	
	@ResponseBody
	@RequestMapping("/rate")
	public R getRate(){

	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/杠杆.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包杠杆查询耗时：" + (l2 - l1));
		return R.ok().put("rate", resultList);
	}
	
	
	@ResponseBody
	@RequestMapping("/type")
	public R getHongBaoType(){

	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/红包类型.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包类型查询耗时：" + (l2 - l1));
		return R.ok().put("type", resultList);
	}

	

}
