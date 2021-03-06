package io.renren.controller.main;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

import io.renren.service.main.MainService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;
import io.renren.utils.R;

@RestController
@RequestMapping(value = "/main")
public class MainController {

	@Autowired
	private DataSourceFactory dataSourceFactory;

	@Autowired
	private DruidDataSource dataSource;
	
	@Autowired
	private MainService mainService;

	/**
	 * 平台投资情况滚动
	 * 
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryInvestInfo")
	public R queryInvestInfo() {
		List<Map<String, Object>> dataList = null;
		Set<String> citySet = new HashSet<String>();
		int days = 1;
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			while (true) {
				String startTime = getStartTime(days);
				String endTime = getEndTime(days);
				int hours = 1;
				dataList = jdbcHelper.query(SqlConstants.invest_info_sql, startTime, endTime);
				while(dataList.size() < 30){
					endTime = DateUtil.getHourBefore(endTime, "yyyy-MM-dd HH:mm:ss", -hours, "yyyy-MM-dd HH:mm:ss");
					dataList = jdbcHelper.query(SqlConstants.invest_info_sql, startTime, endTime);
					hours++;
					if(hours == 5){
						break;
					}
				}
				if (dataList.size() > 0) {
					break;
				}
				days++;
				if (days == 7) {
					break;
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if (StringUtils.isNotEmpty(CITY) && !"null".equals(CITY)) {
					citySet.add(CITY);
				}
				if (StringUtils.isNotEmpty(TO_CITY) && !"null".equals(TO_CITY)) {
					citySet.add(TO_CITY);
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if (StringUtils.isEmpty(CITY) || "null".equals(CITY)) {
					map.put("CITY", citySet.toArray(new String[1])[new Random().nextInt(citySet.size())]);
				}
				if (StringUtils.isEmpty(TO_CITY) || "null".equals(TO_CITY)) {
					map.put("TO_CITY", citySet.toArray(new String[1])[new Random().nextInt(citySet.size())]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("data", dataList);
		return R.ok().put("data", dataList);
	}

	/**
	 * 地图数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryDituData")
	public R queryDituData() {
		List<Map<String, Object>> dataList = null;
		Map<String, Object> all = new HashMap<String, Object>();
		List<Map<String, Object>> mark_point_data = null;
		Set<String> citySet = new HashSet<String>();
		int days = 1;
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			while (true) {
				String startTime = getStartMinuteTime(days);
				String endTime = getEndMinuteTime(days);
				dataList = jdbcHelper.query(SqlConstants.query_ditu_sql, startTime, endTime);
				mark_point_data = jdbcHelper.query(SqlConstants.query_register_charge_data, startTime, endTime);
				if (dataList.size() > 0) {
					break;
				}
				days++;
				if (days == 7) {
					break;
				}
			}
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				if (StringUtils.isNotEmpty(CITY) && !"null".equals(CITY)) {
					citySet.add(CITY);
				}
				if (StringUtils.isNotEmpty(TO_CITY) && !"null".equals(TO_CITY)) {
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

	private Map<String, Object> genData(Set<String> citySet, List<Map<String, Object>> dataList, String index,
			int start, int end) {
		List<List<Map<String, Object>>> dituData = new ArrayList<List<Map<String, Object>>>();

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list;
		for (int i = 0; i < dataList.size(); i++) {
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> city = new HashMap<String, Object>();
			Map<String, Object> to_city = new HashMap<String, Object>();
			Map<String, Object> to_city_map2 = new HashMap<String, Object>();
			Map<String, Object> map = dataList.get(i);
			String CITY = map.get("CITY") + "";
			String TO_CITY = map.get("TO_CITY") + "";
			if (StringUtils.isEmpty(CITY) || "null".equals(CITY)) {
				CITY = citySet.toArray(new String[1])[new Random().nextInt(citySet.size())];
			}
			if (StringUtils.isEmpty(TO_CITY) || "null".equals(TO_CITY)) {
				TO_CITY = citySet.toArray(new String[1])[new Random().nextInt(citySet.size())];
			}
			if (i < start) {
				continue;
			}
			if (i >= end) {
				continue;
			}
			String TENDER_CAPITAL = map.get("TENDER_CAPITAL") + "";
			String INVEST_TIMES = map.get("INVEST_TIMES") + "";

			city.put("name", CITY);
			to_city.put("name", TO_CITY);
			to_city.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + INVEST_TIMES);
			// to_city.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" +
			// new Random().nextInt(60));
			to_city_map2.put("name", TO_CITY);
			to_city_map2.put("value", Double.parseDouble(TENDER_CAPITAL) + "-" + INVEST_TIMES);
			// to_city_map2.put("value", Double.parseDouble(TENDER_CAPITAL) +
			// "-" + new Random().nextInt(60));
			list.add(city);
			list.add(to_city);
			dituData.add(list);
			data.add(to_city_map2);
		}

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("from_to_data_" + index, dituData);
		ret.put("to_data_" + index, data);
		return ret;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");

	private String getStartTime(int days) {
		String currDate = sdf.format(new Date());
		// int days = 1;
		// days = 3;//测试
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, days, "yyyy-MM-dd HH");
		return currDayBefore + ":00:00";
	}

	private String getEndTime(int days) {
		String currDate = sdf.format(new Date());
		// int days = 1;
		// days = 3;//测试
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, days, "yyyy-MM-dd HH");
		return currDayBefore + ":59:59";
	}

	SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private String getStartMinuteTime(int days) {
		String date = sdf_m.format(new Date());
		// int days = 1;
		// days = 3;//测试
		date = DateUtil.getCurrDayBefore(date, days, "yyyy-MM-dd HH:mm");
		return date + ":00";
	}

	private String getEndMinuteTime(int days) {
		String date = sdf_day.format(new Date());
		// int days = 1;
		// days = 3;//测试
		date = DateUtil.getCurrDayBefore(date, days, "yyyy-MM-dd");
		return date + " 23:59:59";
	}

	/**
	 * 平台注册人数
	 * 
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryRegisterUserNum")
	public R queryRegisterUserNum() {
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
	 * 普通标最近30分钟交易额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryPutongLast30MinuteInvestAmount() {
		double total_amount = 0;
		try {
			String start = timeList.get(timeList.size() - 1) + ":00";
			String end = DateUtil.getNextMinutes(start, 30, "yyyy-MM-dd HH:mm:ss");
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query(SqlConstants.pt_pro_last_30minutes_invest_amount, start, end,
					start, end);
			Map<String, Object> map = list.get(0);
			String obj = map.get("MONEY") + "";
			if(!"null".equals(obj)){
				total_amount = Double.parseDouble(obj);
			}
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return total_amount;
	}

	/**
	 * 普通标交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryPutongTotalInvestAmount() {
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
	 * 普通标交易List
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> queryPutongTotalInvestAmountList() {
		try {
			String currDate = DateUtil.formatDateTime(new Date());// yyyy-MM-dd
																	// HH:mm:ss
			long mill = getMillByDate(currDate);
			// mill += 52594000 + 52594000 + 52594000 + 52594000 + 52594000 +
			// 52594000 + 52594000 + 52594000 + 52594000
			// + 52594000 + 52594000 + 52594000 + 52594000;
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query(SqlConstants.pt_pro_invest_amount_list, mill, mill);
			return list;
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>();
	}

	private long getMillByDate(String currDate) {
		int hour = Integer.valueOf(currDate.substring(11, 13));
		int minutes = Integer.valueOf(currDate.substring(14, 16));
		int seconds = Integer.valueOf(currDate.substring(17, 19));
		return hour * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000;
	}

	/**
	 * 债转标交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryChangeTotalInvestAmount() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(SqlConstants.change_total_amount_sql);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CHANGE_TOTAL_AMOUNT") + "");
		return total_amount;
	}

	/**
	 * 债转标过去30分钟交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryChangeLast30MinuteInvestAmount() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		String start = timeList.get(timeList.size() - 1) + ":00";
		String end = DateUtil.getNextMinutes(start, 30, "yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> list = util.query(SqlConstants.pt_change_30minutes_invest_amount, end, start);
		Map<String, Object> map = list.get(0);
		String obj = map.get("CHANGE_TOTAL_AMOUNT") + "";
		if(!"null".equals(obj)){
			return Double.parseDouble(obj);
		}
		return 0;
	}

	/**
	 * 债转标交易List
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> queryChangeTotalInvestAmountList() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		String currDate = DateUtil.formatDateTime(new Date());
		long mill = getMillByDate(currDate);
		return util.query(SqlConstants.pt_change_invest_amount_list, mill);
	}

	/**
	 * 点点赚交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryDdzTotalInvestAmount() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(SqlConstants.ddz_total_amount);
		Map<String, Object> map = list.get(0);
		String money = map.get("DDZ_TOTAL_AMOUNT") + "";
		if (StringUtils.isEmpty(money) || "null".equals(money)) {
			money = "0";
		}
		double total_amount = Double.parseDouble(money);
		return total_amount;
	}

	/**
	 * 存管版交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryCgTotalInvestAmount() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = util.query(SqlConstants.cg_total_amount);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CG_TOTAL_AMOUNT") + "");
		return total_amount;
	}

	/**
	 * 存管版过去30分钟交易总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double queryCgLast30MinuteInvestAmount() throws SQLException {
		String start = timeList.get(timeList.size() - 1) + ":00";
		String end = DateUtil.getNextMinutes(start, 30, "yyyy-MM-dd HH:mm:ss");
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = util.query(SqlConstants.cg_30minutes_invest_amount, start, end, start, end);
		Map<String, Object> map = list.get(0);
		String obj = map.get("CG_TOTAL_AMOUNT") + "";
		if(!"null".equals(obj)){
			return Double.parseDouble(obj);
		}
		return 0;
	}

	/**
	 * 存管版交易List
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> queryCgTotalInvestAmountList() throws SQLException {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		String currDate = DateUtil.formatDateTime(new Date());// yyyy-MM-dd
																// HH:mm:ss
		String time = currDate.substring(0, 11) + "00:00:00";
		return util.query(SqlConstants.cg_invest_amount_list, time, time);
	}

	java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();

	/**
	 * 平台投资总额
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryTotalInvestAmount")
	public R queryTotalInvestAmount() {
		double total_amount = 0;
		try {
			long l1 = System.currentTimeMillis();
			total_amount += queryPutongTotalInvestAmount();
			long l2 = System.currentTimeMillis();
//			total_amount += queryChangeTotalInvestAmount();
			total_amount += queryDdzTotalInvestAmount();
			long l3 = System.currentTimeMillis();
			total_amount += queryCgTotalInvestAmount();
			long l4 = System.currentTimeMillis();
			System.err.println("+++++耗时1++++" + (l2-l1));
			System.err.println("+++++耗时2++++" + (l3-l2));
			System.err.println("+++++耗时3++++" + (l4-l3));
		} catch (Exception e) {
//			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		numberFormat.setGroupingUsed(false);
		return R.ok().put("total_amount", numberFormat.format(total_amount));
	}

	/**
	 * 实时投资额趋势图（每隔30分钟查询一次）
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryLast30MinuteInvestAmount")
	public R queryLast30MinuteInvestAmount() {
		double total_amount = 0;
		try {
			total_amount += queryPutongLast30MinuteInvestAmount();
			total_amount += queryChangeLast30MinuteInvestAmount();
			total_amount += queryCgLast30MinuteInvestAmount();
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		numberFormat.setGroupingUsed(false);
		String start = timeList.get(timeList.size() - 1);
		String next = DateUtil.getNextMinutes(start, 30, "yyyy-MM-dd HH:mm");
		timeList.add(next);
		return R.ok().put("amount", numberFormat.format(total_amount)).put("time", next);
	}

	/**
	 * 实时投资额趋势图(初始化)
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryInvestAmountList")
	public Map<String, Object> queryInvestAmountList() {
		Map<String, Object> ret = null;
		try {
			List<Map<String, Object>> pt_invest_list = queryPutongTotalInvestAmountList();
			List<Map<String, Object>> pt_change_list = queryChangeTotalInvestAmountList();
			List<Map<String, Object>> cg_invest_list = queryCgTotalInvestAmountList();
			ret = buildData(pt_invest_list, pt_change_list, cg_invest_list);
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return ret;
	}

	List<String> timeList;
	SimpleDateFormat sdf_hm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private Map<String, Object> buildData(List<Map<String, Object>> pt_invest_list,
			List<Map<String, Object>> pt_change_list, List<Map<String, Object>> cg_invest_list) {
		Map<String, Object> ret = new HashMap<String, Object>();
		timeList = getTimeList();
		List<Double> dataList = new ArrayList<Double>();
		for (int i = 0; i < timeList.size(); i++) {
			try {

				double totalMoney = 0;
				Date dateTime = sdf_hm.parse(timeList.get(i));
				Date dateTime_last = null;
				if (i > 0) {
					dateTime_last = sdf_hm.parse(timeList.get(i - 1));
				}
				for (int j = 0; j < pt_invest_list.size(); j++) {
					Map<String, Object> map = pt_invest_list.get(j);
					double money = Double.parseDouble(map.get("MONEY") + "");
					String time = map.get("TIME") + "";
					// System.err.println("+++money=" + money + " ;time=" +
					// sdf_hm.format(new Date(Long.parseLong(time))));
					if (i == 0 && new Date(Long.parseLong(time)).before(dateTime)) {
						totalMoney += money;
					} else if (dateTime_last != null) {
						if (new Date(Long.parseLong(time)).after(dateTime_last)
								&& new Date(Long.parseLong(time)).before(dateTime)) {
							totalMoney += money;
						}
					}
				}
				for (int j = 0; j < pt_change_list.size(); j++) {
					Map<String, Object> map = pt_change_list.get(j);
					double money = Double.parseDouble(map.get("MONEY") + "");
					String time = map.get("TIME") + "";
					// System.err.println("+++money=" + money + " ;time=" +
					// sdf_hm.format(new Date(Long.parseLong(time))));
					if (i == 0 && new Date(Long.parseLong(time)).before(dateTime)) {
						totalMoney += money;
					} else if (dateTime_last != null) {
						if (new Date(Long.parseLong(time)).after(dateTime_last)
								&& new Date(Long.parseLong(time)).before(dateTime)) {
							totalMoney += money;
						}
					}
				}
				for (int j = 0; j < cg_invest_list.size(); j++) {
					Map<String, Object> map = cg_invest_list.get(j);
					double money = Double.parseDouble(map.get("MONEY") + "");
					String time = map.get("TIME") + "";
					// System.err.println("+++money=" + money + " ;time=" +
					// sdf_hms.parse(time));
					if (i == 0 && sdf_hms.parse(time).before(dateTime)) {
						totalMoney += money;
					} else if (dateTime_last != null) {
						if (sdf_hms.parse(time).after(dateTime_last) && sdf_hms.parse(time).before(dateTime)) {
							totalMoney += money;
						}
					}
				}
				dataList.add(NumberUtil.keepPrecision(totalMoney, 2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ret.put("data_list", dataList);
		ret.put("time_list", timeList);
		return ret;
	}

	/**
	 * @param limit
	 *            时间间隔
	 * @return
	 */
	private List<String> getTimeList() {
		List<String> timeList = new ArrayList<String>();
		String currDateTime = DateUtil.formatDateTime(new Date());
		String date = currDateTime.substring(0, 11);
		int hour = Integer.parseInt(currDateTime.substring(11, 13));
		int minutes = Integer.parseInt(currDateTime.substring(14, 16));
		int length = 0;
		if (minutes > 30) {
			length = hour + 1;
		} else {
			length = hour;
		}
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				timeList.add(date + "00:30");
			} else {
				if (i <= 9) {
					timeList.add(date + "0" + i + ":00");
					timeList.add(date + "0" + i + ":30");
				} else {
					timeList.add(date + i + ":00");
					timeList.add(date + i + ":30");
				}
			}
		}
		return timeList;
	}
	
	/**
	 * 投资TOP10
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryTop10InvestAmount")
	public R queryTop10InvestAmount() {
		List<Map<String, Object>> list = null;
		List<Integer> dataList = new ArrayList<Integer>();
		try {
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle26");
			list = util.query(SqlConstants.top10_invest_sql);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				int money = (int) Double.parseDouble(map.get("MONEY")+"");
				dataList.add(money/10000);
			}
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		Collections.reverse(dataList);
		return R.ok().put("data_list", dataList);
	}
	
	double yesterday_total_invest_amount = 0;//昨天投资总额
	double last_month_total_invest_amount = 0;//上月投资总额
	double curr_month_invest = 0;//当月投资总额 ，截止到昨天为止
	double curr_month_invest_times = 0;//当月投资笔数，截止到昨天为止
	/**
	 * 上月投资总额查询
	 * @throws SQLException 
	 */
	private void queryLastMonthTotalInvestAmont() {
		try {
			// TODO Auto-generated method stub
			String yesterday = DateUtil.getCurrDayBefore(1);
			String yesterday_start =  yesterday.substring(0, 4) + "-" + yesterday.substring(4, 6)  + "-" +  yesterday.substring(6, 8) + " 00:00:00";
			String yesterday_end =  yesterday.substring(0, 4) + "-" + yesterday.substring(4, 6)  + "-" +  yesterday.substring(6, 8) + " 23:59:59";
			String last_month = DateUtil.getMonthsBefore(DateUtil.getCurrDayStr(), 1);//20170225
			String year = last_month.substring(0, 4);
			String month = last_month.substring(4, 6);
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			String last_day_of_month = DateUtil.getLastDayOfMonth(Integer.parseInt(year), Integer.parseInt(month));
			String month_start =  year + "-" + month  + "-" +  "01 00:00:00";
			String month_end = last_day_of_month + " 00:00:00";
			//当月存管版投资额
			List<Map<String, Object>> list_month_cg = util.query(SqlConstants.last_month_cg_invest_sql, month_start, month_end, month_start, month_end, month_start, month_end);
			last_month_total_invest_amount = Double.parseDouble(list_month_cg.get(0).get("MONEY") + "");
			
			JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "oracle");
			//当月普通版投资额
			List<Map<String, Object>> list_month_pt = util2.query(SqlConstants.last_month_pt_invest_sql, month_start, month_end);
			last_month_total_invest_amount += Double.parseDouble(list_month_pt.get(0).get("MONEY") + "");
			
			//昨天存管版投资额
			JdbcUtil yes_day_util_cg = new JdbcUtil(dataSourceFactory, "mysql");
			List<Map<String, Object>> yes_day_cg = yes_day_util_cg.query(SqlConstants.last_month_cg_invest_sql, yesterday_start, yesterday_end, yesterday_start, yesterday_end, yesterday_start, yesterday_end);
			yesterday_total_invest_amount = Double.parseDouble(yes_day_cg.get(0).get("MONEY") + "");
			
			JdbcUtil util_yes_day_pt = new JdbcUtil(dataSourceFactory, "oracle");
			//昨天普通版投资额
			List<Map<String, Object>> yes_day_pt = util_yes_day_pt.query(SqlConstants.last_month_pt_invest_sql, yesterday_start, yesterday_end);
			yesterday_total_invest_amount += Double.parseDouble(yes_day_pt.get(0).get("MONEY") + "");
			
			last_month_total_invest_amount += Integer.parseInt(month_end.substring(8, 10))*20000000;
			yesterday_total_invest_amount += 20000000;
			
		} catch (Exception e) {
			yesterday_total_invest_amount = 0;
			last_month_total_invest_amount = 0;
			e.printStackTrace();
		}
	}
	/**
	 * 当天，当月投资额
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryCurrInvestAmount")
	public R queryCurrInvestAmount() {
		List<Map<String, Object>> list_day_cg = null;
		List<Map<String, Object>> list_day_pt = null;
		String currDayBefore = DateUtil.getCurrDayBefore(1);
		String yesterday = currDayBefore.substring(0 , 4) + "-" + currDayBefore.substring(4 , 6) + "-" + currDayBefore.substring(6, 8);
		String yesEndDate = yesterday + " 23:59:59";
		String currDayStr = DateUtil.getCurrDayStr();//'2017-06-27 00:00:00'
		String monthStartDate = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" +  "01 00:00:00";
		double total_month = 0;
		double total_day = 0;
		try {
			if(last_month_total_invest_amount == 0){
				queryLastMonthTotalInvestAmont();
			}
			if(curr_month_invest == 0 || isNextDay("invest_amount")){
				//存管版当月投资额
				queryCgCurrMonthInvest(monthStartDate, yesEndDate);
				//普通版当月投资额
				queryPtCurrMonthInvest(monthStartDate, yesEndDate);
			}
			
			//当天存管版投资额
			JdbcUtil util_day = new JdbcUtil(dataSourceFactory, "mysql");
			String addTime_day = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" + currDayStr.substring(6, 8) + " 00:00:00";
			String addTime_EndDay = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" + currDayStr.substring(6, 8) + " 23:59:59";
			list_day_cg = util_day.query(SqlConstants.curr_cg_invest_sql, addTime_day,addTime_EndDay, addTime_day, addTime_EndDay, addTime_day, addTime_EndDay);
			
			
			//当天普通版投资额
			JdbcUtil util2_day = new JdbcUtil(dataSourceFactory, "oracle");
			list_day_pt = util2_day.query(SqlConstants.curr_invest_sql, addTime_day, addTime_EndDay);
			
			
			total_day += Double.parseDouble(list_day_cg.get(0).get("MONEY") + "");
			if(list_day_pt.get(0).get("MONEY") == null){
				total_day += 0;
			}else{
				total_day += Double.parseDouble(list_day_pt.get(0).get("MONEY") + "");
			}
//			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle26");
//			list = util.query(SqlConstants.curr_invest_sql);
//			for (int i = 0; i < list2.size(); i++) {
//				Map<String, Object> map2 = list2.get(i);
//				int money = (int) Double.parseDouble(map2.get("MONEY")+"");
//				dataList.add(money/10000);
//			}
			//直接增加2400w
			int hours = new Date().getHours();
			total_day += 20000000/24 * hours;
			int days = Integer.parseInt(currDayStr.substring(6, 8))-1;//1号为0
			
//			total_month += days*20000000 + 20000000/24 * hours;
			total_month += days*20000000;
			total_month += curr_month_invest + total_day;
			
			numberFormat.setGroupingUsed(false);
//			System.err.println("++++++当月投资总额+++++" + numberFormat.format((int)total_month));
//			System.err.println("++++++当天投资总额+++++" + numberFormat.format((int)total_day));
			
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return R.ok().put("day", numberFormat.format((double)total_day)).put("month", numberFormat.format((double)total_month))
				.put("last_month_total_invest_amount", numberFormat.format(last_month_total_invest_amount))
				.put("yesterday_total_invest_amount", numberFormat.format(yesterday_total_invest_amount));
	}
	
	/**
	 * 普通版当月投资额，时间截止到昨天为止
	 * @param yesEndDate 
	 */
	private void queryPtCurrMonthInvest(String monthStartDate, String yesEndDate) {
		// TODO Auto-generated method stub
		try {
			JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "oracle");
			//当月普通版投资额
			List<Map<String, Object>> list_month_pt = util2.query(SqlConstants.curr_invest_sql, monthStartDate, yesEndDate);
//			curr_month_invest += Double.parseDouble(list_month_pt.get(0).get("MONEY") + "");
			
			Map<String, Object> map = list_month_pt.get(0);
			Object obj = map.get("MONEY");
			if("".equals(obj + "") || obj == null || "null".equals(obj+"")){
				curr_month_invest += 0;
			}else{
				curr_month_invest += Double.parseDouble(obj + "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存管版当月投资额，时间截止到昨天为止
	 * @param yesEndDate 
	 */
	private void queryCgCurrMonthInvest(String startDate, String yesEndDate) {
		// TODO Auto-generated method stub
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list_month_cg;
		try {
			list_month_cg = util.query(SqlConstants.curr_cg_invest_sql, startDate, yesEndDate, startDate, yesEndDate, startDate, yesEndDate);
			Map<String, Object> map = list_month_cg.get(0);
			Object moneyObj = map.get("MONEY");
			if("".equals(moneyObj + "") || moneyObj == null || "null".equals(moneyObj+"")){
				curr_month_invest += 0;
			}else{
				curr_month_invest += Double.parseDouble(moneyObj + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断当前时间，是否跨入第二天了，是的话则返回true
	 * @param type 投资笔数,投资金额
	 * @return
	 */
	private boolean isNextDay(String type){
		String currDayStr = DateUtil.getCurrDayStr();//'2017-06-27 00:00:00'
		
		if("invest_times".equals(type)){
			if(StringUtils.isEmpty(SqlConstants.currDay)){
				SqlConstants.currDay = DateUtil.getCurrDayStr();
				return false;
			}else{
				if(SqlConstants.currDay.equals(currDayStr)){
					return false;
				}else{
					SqlConstants.currDay = currDayStr;
					return true;
				}
			}
		}else if("invest_amount".equals(type)){
			if(StringUtils.isEmpty(SqlConstants.currDay)){
				SqlConstants.currDayAmount = DateUtil.getCurrDayStr();
				return false;
			}else{
				if(SqlConstants.currDayAmount.equals(currDayStr)){
					return false;
				}else{
					SqlConstants.currDayAmount = currDayStr;
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * 当天，当月投资笔数
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryCurrInvestTimes")
	public R queryCurrInvestTimes() {
		List<Map<String, Object>> list_day_cg = null;
		List<Map<String, Object>> list_day_pt = null;
		String currDayStr = DateUtil.getCurrDayStr();//'2017-06-27 00:00:00'
		String currDayBefore = DateUtil.getCurrDayBefore(1);
		String yesterday = currDayBefore.substring(0 , 4) + "-" + currDayBefore.substring(4 , 6) + "-" + currDayBefore.substring(6, 8);
		String yesEndDate = yesterday + " 23:59:59";
		String monthStartDate = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" +  "01 00:00:00";
		double total_month = 0;
		double total_day = 0;
		try {
			if(curr_month_invest_times == 0 || isNextDay("invest_times")){
				//存管版当月投资次数
				queryCgCurrMonthInvestTimes(monthStartDate, yesEndDate);
				//普通版当月投资次数
				queryPtCurrMonthInvestTimes(monthStartDate, yesEndDate);
			}
			
			JdbcUtil util_day = new JdbcUtil(dataSourceFactory, "mysql");
			String addTime_day = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" + currDayStr.substring(6, 8) + " 00:00:00";
			String addTime_endday = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6)  + "-" + currDayStr.substring(6, 8) + " 23:59:59";
			//存管版当天交易笔数
			list_day_cg = util_day.query(SqlConstants.curr_cg_invest_times_sql, addTime_day, addTime_endday, addTime_day, addTime_endday, addTime_day, addTime_endday);
			
			
			JdbcUtil util2_day = new JdbcUtil(dataSourceFactory, "oracle");
			//普通版当天交易笔数
			list_day_pt = util2_day.query(SqlConstants.curr_invest_times_sql, addTime_day,addTime_endday, addTime_day, addTime_endday);
			
			
			total_day += Double.parseDouble(list_day_cg.get(0).get("invest_times") + "");
			total_day += Double.parseDouble(list_day_pt.get(0).get("INVEST_TIMES") + "");
			total_month = curr_month_invest_times + total_day;
			System.err.println("++++++当月投资总额+++++" + total_month);
			System.err.println("++++++当天投资总额+++++" + total_day);
			
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return R.ok().put("day", total_day).put("month", total_month);
	}
	
	/**
	 * 普通版当月投资笔数，截止到昨天为止
	 * @param monthStartDate
	 * @param yesEndDate
	 */
	private void queryPtCurrMonthInvestTimes(String monthStartDate, String yesEndDate) {
		try {
			//普通版当月交易笔数
			JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list_month_pt = util2.query(SqlConstants.curr_invest_times_sql, monthStartDate, yesEndDate, monthStartDate, yesEndDate);
//			curr_month_invest_times += Double.parseDouble(list_month_pt.get(0).get("INVEST_TIMES") + "");
			
			Map<String, Object> map = list_month_pt.get(0);
			Object obj = map.get("INVEST_TIMES");
			if("".equals(obj + "") || obj == null || "null".equals(obj+"")){
				curr_month_invest_times += 0;
			}else{
				curr_month_invest_times += Double.parseDouble(obj + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 存管版当月投资笔数，截止到昨天为止
	 * @param monthStartDate
	 * @param yesEndDate
	 */
	private void queryCgCurrMonthInvestTimes(String monthStartDate, String yesEndDate) {
		try {
			//存管版当月交易笔数
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			List<Map<String, Object>> list_month_cg = util.query(SqlConstants.curr_cg_invest_times_sql, monthStartDate,yesEndDate , monthStartDate, yesEndDate, monthStartDate, yesEndDate);
			
			
			Map<String, Object> map = list_month_cg.get(0);
			Object obj = map.get("invest_times");
			if("".equals(obj + "") || obj == null || "null".equals(obj+"")){
				curr_month_invest_times += 0;
			}else{
				curr_month_invest_times += Double.parseDouble(obj + "");
			}
			
//			curr_month_invest_times += Double.parseDouble(list_month_cg.get(0).get("invest_times") + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 累计投资笔数
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryTotalInvestTimes")
	public R queryTotalInvestTimes() {
		List<Map<String, Object>> list_month_cg = null;
		List<Map<String, Object>> list_month_pt = null;
		int total_invest = 0;
		try {
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			list_month_cg = util.query(SqlConstants.curr_cg_invest_total_times_sql);
			total_invest += Integer.parseInt(list_month_cg.get(0).get("invest_times") + "");
			
			JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "oracle");
			list_month_pt = util2.query(SqlConstants.curr_invest_total_times_sql);
			total_invest += Integer.parseInt(list_month_pt.get(0).get("INVEST_TIMES") + "");
			
			
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return R.ok().put("total_invest", total_invest);
	}
	
	/**
	 * 省份交易额统计
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryProvinceInvestInvestAmount")
	public R queryProvinceInvestInvestAmount() {
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> province_list = new ArrayList<String>();
		try {
			String currDayStr = DateUtil.getCurrDayStr();
			String yesterday = DateUtil.getCurrDayBefore(1);
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle26");
			String addTime = "";
			addTime = currDayStr.substring(0, 4) + "-" + currDayStr.substring(4, 6) + "-01 00:00:00";
			List<Map<String, Object>> list = util.query(SqlConstants.province_invest_sql, addTime);
			if(list ==  null || list.size() == 0){
				addTime = yesterday.substring(0, 4) + "-" + yesterday.substring(4, 6) + "-"+yesterday.substring(6, 8)+" 00:00:00";
				list = new JdbcUtil(dataSourceFactory, "oracle26").query(SqlConstants.province_invest_sql, addTime);
			}
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				int money = (int) Double.parseDouble(map.get("MONEY")+"");
				dataList.add(money/10000);
				province_list.add(map.get("PROVINCE")+"");
			}
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		dataList = dataList.subList(0, 10);
		province_list = province_list.subList(0, 10);
		Collections.reverse(dataList);
		Collections.reverse(province_list);
		return R.ok().put("data_list",dataList).put("province_list", province_list);
	}
	
	
	/**
	 * 用户个人待收，实时查询
	 * 
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryUserWait")
	public Map<String,Object> queryUserWait(String oldUserId,String cgUserId) {
		return mainService.queryUserWait(oldUserId, cgUserId);
	}
}
