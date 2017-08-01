package io.renren.controller.yunying.phonesale;

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

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/phonesale")
public class PhoneSaleController {

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
	 * 日报列表
	 */
	@ResponseBody
	@RequestMapping("/daylist")
	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String investEndTime) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			Thread queryThread = new Thread(
					new QueryListThread(dataSourceFactory, resultList, start, end, "day", investEndTime));
			Thread totalThread = new Thread(
					new QueryListTotalThread(dataSourceFactory, totalList, "day", investEndTime));
			queryThread.start();
			totalThread.start();
			queryThread.join();
			totalThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++电销日报明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/monthlist")
	@RequiresPermissions("phonesale:list")
	public R list(Integer page, Integer limit, String statPeriod) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

		try {
			Thread queryThread = new Thread(
					new QueryListThread(dataSourceFactory, resultList, start, end, "month", ""));
			Thread totalThread = new Thread(new QueryListTotalThread(dataSourceFactory, totalList, "month", ""));
			queryThread.start();
			totalThread.start();
			queryThread.join();
			totalThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("+++++++++++++++resultList+++++++++++++" + resultList.size());

		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++电销月报明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String investEndTime = map.get("investEndTime") + "";
		String selectType = map.get("selectType") + "";
		String reportType = map.get("reportType") + "";
		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {
			if ("list".equals(selectType) || "day".equals(reportType)) {
				Thread queryThread = new Thread(
						new QueryListThread(dataSourceFactory, dataList, 0, 100 * 10000, reportType, investEndTime));
				queryThread.start();
				queryThread.join();
			} else if ("total".equals(selectType)) {
				String path = this.getClass().getResource("/").getPath();
				String detail_sql;
				String startTime = "  CASE WHEN mark=1 THEN TO_CHAR(tud.call_date,'yyyyMMdd') ELSE CASE WHEN TO_CHAR(SYSDATE,'yyyyMMdd') BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'yyyyMM')||'01' AND TO_CHAR(SYSDATE,'yyyyMM')||'04' THEN TO_CHAR(ADD_MONTHS(SYSDATE,-1),'yyyyMM')||'01' ELSE TO_CHAR(SYSDATE,'yyyyMM')||'01' END END ";
				String endTime = " CASE WHEN TO_CHAR(SYSDATE, 'yyyyMMdd') BETWEEN TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), 'yyyyMM') || '01' AND TO_CHAR (SYSDATE, 'yyyyMM') || '04' THEN TO_CHAR (SYSDATE, 'yyyyMM') || '03' ELSE TO_CHAR ( ADD_MONTHS (SYSDATE, 1), 'yyyyMM' ) || '03' END ";
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "phone_sale_month_total_sql.txt"));
				detail_sql = detail_sql.replace("${investStartTime}", startTime);
				detail_sql = detail_sql.replace("${investEndTime}", endTime);
				dataList.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			}else if ("day_report_total".equals(selectType)) {
				String path = this.getClass().getResource("/").getPath();
				String detail_sql;
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "phone_sale_month_total_sql.txt"));
				String endTime = investEndTime;
				String startTime = endTime.substring(0 , 6) + "01";
				detail_sql = detail_sql.replace("${investStartTime}", startTime);
				detail_sql = detail_sql.replace("${investEndTime}", endTime);
				dataList.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "";
		if ("day".equals(reportType)) {
			headMap = getDayListExcelFields();
			title = "电销数据-日报-明细-" + investEndTime;
		} else {
			if ("list".equals(selectType)) {
				headMap = getMonthListExcelFields();
				title = "电销数据-月度-明细";
			} else if ("total".equals(selectType)) {
				headMap = getMonthTotalExcelFields();
				title = "电销数据-月度-汇总";
			}else if ("day_report_total".equals(selectType)) {
				headMap = getMonthTotalExcelFields();
				title = "电销数据-日报-汇总-" + investEndTime;
			}
		}

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("统计日期", "统计日期");

		headMap.put("用户名", "用户名");
		headMap.put("用户姓名", "用户姓名");
		headMap.put("是否双系统", "是否双系统");

		headMap.put("电销人员", "电销人员");
		headMap.put("电销结果", "电销结果");
		headMap.put("电销日期", "电销日期");

		headMap.put("是否投资", "是否投资");
		headMap.put("接通后-实际投资金额", "接通后-实际投资金额");
		headMap.put("接通后-销售奖励金额", "接通后-销售奖励金额");

		headMap.put("接通后-年化投资金额", "接通后-年化投资金额");
		headMap.put("接通后-投资次数", "接通后-投资次数");
		headMap.put("接通后-首次投资时间", "接通后-首次投资时间");

		headMap.put("接通后-末次投资时间", "接通后-末次投资时间");
		headMap.put("接通前-实际投资金额", "接通前-实际投资金额");
		headMap.put("接通前-销售奖励金额", "接通前-销售奖励金额");

		headMap.put("接通前-年化投资金额", "接通前-年化投资金额");
		headMap.put("接通前-投资次数", "接通前-投资次数");
		headMap.put("接通前-首次投资时间", "接通前-首次投资时间");
		headMap.put("接通前-末次投资时间", "接通前-末次投资时间");

		headMap.put("当月是否投资", "当月是否投资");
		headMap.put("当月投资次数", "当月投资次数");
		headMap.put("当月实际投资金额", "当月实际投资金额");
		headMap.put("当月销售奖励金额", "当月销售奖励金额");

		headMap.put("当月年化投资金额", "当月年化投资金额");
		headMap.put("当月首次投资时间", "当月首次投资时间");
		headMap.put("当月末次投资时间", "当月末次投资时间");

		headMap.put("当天是否投资", "当天是否投资");
		headMap.put("当天实际投资金额", "当天实际投资金额");
		headMap.put("当天销售奖励金额", "当天销售奖励金额");

		headMap.put("当天年化投资金额", "当天年化投资金额");
		headMap.put("当天投资次数", "当天投资次数");

		return headMap;

	}

	private Map<String, String> getMonthTotalExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("电销人员", "电销人员");
		headMap.put("客户总量(人)", "客户总量(人)");
		headMap.put("有效客户外呼数(人)", "有效客户外呼数(人)");

		headMap.put("投资人数(人)", "投资人数(人)");
		headMap.put("投资总额(元)", "投资总额(元)");
		headMap.put("年化投资总额(元)", "年化投资总额(元)");

		headMap.put("人均年化投资额(元)", "人均年化投资额(元)");
		headMap.put("接通率", "接通率");
		headMap.put("有效外呼转化率", "有效外呼转化率");

		headMap.put("总体转化率", "总体转化率");

		return headMap;
	}

	public Map<String, String> getMonthListExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("用户名", "用户名");
		headMap.put("用户姓名", "用户姓名");
		headMap.put("是否双系统", "是否双系统");

		headMap.put("电销人员", "电销人员");
		headMap.put("电销结果", "电销结果");
		headMap.put("电销日期", "电销日期");

		headMap.put("是否投资", "是否投资");
		headMap.put("接通后-实际投资金额", "接通后-实际投资金额");
		headMap.put("接通后-销售奖励金额", "接通后-销售奖励金额");

		headMap.put("接通后-年化投资金额", "接通后-年化投资金额");
		headMap.put("接通后-投资次数", "接通后-投资次数");
		headMap.put("接通后-首次投资时间", "接通后-首次投资时间");

		headMap.put("接通后-末次投资时间", "接通后-末次投资时间");
		headMap.put("接通前-实际投资金额", "接通前-实际投资金额");
		headMap.put("接通前-销售奖励金额", "接通前-销售奖励金额");

		headMap.put("接通前-年化投资金额", "接通前-年化投资金额");
		headMap.put("接通前-投资次数", "接通前-投资次数");
		headMap.put("接通前-首次投资时间", "接通前-首次投资时间");

		headMap.put("接通前-末次投资时间", "接通前-末次投资时间");
		
		headMap.put("当日投资次数", "当日投资次数");
		headMap.put("当日投资金额", "当日投资金额");
		headMap.put("当日年化金额", "当日年化金额");
		headMap.put("当月投资次数", "当月投资次数");
		headMap.put("当月投资金额", "当月投资金额");
		headMap.put("当月年化金额", "当月年化金额");
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
			}else{
				endTime = investEndTime.replace("-", "");
				startTime = endTime.substring(0 , 6) + "01";
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
					detail_sql = detail_sql.replace("${investStartTime}", investEndTime.substring(0, 6) + "01");
					detail_sql = detail_sql.replace("${month}", investEndTime.substring(0, 6));
				} else if ("month".equals(reportType)) {
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "phone_sale_month_detail_sql.txt"));
					String currDayStr = DateUtil.getCurrDayStr();
					String monthsBefore = DateUtil.getMonthsBefore(currDayStr, 1);
					int day = Integer.parseInt(currDayStr.substring(6, 8));
					String startDate = null;
					String endDate = null;
					startDate = monthsBefore.substring(0, 6) + "01";
					if(day >= 4){//大于等于4号
						endDate = currDayStr.substring(0, 6) + "03";
					}else{
						endDate = DateUtil.getCurrDayBefore(1);
					}
					detail_sql = detail_sql.replace("${startDate}", startDate);
					detail_sql = detail_sql.replace("${endDate}", endDate);
				}
				// 分页查询
				detail_sql = detail_sql.replace("${selectSql}", "*");
				detail_sql = detail_sql.replace("${pageStartSql}", "and RN > " + start);
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
					detail_sql = detail_sql.replace("${investStartTime}", investEndTime.substring(0, 6) + "01");
					detail_sql = detail_sql.replace("${month}", investEndTime.substring(0, 6));
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
