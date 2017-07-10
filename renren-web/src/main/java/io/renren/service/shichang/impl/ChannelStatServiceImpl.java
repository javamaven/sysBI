package io.renren.service.shichang.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.entity.DimChannelEntity;
import io.renren.service.DimChannelService;
import io.renren.service.shichang.ChannelStatService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.NumberUtil;
import io.renren.utils.PageUtils;

@Service("channelStatService")
public class ChannelStatServiceImpl implements ChannelStatService {
	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	DataSourceFactory dataSourceFactory;

	@Override
	public PageUtils queryTotalList(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelName) {
		int start = (page - 1) * limit;
		int end = start + limit;
		long l1 = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
		channelDataMap = getChannelLabelKeyMap(channelList);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			List<String> channelLabelList = new ArrayList<>();
			Map<String, Map<String, Object>> registerMap = new HashMap<>();
			Map<String, Map<String, Object>> firstInvestMap = new HashMap<>();
			Map<String, Map<String, Object>> multiInvestMap = new HashMap<>();
			Map<String, Map<String, Object>> firstInvestMoneyMap = new HashMap<>();// 首投金额（万元）
			Map<String, Map<String, Object>> totalInvestMoneyMap = new HashMap<>();// 累投金额（万元）

			Map<String, Map<String, Object>> firstInvestYearAmontMap = new HashMap<>();// 首投年化投资金额（万元）
			Map<String, Map<String, Object>> yearTotalAmountMap = new HashMap<>();// 年化累投金额（万元）
			Map<String, Map<String, Object>> daishouUserNumMap = new HashMap<>();// 待收>100人数

			Thread t1 = new Thread(
					new QueryThread(dataSourceFactory, registerMap, channelLabelList, getQuerySql("1.注册人数"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			Thread t2 = new Thread(
					new QueryThread(dataSourceFactory, firstInvestMap, channelLabelList, getQuerySql("2.首投人数"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			Thread t3 = new Thread(
					new QueryThread(dataSourceFactory, multiInvestMap, channelLabelList, getQuerySql("3.复投人数"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			Thread t4 = new Thread(
					new QueryThread(dataSourceFactory, firstInvestMoneyMap, channelLabelList, getQuerySql("4.首投金额（万元）"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			Thread t5 = new Thread(
					new QueryThread(dataSourceFactory, totalInvestMoneyMap, channelLabelList, getQuerySql("5.累投金额（万元）"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			Thread t6 = new Thread(new QueryThread(dataSourceFactory, firstInvestYearAmontMap, channelLabelList,
					getQuerySql("6.首投年化投资金额（万元）"), registerStartTime, registerEndTime, firstInvestStartTime,
					firstInvestEndTime));
			Thread t7 = new Thread(new QueryThread(dataSourceFactory, yearTotalAmountMap, channelLabelList,
					getQuerySql("7.年化累投金额（万元）"), registerStartTime, registerEndTime, firstInvestStartTime,
					firstInvestEndTime));
			Thread t8 = new Thread(
					new QueryThread(dataSourceFactory, daishouUserNumMap, channelLabelList, getQuerySql("8.待收>100人数"),
							registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime));
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t5.start();
			t6.start();
			t7.start();
			t8.start();
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
			// 聚合数据
			unionData(resultList, channelLabelList, registerMap, firstInvestMap, multiInvestMap, firstInvestMoneyMap,
					totalInvestMoneyMap, firstInvestYearAmontMap, yearTotalAmountMap, daishouUserNumMap, channelName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (end > resultList.size()) {
			retList.addAll(resultList.subList(start, resultList.size()));
		} else {
			retList.addAll(resultList.subList(start, end));
		}
		PageUtils pageUtil = new PageUtils(retList, resultList.size(), limit, page);
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++渠道实时统计耗时+++++++++" + (l2 - l1));
		return pageUtil;
	}

	/**
	 * 将所有结果集，按渠道合并
	 * 
	 * @param resultList
	 * @param channelLabelList
	 * @param registerMap
	 * @param firstInvestMap
	 * @param multiInvestMap
	 * @param firstInvestMoneyMap
	 * @param daishouUserNumMap
	 * @param yearTotalAmountMap
	 * @param firstInvestYearAmontMap
	 * @param totalInvestMoneyMap
	 */
	private void unionData(List<Map<String, Object>> resultList, List<String> channelLabelList,
			Map<String, Map<String, Object>> registerMap, Map<String, Map<String, Object>> firstInvestMap,
			Map<String, Map<String, Object>> multiInvestMap, Map<String, Map<String, Object>> firstInvestMoneyMap,
			Map<String, Map<String, Object>> totalInvestMoneyMap,
			Map<String, Map<String, Object>> firstInvestYearAmontMap,
			Map<String, Map<String, Object>> yearTotalAmountMap, Map<String, Map<String, Object>> daishouUserNumMap,
			String channelName) {
		Map<String, Object> data = null;
		for (int i = 0; i < channelLabelList.size(); i++) {
			String channelLabel = channelLabelList.get(i);
			data = new HashMap<String, Object>();
			// 注册人数
			if (registerMap.containsKey(channelLabel)) {
				data.put("channel_name", registerMap.get(channelLabel).get("channel_name"));
				data.put("channel_label", channelLabel);
				data.put("注册人数", registerMap.get(channelLabel).get("注册人数"));
			}
			boolean flag = false;
			if (StringUtils.isNotEmpty(channelName) && !"null".equals(channelName)) {
				String[] split = channelName.split(",");
				for (int j = 0; j < split.length; j++) {
//					if (split[j].equals(data.get("channel_name"))) {
					if(data.get("channel_name") != null){
						if (data.get("channel_name").toString().contains(split[j])) {
							flag = true;
							break;
						}
					}
				}
				if (!flag) {// false时，过滤掉该渠道
					continue;
				}
			}

			// 首投人数
			if (firstInvestMap.containsKey(channelLabel)) {
				data.put("首投人数", firstInvestMap.get(channelLabel).get("首投人数"));
			}
			// 转化率
			double st_num = data.get("首投人数") == null ? 0 : Double.parseDouble(data.get("首投人数").toString());
			double reg_num = data.get("注册人数") == null ? 0 : Double.parseDouble(data.get("注册人数").toString());
			if (reg_num == 0) {
				data.put("转化率", "0.00%");
			} else {
				data.put("转化率", NumberUtil.keepPrecision(st_num * 100 / reg_num, 2) + "%");
			}
			// 复投人数
			if (multiInvestMap.containsKey(channelLabel)) {
				data.put("复投人数", multiInvestMap.get(channelLabel).get("复投人数"));
			}
			// 复投率
			double multi_num = data.get("复投人数") == null ? 0 : Double.parseDouble(data.get("复投人数").toString());
			if (st_num == 0) {
				data.put("复投率", "0.00%");
			} else {
				data.put("复投率", NumberUtil.keepPrecision(multi_num * 100 / st_num, 2) + "%");
			}
			// 首投金额（万元）
			if (firstInvestMoneyMap.containsKey(channelLabel)) {
				data.put("首投金额（万元）", firstInvestMoneyMap.get(channelLabel).get("首投金额（万元）"));
			}
			// 累投金额（万元）
			if (totalInvestMoneyMap.containsKey(channelLabel)) {
				data.put("累投金额（万元）", totalInvestMoneyMap.get(channelLabel).get("累投金额（万元）"));
			}
			// 复投金额（万元）
			data.put("复投金额（万元）", NumberUtil.keepPrecision(
					Double.parseDouble(data.get("累投金额（万元）") == null ? "0" : data.get("累投金额（万元）").toString())
							- Double.parseDouble(data.get("首投金额（万元）") == null ? "0" : data.get("首投金额（万元）").toString()),
					2));
			// 首投年化投资金额（万元）
			if (firstInvestYearAmontMap.containsKey(channelLabel)) {
				data.put("首投年化投资金额（万元）", firstInvestYearAmontMap.get(channelLabel).get("首投年化投资金额（万元）"));
			}
			// 年化累投金额（万元）
			if (yearTotalAmountMap.containsKey(channelLabel)) {
				data.put("年化累投金额（万元）", yearTotalAmountMap.get(channelLabel).get("年化累投金额（万元）"));
			}
			// 年化复投金额（万元）
			data.put("年化复投金额（万元）",
					NumberUtil.keepPrecision(
							Double.parseDouble(data.get("年化累投金额（万元）") == null ? "0" : data.get("年化累投金额（万元）").toString())
									- Double.parseDouble(data.get("首投年化投资金额（万元）") == null ? "0"
											: data.get("首投年化投资金额（万元）").toString()),
							2));
			// 待收>100人数
			if (daishouUserNumMap.containsKey(channelLabel)) {
				data.put("待收>100人数", daishouUserNumMap.get(channelLabel).get("待收大于100人数"));
			}
			resultList.add(data);
		}
	}

	/**
	 * 传入指标名字,解析出对应指标统计sql
	 * 
	 * @param name
	 * @return
	 */
	private String getQuerySql(String name) {
		String sql = "";
		String path = this.getClass().getResource("/").getPath();
		try {
			boolean flag = false;
			List<String> lines = FileUtils.readLines(new File(path + File.separator + "sql/市场部渠道实时统计.txt"));
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				// System.err.println(line);
				if (line.contains(name)) {
					flag = true;
					continue;
				}
				if (flag && !line.contains("##")) {
					sql += line + " ";
				}
				if (sql.length() > 0 && line.contains("##")) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.err.println("++++++++++" + name + "查询sql: " + sql);
		return sql;
	}

	Map<String, String> channelDataMap = null;

	class QueryThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private Map<String, Map<String, Object>> resultMap;
		private String querySql;
		private String registerStartTime;
		private String registerEndTime;
		private String firstInvestStartTime;
		private String firstInvestEndTime;
		private List<String> channelLabelList;

		public QueryThread(DataSourceFactory dataSourceFactory, Map<String, Map<String, Object>> resultMap,
				List<String> channelLabelList, String querySql, String registerStartTime, String registerEndTime,
				String firstInvestStartTime, String firstInvestEndTime) {
			this.dataSourceFactory = dataSourceFactory;
			this.resultMap = resultMap;
			this.querySql = querySql;
			this.registerStartTime = registerStartTime == null ? "" : registerStartTime;
			this.registerEndTime = registerEndTime == null ? "" : registerEndTime;
			this.firstInvestStartTime = firstInvestStartTime == null ? "" : firstInvestStartTime;
			this.firstInvestEndTime = firstInvestEndTime == null ? "" : firstInvestEndTime;
			this.channelLabelList = channelLabelList;
		}

		@Override
		public void run() {
			try {
				String registerTimeCond = "";
				if (StringUtils.isEmpty(registerStartTime) && StringUtils.isNotEmpty(firstInvestStartTime)) {
					registerStartTime = firstInvestStartTime;
				} else if (StringUtils.isNotEmpty(registerStartTime) && StringUtils.isEmpty(firstInvestStartTime)) {
					firstInvestStartTime = registerStartTime;
				}
				if (StringUtils.isEmpty(registerEndTime) && StringUtils.isNotEmpty(firstInvestEndTime)) {
					registerEndTime = firstInvestEndTime;
				} else if (StringUtils.isNotEmpty(registerEndTime) && StringUtils.isEmpty(firstInvestEndTime)) {
					firstInvestEndTime = registerEndTime;
				}
				// 注册时间条件
				if (StringUtils.isNotEmpty(registerStartTime)) {
					registerTimeCond += " and u1.register_time >= '" + registerStartTime + " 00:00:00' ";
				}
				if (StringUtils.isNotEmpty(registerEndTime)) {
					registerTimeCond += " and u1.register_time < '" + registerEndTime + " 00:00:00' ";
				}
				// 首投时间查询条件
				String firstInvestTimeCond = "";
				/// 首投时间查询条件
				if (StringUtils.isNotEmpty(firstInvestStartTime)) {
					firstInvestTimeCond += " and s.st_time >= '" + firstInvestStartTime + " 00:00:00' ";
				}
				if (StringUtils.isNotEmpty(firstInvestEndTime)) {
					firstInvestTimeCond += " and s.st_time < '" + firstInvestEndTime + " 00:00:00' ";
				}

				querySql = querySql.replace("${registerTimeCond}", registerTimeCond);
				querySql = querySql.replace("${firstInvestTimeCond}", firstInvestTimeCond);
				long l1 = System.currentTimeMillis();
				JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
				List<Map<String, Object>> list = util.query(querySql);
				long l2 = System.currentTimeMillis();
				System.err.println(querySql);
				System.err.println(" ++++耗时：" + (l2 - l1));
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					Object channelLabel = map.get("activity_tag");
					if (channelLabel == null || channelLabel.toString().equals("")) {
						map.put("activity_tag", "weizhi");
						map.put("channel_name", "未知");
					} else {
						map.put("channel_name", channelDataMap.get(channelLabel.toString()));
					}
					if (!channelLabelList.contains(map.get("activity_tag"))) {
						channelLabelList.add(map.get("activity_tag") + "");
					}
					resultMap.put(map.get("activity_tag").toString(), map);
				}
			} catch (SQLException e) {
				System.err.println("++++++++error sql +++++++++++ " + querySql);
				e.printStackTrace();
			}
		}
	}

	private Map<String, String> getChannelLabelKeyMap(List<DimChannelEntity> channelList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelNameBack());
		}
		return dataMap;
	}

	private Map<String, String> getChannelNameKeyMap(List<DimChannelEntity> channelList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			dataMap.put(dimChannelEntity.getChannelNameBack(), dimChannelEntity.getChannelLabel());
		}
		return dataMap;
	}

	@Override
	public PageUtils queryDetailList(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelLabel) {
		int start = (page - 1) * limit;
		int end = start + limit;
		long l1 = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
		channelDataMap = getChannelLabelKeyMap(channelList);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String querySql = "";
		String registerTimeCond = "";
		if (StringUtils.isEmpty(registerStartTime) && StringUtils.isNotEmpty(firstInvestStartTime)) {
			registerStartTime = firstInvestStartTime;
		} else if (StringUtils.isNotEmpty(registerStartTime) && StringUtils.isEmpty(firstInvestStartTime)) {
			firstInvestStartTime = registerStartTime;
		}
		if (StringUtils.isEmpty(registerEndTime) && StringUtils.isNotEmpty(firstInvestEndTime)) {
			registerEndTime = firstInvestEndTime;
		} else if (StringUtils.isNotEmpty(registerEndTime) && StringUtils.isEmpty(firstInvestEndTime)) {
			firstInvestEndTime = registerEndTime;
		}
		// 注册时间条件
		if (StringUtils.isNotEmpty(registerStartTime)) {
			registerTimeCond += " and u1.register_time >= '" + registerStartTime + " 00:00:00' ";
		}
		if (StringUtils.isNotEmpty(registerEndTime)) {
			registerTimeCond += " and u1.register_time < '" + registerEndTime + " 00:00:00' ";
		}
		// 首投时间查询条件
		String firstInvestTimeCond = "";
		/// 首投时间查询条件
		if (StringUtils.isNotEmpty(firstInvestStartTime)) {
			firstInvestTimeCond += " and s.st_time >= '" + firstInvestStartTime + " 00:00:00' ";
		}
		if (StringUtils.isNotEmpty(firstInvestEndTime)) {
			firstInvestTimeCond += " and s.st_time < '" + firstInvestEndTime + " 00:00:00' ";
		}
		String path = this.getClass().getResource("/").getPath();
		try {
			querySql = FileUtils.readFileToString(new File(path + File.separator + "sql/市场部渠道实时统计明细数据.txt"));
			querySql = querySql.replace("${registerTimeCond}", registerTimeCond);
			querySql = querySql.replace("${firstInvestTimeCond}", firstInvestTimeCond);
			if (StringUtils.isEmpty(channelLabel)) {
				querySql = querySql.replace("${channelLabelCond}", "");
			} else {
				querySql = querySql.replace("${channelLabelCond}", " and u1.activity_tag='" + channelLabel + "' ");
			}
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			resultList = util.query(querySql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < resultList.size(); i++) {
			Map<String, Object> map = resultList.get(i);

			double st_money = Double.parseDouble(map.get("st_money") == null ? "0" : map.get("st_money").toString());
			double total_money = Double.parseDouble(map.get("累投金额") == null ? "0" : map.get("累投金额").toString());
			double year_invest_money = Double
					.parseDouble(map.get("年化累投金额") == null ? "0" : map.get("年化累投金额").toString());
			double days = Double.parseDouble(map.get("days") == null ? "0" : map.get("days").toString());
			map.put("channel_name", channelDataMap.get(map.get("activity_tag")));
			// 首投年化投资金额
			map.put("首投年化投资金额", NumberUtil.keepPrecision(st_money * days / 360, 2));
			// 复投金额
			map.put("复投金额", NumberUtil.keepPrecision(total_money - st_money, 2));
			// 年化复投金额
			map.put("年化复投金额",
					NumberUtil.keepPrecision(year_invest_money - Double.parseDouble(map.get("首投年化投资金额") + ""), 2));
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (end > resultList.size()) {
			retList.addAll(resultList.subList(start, resultList.size()));
		} else {
			retList.addAll(resultList.subList(start, end));
		}
		PageUtils pageUtil = new PageUtils(retList, resultList.size(), limit, page);
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++渠道实时统计明细查询耗时+++++++++" + (l2 - l1));
		return pageUtil;
	}

}
