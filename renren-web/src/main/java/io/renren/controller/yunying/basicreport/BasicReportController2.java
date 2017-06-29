package io.renren.controller.yunying.basicreport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/basicreport")
public class BasicReportController2 {
	@Autowired
	private BasicReportService service;
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	/**
	 * 电销外呼申请历史数据（注册未投资用户） 导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/phoneSaleHistoryListExport")
	public void phoneSaleHistoryListExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		int limitNum = 100000;
		String limitNumStr = map.get("limitNum") + "";
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		if(StringUtils.isNotEmpty(limitNumStr)){
			limitNum = Integer.parseInt(limitNumStr);
		}
		R retData = phoneSaleHistory(1, limitNum, registerStartTime, registerEndTime, limitNumStr);
		PageUtils page = (PageUtils) retData.get("page");
		
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("存管版用户ID", "存管版用户ID");
		headMap.put("用户名", "用户名");
		headMap.put("电话", "电话");
		
		headMap.put("注册时间", "注册时间");
		headMap.put("用户来源", "用户来源");
		headMap.put("实名认证", "实名认证");
		
		headMap.put("红包类型", "红包类型");
		headMap.put("有效期", "有效期");
		
		
		headMap.put("真实姓名", "真实姓名");
		headMap.put("普通版账户余额", "普通版账户余额");
		headMap.put("存管版账户余额", "存管版账户余额");
		
		headMap.put("普通版用户ID", "普通版用户ID");
		headMap.put("存管版是否开户", "存管版是否开户");
		headMap.put("用户ID", "用户ID");
		
		String title = "外包外呼-注册后未投资用户";
		
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
			//保存已经导出的数据到表phone_sale_history_export
			service.batchInsertPhoneSaleData(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 电销外呼申请历史数据（注册未投资用户）
	 * @param page
	 * @param limit
	 * @param registerStartTime
	 * @param registerEndTime
	 * @param limitNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/phoneSaleHistoryList")
	public R phoneSaleHistory(Integer page, Integer limit, String registerStartTime, String registerEndTime, String limitNum) {
		int size = 0;
		if(StringUtils.isNotEmpty(limitNum)){
			size = Integer.parseInt(limitNum);
		}
		if(size < limit && size > 0){
			limit = size;
		}
		int start = (page - 1) * limit;
		int end = start + limit;
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			registerStartTime += " 00:00:00";
			registerEndTime += " 23:59:59";
			Thread queryThread = new Thread(new QueryPhoneSaleHistoryListThread("list", dataSourceFactory, resultList,
					start, end, registerStartTime, registerEndTime));
			Thread totalThread = new Thread(new QueryPhoneSaleHistoryListThread("total", dataSourceFactory, totalList,
					start, end, registerStartTime, registerEndTime));
			queryThread.start();
			totalThread.start();
			queryThread.join();
			totalThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		if(total > size && size >0){
			total = size;
		}
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);

		return R.ok().put("page", pageUtil);

	}
	
	class QueryPhoneSaleHistoryListThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;
		private String registerStartTime;
		private String registerEndTime;

		public QueryPhoneSaleHistoryListThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list,
				int start, int end, String registerStartTime, String registerEndTime) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start;
			this.end = end;
			this.registerStartTime = registerStartTime == null ? "" : registerStartTime;
			this.registerEndTime = registerEndTime == null ? "" : registerEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/外包外呼历史数据提取.txt"));
				
				if("list".equals(statType)){
					detail_sql = detail_sql.replace("${selectSql}", " * ");
					detail_sql = detail_sql.replace("${pageStartSql}", " and RN >= " + start);
					detail_sql = detail_sql.replace("${pageEndSql}", " and ROWNUM <= " + end);
					
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, registerStartTime, registerEndTime));
				}else if("total".equals(statType)){
					detail_sql = detail_sql.replace("${selectSql}", "count(1) total");
					detail_sql = detail_sql.replace("${pageStartSql}","");
					detail_sql = detail_sql.replace("${pageEndSql}", "");
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, registerStartTime, registerEndTime));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	@ResponseBody
	@RequestMapping("/registNotInvest")
	public R registNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {

		int start = (page - 1) * limit;
		int end = start + limit;
		PageUtils pageUtil = service.queryList(page, limit, registerStartTime, registerEndTime, start, end);

		return R.ok().put("page", pageUtil);

	}
	
	@ResponseBody
	@RequestMapping("/firstInvestNotMulti")
	public R firstInvestNotMulti(Integer page, Integer limit, String statPeriod) {
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("limit", limit);
		map.put("statPeriod", statPeriod);
		PageUtils pageUtil = service.queryFirstInvestNotMultiList(map);
		return R.ok().put("page", pageUtil);

	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportFirstInvestNotMultiExcel")
	public void exportFirstInvestNotMultiExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		PageUtils page = service.queryFirstInvestNotMultiList(map);
		
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFirstInvestNotMultiFields();
		String title = "本月注册首投后三天未复投用户";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	/**
	 * 注册三天未投资用户数据
	 * 
	 * @param page
	 * @param limit
	 * @param registerStartTime
	 * @param registerEndTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/registThreeDaysNotInvest")
	public R registThreeDaysNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {

		int start = (page - 1) * limit;
		int end = start + limit;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registerStartTime", registerStartTime);
		params.put("registerEndTime", registerEndTime);
		List<Map<String, Object>> list = service.queryRegisterThreeDaysNotInvestList(params);

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			if (end > list.size()) {
				retList.addAll(list.subList(start, list.size()));
			} else {
				retList.addAll(list.subList(start, end));
			}
		}
		if (retList.size() > 0) {
			service.getAmontByUserId(retList);// 统计用户的账户余额
		}

		PageUtils pageUtil = new PageUtils(list, list.size(), limit, page);

		return R.ok().put("page", pageUtil);

	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		PageUtils page = service.queryList(1, 10000, registerStartTime, registerEndTime, 0, 10000);
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String Hour = registerEndTime.substring(11 , 13);
		String title = "注册一小时未投资用户-W-" + month + day + "_" + Hour + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	/**
	 * 注册三天未投资导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportRegisterThreeDaysExcel")
	public void exportRegisterThreeDaysExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerEndTime = map.get("registerEndTime") + "";
		List<Map<String, Object>> dataList = service.queryRegisterThreeDaysNotInvestList(map);
		service.getAmontByUserId(dataList);// 统计用户的账户余额
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String title = "注册3天未投资用户-W-" + month + day + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
