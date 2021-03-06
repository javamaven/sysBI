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
@RequestMapping(value = "/yunying/yyp2p")
public class MonthlyReportYyController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="运营月报";
	
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
	public R daylist(Integer page, Integer limit, String invest_end_time,String invest_month_time) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {


			String currDate = sdf.format(new Date());
//			String currDate = "201612";
			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			for (int i = 1; i <= month; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				System.err.println("查询日期" + lastDayOfMonth);
				
				String path = this.getClass().getResource("/").getPath();
				String detail_sql;
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/Yymonthly.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				list.add(new HashMap<String, Object>());
				resultList.add(list.get(0));
			}
		
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
	public R daylist1(Integer page, Integer limit, String invest_month_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;


		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		
		

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyXm.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", invest_month_time);
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

		System.err.println("++++++++运营周报明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/huikuanlist")
	@RequiresPermissions("phonesale:list")
	public R huiKuanList(Integer page, Integer limit, String invest_month_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;


		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyHk.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", invest_month_time);
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

		System.err.println("++++++++电销日报明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/zichanlist")
	@RequiresPermissions("phonesale:list")
	public R ziChanList(Integer page, Integer limit, String invest_month_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");

		}
		String bbday="";
		bbday=invest_month_time;
		String lastday="";
		lastday=invest_month_time+"01";
		String time="";
		time=DateUtil.getLastDayOfMonth(invest_month_time);
		if (StringUtils.isNotEmpty(time)) {
			time = time.replace("-", "");

		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZc.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", time);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${bbday}", bbday);
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

		System.err.println("++++++++电销日报明细查询耗时：" + (l2 - l1));
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
		String invest_month_time = map.get("invest_month_time") + "";
		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		
		String bbday="";
		bbday=invest_month_time;
		String lastday="";
		lastday=invest_month_time+"01";
		String time="";
		time=DateUtil.getLastDayOfMonth(invest_month_time);
		if (StringUtils.isNotEmpty(time)) {
			time = time.replace("-", "");

		}
		
		List<Map<String, Object>> resultList3 = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyZc.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", time);
			detail_sql = detail_sql.replace("${lastday}", lastday);
			detail_sql = detail_sql.replace("${bbday}", bbday);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList3.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//查询待收
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {


			String currDate = sdf.format(new Date());
			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			for (int i = 1; i < month; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				System.err.println("查询日期" + lastDayOfMonth);
				
				String path = this.getClass().getResource("/").getPath();
				String detail_sql;
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/Yymonthly.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				list.add(new HashMap<String, Object>());
				resultList.add(list.get(0));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//查询项目
		List<Map<String, Object>> resultList1 = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyXm.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", invest_month_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList1.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//查询回款
		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/YymonthlyHk.txt"));
			detail_sql = detail_sql.replace("${invest_month_time}", invest_month_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList2.addAll(list);
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
		Map<String, String> headMap = getDsListExcelFields();
		String title = "待收金额（万元）";
		// 查询列表数据
		JSONArray va2 = new JSONArray();
		for (int i = 0; i < resultList1.size(); i++) {
			va2.add(resultList1.get(i));
		}
		Map<String, String> headMap2 = getXmListExcelFields();
		String title2 = "项目年华投资金额（万元）";
		// 查询列表数据
		JSONArray va3 = new JSONArray();
		for (int i = 0; i < resultList2.size(); i++) {
			va3.add(resultList2.get(i));
		}
		Map<String, String> headMap3 = getHkListExcelFields();
		String title3 = "回款金额（万元）";
		// 查询列表数据
		JSONArray va4 = new JSONArray();
		for (int i = 0; i < resultList3.size(); i++) {
			va4.add(resultList3.get(i));
		}
		Map<String, String> headMap4 = getHkListExcelFields22();
		String title4 = "销售的资产期限";
		
		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);
		titleList.add(title3);
		titleList.add(title4);
		
		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);
		headMapList.add(headMap3);
		headMapList.add(headMap4);
		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);
		ja.add(va3);
		ja.add(va4);

		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
//		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	private Map<String, String> getDsListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("DAYS", "日期");
		headMap.put("WAIT", "待收金额（万元）");

		return headMap;

	}
	private Map<String, String> getXmListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("MONTH", "月份");
		headMap.put("TENDER", "项目年华投资金额（万元）");

		return headMap;

	}
	
	private Map<String, String> getHkListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("MONTH", "月份");
		headMap.put("MONEY", "回款金额（万元）");


		return headMap;

	}
	private Map<String, String> getHkListExcelFields22() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("MONTH", "日期");
		headMap.put("QIXIAN", "资产期限");
		headMap.put("MONEY", "资产金额");


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
