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
@RequestMapping(value = "/yunying/userred")
public class UserRedPacketController {


	@Autowired
	private DataSourceFactory dataSourceFactory;

	

	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String begin_time,String end_time,String huodong_name,
			String hongbao_name,String hongbao_id, String user_type,String channelName, 
			String userName,String userId) {
//		Map<String,Object> params = new HashMap<String, Object>();
		List<String> paramsList = new ArrayList<>();
		
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
		
		if (StringUtils.isNotEmpty(begin_time)) {
			begin_time=" and to_char(uv.receive_time-1,'yyyy-mm-dd') >= '"+begin_time+"'";
		}else{
			begin_time="";
		}
		if (StringUtils.isNotEmpty(end_time)) {
			end_time=" and to_char(uv.receive_time-1,'yyyy-mm-dd') <= '"+end_time+"' ";
		}else{
			end_time="";
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
			userId=" AND ID in "+idCond+" ";
			paramsList.add(userId);
		}else{
			userId="";
		}

		if (StringUtils.isNotEmpty(huodong_name)) {
			huodong_name=" AND PURPOSE like '%"+huodong_name+"%'";
			paramsList.add(huodong_name);
		}else{
			huodong_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name)) {
			hongbao_name=" AND NAME like '%"+hongbao_name+"%'";
			paramsList.add(hongbao_name);
		}else{
			hongbao_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id)) {
			hongbao_id=" AND WEIZHI = "+hongbao_id+" ";
			paramsList.add(hongbao_id);
		}else{
			hongbao_id="";
		}
		if (StringUtils.isNotEmpty(user_type)) {
			user_type=" AND TYPE like '%"+user_type+"%'";
			paramsList.add(user_type);
		}else{
			user_type="";
		}
		if (StringUtils.isNotEmpty(channelName)) {
			channelName=" AND channelName like '%"+channelName+"%'";
			paramsList.add(channelName);
		}else{
			channelName="";
		}
		
		if (StringUtils.isNotEmpty(userName)) {
			userName=" AND REALNAME= "+userName+" ";
			paramsList.add(userName);
		}else{
			userName="";
		}
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryThread("list", dataSourceFactory, resultList, start, end, paramsList,begin_time,end_time));
			Thread thread2 = new Thread(new QueryThread("total", dataSourceFactory, totalList, 0,0, paramsList,begin_time,end_time));
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

		public QueryThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String begin_time,String end_time) {
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
					
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_list.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					detail_sql = detail_sql.replace("${begin_time}", begin_time);
					detail_sql = detail_sql.replace("${end_time}", end_time);
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, end, start));
				}else if("total".equals(statType)){
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_total.txt"));
					detail_sql = detail_sql.replace("${queryCond}", queryCond);
					detail_sql = detail_sql.replace("${begin_time}", begin_time);
					detail_sql = detail_sql.replace("${end_time}", end_time);
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
	public R daylist1(Integer page, Integer limit, String yingxiao_begin,String yingxiao_end,String touzi_begin,
			String touzi_end,String hongbao_begin, String hongbao_end,String channelName1, String userName1,
			String userType,String userId1) {
		List<String> paramsList = new ArrayList<>();
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
		if (StringUtils.isNotEmpty(yingxiao_begin)) {
			yingxiao_begin=" and to_char(hd.receive_time,'yyyy-mm-dd') >= '"+yingxiao_begin+"'";
		}else{
			yingxiao_begin="";
		}
		if (StringUtils.isNotEmpty(yingxiao_end)) {
			yingxiao_end=" and to_char(hd.receive_time,'yyyy-mm-dd') <= '"+yingxiao_end+"' ";
		}else{
			yingxiao_end="";
		}
		
		if (StringUtils.isNotEmpty(touzi_begin)) {
			touzi_begin=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin+"' and '"+touzi_end+"'";
		}else {
			touzi_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_begin)) {
			hongbao_begin=" AND USEABLEMONEY >='  "+hongbao_begin+"'";
			paramsList.add(hongbao_begin);
		}
		
		if (StringUtils.isNotEmpty(hongbao_end)) {
			hongbao_end=" AND USEABLEMONEY <='  "+hongbao_end+"'";
			paramsList.add(hongbao_end);
		}
		
		if (StringUtils.isNotEmpty(channelName1)) {
			channelName1=" AND channelName like '%"+channelName1+"%'";
			paramsList.add(channelName1);
		}

		if (StringUtils.isNotEmpty(userName1)) {
			userName1=" AND REALNAME = "+userName1+" ";
			paramsList.add(userName1);
		}
		
		if (StringUtils.isNotEmpty(userType)) {
			userType=" AND userType like '%"+userType+"%'";
			paramsList.add(userType);
		}
		
//		if (StringUtils.isNotEmpty(userId1)) {
//			userId1=" AND ID like = "+userId1+" ";
//			paramsList.add(userId1);
//		}
		
		if (StringUtils.isNotEmpty(userId1)) {
			List<String> idList = new ArrayList<>();
			String[] split = userId1.split("\n");
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
			userId1=" AND ID in "+idCond+" ";
			paramsList.add(userId1);
		}else{
			userId1="";
		}
		
		
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
//		try {
//			String path = this.getClass().getResource("/").getPath();
//			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
//			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
//			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
//			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
//			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
//			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
//			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
//			detail_sql = detail_sql.replace("${userType}",userType );
//			detail_sql = detail_sql.replace("${userId1}",userId1 );
//			detail_sql = detail_sql.replace("${userName1}",userName1 );
//			detail_sql = detail_sql.replace("${pageStartSql}", " and RN > " + start + "");
//			detail_sql = detail_sql.replace("${pageEndSql}", " and RN <= " + end + "");
//			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
//			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
//			resultList.addAll(list);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		int total = 0;
//		try {
//			String path = this.getClass().getResource("/").getPath();
//			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
//			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
//			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
//			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
//			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
//			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
//			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
//			detail_sql = detail_sql.replace("${userType}",userType );
//			detail_sql = detail_sql.replace("${userId1}",userId1 );
//			detail_sql = detail_sql.replace("${userName1}",userName1 );
//			detail_sql = detail_sql.replace("${pageStartSql}", "");
//			detail_sql = detail_sql.replace("${pageEndSql}", "");
//			detail_sql = detail_sql.replace("${selectSql}", " COUNT(1) TOTAL ");
//			List<Map<String, Object>> total_list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
//			Map<String, Object> map = total_list.get(0);
//			total = Integer.parseInt(map.get("TOTAL").toString());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread thread1 = new Thread(new QueryUserRedPackQkThread("list", dataSourceFactory, resultList, start, end, paramsList,touzi_begin,yingxiao_begin,yingxiao_end));
			Thread thread2 = new Thread(new QueryUserRedPackQkThread("total", dataSourceFactory, totalList, 0,0, paramsList,touzi_begin,yingxiao_begin,yingxiao_end));
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

		public QueryUserRedPackQkThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start2, int end2, List<String> paramsList,String touzi_begin,String yingxiao_begin, String yingxiao_end) {
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

				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk_"+statType+".txt"));
				detail_sql = detail_sql.replace("${queryCond}", queryCond);
				detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin);
				detail_sql = detail_sql.replace("${yingxiao_begin}", yingxiao_begin);
				detail_sql = detail_sql.replace("${yingxiao_end}", yingxiao_end);
				
				if("list".equals(statType)){
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, end, start));
				}else {
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
				}
