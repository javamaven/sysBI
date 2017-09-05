package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.model.MapInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;
import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/zbp2p")
public class MonthlyReportZbController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="运营周报";



	/**
	 * 周报列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		long l1 = System.currentTimeMillis();
		String lastSevenday="";
		String lastday="";
		String day="";
		String dayday="";
		lastSevenday = DateUtil.getCurrDayBefore(invest_end_time, 7, "yyyy-MM-dd");
		lastday = DateUtil.getCurrDayBefore(lastSevenday, 6, "yyyy-MM-dd");
		
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			dayday = invest_stat_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_end_time)) {
			day = invest_end_time.replace("-", "");
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZb.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			detail_sql = detail_sql.replace("${lastSevenday}", lastSevenday);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${day}", day);
			detail_sql = detail_sql.replace("${dayday}", dayday);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			String NUM1="";
			String NUM2="";
			String XIXI="";
			Map<String, Object> map = new HashMap<>();
			map.put("NUM1", NUM1);
			map.put("NUM2", NUM2);
			map.put("XIXI", XIXI);
			 List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list.size(); i++) {
				 map=list.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map.put("NUM2", NUM2)+"";
				 if(date.equals("1")){
					 NUM2="本周新增到年底的待收";
					 System.out.println(NUM2);
					 }else if (date.equals("2")) {
						 NUM2="注册人数";
					}else if (date.equals("3")) {
						 NUM2="当天项目年化投资额（万元）";
					}else if (date.equals("4")) {
						 NUM2="项目天数";
					}
					else{
						NUM2=date+"";
					}
				 map.put("NUM2", NUM2);
		
			}
			list1.addAll(list);
			 List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list1.size(); i++) {
				 map=list1.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map.put("XIXI", XIXI)+"";
				 if(date.equals("1")){
					 XIXI="上周新增到年底的待收";
					 System.out.println(NUM2);
					 }else if (date.equals("2")) {
						 XIXI="首投人数";
					}else if (date.equals("3")) {
						 XIXI="当天项目投资额（万元）";
					}else if (date.equals("4")) {
						 XIXI="项目金额";
					}
					else{
						XIXI=date+"";
					}
				 map.put("XIXI", XIXI);
		
			}
			list2.addAll(list1);
			resultList.addAll(list2);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++越秀P2P查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist")
	public R daylist1(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		String lastSevenday="";
		String lastday="";
		String day="";
		String dayday="";
		String afterday="";
		String firstday="";
		String firstday2="";
		lastSevenday = DateUtil.getCurrDayBefore(invest_end_time, 7, "yyyy-MM-dd");
		lastday = DateUtil.getCurrDayBefore(lastSevenday, 6, "yyyy-MM-dd");
		afterday=DateUtil.getCurrDayBefore(invest_end_time, -6, "yyyy-MM-dd");;
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			dayday = invest_stat_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_end_time)) {
			day = invest_end_time.replace("-", "");
		}
		firstday2=dayday.substring(0,6);
		firstday=dayday.substring(0,6)+"01";
		String beforeOneday=DateUtil.getCurrDayBefore(invest_end_time,-1, "yyyy-MM-dd");
		
		String beforeSevenday=DateUtil.getCurrDayBefore(beforeOneday,-6, "yyyy-MM-dd");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZbb.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			detail_sql = detail_sql.replace("${lastSevenday}", lastSevenday);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${afterday}", afterday);
			detail_sql = detail_sql.replace("${beforeOneday}", beforeOneday);
			detail_sql = detail_sql.replace("${beforeSevenday}", beforeSevenday);
			detail_sql = detail_sql.replace("${firstday2}", firstday2);
			detail_sql = detail_sql.replace("${firstday}", firstday);
			detail_sql = detail_sql.replace("${day}", day);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			String BZZHUCE="";
			String BZRENZHENG="";
			String BZSHOUTOU="";
			String SZZHUCE="";
			Map<String, Object> map = new HashMap<>();
			map.put("BZZHUCE", BZZHUCE);
			map.put("BZRENZHENG", BZRENZHENG);
			map.put("BZSHOUTOU", BZSHOUTOU);
			map.put("SZZHUCE", SZZHUCE);
			 List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list.size(); i++) {
				 map=list.get(i);
				String date=map.put("BZZHUCE", BZZHUCE)+"";
				 if(date.equals("1")){
					 BZZHUCE="本周年化投资金额（万元）";
					 System.out.println(BZZHUCE);
					 }else if (date.equals("2")) {
						 BZZHUCE="普通版回款（万元）";
					}else if (date.equals("3")) {
						 BZZHUCE="本月销售年化交易额";
					}else if (date.equals("-1")) {
						 BZZHUCE="";
					}
					 else{
						BZZHUCE=date+"";
					}

				 map.put("BZZHUCE", BZZHUCE);		
			}
			 list1.addAll(list);
			 List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list1.size(); i++) {
				 map=list1.get(i);
				String date=map.put("BZRENZHENG", BZRENZHENG)+"";
				 if(date.equals("1")){
					 BZRENZHENG="本周投资金额（万元";
					 }else if (date.equals("2")) {
						 BZRENZHENG="存管版回款（万元）";
					}else if (date.equals("3")) {
						 BZRENZHENG="";
					}else if (date.equals("-1")) {
						 BZRENZHENG="";
					}
					 else{
						BZRENZHENG=date+"";
					}
				 map.put("BZRENZHENG", BZRENZHENG);
		
			}
			 list2.addAll(list1);
			 List<Map<String, Object>> list3 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list2.size(); i++) {
				 map=list2.get(i);

				String date=map.put("BZSHOUTOU", BZSHOUTOU)+"";
				 if(date.equals("1")){
					 BZSHOUTOU="上周年化投资金额（万元）";
					 System.out.println(BZSHOUTOU);
					 }else if (date.equals("2")) {
						 BZSHOUTOU="总回款( 万元)";
					}else if (date.equals("3")) {
						BZSHOUTOU="";
					}else if (date.equals("-1")) {
						BZSHOUTOU="";
					}
					 else{
						BZSHOUTOU=date+"";
					}
				 map.put("BZSHOUTOU", BZSHOUTOU);
		
			}
			 list3.addAll(list2);
			 List<Map<String, Object>> list4 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list3.size(); i++) {
				 map=list3.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map.put("SZZHUCE", SZZHUCE)+"";
				 if(date.equals("1")){
					 SZZHUCE="上周投资金额（万元）";
					 System.out.println(SZZHUCE);
					 }else if (date.equals("2")) {
						 SZZHUCE="理财解锁金额";
					}else if (date.equals("3")) {
						SZZHUCE="";
					}else if (date.equals("-1")) {
						SZZHUCE="";
					}
					 else{
						SZZHUCE=date+"";
					}
				 map.put("SZZHUCE", SZZHUCE);
		
			}
			 list4.addAll(list3);
			
			
			resultList.addAll(list4);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++电销日报明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	
	
	@ResponseBody
	@RequestMapping("/daishoulist")
	public R dslist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			
			for (int i = 0; i < 7; i++) {
				if(i==0){
					invest_end_time=invest_end_time+"";
				}else{
					invest_end_time = DateUtil.getCurrDayBefore(invest_end_time, 1, "yyyy-MM-dd");
				}
			
			
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyDs.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++运营周报待收查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/zichanlist")
	public R zichanlist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		String end_time="";
		String firstday3="";
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			invest_stat_time = invest_stat_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_end_time)) {
			 end_time = invest_end_time.replace("-", "");
			firstday3=invest_stat_time.substring(0,6)+"01";
		}
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZbZc.txt"));
			detail_sql = detail_sql.replace("${invest_end_time}", end_time);
			detail_sql = detail_sql.replace("${invest_stat_time}", invest_stat_time);
			detail_sql = detail_sql.replace("${firstday3}", firstday3);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++运营周报待收查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/weizhilist")
	public R weiZhilist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
	
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/周报投资额.txt"));
			detail_sql = detail_sql.replace("${invest_end_time}", invest_end_time);
			detail_sql = detail_sql.replace("${invest_stat_time}", invest_stat_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++运营周报周投资额查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 
	 * @param params
	 * @param request
	 * 
	 */
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String invest_end_time = map.get("invest_end_time") + "";
		String invest_stat_time = map.get("invest_stat_time") + "";
		String invest_end_time_ = invest_end_time;
		String invest_stat_time_ = invest_stat_time;
		String lastSevenday="";
		String lastday="";
		String day="";
		String dayday="";
		String afterday="";
		String firstday="";
		String firstday2="";
		String end_time=invest_end_time+"";
		String firstday3="";
		lastSevenday = DateUtil.getCurrDayBefore(invest_end_time, 7, "yyyy-MM-dd");
		lastday = DateUtil.getCurrDayBefore(lastSevenday, 6, "yyyy-MM-dd");
		afterday=DateUtil.getCurrDayBefore(invest_end_time, -6, "yyyy-MM-dd");;
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			dayday = invest_stat_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_end_time)) {
			day = invest_end_time.replace("-", "");
		}
		
		firstday2=dayday.substring(0,6);
		firstday=dayday.substring(0,6)+"01";
		String beforeOneday=DateUtil.getCurrDayBefore(invest_end_time,-1, "yyyy-MM-dd");
		
		String beforeSevenday=DateUtil.getCurrDayBefore(beforeOneday,-6, "yyyy-MM-dd");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZb.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			detail_sql = detail_sql.replace("${lastSevenday}", lastSevenday);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${day}", day);
			detail_sql = detail_sql.replace("${dayday}", dayday);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			String NUM1="";
			String NUM2="";
			String XIXI="";
			Map<String, Object> map2 = new HashMap<>();
			map2.put("NUM1", NUM1);
			map2.put("NUM2", NUM2);
			map2.put("XIXI", XIXI);
			 List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list.size(); i++) {
				 map2=list.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map2.put("NUM2", NUM2)+"";
				 if(date.equals("1")){
					 NUM2="本周新增到年底的待收";
					 System.out.println(NUM2);
					 }else if (date.equals("2")) {
						 NUM2="注册人数";
					}else if (date.equals("3")) {
						 NUM2="当天项目年化投资额（万元）";
					}else if (date.equals("4")) {
						 NUM2="项目天数";
					}
					else{
						NUM2=date+"";
					}
				 map2.put("NUM2", NUM2);
		
			}
			list1.addAll(list);
			 List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list1.size(); i++) {
				 map2=list1.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map2.put("XIXI", XIXI)+"";
				 if(date.equals("1")){
					 XIXI="上周新增到年底的待收";
					 System.out.println(NUM2);
					 }else if (date.equals("2")) {
						 XIXI="首投人数";
					}else if (date.equals("3")) {
						 XIXI="当天项目投资额（万元）";
					}else if (date.equals("4")) {
						 XIXI="项目金额";
					}
					else{
						XIXI=date+"";
					}
				 map2.put("XIXI", XIXI);
		
			}
			list2.addAll(list1);
			resultList.addAll(list2);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		


		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZbb.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			detail_sql = detail_sql.replace("${lastSevenday}", lastSevenday);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${afterday}", afterday);
			detail_sql = detail_sql.replace("${beforeOneday}", beforeOneday);
			detail_sql = detail_sql.replace("${beforeSevenday}", beforeSevenday);
			detail_sql = detail_sql.replace("${firstday2}", firstday2);
			detail_sql = detail_sql.replace("${firstday}", firstday);
			detail_sql = detail_sql.replace("${day}", day);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			String BZZHUCE="";
			String BZRENZHENG="";
			String BZSHOUTOU="";
			String SZZHUCE="";
			Map<String, Object> map3 = new HashMap<>();
			map3.put("BZZHUCE", BZZHUCE);
			map3.put("BZRENZHENG", BZRENZHENG);
			map3.put("BZSHOUTOU", BZSHOUTOU);
			map3.put("SZZHUCE", SZZHUCE);
			 List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list.size(); i++) {
				 map3=list.get(i);
				String date=map3.put("BZZHUCE", BZZHUCE)+"";
				 if(date.equals("1")){
					 BZZHUCE="本周年化投资金额（万元）";
					 System.out.println(BZZHUCE);
					 }else if (date.equals("2")) {
						 BZZHUCE="普通版回款（万元）";
					}else if (date.equals("3")) {
						 BZZHUCE="本月销售年化交易额";
					}else if (date.equals("-1")) {
						 BZZHUCE="";
					}
					 else{
						BZZHUCE=date+"";
					}

				 map3.put("BZZHUCE", BZZHUCE);		
			}
			 list1.addAll(list);
			 List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list1.size(); i++) {
				 map3=list1.get(i);
				String date=map3.put("BZRENZHENG", BZRENZHENG)+"";
				 if(date.equals("1")){
					 BZRENZHENG="本周投资金额（万元";
					 }else if (date.equals("2")) {
						 BZRENZHENG="存管版回款（万元）";
					}else if (date.equals("3")) {
						 BZRENZHENG="";
					}else if (date.equals("-1")) {
						 BZRENZHENG="";
					}
					 else{
						BZRENZHENG=date+"";
					}
				 map3.put("BZRENZHENG", BZRENZHENG);
		
			}
			 list2.addAll(list1);
			 List<Map<String, Object>> list3 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list2.size(); i++) {
				 map3=list2.get(i);

				String date=map3.put("BZSHOUTOU", BZSHOUTOU)+"";
				 if(date.equals("1")){
					 BZSHOUTOU="上周年化投资金额（万元）";
					 System.out.println(BZSHOUTOU);
					 }else if (date.equals("2")) {
						 BZSHOUTOU="总回款( 万元)";
					}else if (date.equals("3")) {
						BZSHOUTOU="";
					}else if (date.equals("-1")) {
						BZSHOUTOU="";
					}
					 else{
						BZSHOUTOU=date+"";
					}
				 map3.put("BZSHOUTOU", BZSHOUTOU);
		
			}
			 list3.addAll(list2);
			 List<Map<String, Object>> list4 = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < list3.size(); i++) {
				 map3=list3.get(i);
//				 System.out.println("NUM2===="+map.put("NUM2", NUM2));
				String date=map3.put("SZZHUCE", SZZHUCE)+"";
				 if(date.equals("1")){
					 SZZHUCE="上周投资金额（万元）";
					 System.out.println(SZZHUCE);
					 }else if (date.equals("2")) {
						 SZZHUCE="理财解锁金额";
					}else if (date.equals("3")) {
						SZZHUCE="";
					}else if (date.equals("-1")) {
						SZZHUCE="";
					}
					 else{
						SZZHUCE=date+"";
					}
				 map3.put("SZZHUCE", SZZHUCE);
		
			}
			 list4.addAll(list3);
			
			
			resultList2.addAll(list4);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		List<Map<String, Object>> resultList3 = new ArrayList<Map<String, Object>>();

		try {
			
			for (int i = 0; i < 7; i++) {
				if(i==0){
					invest_end_time=invest_end_time+"";
				}else{
					invest_end_time = DateUtil.getCurrDayBefore(invest_end_time, 1, "yyyy-MM-dd");
				}
			
			
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyDs.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList3.addAll(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Map<String, Object>> resultList4 = new ArrayList<Map<String, Object>>();
		try {
			if (StringUtils.isNotEmpty(end_time)) {
				end_time = end_time.replace("-", "");
				invest_stat_time = invest_stat_time.replace("-", "");
				firstday3=invest_stat_time.substring(0,6)+"01";
			}
		
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZbZc.txt"));
			detail_sql = detail_sql.replace("${invest_end_time}", end_time);
			detail_sql = detail_sql.replace("${invest_stat_time}", invest_stat_time);
			detail_sql = detail_sql.replace("${firstday3}", firstday3);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList4.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		R r=weiZhilist(1, 1000000, invest_end_time_, invest_stat_time_);
		PageUtils pageUtil = (PageUtils) r.get("page");
		
		List<Map<String,Object>> resultList5 = (List<Map<String, Object>>) pageUtil.getList();
		
		
		
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = getDayListExcelFields();
		String title = "周报(新增待收，首投人数，投资额，项目)";
		
		// 查询列表数据
		JSONArray va2 = new JSONArray();
		for (int i = 0; i < resultList2.size(); i++) {
			va2.add(resultList2.get(i));
		}
		Map<String, String> headMap2 = getDayListExcelFields2();
		String title2 = "周报(人数明细,投资金额)";
		
		// 查询列表数据
		JSONArray va3 = new JSONArray();
		for (int i = 0; i < resultList3.size(); i++) {
			va3.add(resultList3.get(i));
		}
		
	
		Map<String, String> headMap3 = getDayListExcelFields3();
		String title3 = "当前待收金额(前七天)";
		
		// 查询列表数据
		JSONArray va4 = new JSONArray();
		for (int i = 0; i < resultList4.size(); i++) {
			va4.add(resultList4.get(i));
		}
		
		Map<String, String> headMap4 = getDayListExcelFields4();
		String title4 = "周销售资产的期限分布";
		
		// 查询列表数据
		JSONArray va5 = new JSONArray();
		for (int i = 0; i < resultList5.size(); i++) {
			va5.add(resultList5.get(i));
		}
		
		Map<String, String> headMap5 = getDayListExcelFields5();
		String title5 = "周销售额";
		
		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);
		titleList.add(title3);
		titleList.add(title4);
		titleList.add(title5);
		
		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);
		headMapList.add(headMap3);
		headMapList.add(headMap4);
		headMapList.add(headMap5);
		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);
		ja.add(va3);
		ja.add(va4);
		ja.add(va5);
		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
	}

	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("NUM1", "日期");
		headMap.put("NUM2", "指标名称");
		headMap.put("XIXI", "指标名称2");
	
		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STATPERIOD", "日期");
		headMap.put("BZZHUCE", "本周注册人数");
		headMap.put("BZRENZHENG", "本周认证人数");
		headMap.put("BZSHOUTOU", "本周首投人数");
		headMap.put("SZZHUCE", "上周注册人数");
		headMap.put("SZRENZHENG", "上周认证人数");
		headMap.put("SZSHOUTOU", "上周首投人数");
		headMap.put("ZZHUCE", "总注册人数");
		headMap.put("ZSHOUTOU", "总首投人数");
		headMap.put("BYZHUCE", "本月注册人数");
		headMap.put("BYSHOUTOU", "本月首投人数");
		return headMap;

	}

	private Map<String, String> getDayListExcelFields3() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STATPERIOD", "日期");
		headMap.put("DAISHOU", "待收");
	
		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields4() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("QIXIAN", "期限（月）");
		headMap.put("MONEY", "销售金额（万元）");
	
		return headMap;

	}
	private Map<String, String> getDayListExcelFields5() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("D_DAY", "日期");
		headMap.put("SALE_NIAN_TENDER", "年化销售额");
		headMap.put("SALE", "销售额");
	
		return headMap;

	}


	
	
}
