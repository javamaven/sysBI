package io.renren.controller.shichang;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MapUtil;
import io.renren.util.NumberUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 渠道首投用戶大盤全景
 */
@RestController
@RequestMapping("/channel/st")
public class ChannelAnalyseController {
	
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	UserBehaviorService userBehaviorService;
	
	@ResponseBody
	@RequestMapping("/channelZhiliangList")
	public R channelZhiliangList(Integer page, Integer limit, String startDate, String endDate) {
		List<Map<String, Object>> dataList = null;
		String detail_sql = null;
		try {
			startDate = startDate.replace("-", "");
			endDate = endDate.replace("-", "");
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/渠道分析/渠道质量.txt"));
			detail_sql = detail_sql.replace("${startDate}", startDate);
			detail_sql = detail_sql.replace("${endDate}", endDate);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		PageUtils pageUtil = new PageUtils(dataList, dataList.size(), limit, page);
		return R.ok().put("page", pageUtil );
	}
	
	@ResponseBody
	@RequestMapping("/channelUserLiuCunList")
	public R channelUserLiuCunList(Integer page, Integer limit, String month) {
		List<Map<String, Object>> dataList = null;
		String detail_sql = null;
		try {
			month = month.replace("-", "");
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/渠道分析/渠道用户留存分析.txt"));
			detail_sql = detail_sql.replace("${month}", month);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		PageUtils pageUtil = new PageUtils(dataList, dataList.size(), limit, page);
		return R.ok().put("page", pageUtil );
	}
	
	@ResponseBody
	@RequestMapping("/channelWithdrawList")
	public R channelWithdrawList(Integer page, Integer limit, String startDate, String endDate) {
		List<Map<String, Object>> dataList = null;
		String detail_sql = null;
		try {
			startDate = startDate.replace("-", "");
			endDate = endDate.replace("-", "");
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/渠道分析/渠道提现用户预警.txt"));
			detail_sql = detail_sql.replace("${startDate}", startDate);
			detail_sql = detail_sql.replace("${endDate}", endDate);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);
			String v1 = MapUtil.getValue(map, "当月首投用户占比");
			String v2 = MapUtil.getValue(map, "上月首投用户占比");
			String v3 = MapUtil.getValue(map, "两月前首投用户占比");
			String v4 = MapUtil.getValue(map, "三月前首投用户占比");
			double total = 1;
			if(!StringUtils.isEmpty(v1)){
				total = total - Double.parseDouble(v1);
			}
			if(!StringUtils.isEmpty(v2)){
				total = total - Double.parseDouble(v2);
			}
			if(!StringUtils.isEmpty(v3)){
				total = total - Double.parseDouble(v3);
			}
			if(!StringUtils.isEmpty(v4)){
				total = total - Double.parseDouble(v4);
			}
			map.put("其他用户占比", NumberUtil.keepPrecision(total, 4));
		}
		
		PageUtils pageUtil = new PageUtils(dataList, dataList.size(), limit, page);
		return R.ok().put("page", pageUtil );
	}
	
	
	@ResponseBody
	@RequestMapping("/channelUserHuoyueLiuCunList")
	public R channelUserHuoyueLiuCunList(Integer page, Integer limit, String month) {
		List<Map<String, Object>> dataList = null;
		String detail_sql = null;
		try {
			month = month.replace("-", "");
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/渠道分析/渠道用户活跃留存分析.txt"));
			detail_sql = detail_sql.replace("${month}", month);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		PageUtils pageUtil = new PageUtils(dataList, dataList.size(), limit, page);
		return R.ok().put("page", pageUtil );
	}
	
	@ResponseBody
	@RequestMapping("/channelDaishouLiuCunList")
	public R channelDaishouLiuCunList(Integer page, Integer limit, String month) {
		List<Map<String, Object>> dataList = null;
		String detail_sql = null;
		try {
			month = month.replace("-", "");
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/渠道分析/渠道待收留存分析.txt"));
			detail_sql = detail_sql.replace("${month}", month);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		PageUtils pageUtil = new PageUtils(dataList, dataList.size(), limit, page);
		return R.ok().put("page", pageUtil );
	}
	
	
	@RequestMapping("/queryChannelList")
	public R queryChannelList(@RequestBody Map<String, Object> params) {
		List<Map<String, Object>> dataList = null;
		String querySql = "select channel_name from dm_report_channel_st_dapan group by channel_name";
		try {
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(querySql);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return R.ok().put("channel_list", dataList);
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String channelName) {
		List<Map<String, Object>> dataList = null;
		String querySql = "select * from dm_report_channel_st_dapan where 1=1 and channel_name=? ";
		List<Map<String, Object>> retDataList = null;
		if(StringUtils.isEmpty(channelName)){
			channelName = "全部渠道";
		}
		try {
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(querySql, channelName);
			Map<String,Map<String,Object>> dataMap = new HashMap<String, Map<String,Object>>();
			System.err.println(dataList);
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String month = map.get("MONTH") + "";
				String CHANNEL_NAME = map.get("CHANNEL_NAME") + "";
				map.put(month + "^" + CHANNEL_NAME, map);
				dataMap.put(month + "^" + CHANNEL_NAME, map);
			}
			retDataList = transData(dataMap, channelName);
		} catch (SQLException e) {
			System.err.println("++++++++error sql +++++++++++ " + querySql);
			e.printStackTrace();
		} 
		
		PageUtils pageUtil = new PageUtils(retDataList, retDataList.size(), 1000, page);
		return R.ok().put("page", pageUtil );
	}
	
	/**
	 * 将查询回来的数据：进行行列转换
	 * @param dataMap
	 * @param channelName
	 * @return
	 */
	private List<Map<String, Object>> transData(Map<String, Map<String, Object>> dataMap, String channelName) {
		String[] indexNameArr = {"注册人数","首投用户数","首投转化率","首投用户当月内投资金额","当月总用户投资总额","首投用户投资金额占比","首投用户人均投资额",
				"首投用户投资次数","首投用户人均投资次数","首投用户红包使用次数","首投用户红包成本","当月总用户红包总成本","首投用户成本占比","首投用户人均红包成本",
				"新手标投资人数","新手标投资额","新手标投资次数","新手标用户占比","新手标占首投用户投资额","人均新手标投资额","新手标红包使用金额","新手标红包使用次数",
				"首投用户30天内投资额","首投用户30天后待收","首投用户60天后待收","首投用户90天后待收","首投用户180天后待收","首投用户30天后留存率","首投用户60天后留存率",
				"首投用户90天后留存率","首投用户180天后留存率","首投用户30天后有待收人数","首投用户60天后有待收人数","首投用户90天后有待收人数","首投用户180天后有待收人数"};
		String[] fiedsArr = {"REG_USER_NUM","ST_USER_NUM","ST_TRAN_RATE","ST_USER_INV_MONEY","ALL_USER_INV_MONEY","ST_USER_INV_MONEY_RATE",
				"ST_USER_AVG_INV_MONEY","ST_USER_INV_TIMES","ST_USER_AVG_INV_TIMES","ST_USER_USE_HONGBAO_TIMES","ST_USER_USE_HONGBAO",
				"ALL_USER_USE_HONGBAO","ST_USER_CHENGBEN_RATE","ST_USER_AVG_HONGBAO","NEW_PROJECT_INV_USERS","NEW_PROJECT_INV_MONEY",
				"NEW_PROJECT_INV_TIMES","NEW_PROJECT_USER_RATE","NEW_PROJECT_ST_USER_RATE","NEW_PROJECT_AVG_INV_MONEY",
				"NEW_PROJECT_USE_HONGBAO","NEW_PROJECT_USE_HONGBAO_TIMES","ST_USER_30DAY_INV","ST_USER_30DAY_WAIT","ST_USER_60DAY_WAIT",
				"ST_USER_90DAY_WAIT","ST_USER_180DAY_WAIT","ST_USER_DAY30_LIUCUN_RATE","ST_USER_DAY60_LIUCUN_RATE","ST_USER_DAY70_LIUCUN_RATE",
				"ST_USER_DAY180_LIUCUN_RATE","ST_USER_DAY30_WAIT_USERS","ST_USER_DAY60_WAIT_USERS","ST_USER_DAY90_WAIT_USERS",
				"ST_USER_DAY180_WAIT_USERS"};
		String[] monthArr = {"201701","201702","201703","201704","201705","201706","201707","201708","201709","201710","201711","201712"};
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < indexNameArr.length; i++) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("月份", indexNameArr[i]);
			for (int j = 0; j < monthArr.length; j++) {
				Map<String, Object> map2 = dataMap.get(monthArr[j] + "^" + channelName);
				if(map2 != null && !map2.isEmpty()){
					map.put(monthArr[j], map2.get(fiedsArr[i]));
				}
			}
			newList.add(map);
		}
		return newList;
	}
	
	
	/**
	 * 渠道首投用户大盘全景导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道首投用户大盘全景";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String channelName = map.get("channelName") + "";
		// 查询列表数据
		R dataR = list(1, 10000, channelName);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("月份","月份");
		headMap.put("201701","201701");
		headMap.put("201702","201702");
		headMap.put("201703","201703");
		headMap.put("201704","201704");
		headMap.put("201705","201705");
		headMap.put("201706","201706");
		headMap.put("201707","201707");
		headMap.put("201708","201708");
		String title = "渠道首投用户大盘全景";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道质量分析导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportZhiliangExcel")
	public void exportZhiliangExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道质量分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String startDate = map.get("startDate") + "";
		String endDate = map.get("endDate") + "";
		// 查询列表数据
		R dataR = channelZhiliangList(1, 10000, startDate, endDate);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称","渠道名称");
		headMap.put("渠道标记","渠道标记");
		headMap.put("类型","类型");
		headMap.put("付费方式","付费方式");
		headMap.put("注册用户","注册用户");
		headMap.put("FST_USER_CNT","首投用户");
		headMap.put("首投用户占比","首投用户占比");
		headMap.put("新手标用户","新手标用户");
		headMap.put("首投用户投资金额","首投用户投资金额");
		
		headMap.put("首投用户红包使用金额","首投用户红包使用金额");
		headMap.put("新手标投资额","新手标投资额");
		headMap.put("新手标红包使用金额","新手标红包使用金额");
		headMap.put("首投用户投资次数","首投用户投资次数");
		
		headMap.put("首投用户红包使用次数","首投用户红包使用次数");
		headMap.put("周期内末日待收金额","周期内末日待收金额");
		headMap.put("人均待收金额","人均待收金额");
		
		headMap.put("人均投资金额","人均投资金额");
		headMap.put("人均红包使用金额","人均红包使用金额");
		
      	
		String title = "渠道质量分析";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportWithdrawExcel")
	public void exportWithdrawExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道提现用户预警";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String startDate = map.get("startDate") + "";
		String endDate = map.get("endDate") + "";
		// 查询列表数据
		R dataR = channelWithdrawList(1, 10000, startDate, endDate);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称","渠道名称");
		headMap.put("渠道标记","渠道标记");
		
		headMap.put("周期内提现用户","周期内提现用户");
		headMap.put("周期末日待收","周期末日待收");
		headMap.put("周期内提现金额","周期内提现金额");
		
		headMap.put("周期内投资总金额","周期内投资总金额");
		headMap.put("提现占投资比例","提现占投资比例");
		headMap.put("人均提现金额","人均提现金额");
		
		headMap.put("当月首投用户占比","当月首投用户占比");
		headMap.put("上月首投用户占比","上月首投用户占比");
		headMap.put("两月前首投用户占比","两月前首投用户占比");
		
		headMap.put("三月前首投用户占比","三月前首投用户占比");
		headMap.put("其他用户占比","其他用户占比");
		
		String title = "渠道提现用户预警";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道用户留存分析导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportUserLiucunExcel")
	public void exportUserLiucunExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道用户留存分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String month = map.get("month") + "";
		// 查询列表数据
		R dataR = channelUserLiuCunList(1, 10000, month);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称","渠道名称");
		headMap.put("渠道标记","渠道标记");
		headMap.put("当月新增首投用户","当月新增首投用户");
		headMap.put("30天后","30天后");
		headMap.put("60天后","60天后");
		headMap.put("90天后","90天后");
		headMap.put("120天后","120天后");
		headMap.put("150天后","150天后");
		headMap.put("180天后","180天后");
		
		headMap.put("270天后","270天后");
		headMap.put("360天后","360天后");
		
		String title = "渠道用户留存分析";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道待收留存分析导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportDaishouLiucunExcel")
	public void exportDaishouLiucunExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道待收留存分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String month = map.get("month") + "";
		// 查询列表数据
		R dataR = channelDaishouLiuCunList(1, 10000, month);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称","渠道名称");
		headMap.put("渠道标记","渠道标记");
		headMap.put("首投用户30天内投资额","首投用户30天内投资额");
		headMap.put("30天后","30天后");
		headMap.put("60天后","60天后");
		headMap.put("90天后","90天后");
		headMap.put("120天后","120天后");
		headMap.put("150天后","150天后");
		headMap.put("180天后","180天后");
		
		headMap.put("270天后","270天后");
		headMap.put("360天后","360天后");
		
		String title = "渠道待收留存分析";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportUserHuoyueLiucunExcel")
	public void exportUserHuoyueLiucunExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="渠道用户活跃留存分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String month = map.get("month") + "";
		// 查询列表数据
		R dataR = channelUserHuoyueLiuCunList(1, 10000, month);
		PageUtils pageUtil = (PageUtils) dataR.get("page");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtil.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称","渠道名称");
		headMap.put("渠道标记","渠道标记");
		headMap.put("新增首投用户","新增首投用户");
		headMap.put("首投用户30天内人均投资次数","首投用户30天内人均投资次数");
		headMap.put("30天后","30天后");
		headMap.put("60天后","60天后");
		headMap.put("90天后","90天后");
		headMap.put("120天后","120天后");
		headMap.put("150天后","150天后");
		headMap.put("180天后","180天后");
		
		headMap.put("270天后","270天后");
		headMap.put("360天后","360天后");
		
		String title = "渠道用户活跃留存分析";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
