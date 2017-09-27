package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.NumberUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/daishouqujian")
public class DaiShouQuJianController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="待收区间";

	
	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/待收区间.txt"));
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

		System.err.println("++++++++待收区间查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/ddylist")
	public R ddylist(Integer page, Integer limit) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/年龄区间.txt"));
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

		System.err.println("++++++++年龄区间查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		R r=daylist(1, 10000);
		PageUtils pageUtil = (PageUtils) r.get("page");	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);
			Object num = map.get("NUM_RATE");
			Object await = map.get("AWAIT_RATE");
			Object avg = map.get("AVG_APR");
			map.put("NUM_RATE", NumberUtil.keepPrecision(Double.parseDouble(num.toString())*100, 2) +"%");
			map.put("AWAIT_RATE", NumberUtil.keepPrecision(Double.parseDouble(await.toString())*100, 2) +"%");
			map.put("AVG_APR", NumberUtil.keepPrecision(Double.parseDouble(avg.toString())*100, 2) +"%");
			va.add(map);
		}
		Map<String, String> headMap = null;
		String title = "待收区间数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportMonthListExcel2(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		R r=ddylist(1, 10000);
		PageUtils pageUtil = (PageUtils) r.get("page");	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);
			Object num = map.get("NUM_RATE");
			Object await = map.get("AWAIT_RATE");
//			Object avg = map.get("AVG_APR");
			map.put("NUM_RATE", NumberUtil.keepPrecision(Double.parseDouble(num.toString())*100, 2) +"%");
			map.put("AWAIT_RATE", NumberUtil.keepPrecision(Double.parseDouble(await.toString())*100, 2) +"%");
//			map.put("AVG_APR", NumberUtil.keepPrecision(Double.parseDouble(avg.toString())*100, 2) +"%");
			va.add(map);
		}
		Map<String, String> headMap = null;
		String title = "年龄区间数据";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("G_AWAIT", "待收本金区间");
		headMap.put("NUM", "人数");
		headMap.put("NUM_RATE", "人数占比");
		headMap.put("AWAIT", "待收本金");
		headMap.put("AWAIT_RATE", "待收占比");
		headMap.put("AVG_AWAIT", "人均待收");
		headMap.put("AVG_PERIOD", "加权期限");
		headMap.put("AVG_APR", "加权利率");
		headMap.put("INTEREST", "当日利息");

		return headMap;

	}

	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("G_OLDS", "年龄区间");
		headMap.put("NUM", "人数");
		headMap.put("NUM_RATE", "人数占比");
		headMap.put("AWAIT", "待收本金");
		headMap.put("AWAIT_RATE", "待收占比");
		headMap.put("AVG_AWAIT", "人均待收");
		
		headMap.put("MAN", "男性人数");
		headMap.put("WOMAN", "女性人数");
		headMap.put("WEIZHI", "性别未知人数");
		
		headMap.put("AVG_MAN_AWAIT", "男性人均待收");
		headMap.put("AVG_WOMAN_AWAIT", "女性人均待收");
		headMap.put("AVG_WEIZHI_AWAIT", "性别未知人均待收");

		return headMap;

	}




}
