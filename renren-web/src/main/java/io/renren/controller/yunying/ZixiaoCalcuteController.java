package io.renren.controller.yunying;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.controller.AbstractController;
import io.renren.entity.ChannelStftInfoEntity;
import io.renren.entity.SysUserEntity;
import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import static io.renren.utils.ShiroUtils.getUserEntity;
import static io.renren.utils.ShiroUtils.getUserId;

@RestController
@RequestMapping("/yunying/zixiao")
public class ZixiaoCalcuteController extends AbstractController {
	
	private String caiwuMubiao = "3.20%";
	private String caiwu_index = "费用/年化比(17年累计)";
	private String yunying_curr_month_invest = "本月年化交易额";
	private String yunying_curr_month_await = "本月销售新增年底待收金额";
	
	private String shichang_reg_user_num = "本月注册人数";
	private String shichang_first_invest_user_num = "本月首投人数";
	private String shichang_curr_month_roi = "本月首投用户本月累投ROI";
	private String shichang_hongbao_cost = "市场部本月红包成本";
	private String shichang_channel_cost = "市场部本月渠道成本";
	private String shichang_phonesale_cost = "市场部电销成本";
	private String shichang_curr_month_channel_year_invest = "当月首投用户本月累投金额";

	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	DruidDataSource dataSource;
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="绩效核算表";
	