//				if("list".equals(statType)){
//					
//					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_list.txt"));
//					detail_sql = detail_sql.replace("${queryCond}", queryCond);
//					detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin);
//					detail_sql = detail_sql.replace("${yingxiao_begin}", yingxiao_begin);
//					detail_sql = detail_sql.replace("${yingxiao_end}", yingxiao_end);
//					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, end, start));
//				}else if("total".equals(statType)){
//					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_total.txt"));
//					detail_sql = detail_sql.replace("${queryCond}", queryCond);
//					detail_sql = detail_sql.replace("${touzi_begin}", touzi_begin);
//					detail_sql = detail_sql.replace("${yingxiao_begin}", yingxiao_begin);
//					detail_sql = detail_sql.replace("${yingxiao_end}", yingxiao_end);
//					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
//				}
				
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
		
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String end_time = map.get("end_time") + "";
		String userId = map.get("userId") + "";
		String begin_time = map.get("begin_time") + "";
		String huodong_name = map.get("huodong_name") + "";
		String userName = map.get("userName") + "";
		String channelName = map.get("channelName") + "";
		String user_type = map.get("user_type") + "";
		String hongbao_id = map.get("hongbao_id") + "";
		String hongbao_name = map.get("hongbao_name") + "";
		
		R r = daylist(1, 1000000, begin_time, end_time, huodong_name, hongbao_name, hongbao_id, user_type, channelName, userName, userId);
		PageUtils pageUtil = (PageUtils) r.get("page");
	
	


