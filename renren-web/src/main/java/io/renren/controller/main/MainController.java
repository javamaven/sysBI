package io.renren.controller.main;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.utils.R;

@RestController
@RequestMapping(value = "/main")
public class MainController {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Autowired
	private DruidDataSource dataSource;
	
	/**
	 * 平台投资情况滚动
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryInvestInfo")
	public R queryInvestInfo()  {
		List<Map<String,Object>> dataList = null;
		Set<String> citySet = new HashSet<String>();
		int days = 1;
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			while(true){
				String startTime = getStartTime(days);
				String endTime = getEndTime(days);
				dataList = jdbcHelper.query(SqlConstants.invest_info_sql, startTime, endTime);
				if(dataList.size() > 0){
					break;
				}
				days++;
				if(days == 7){
					break;
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if(StringUtils.isNotEmpty(CITY) && !"null".equals(CITY)){
					citySet.add(CITY);
				}
				if(StringUtils.isNotEmpty(TO_CITY) && !"null".equals(TO_CITY)){
					citySet.add(TO_CITY);
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if(StringUtils.isEmpty(CITY) || "null".equals(CITY)){
					map.put("CITY", citySet.toArray(new String[1])[new Random().nextInt(citySet.size())]);
				}
				if(StringUtils.isEmpty(TO_CITY) || "null".equals(TO_CITY)){
					map.put("TO_CITY", citySet.toArray(new String[1])[new Random().nextInt(citySet.size())]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("data", dataList);
		return R.ok().put("data", dataList);
	}
	
	
	
	/**
	 * 地图数据
	 * @return
	 */
	@RequestMapping(value = "/queryDituData")
	public R queryDituData()  {
		List<Map<String,Object>> dataList = null;
		Map<String, Object> all = new HashMap<String,Object>();
		List<Map<String, Object>> mark_point_data = null;
		Set<String> citySet = new HashSet<String>();
		int days = 1;
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			while(true){
				String startTime = getStartMinuteTime(days);
				String endTime = getEndMinuteTime(days);
				dataList = jdbcHelper.query(SqlConstants.query_ditu_sql, startTime, endTime);
				mark_point_data = jdbcHelper.query(SqlConstants.query_register_charge_data, startTime, endTime);
				if(dataList.size() > 0){
					break;
				}
				days++;
				if(days == 7){
					break;
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if(StringUtils.isNotEmpty(CITY) && !"null".equals(CITY)){
					citySet.add(CITY);
				}
				if(StringUtils.isNotEmpty(TO_CITY) && !"null".equals(TO_CITY)){
					citySet.add(TO_CITY);
				}
			}
			all.putAll(genData(citySet, dataList, "1", 0, 4));
			all.putAll(genData(citySet, dataList, "2", 4, 8));
			all.putAll(genData(citySet, dataList, "3", 8, 12));
			all.putAll(genData(citySet, dataList, "4", 12, 16));
			all.putAll(genData(citySet, dataList, "5", 16, 20));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return R.ok().put("data", all).put("mark_point_data", mark_point_data);
	}
	
	
	private Map<String,Object> genData(Set<String> citySet, List<Map<String, Object>> dataList, String index, int start, int end) {
		List<List<Map<String,Object>>> dituData = new ArrayList<List<Map<String,Object>>>();
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list;
		for (int i = 0; i < dataList.size(); i++) {
			list = new ArrayList<Map<String,Object>>();
			Map<String,Object> city = new HashMap<String,Object>();
			Map<String,Object> to_city = new HashMap<String,Object>();
			Map<String,Object> to_city_map2 = new HashMap<String,Object>();
			Map<String, Object> map = dataList.get(i);
			String CITY = map.get("CITY") + "";
			String TO_CITY = map.get("TO_CITY") + "";
			if(StringUtils.isEmpty(CITY) || "null".equals(CITY)){
				CITY = citySet.toArray(new String[1])[new Random().nextInt(citySet.size())];
			}
			if(StringUtils.isEmpty(TO_CITY) || "null".equals(TO_CITY)){
				TO_CITY = citySet.toArray(new String[1])[new Random().nextInt(citySet.size())];
			}
			if(i < start){
				continue;
			}
			if(i >= end){
				continue;
			}
			String TENDER_CAPITAL = map.get("TENDER_CAPITAL") + "";
			String INVEST_TIMES = map.get("INVEST_TIMES") + "";
			
			city.put("name", CITY);
			to_city.put("name", TO_CITY);
			to_city.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + INVEST_TIMES);
//			to_city.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + new Random().nextInt(60));
			to_city_map2.put("name", TO_CITY);
			to_city_map2.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + INVEST_TIMES);
//			to_city_map2.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + new Random().nextInt(60));
			list.add(city);
			list.add(to_city);
			dituData.add(list);
			data.add(to_city_map2);
		}
		
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("from_to_data_" + index, dituData);
		ret.put("to_data_" + index, data);
		return ret;
	}
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
	private String getStartTime(int days) {
		String currDate = sdf.format(new Date());
//		int days = 1;
//		days = 3;//测试
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, days, "yyyy-MM-dd HH");
		return currDayBefore + ":00:00";
	}
	
	private String getEndTime(int days) {
		String currDate = sdf.format(new Date());
//		int days = 1;
//		days = 3;//测试
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, days, "yyyy-MM-dd HH");
		return currDayBefore + ":59:59";
	}
	
	SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String getStartMinuteTime(int days) {
		String date = sdf_m.format(new Date());
//		int days = 1;
//		days = 3;//测试
		date = DateUtil.getCurrDayBefore(date, days, "yyyy-MM-dd HH:mm");
		return date + ":00";
	}
	
	private String getEndMinuteTime(int days) {
		String date = sdf_day.format(new Date());
//		int days = 1;
//		days = 3;//测试
		date = DateUtil.getCurrDayBefore(date, days, "yyyy-MM-dd");
		return date + " 23:59:59";
	}


	/**
	 * 平台注册人数
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryRegisterUserNum")
	public R queryRegisterUserNum()  {
		int register_user = 0;
		try {
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query("select  COUNT(1) as  register_user  from  diyou_users");
			Map<String, Object> map = list.get(0);
			register_user = Integer.parseInt(map.get("REGISTER_USER") + "");
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return R.ok().put("register_user", register_user);
	}
	
	/**
	 * 普通标交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryPutongTotalInvestAmount(){
		double total_amount = 0;
		try {
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query(SqlConstants.pt_total_amount);
			Map<String, Object> map = list.get(0);
			total_amount = Double.parseDouble(map.get("TOTAL_AMOUNT") + "");
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		} 
		return total_amount;
	}
	
	/**
	 * 债转标交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryChangeTotalInvestAmount() throws SQLException{
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(SqlConstants.change_total_amount_sql);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CHANGE_TOTAL_AMOUNT") + "");
		return total_amount;
	}
	

	/**
	 * 点点赚交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryDdzTotalInvestAmount() throws SQLException{
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(SqlConstants.ddz_total_amount);
		Map<String, Object> map = list.get(0);
		String money = map.get("DDZ_TOTAL_AMOUNT") + "";
		if(StringUtils.isEmpty(money) || "null".equals(money)){
			money = "0";
		}
		double total_amount = Double.parseDouble(money);
		return total_amount;
	}
	

	/**
	 * 存管版交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryCgTotalInvestAmount() throws SQLException{
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = util.query(SqlConstants.cg_total_amount);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CG_TOTAL_AMOUNT") + "");
		return total_amount;
	}
	
	java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();
	
	/**
	 * 平台投资总额
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryTotalInvestAmount")
	public R queryTotalInvestAmount() {
		double total_amount = 0;
		try {
			total_amount += queryPutongTotalInvestAmount();
			total_amount += queryChangeTotalInvestAmount();
			total_amount += queryDdzTotalInvestAmount();
			total_amount += queryCgTotalInvestAmount();
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		} 
		numberFormat.setGroupingUsed(false);  
		return R.ok().put("total_amount", numberFormat.format(total_amount));
	}
}