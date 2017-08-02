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
	public R daylist(Integer page, Integer limit,String statPeriod) {
//		if (StringUtils.isNotEmpty(invest_month_time)) {
//			invest_month_time = invest_month_time.replace("-", "");
//		}
		
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
	@RequiresPermissions("phonesale:list")
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
		
		
		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);

		
		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);

		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);


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