	@SuppressWarnings("deprecation")
	public Map<String,Object> getYunyingMubiao(int month){
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		Map<String,Object> mubiaoMap = new HashMap<>();
		String path = this.getClass().getResource("/").getPath();
		try {
			List<String> readLines = FileUtils.readLines(new File(path + File.separator + "sql/绩效/运营部目标.txt"));
			for (int i = 0; i < readLines.size(); i++) {
				String line = readLines.get(i);
				if(line.contains("_" + month + "月")){
					String[] split = line.split(",");
					mubiaoMap.put(split[0], split[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mubiaoMap;
	}
	
	@SuppressWarnings("deprecation")
	public Map<String,Object> getShichangMubiao(int month){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		Map<String,Object> mubiaoMap = new HashMap<>();
		String path = this.getClass().getResource("/").getPath();
		try {
			List<String> readLines = FileUtils.readLines(new File(path + File.separator + "sql/绩效/市场部目标.txt"));
			for (int i = 0; i < readLines.size(); i++) {
				String line = readLines.get(i);
				if(line.contains("_" + month + "月")){
					String[] split = line.split(",");
					mubiaoMap.put(split[0], split[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mubiaoMap;
	}
	
	@SuppressWarnings("deprecation")
	public Map<String,Object> getAuthData(){
		
		Map<String,Object> mubiaoMap = new HashMap<>();
		String path = this.getClass().getResource("/").getPath();
		try {
			List<String> readLines = FileUtils.readLines(new File(path + File.separator + "sql/绩效/各部门负责人账号权限控制.txt"));
			for (int i = 0; i < readLines.size(); i++) {
				String line = readLines.get(i);
				String[] split = line.split(",");
				mubiaoMap.put(split[0], split[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mubiaoMap;
	}
	
	/**
	 * 财务部录入确认，发起任务
	 * @param page
	 * @param limit
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/inputCompleteValue")
	public R inputCompleteValue(@RequestBody Map<String, Object> params){
//		caiwuMubiao
		String completeValue = params.get("completeValue") + "";
		String month = params.get("month") + "";
		String index = "费用/年化比(17年累计)";
		double complete = Double.parseDouble(completeValue);
		double mubiao = Double.parseDouble(caiwuMubiao.replace("%", ""));
		String destination_value = caiwuMubiao;
		String complete_rate = NumberUtil.keepPrecision((2 - complete/mubiao)*100, 2) + "%";
		String status = "等待财务部录入";
		completeValue += "%";
		boolean flag = addTask(month, index, completeValue, destination_value, complete_rate, status, "财务部录入");
		return R.ok().put("code", flag? "success":"fail");
	}
	
	@ResponseBody
	@RequestMapping("/queren")
	public R queren(@RequestBody Map<String, Object> params){
		String index = params.get("指标") + "";
		String destination_value = params.get("目标值") + "";
		String complete_rate = params.get("达成率") + "";
		String status = params.get("状态") + "";
		status = status.substring( status.indexOf("等待"),  status.indexOf("</"));
		String completeValue = params.get("完成值") + "";
		String month = params.get("month") + "";
		String desc = params.get("description") + "";
		boolean flag = addTask(month, index, completeValue, destination_value, complete_rate, status, desc);
		return R.ok().put("code", flag? "success":"fail");
	}
	
	@ResponseBody
	@RequestMapping("/shichangQueren")
	public R shichangQueren(@RequestBody Map<String, Object> params){
		String index = params.get("指标") + "";
		String destination_value = params.get("目标值") + "";
		String complete_rate = params.get("达成率") + "";
		String status = params.get("状态") + "";
		status = status.substring( status.indexOf("等待"),  status.indexOf("</"));
		String completeValue = params.get("完成值") + "";
		completeValue = completeValue.replace(",", "");
		String month = params.get("month") + "";
		String desc = params.get("description") + "";
		boolean flag = addTask(month, index, completeValue, destination_value, complete_rate, status, desc);
		return R.ok().put("code", flag? "success":"fail");
	}
	
	/**
	 * 权限控制
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/hasAuth")
	public R hasAuth(@RequestBody Map<String, Object> params){
		SysUserEntity userEntity = getUserEntity();
		String username = userEntity.getUsername();
		Map<String, Object> authData = getAuthData();
		boolean hasAuth = false;
		String status = params.get("状态") + "";
		if(status.contains("市场")){
			String name_list = authData.get("市场部").toString().trim();
			String[] split = name_list.split("\\^");
			for (int i = 0; i < split.length; i++) {
				if(username.trim().equals(split[i])){
					hasAuth = true;
				}
			}
			
		}else if(status.contains("运营")){
			String name_list = authData.get("运营部").toString().trim();
			String[] split = name_list.split("\\^");
			for (int i = 0; i < split.length; i++) {
				if(username.trim().equals(split[i])){
					hasAuth = true;
				}
			}
		}else if(status.contains("财务")){
			String name_list = authData.get("财务部").toString().trim();
			String[] split = name_list.split("\\^");
			for (int i = 0; i < split.length; i++) {
				if(username.trim().equals(split[i])){
					hasAuth = true;
				}
			}
		}
		
		return R.ok().put("hasAuth", hasAuth);
	}
	
	
	
	
	/**
	 * @param month
	 * @return
	 */
	public List<Map<String,Object>> queryMonthList(String month, String... index){
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String indexNameCond = " and index_name in (";
		for (int i = 0; i < index.length; i++) {
			String indexName = index[i];
			if(i == index.length - 1){
				indexNameCond += "'" + indexName + "'";
			}else{
				indexNameCond += "'" + indexName + "',";
			}
		}
		indexNameCond += ")";
		String sql = "select * from dm_report_jixiao_check_record where month=? "+indexNameCond+" and batch = (select max(batch) from dm_report_jixiao_check_record where month=?) order by start_time desc ";
		List<Map<String, Object>> resultList = null;
		try {
			resultList = jdbcHelper.query(sql, month, month);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 查询某月最大批次
	 * @param month
	 * @return
	 */
	public Map<String,Object> queryMonthMaxBatch(String month, String... index){
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String indexNameCond = " and index_name in (";
		for (int i = 0; i < index.length; i++) {
			String indexName = index[i];
			if(i == index.length - 1){
				indexNameCond += "'" + indexName + "'";
			}else{
				indexNameCond += "'" + indexName + "',";
			}
		}
		indexNameCond += ")";
		String sql = "select * from dm_report_jixiao_check_record where month=? "+indexNameCond+" and batch = (select max(batch) from dm_report_jixiao_check_record where month=?) order by start_time desc ";
		try {
			List<Map<String, Object>> resultList = jdbcHelper.query(sql, month, month);
			if(resultList != null && resultList.size() > 0){
				return resultList.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 获取下一个状态
	 * @param currStatus
	 * @param index
	 * @return
	 */
	public String getNextStatus(String currStatus, String index, Map<String,Object> map){
		SysUserEntity userEntity = getUserEntity();
		String username = userEntity.getUsername();//当前登录帐号
		Map<String, Object> params = new HashMap<>();
		params.put("状态", currStatus);
		R r = hasAuth(params);
		boolean hasAuth = (boolean) r.get("hasAuth");
		if(caiwu_index.equals(index)){
			if("等待财务部录入".equals(currStatus)){
				map.put("cancel", hasAuth);
				return "已完成";
			}else if("等待运营部确认".equals(currStatus)){
				return "已完成";
			}else{
				return "等待财务部录入";
			}
		}else if(yunying_curr_month_invest.equals(index) || yunying_curr_month_await.equals(index)){
			if("等待运营部确认".equals(currStatus)){
				map.put("cancel", hasAuth);
				return "等待财务部确认";
			}else if("等待财务部确认".equals(currStatus)){
				return "已完成";
			}else{
				return "等待运营部确认";
			}
		}else if(shichang_phonesale_cost.equals(index) || shichang_channel_cost.equals(index)){//电销成本
			if("等待市场部录入".equals(currStatus)){
				return "等待财务部确认";
			}else if("等待财务部确认".equals(currStatus)){
				return "已完成";
			}else{
				return "等待市场部录入";
			}
		}else if(shichang_reg_user_num.equals(index) || shichang_first_invest_user_num.equals(index)){
			if("等待市场部确认".equals(currStatus)){
				return "已完成";
			}else if("等待财务部确认".equals(currStatus)){
				return "已完成";
			}else{
				return "等待市场部确认";
			}
		}else if(shichang_curr_month_roi.equals(index)
				|| shichang_hongbao_cost.equals(index) || shichang_curr_month_channel_year_invest.equals(index)){
			if("等待市场部确认".equals(currStatus)){
				return "等待财务部确认";
			}else if("等待财务部确认".equals(currStatus)){
				return "已完成";
			}else{
				return "等待市场部确认";
			}
		}
		return "";
	}
	
	/**
	 * 发起任务
	 */
	public boolean addTask(String month, String index, String complete_value, String destination_value, 
			String complete_rate, String status, String desc){
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String sql = "INSERT INTO dm_report_jixiao_check_record ( `month`, `batch`, `parent_batch`, `start_user_id`, "
				+ "`start_time`, `index_name`, `complete_value`, `destination_value`, `complete_rate`, `status`, `description`)"
				+ " values(?,?,?,?,now(),?,?,?,?,?,?)";
		int batch = 1;
		int parent_batch = -1;
		Map<String,Object> lastTask = queryMonthMaxBatch(month, index);
		if(lastTask != null){
			batch = Integer.parseInt(lastTask.get("batch") + "");//有撤销才+1
			parent_batch = Integer.parseInt(lastTask.get("id") + "");
		}
		long start_user_id = getUserId();
//		status = getNextStatus(status, index);
		try {
			jdbcHelper.execute(sql , month, batch, parent_batch, start_user_id, index, complete_value, 
					destination_value, complete_rate , status, desc);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String statPeriod){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		String detail_sql = "";
		List<Map<String, Object>> retList = null;
		int month = Integer.parseInt(statPeriod.substring(5, 7));
		try {
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/运营市场绩效-交易额.txt"));
			
			retList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, statPeriod, statPeriod + "-01", statPeriod );
			
			//查询运营部每月目标
			List<Map<String, Object>> monthList = queryMonthList(month + "", yunying_curr_month_invest, yunying_curr_month_await);
			Map<String, Object> yunyingMubiao = getYunyingMubiao(month);
			for (int i = 0; i < retList.size(); i++) {
				Map<String, Object> map = retList.get(i);
				String zhibiao = map.get("指标") + "";
				String completeStr = map.get("完成值") + "";
				if(yunying_curr_month_invest.equals(zhibiao)){
					map.put("目标值", yunyingMubiao.get("本月年化交易额目标_"+month+"月"));
				}else if(yunying_curr_month_await.equals(zhibiao)){
					map.put("目标值", yunyingMubiao.get("本月销售新增年底待收目标_"+month+"月"));
				}
				double complete = 0;
				if(!StringUtils.isEmpty(completeStr) && !"null".equals(completeStr)){
					complete = Double.parseDouble(completeStr);
				}
				double mubiao = Double.parseDouble(map.get("目标值").toString());
				map.put("达成率", io.renren.util.NumberUtil.keepPrecision(complete*100/mubiao, 2) + "%");
				
				for (int j = 0; j < monthList.size(); j++) {
					Map<String, Object> map2 = monthList.get(j);
					String currStatus = map2.get("status").toString();
					String index_name = map2.get("index_name").toString();
					if(map2.get("index_name").toString().equals(zhibiao)){
						map.put("状态", getNextStatus(currStatus, index_name, map));
						break;
					}
				}
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//费用/年化比( 17年累计)	1.88%	141.3%	已完成	确认
		Map<String, Object> taskMap = queryMonthMaxBatch(month + "", caiwu_index);
		Map<String, Object> map = new HashMap<>();
		if(taskMap == null){
			map.put("状态", "等待财务部录入");
		}else{
			map.put("状态", getNextStatus(taskMap.get("status") + "", caiwu_index, map));
			map.put("完成值", taskMap.get("complete_value"));
			map.put("达成率", taskMap.get("complete_rate"));
		}
		map.put("指标", caiwu_index);
		map.put("目标值", caiwuMubiao);//写死
		retList.add(map);
		PageUtils pageUtil = new PageUtils(retList, retList.size(), limit, page);
		return R.ok().put("page", pageUtil);
	}
	
	class QueryThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list = null;
		private String startDate;
		private String endDate;
		private String querySql;
		private String month;

		public QueryThread(String querySql,DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, 
				String startDate, String endDate, String month) {
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.startDate = startDate == null ? "" : startDate;
			this.endDate = endDate == null ? "" : endDate;
			this.querySql = querySql;
			this.month = month;
		}

		@Override
		public void run() {
			try {
				if(StringUtils.isNotEmpty(startDate)){
					querySql = querySql.replace("${startDate}", "'"+startDate + "'");
				}
				if(StringUtils.isNotEmpty(endDate)){
					querySql = querySql.replace("${endDate}",  "'"+endDate + "'");
				}
				if(StringUtils.isNotEmpty(month)){
					querySql = querySql.replace("${month}",  "'"+month + "'");
				}
				list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(querySql));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 市场部本月，注册人数，首投人数
	 */
	@ResponseBody
	@RequestMapping("/regFirstInvestList")
	public R regFirstInvestList(Integer page, Integer limit, String statPeriod){
		String detail_sql = "";
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(statPeriod.replace("-", ""));
		String lastmonth = DateUtil.getMonthsBefore(lastDayOfMonth.replace("-", ""), 1);
		String lastMonthFirstDay = lastmonth.substring(0, 4) + "-" +lastmonth.substring(4, 6) + "-01";
		int month = Integer.parseInt(statPeriod.substring(5, 7));
		try {
			String path = this.getClass().getResource("/").getPath();
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/市场部注册首投人数.txt"));
			detail_sql = detail_sql.replace("${month}",  "'" + statPeriod + "'");
			detail_sql = detail_sql.replace("${startTime}", "'" + statPeriod + "-01 00:00:00'");
			detail_sql = detail_sql.replace("${endTime}", "'" + lastDayOfMonth + " 23:59:59'");
			List<Map<String, Object>> queryList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
//			List<Map<String, Object>> queryList2 = new JdbcUtil(dataSourceFactory, "oracle26")
//					.query(FileUtil.readAsString(new File(path + File.separator + "sql/绩效/test.txt")));
			//市场部红包成本
			String queryCostSql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/市场部本月红包成本.txt"));
			List<Map<String, Object>> hongbaoCostList = new ArrayList<>();
			String startDate = statPeriod + "-01";
			String endDate = lastDayOfMonth;
			//市场部渠道成本
//			String channelCostSql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/市场部本月渠道成本.txt"));
			List<Map<String, Object>> channelCostList = new ArrayList<>();
			//本月推广的渠道年化投资金额
			String channelYearInvestSql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/本月推广的渠道年化投资金额.txt"));
			List<Map<String, Object>> channelYearInvestList = new ArrayList<>();
			channelYearInvestSql = channelYearInvestSql.replace("${month}", statPeriod.replace("-", ""));
//			channelYearInvestSql = channelYearInvestSql.replace("${startTime}", "'" + statPeriod + "-01 00:00:00'");
//			channelYearInvestSql = channelYearInvestSql.replace("${endTime}", "'" + lastDayOfMonth + " 23:59:59'");
//			channelYearInvestSql = channelYearInvestSql.replace("${lastMonthFirstDay}", "'" + lastMonthFirstDay + "'");
//			channelYearInvestSql = channelYearInvestSql.replace("${currMonthEndDay}", "'" + lastDayOfMonth + "'");
			Thread t1 = new Thread(new QueryThread(queryCostSql, dataSourceFactory, hongbaoCostList, startDate, endDate, null));
//			Thread t2 = new Thread(new QueryThread(channelCostSql, dataSourceFactory, channelCostList, null, null, statPeriod));
			Thread t3 = new Thread(new QueryThread(channelYearInvestSql, dataSourceFactory, channelYearInvestList, null, null, null));
			t1.start();
//			t2.start();
			t3.start();
			t1.join();
//			t2.join();
			t3.join();
			
			Map<String, Object> shichangMubiaoMap = getShichangMubiao(month);
			if(queryList.size() > 0){
				Map<String, Object> map = queryList.get(0);
				//本月注册人数
				Map<String, Object> reg_map = queryMonthMaxBatch(month + "", shichang_reg_user_num);
				Map<String, Object> reg_user_map = new HashMap<>();
				reg_user_map.put("month", map.get("MONTH"));
				reg_user_map.put("指标", shichang_reg_user_num);
				reg_user_map.put("完成值", map.get(shichang_reg_user_num));
				reg_user_map.put("目标值", shichangMubiaoMap.get("本月注册人数目标_"+month+"月").toString().trim());
				double complete = Double.parseDouble(reg_user_map.get("完成值") + "");
				Object mubiaoObj = reg_user_map.get("目标值");
				double mubiao = 0;
				if(mubiaoObj != null && !"".equals(mubiaoObj.toString().trim())){
					mubiao = Double.parseDouble(mubiaoObj.toString());
					reg_user_map.put("达成率", io.renren.util.NumberUtil.keepPrecision(complete*100/mubiao, 2) + "%");
				}
				if(reg_map == null){
					reg_user_map.put("状态", "等待市场部确认");
				}else{
					reg_user_map.put("状态", getNextStatus(reg_map.get("status") + "", shichang_reg_user_num, map));
				}
				retList.add(reg_user_map);
				//本月首投人数
				Map<String, Object> first_invest_map = queryMonthMaxBatch(month + "", shichang_first_invest_user_num);
				Map<String, Object> first_invest_user_map = new HashMap<>();
				first_invest_user_map.put("month", map.get("MONTH"));
				first_invest_user_map.put("指标", shichang_first_invest_user_num);
				first_invest_user_map.put("完成值", map.get(shichang_first_invest_user_num));
				first_invest_user_map.put("目标值", shichangMubiaoMap.get("本月首投人数目标_"+month+"月").toString().trim());
				double complete_st = Double.parseDouble(first_invest_user_map.get("完成值") + "");
				Object mubiaoObj_st = first_invest_user_map.get("目标值");
				double mubiao_ft = 0;
				if(mubiaoObj_st != null && !"".equals(mubiaoObj_st.toString().trim())){
					mubiao_ft = Double.parseDouble(mubiaoObj_st.toString());
					first_invest_user_map.put("达成率", io.renren.util.NumberUtil.keepPrecision(complete_st*100/mubiao_ft, 2) + "%");
				}
				if(first_invest_map == null){
					first_invest_user_map.put("状态", "等待市场部确认");
				}else{
					first_invest_user_map.put("状态", getNextStatus(first_invest_map.get("status") + "", shichang_first_invest_user_num, map));
				}
				retList.add(first_invest_user_map);
				
				//本月首投用户本月累投ROI	
				Map<String, Object> roi_map = queryMonthMaxBatch(month + "", shichang_curr_month_roi);
				Map<String, Object> map__ = new HashMap<>();
//				if(taskMap == null){
//					map.put("状态", "等待财务部录入");
//				}else{
//					map.put("状态", getNextStatus(taskMap.get("status") + "", caiwu_index));
//					map.put("完成值", taskMap.get("complete_value"));
//					map.put("达成率", taskMap.get("complete_rate"));
//				}
				map__.put("指标", shichang_curr_month_roi);
				map__.put("目标值", "");//写死
				if(roi_map == null){
					map__.put("状态", "等待市场部确认");
				}else{
					map__.put("状态", getNextStatus(roi_map.get("status").toString(), shichang_curr_month_roi, map__));
				}
				retList.add(map__);
				
				//市场部红包成本
				Map<String, Object> shichang_hongbao_cost_map = queryMonthMaxBatch(month + "", shichang_hongbao_cost);
				Map<String, Object> hongbao_cost_map = new HashMap<>();
				hongbao_cost_map.put("month", map.get("MONTH"));
				hongbao_cost_map.put("指标", shichang_hongbao_cost);
				if(hongbaoCostList.size() > 0){
					hongbao_cost_map.put("完成值", hongbaoCostList.get(0).get(shichang_hongbao_cost));
				}
				if(shichang_hongbao_cost_map == null){
					hongbao_cost_map.put("状态", "等待市场部确认");
				}else{
					hongbao_cost_map.put("状态", getNextStatus(shichang_hongbao_cost_map.get("status") + "", shichang_hongbao_cost, map));
				}
				retList.add(hongbao_cost_map);
				//市场部渠道成本
				Map<String, Object> shichang_channel_cost_map = queryMonthMaxBatch(month + "", shichang_channel_cost);
				Map<String, Object> channel_cost_map = new HashMap<>();
				channel_cost_map.put("month", map.get("MONTH"));
				channel_cost_map.put("指标", shichang_channel_cost);
				if(shichang_channel_cost_map == null){
					channel_cost_map.put("完成值", "");
				}else{
					channel_cost_map.put("完成值", shichang_channel_cost_map.get("complete_value"));
				}
				if(shichang_channel_cost_map == null){
					channel_cost_map.put("状态", "等待市场部录入");
				}else{
					channel_cost_map.put("状态", getNextStatus(shichang_channel_cost_map.get("status") + "", shichang_channel_cost, map));
				}
				retList.add(channel_cost_map);
				//市场部电销成本
//				List<Map<String, Object>> queryMonthList = queryMonthList(month + "", shichang_phonesale_cost);
				Map<String, Object> queryMonthMaxBatch = queryMonthMaxBatch(month + "", shichang_phonesale_cost);
				Map<String, Object> phone_cost_map = new HashMap<>();
				phone_cost_map.put("month", map.get("MONTH"));
				phone_cost_map.put("指标", shichang_phonesale_cost);
				if(queryMonthMaxBatch == null){
					phone_cost_map.put("状态", "等待市场部录入");
				}else{
					phone_cost_map.put("状态", getNextStatus(queryMonthMaxBatch.get("status").toString(), shichang_phonesale_cost, phone_cost_map));
					phone_cost_map.put("完成值", queryMonthMaxBatch.get("complete_value"));
				}
				retList.add(phone_cost_map);
				//本月推广的渠道年化投资金额
				Map<String, Object> tuiguagn_map = queryMonthMaxBatch(month + "", shichang_curr_month_channel_year_invest);
				Map<String, Object> curr_month_year_invest_map = new HashMap<>();
				curr_month_year_invest_map.put("month", map.get("MONTH"));
				curr_month_year_invest_map.put("指标", shichang_curr_month_channel_year_invest);
				if(channelYearInvestList.size() > 0){
					curr_month_year_invest_map.put("完成值", channelYearInvestList.get(0).get(shichang_curr_month_channel_year_invest));
				}
				if(tuiguagn_map == null){
					curr_month_year_invest_map.put("状态", "等待市场部确认");
				}else{
					curr_month_year_invest_map.put("状态", getNextStatus(tuiguagn_map.get("status").toString(), shichang_curr_month_channel_year_invest, curr_month_year_invest_map));
				}
				retList.add(curr_month_year_invest_map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//计算roi
		setRoi(retList);
		PageUtils pageUtil = new PageUtils(retList, retList.size(), limit, page);
		return R.ok().put("page", pageUtil);
	}

	private void setRoi(List<Map<String, Object>> retList) {
		double total_cost = 0;
		double total_invest = 0;
		double phone_cost = 0;
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			String zhibiao = map.get("指标").toString();
			Object value = map.get("完成值");
			if(zhibiao.equals(shichang_hongbao_cost) || zhibiao.equals(shichang_channel_cost)){
				if(value == null || "".equals(value.toString())){
					total_cost += 0;
				}else{
					total_cost += Double.parseDouble(value.toString());
				}
			}else if(zhibiao.equals(shichang_phonesale_cost)){
				if(value != null && !"".equals(value) && !"null".equals(value)){
					phone_cost = Double.parseDouble(value.toString());
					total_cost += Double.parseDouble(value.toString());
				}else{
					total_cost += 0;
				}
			}else if(zhibiao.equals(shichang_curr_month_channel_year_invest)){
				if(value != null && value.toString().trim().length() > 0){
					total_invest = Double.parseDouble(value.toString());
				}
			}
			
		}
		
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			String zhibiao = map.get("指标").toString();
			if(zhibiao.equals(shichang_curr_month_roi) && phone_cost > 0){
				double value = total_invest/total_cost;
				map.put("完成值", NumberUtil.keepPrecision(value, 2));
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String data, HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String,Object> dataMap = JSON.parseObject(data, Map.class);
		String path = this.getClass().getResource("/").getPath();
		String indexName = dataMap.get("indexName") + "";
		String month = dataMap.get("month") + "";
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(month.replace("-", ""));
		String lastMonthDate = DateUtil.getMonthsBefore(lastDayOfMonth.replace("-", ""), 1);
		String lastMonth = lastMonthDate.substring(0, 4) + "-" + lastMonthDate.substring(4, 6);
		String querySql = "";
		String excelTitle = "";
		List<Map<String,Object>> dataList = new ArrayList<>();
		Map<String, String> headMap = null;
		if(shichang_hongbao_cost.equals(indexName)){
			headMap = getHongbaoExcelFields();
			excelTitle = "市场部本月红包成本明细";
			String sql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/红包成本明细.txt"));
			querySql = sql.replace("${startDate}", month + "-01");
			querySql = querySql.replace("${endDate}", lastDayOfMonth);
			querySql = querySql.replace("${month}", month);
		}else if(shichang_channel_cost.equals(indexName)){
			headMap = getChannelCostExcelFields();
			excelTitle = "渠道成本明细";
			String sql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/渠道成本明细.txt"));
			querySql = sql.replace("${month}", month);
		}else if(shichang_curr_month_channel_year_invest.equals(indexName)){
			excelTitle = shichang_curr_month_channel_year_invest + "明细";
			headMap = getYearInvestExcelFields();
			String sql = FileUtil.readAsString(new File(path + File.separator + "sql/绩效/首投年化投资额明细.txt"));
			querySql = sql.replace("${lastMonth}", lastMonth);
			querySql = querySql.replace("${month}", month.replace("-", ""));
		}
		try {
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(querySql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray dataArray = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,Object> entity = dataList.get(i);
			dataArray.add(entity);
		}
		ExcelUtil.downloadExcelFile(excelTitle, headMap, dataArray, response);
	}

	private Map<String, String> getHongbaoExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("名称", "名称");
		headMap.put("券号NID/红包模板ID", "券号NID/红包模板ID");
		headMap.put("发放人数", "发放人数");
		
		headMap.put("使用人数", "使用人数");
		headMap.put("使用总金额(元)", "使用总金额(元)");
		headMap.put("用户首投时使用金额(元)", "用户首投时使用金额(元)");
		
		headMap.put("红包所属系统", "红包所属系统");
		headMap.put("数据统计周期", "数据统计周期");
		headMap.put("用途", "用途");
		
		headMap.put("所属于部门", "所属于部门");
		headMap.put("成本分摊方式", "成本分摊方式");
		headMap.put("市场部费用(元)", "市场部费用(元)");
		
		headMap.put("运营部费用(元)", "运营部费用(元)");
		return headMap;
	}
	
	private Map<String, String> getChannelCostExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("渠道名称", "渠道名称");
		headMap.put("渠道费用", "渠道费用");
		return headMap;
	}
	
	private Map<String, String> getYearInvestExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("投资时间", "投资时间");
		headMap.put("用户ID", "用户ID");
		headMap.put("投资金额", "投资金额");
		return headMap;
	}
	
}