//		
//		String hongbao_begin = map.get("hongbao_begin") + "";
//		String hongbao_end = map.get("hongbao_end") + "";
//		String yingxiao_begin = map.get("yingxiao_begin") + "";
//		String yingxiao_end = map.get("yingxiao_end") + "";
//		String touzi_begin = map.get("touzi_begin") + "";
//		String channelName1 = map.get("channelName1") + "";
//		String userType = map.get("userType") + "";
//		String userId1 = map.get("userId1") + "";
//		String userName1 = map.get("userName1") + "";
//		String touzi_end = map.get("touzi_end") + "";
//		
//	
//		

		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = getDayListExcelFields();
		String title = "用户收到红包明细";
		
//		// 查询列表数据
//		JSONArray va2 = new JSONArray();
//		for (int i = 0; i < resultList2.size(); i++) {
//			va2.add(resultList2.get(i));
//		}
//		Map<String, String> headMap2 = getDayListExcelFields2();
//		String title2 = "用户获得红包累计情况";

		List<String> titleList = new ArrayList<>();
		titleList.add(title);
//		titleList.add(title2);

		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
//		headMapList.add(headMap2);

		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
//		ja.add(va2);

		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
		
	}

	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportQingKuangListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String hongbao_begin = map.get("hongbao_begin") + "";
		String hongbao_end = map.get("hongbao_end") + "";
		String yingxiao_begin = map.get("yingxiao_begin") + "";
		String yingxiao_end = map.get("yingxiao_end") + "";
		String touzi_begin = map.get("touzi_begin") + "";
		String channelName1 = map.get("channelName1") + "";
		String userType = map.get("userType") + "";
		String userId1 = map.get("userId1") + "";
		String userName1 = map.get("userName1") + "";
		String touzi_end = map.get("touzi_end") + "";
		
		
		R r = daylist1(1, 1000000, yingxiao_begin, yingxiao_end, touzi_begin, touzi_end, hongbao_begin, hongbao_end, channelName1, userName1, userType, userId1);
		PageUtils pageUtil = (PageUtils) r.get("page");
	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
	
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "用户获得红包累计情况";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("D_DATE", "查询日期");
		headMap.put("ID", "用户id");
		headMap.put("USERNAME", "用户名");
		headMap.put("REALNAME", "用户姓名");
		headMap.put("PHONE", "用户手机号");
		headMap.put("PURPOSE", "用户参与活动描述");
		headMap.put("NAME", "红包名称");
		headMap.put("WEIZHI", "红包模板id");
		headMap.put("HBTIME", "参与活动日期");
		headMap.put("HBMONEY", "收到红包金额");
		headMap.put("USEMONEY", "使用红包金额");
		headMap.put("RATE", "红包抵扣率");
		
		headMap.put("ANNUALIZED", "是否现金红包");
		headMap.put("RETIME", "获取红包时间");
		headMap.put("ENDTIME", "红包失效时间");
		headMap.put("USERTIMES", "红包使用次数");
		headMap.put("TENDER", "使用红包投资金额");
		headMap.put("SUMTENDER", "使用红包年化投资金额");
		headMap.put("ROI", "ROI");
		
		headMap.put("NROI", "年化ROI");
		headMap.put("AW", "用户待收金额");
		headMap.put("TYPE", "用户分类标签");
		headMap.put("CHANNELNAME", "渠道名称");
		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("D_DAY", "查询日期");
		headMap.put("ID", "用户id");
		headMap.put("USERNAME", "用户名");
		headMap.put("REALNAME", "用户姓名");
		headMap.put("PHONE", "用户手机号");
		headMap.put("ATI_TIME", "最近一次营销日期");
		headMap.put("DIF", "最近一次营销距今天数");
		headMap.put("COUT_HB", "获得红包个数");
		headMap.put("COUNTU", "使用红包个数");
		headMap.put("GMONEY", "获得红包总金额");
		headMap.put("USEMONEY", "使用红包金额");
		headMap.put("USEABLEMONEY", "剩余可用红包金额");
		
		headMap.put("MONEYI", "使用红包金额占比");
		headMap.put("USERTIME", "使用红包个数占比");
		headMap.put("TIMEI", "投资使用红包次数占比");
		headMap.put("TENDER", "使用红包投资金额");
		headMap.put("SUMTENDER", "使用红包年化投资金额");
		headMap.put("ROI", "使用红包投资ROI");
		headMap.put("NROI", "使用红包年化投资ROI");
		
		headMap.put("AW", "用户待收金额");
		
		
		headMap.put("CHANNELNAME", "渠道名称");
		headMap.put("TYPE", "用户分类标签");
		return headMap;

	}
	

}
