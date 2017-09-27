package io.renren.controller.yunying.yunyingtool;

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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
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
import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.MapUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.R;

@RestController
@RequestMapping("/yunying/yunyingtool")
public class UserLifeYunyingToolController extends AbstractController {

	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	DruidDataSource dataSource;
	
	@Autowired
	private UserBehaviorService userBehaviorService;
	
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
	 * 查询明细数据
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> queryDetailData(Map<String,Object> params){
		
		String path = this.getClass().getResource("/").getPath();
		String detail_sql = "";
		List<Map<String, Object>> retList = null;
		String user_type = MapUtil.getValue(params, "user_type");
		String timeLimit = MapUtil.getValue(params, "timeLimit");
		String isLiucun = MapUtil.getValue(params, "isLiucun");
		String isYingli = MapUtil.getValue(params, "isYingli");
		String first_invest_month_start = MapUtil.getValue(params, "first_invest_month_start");
		String first_invest_month_end = MapUtil.getValue(params, "first_invest_month_end");
		first_invest_month_start = first_invest_month_start.replace("-", "");
		first_invest_month_end = first_invest_month_end.replace("-", "");
		if("经营1-30天".equals(timeLimit)){
			timeLimit = " and diff=1 ";
		}else if("经营31-90天".equals(timeLimit)){
			timeLimit = " and diff=2 ";
		}else if("经营91-180天".equals(timeLimit)){
			timeLimit = " and diff=3 ";
		}else if("经营181天及以上".equals(timeLimit)){
			timeLimit = " and diff=4 ";
		}else if("汇总".equals(timeLimit)){
			timeLimit = "";
		}
		if("留存".equals(isLiucun)){
			isLiucun = "1";
		}else if("流失".equals(isLiucun)){
			isLiucun = "2";
		}
		if("盈利".equals(isYingli)){
			isYingli = "1";
		}else if("亏损".equals(isYingli)){
			isYingli = "2";
		}
		try {
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营部/用户生命周期运营工具-明细.txt"));
			detail_sql = detail_sql.replace("${first_invest_month_start}", first_invest_month_start);
			detail_sql = detail_sql.replace("${first_invest_month_end}", first_invest_month_end);
			detail_sql = detail_sql.replace("${timeLimitCond}", timeLimit);
			detail_sql = detail_sql.replace("${isLiucun}", isLiucun);
			detail_sql = detail_sql.replace("${isYingli}", isYingli);
			if("all_user".equals(user_type)){
				detail_sql = detail_sql.replace("${userTypeCond}", "");
			}else{
				detail_sql = detail_sql.replace("${userTypeCond}", " and user_level='" + user_type + "' ");
			}
			retList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retList;
	}
	
	/**
	 * 电销筛选存管版用户数据 导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportDetail")
	public void phoneSaleCgUserListExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="用户生命周期运营工具";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		List<Map<String, Object>> dataList = queryDetailData(map);
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("FT_MONTH","首投月份");
		headMap.put( "USER_ID","用户ID");
		headMap.put( "USERNAME","用户名");
		headMap.put("REALNAME","用户姓名");
		
		headMap.put("PHONE","手机号");
		headMap.put("PL_TYPE","是否盈利");
		headMap.put( "USER_LEVEL","用户分类");
		headMap.put("DIFF","经营时间分段");
		headMap.put("LC","是否待收大于0");
		
		headMap.put("RESOURCE_KIND","用户来源");
		headMap.put("CHANNEL_NAME","渠道名称");
		headMap.put( "ACTIVITY_TAG","渠道标记");
		headMap.put("CHANNEL_COST","渠道成本");
		
		headMap.put("MARKING_VOCHE_COST","渠道红包成本");
		headMap.put("OPERATIONS_VOCHE_COST","运营红包成本");
		headMap.put( "REBATE_COST","返利成本");
		headMap.put("INCREASES_COST","加息成本");
		
		headMap.put("CALL_SALL_COST_NEW","拉新电销成本");
		headMap.put("CALL_SALL_COST_OLD","召回电销成本");
		headMap.put( "CREATE_PROFIT","利差收入");
		headMap.put("CAC","获客总成本");
		
		headMap.put( "COC","运营总成本");
		headMap.put("LTV","收益");
		headMap.put( "PROFIT_LOSS","盈亏金额");
		headMap.put("SUM_CAPTIAL","交易额");
		
		headMap.put( "SUM_NCAPTIAL","年化交易额");
		headMap.put("COU_TENDER","投资总次数");
		headMap.put( "CL_I","创利系数");
		headMap.put("COC_I","运营系数");
		
		headMap.put( "BP","盈亏平衡点");
		headMap.put("MONEY_WAIT","待收金额");
		
		
		String title = "用户生命周期运营工具-明细";
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestBody Map<String, Object> params){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看","用户生命周期运营工具"," ");
		String detail_sql = "";
		List<Map<String, Object>> retList = new ArrayList<>();
		String user_type = MapUtil.getValue(params, "user_type");
		String first_invest_month_start = MapUtil.getValue(params, "first_invest_month_start");
		String first_invest_month_end = MapUtil.getValue(params, "first_invest_month_end");
		first_invest_month_start = first_invest_month_start.replace("-", "");
		first_invest_month_end = first_invest_month_end.replace("-", "");
		try {
			String path = this.getClass().getResource("/").getPath();
			if("all_user".equals(user_type)){
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营部/用户生命周期运营工具-总.txt"));
			}else{
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营部/用户生命周期运营工具.txt"));
				detail_sql = detail_sql.replace("${user_type}", user_type);
			}
			detail_sql = detail_sql.replace("${first_invest_month_start}", first_invest_month_start);
			detail_sql = detail_sql.replace("${first_invest_month_end}", first_invest_month_end);
			retList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Map<String,Map<String,Object>> retMap = new HashMap<>();
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			String timeDiff = MapUtil.getValue(map, "经营时间分段");
			String isLiucun = MapUtil.getValue(map, "是否留存");
			String isYingli = MapUtil.getValue(map, "是否盈利");
			retMap.put(timeDiff + "^" + isLiucun + "^" + isYingli, map);
		}
		return R.ok().put("data", retMap);
	}
	
	
	
}
