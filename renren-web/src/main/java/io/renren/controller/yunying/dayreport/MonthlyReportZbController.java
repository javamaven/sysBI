package io.renren.controller.yunying.dayreport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/zbp2p")
public class MonthlyReportZbController {


	@Autowired
	private DataSourceFactory dataSourceFactory;

	/**
	 * 上传文件
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	@RequiresPermissions("phonesale:upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String[] fields = { "number", "user_name", "real_name", "register_time", "call_sale", "call_date",
					"call_result", "is_invest", "real_invest_amount", "sale_jiangli_amount", "year_invest_amount",
					"first_invest_time" };
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");

			// 清空表
			String tuncate_sql = "truncate table phone_sale_excel_data ";
			new JdbcUtil(dataSourceFactory, "oracle26").execute(tuncate_sql);
			// 插入表
			String sql = "insert into phone_sale_excel_data values(?,?,?,?,?,?)";
			List<List<Object>> dataList = getInsertDataList(list);
			new JdbcUtil(dataSourceFactory, "oracle26").batchInsert(sql, dataList);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}

	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		
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
	@RequiresPermissions("phonesale:list")
	public R daylist1(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		String lastSevenday="";
		String lastday="";
		String day="";
		String dayday="";
		String afterday="";
		lastSevenday = DateUtil.getCurrDayBefore(invest_end_time, 7, "yyyy-MM-dd");
		lastday = DateUtil.getCurrDayBefore(lastSevenday, 6, "yyyy-MM-dd");
		afterday=DateUtil.getCurrDayBefore(invest_end_time, -6, "yyyy-MM-dd");;
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			dayday = invest_stat_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_end_time)) {
			day = invest_end_time.replace("-", "");
		}
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
					}else{
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
					}else{
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
					}else{
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
					}else{
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
	@RequiresPermissions("phonesale:list")
	public R dslist(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {

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
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String invest_end_time = map.get("invest_end_time") + "";
		String invest_stat_time = map.get("invest_stat_time") + "";
		String lastSevenday="";
		String lastday="";
		String day="";
		String dayday="";
		String afterday="";
		lastSevenday = DateUtil.getCurrDayBefore(invest_end_time, 7, "yyyy-MM-dd");
		lastday = DateUtil.getCurrDayBefore(lastSevenday, 6, "yyyy-MM-dd");
		afterday=DateUtil.getCurrDayBefore(invest_end_time, -6, "yyyy-MM-dd");;
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
					}else{
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
					}else{
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
					}else{
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
					}else{
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
		
		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);
		titleList.add(title3);
		
		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);
		headMapList.add(headMap3);
		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);
		ja.add(va3);

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


	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");

	private List<List<Object>> getInsertDataList(List<Map<String, Object>> excelList) {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		List<Object> list = null;
		for (int i = 0; i < excelList.size(); i++) {
			list = new ArrayList<Object>();
			Map<String, Object> map = excelList.get(i);
			list.add(map.get("user_name") + "");
			list.add(2);
			try {
				list.add(dateSdf.parse(map.get("call_date") + ""));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			list.add(map.get("call_result") + "");// 打电话结果
			list.add(map.get("call_sale") + "");// 电销人员
			list.add(null);

			dataList.add(list);
		}
		return dataList;
	}

	/**
	 * MultipartFile 转换成File
	 * 
	 * @param multfile
	 *            原文件类型
	 * @return File
	 * @throws IOException
	 */
	private File multipartToFile(MultipartFile multfile) throws IOException {
		CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
		// 这个myfile是MultipartFile的
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		// 手动创建临时文件
		if (file.length() < 2048) {
			File tmpFile = new File(
					System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getName());
			multfile.transferTo(tmpFile);
			return tmpFile;
		}
		return file;
	}

	/**
	 * 电销日报月报：列表分页列表
	 * 
	 * @author Administrator
	 *
	 */
	class QueryListThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> resultList;
		private int start;
		private int end;
		private String reportType;// day , month
		private String investEndTime;

		public QueryListThread(DataSourceFactory dataSourceFactory, List<Map<String, Object>> resultList, int start,
				int end, String reportType, String investEndTime) {
			this.dataSourceFactory = dataSourceFactory;
			this.resultList = resultList;
			this.start = start;
			this.end = end;
			this.reportType = reportType;
			this.investEndTime = investEndTime == null ? "" : investEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				if ("day".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_day_detail_sql.txt"));
				} else if ("month".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_month_detail_sql.txt"));
				}
				// 分页查询
				detail_sql = detail_sql.replace("${selectSql}", "*");
				detail_sql = detail_sql.replace("${pageStartSql}", "and RN >= " + start);
				detail_sql = detail_sql.replace("${pageEndSql}", "and ROWNUM <= " + end);
				detail_sql = detail_sql.replace("${investEndTime}", investEndTime);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 电销月报：列表总行数
	 * 
	 * @author Administrator
	 *
	 */
	class QueryListTotalThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> totalList;
		private String reportType;
		private String investEndTime;

		public QueryListTotalThread(DataSourceFactory dataSourceFactory, List<Map<String, Object>> totalList,
				String reportType, String investEndTime) {
			this.dataSourceFactory = dataSourceFactory;
			this.totalList = totalList;
			this.reportType = reportType;
			this.investEndTime = investEndTime == null ? "" : investEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				if ("day".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_day_detail_sql.txt"));
				} else if ("month".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_month_detail_sql.txt"));
				}
				detail_sql = detail_sql.replace("${selectSql}", "count(1) total");
				detail_sql = detail_sql.replace("${pageStartSql}", "");
				detail_sql = detail_sql.replace("${pageEndSql}", "");
				detail_sql = detail_sql.replace("${investEndTime}", investEndTime);
				totalList.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
