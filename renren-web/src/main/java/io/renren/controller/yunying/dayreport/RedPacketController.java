package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@RequestMapping(value = "/yunying/hongbao")
public class RedPacketController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="月度运营成本";
	
	/**
	 * 存管红包明细列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit,String statPeriod) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		String invest_month="";
		String firstDay="";
		if (StringUtils.isNotEmpty(statPeriod)) {
			invest_month = statPeriod.substring(0,7);
			firstDay=statPeriod+"-01";
	}
		int year = Integer.parseInt(statPeriod.substring(0,4));
		int month = Integer.parseInt(statPeriod.substring(6,7));
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/redPacket.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investMonthTime}", firstDay);
			detail_sql = detail_sql.replace("${invest_month}", invest_month);
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

		System.err.println("++++++++月度运营成本查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist")
	public R daylist1(Integer page, Integer limit, String statPeriod) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		
		String firstDay="";
		int year = Integer.parseInt(statPeriod.substring(0,4));
		int month = Integer.parseInt(statPeriod.substring(6,7));
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);//最后一天
		String invest_month="";
		String wuMonth="";
		String firstMonth="";
		if (StringUtils.isNotEmpty(statPeriod)) {
			invest_month = statPeriod.substring(0,7);// 2017-05
			firstMonth = statPeriod.substring(0,4)+"01";// 201701
			wuMonth = invest_month.replace("-", "");//201705
			firstDay=statPeriod+"-01";
	}
		

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/redPacketHz.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investMonthTime}", statPeriod);
			detail_sql = detail_sql.replace("${invest_month}", invest_month);
			detail_sql = detail_sql.replace("${wuMonth}", wuMonth);
			detail_sql = detail_sql.replace("${firstMonth}", firstMonth);
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

		System.err.println("++++++++红包成本汇总查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/ddylist2")
	public R daylist2(Integer page, Integer limit, String statPeriod) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		String invest_month="";
		String firstDay="";
		if (StringUtils.isNotEmpty(statPeriod)) {
			invest_month = statPeriod.substring(0,7);
			firstDay=statPeriod+"-01";
	}
		int year = Integer.parseInt(statPeriod.substring(0,4));
		int month = Integer.parseInt(statPeriod.substring(6,7));
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);

		

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/hongbaomx.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investMonthTime}", firstDay);
			detail_sql = detail_sql.replace("${invest_month}", invest_month);
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

		System.err.println("++++++++红包成本汇总查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String invest_month="";
			String firstDay="";
			if (StringUtils.isNotEmpty(statPeriod)) {
				invest_month = statPeriod.substring(0,7);
				firstDay=statPeriod+"-01";
		}
			int year = Integer.parseInt(statPeriod.substring(0,4));
			int month = Integer.parseInt(statPeriod.substring(6,7));
			String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/redPacket.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investMonthTime}", firstDay);
			detail_sql = detail_sql.replace("${invest_month}", invest_month);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			String firstDay="";
			int year = Integer.parseInt(statPeriod.substring(0,4));
			int month = Integer.parseInt(statPeriod.substring(6,7));
			String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);//最后一天
			String invest_month="";
			String wuMonth="";
			String firstMonth="";
			if (StringUtils.isNotEmpty(statPeriod)) {
				invest_month = statPeriod.substring(0,7);// 2017-05
				firstMonth = statPeriod.substring(0,4)+"01";// 201701
				wuMonth = invest_month.replace("-", "");//201705
				firstDay=statPeriod+"-01";
		}
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/redPacketHz.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investMonthTime}", statPeriod);
			detail_sql = detail_sql.replace("${invest_month}", invest_month);
			detail_sql = detail_sql.replace("${wuMonth}", wuMonth);
			detail_sql = detail_sql.replace("${firstMonth}", firstMonth);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList2.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		R r=daylist2(1, 1000000, statPeriod);
//		PageUtils pageUtil = (PageUtils) r.get("page");	
//		
//		List<Map<String,Object>> resultList3 = (List<Map<String, Object>>) pageUtil.getList();
		
		
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = getDayListExcelFields();
		String title = "存管版红包明细";
		
		// 查询列表数据
		JSONArray va2 = new JSONArray();
		for (int i = 0; i < resultList2.size(); i++) {
			va2.add(resultList2.get(i));
		}
		
		Map<String, String> headMap2 = getDayListExcelFields2();
		String title2 = "运营成本汇总";
		
		// 查询列表数据
//		JSONArray va3 = new JSONArray();
//		for (int i = 0; i < resultList.size(); i++) {
//			va3.add(resultList3.get(i));
//		}
//		Map<String, String> headMap3 = getDayListExcelFields3();
//		String title3 = "存管版红包明细-two";
		
		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);
//		titleList.add(title3);

		
		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);
//		headMapList.add(headMap3);

		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);
//		ja.add(va3);


		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);

//		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("NAME", "渠道名称");
		headMap.put("ID", "券号NID/红包模板ID");
		headMap.put("FAFANG", "发放人数");
		headMap.put("SHIYONG", "使用人数");
		headMap.put("ZMONEY", "使用总金额(元)");
		headMap.put("FIRSTMONEY", "用户首投时使用金额(元)");
		headMap.put("RED", "红包所属系统");
		headMap.put("TIME", "数据统计周期");
		headMap.put("YONGTU", "用途");
		headMap.put("BUMEN", "所属于部门");
		headMap.put("CHENGBEN", "成本分摊方式");
		headMap.put("SHICHANGFEIYONG", "市场部费用(元)");
		headMap.put("YUNYINGFEIYONG", "运营部费用(元)");
		return headMap;

	}
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("HONGBAO", "一级科目");
		headMap.put("YUNYING", "二级");
		headMap.put("CUNGUAN", "归属系统");
		headMap.put("TIME", "统计周期");
		headMap.put("YUNYINGFEIYONG", "成本");

		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields3() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("NAME", "渠道名称");
		headMap.put("ID", "券号NID/红包模板ID");
		headMap.put("FAFANG", "发放人数");
		headMap.put("SHIYONG", "使用人数");
		headMap.put("ZMONEY", "使用总金额(元)");
		headMap.put("FIRSTMONEY", "用户前3次投资使用金额(元)"); 
		headMap.put("RED", "红包所属系统");
		headMap.put("TIME", "数据统计周期");
		headMap.put("YONGTU", "用途");
		headMap.put("BUMEN", "所属于部门");
		headMap.put("CHENGBEN", "成本分摊方式");
		headMap.put("SHICHANGFEIYONG", "市场部费用(元)");
		headMap.put("YUNYINGFEIYONG", "运营部费用(元)");
		return headMap;

	}	
	

	
}
