package io.renren.controller.yunying.dayreport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import io.renren.service.yunying.dayreport.MonthlyReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/p2p")
public class MonthlyReportController {

	@Autowired
	private MonthlyReportService service;

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
		
		PageUtils pageUtil = service.queryList(page, limit, invest_end_time,invest_stat_time);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++广州P2P查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/ddylist")
	@RequiresPermissions("phonesale:list")
	public R daylist1(Integer page, Integer limit, String invest_end_time,String invest_stat_time) {
		long l1 = System.currentTimeMillis();
	
		PageUtils pageUtil = service.queryList1(page, limit, invest_end_time,invest_stat_time);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++广州P2P五大投资人查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String invest_end_time = map.get("invest_end_time") + "";
		String invest_stat_time = map.get("invest_stat_time") + "";
		if (StringUtils.isNotEmpty(invest_end_time)) {
			invest_end_time = invest_end_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			invest_stat_time = invest_stat_time.replace("-", "");
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/P2P.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
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
		Map<String, String> headMap = null;
		String title = "P2P数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void ListExcel(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String invest_end_time = map.get("invest_end_time") + "";
		String invest_stat_time = map.get("invest_stat_time") + "";
		if (StringUtils.isNotEmpty(invest_end_time)) {
			invest_end_time = invest_end_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(invest_stat_time)) {
			invest_stat_time = invest_stat_time.replace("-", "");
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/five.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investStatTime}", invest_stat_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
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
		Map<String, String> headMap = null;
		String title = "本月前五大投资人";
		headMap = getFiveListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STAT_TYPE", "指标明细");
		headMap.put("STAT_NUM", "指标");

		return headMap;

	}

	private Map<String, String> getFiveListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("REALNAME", "姓名");
		headMap.put("TENDER_CAPITAL", "金额(万元)");

		return headMap;

	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/monthTotalList")
	@RequiresPermissions("phonesale:list")
	public R monthTotalList(Integer page, Integer limit, String investEndTime) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		System.err.println("+++++++++++++++start=" + start + " ;end=" + end);
		List<Map<String, Object>> resultList = null;

		String path = this.getClass().getResource("/").getPath();
		String detail_sql;
		String startTime = "  CASE WHEN mark=1 THEN TO_CHAR(tud.call_date,'yyyyMMdd') ELSE CASE WHEN TO_CHAR(SYSDATE,'yyyyMMdd') BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'yyyyMM')||'01' AND TO_CHAR(SYSDATE,'yyyyMM')||'04' THEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'yyyyMM')||'01' ELSE TO_CHAR(SYSDATE,'yyyyMM')||'01' END END ";
		String endTime = " CASE WHEN TO_CHAR(SYSDATE, 'yyyyMMdd') BETWEEN TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), 'yyyyMM') || '01' AND TO_CHAR (SYSDATE, 'yyyyMM') || '04' THEN TO_CHAR (SYSDATE, 'yyyyMM') || '03' ELSE TO_CHAR ( ADD_MONTHS (SYSDATE, 1), 'yyyyMM' ) || '03' END ";
		try {
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "phone_sale_month_total_sql.txt"));
			if (StringUtils.isEmpty(investEndTime)) {
				detail_sql = detail_sql.replace("${investStartTime}", startTime);
				detail_sql = detail_sql.replace("${investEndTime}", endTime);
			} else {
				endTime = investEndTime.replace("-", "");
				startTime = endTime.substring(0, 6) + "01";
				detail_sql = detail_sql.replace("${investStartTime}", startTime);
				detail_sql = detail_sql.replace("${investEndTime}", endTime);
			}
			resultList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (end > total) {
			retList.addAll(resultList.subList(start, total));
		} else {
			retList.addAll(resultList.subList(start, end));
		}
		PageUtils pageUtil = new PageUtils(retList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++电销月报汇总列表信息查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
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
