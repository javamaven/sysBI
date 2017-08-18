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
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/zb2")
public class OperateZbController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="周报之理财计划";


	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
//	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String begin_time,String end_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		if (StringUtils.isNotEmpty(begin_time)) {
			begin_time = begin_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(end_time)) {
			end_time = end_time.replace("-", "");
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/未来14天理财计划解锁金额.txt"));
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${end_time}", end_time);
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

		System.err.println("++++++++周报二期查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist")
//	@RequiresPermissions("phonesale:list")
	public R daylist1(Integer page, Integer limit, String begin_time,String end_time) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		if (StringUtils.isNotEmpty(begin_time)) {
			begin_time = begin_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(end_time)) {
			end_time = end_time.replace("-", "");
		}

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/过去一周每天申请退出的金额.txt"));
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${end_time}", end_time);
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

		System.err.println("++++++++过去一周每天申请退出的金额查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist2")
//	@RequiresPermissions("phonesale:list")
	public R daylist2(Integer page, Integer limit, String begin_time,String end_time) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		if (StringUtils.isNotEmpty(begin_time)) {
			begin_time = begin_time.replace("-", "");
		}
		if (StringUtils.isNotEmpty(end_time)) {
			end_time = end_time.replace("-", "");
		}


		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/每天理财计划底层资产还款金额.txt"));
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${end_time}", end_time);
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

		System.err.println("++++++++每天理财计划底层资产还款金额细查询耗时：" + (l2 - l1));
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
		String begin_time = map.get("begin_time") + "";
		String end_time = map.get("end_time") + "";
		R r=daylist(1, 1000000, begin_time, end_time);
		PageUtils pageUtil = (PageUtils) r.get("page");
		
		
		R r2=daylist1(1, 1000000, begin_time, end_time);
		PageUtils pageUtil2 = (PageUtils) r2.get("page");
		R r3=daylist2(1, 1000000, begin_time, end_time);
		PageUtils pageUtil3 = (PageUtils) r3.get("page");
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();
		List<Map<String,Object>> resultList2 = (List<Map<String, Object>>) pageUtil2.getList();
		List<Map<String,Object>> resultList3 = (List<Map<String, Object>>) pageUtil3.getList();
//		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "理财计划解锁金额";
		headMap = getDayListExcelFields();
		// 查询列表数据
		JSONArray va2 = new JSONArray();
		for (int i = 0; i < resultList2.size(); i++) {
			va2.add(resultList2.get(i));
		}
		Map<String, String> headMap2 = null;
		String title2 = "申请退出的金额";
		headMap2 = getDayListExcelFields2();
		// 查询列表数据
		JSONArray va3 = new JSONArray();
		for (int i = 0; i < resultList3.size(); i++) {
			va3.add(resultList3.get(i));
		}
		Map<String, String> headMap3 = null;
		String title3 = "理财计划底层资产还款金额";
		headMap3 = getDayListExcelFields3();
		
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

		headMap.put("TIME", "日期");
		headMap.put("MONEY", "解锁金额");

		return headMap;

	}
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "日期");
		headMap.put("MONEY", "退出金额");
		return headMap;

	}
	private Map<String, String> getDayListExcelFields3() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "日期");
		headMap.put("MONEY", "还款金额");

		return headMap;

	}

}
