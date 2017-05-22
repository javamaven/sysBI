package io.renren.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;

import io.renren.controller.querythread.ChannelRenewDataQueryThread;
import io.renren.dao.ChannelRenewDataDao;
import io.renren.entity.ChannelRenewDataEntity;
import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelRenewDataService;
import io.renren.service.DimChannelService;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;

@Service("ChannelRenewDataService")
public class ChannelRenewDataServiceImpl implements ChannelRenewDataService {
	@Autowired
	private DruidDataSource dataSource;

	@Autowired
	private ChannelRenewDataDao channelRenewDataDao;

	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	private ChannelRenewDataService service;

	@Override
	public List<ChannelRenewDataEntity> queryChannelCost(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryChannelCost(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryOnlineTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryOnlineTime(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearAmount(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearRoi(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestYearRoi(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay30FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryDay30FirstInvestYearRoi(map);
		// return queryFirstInvestYearRoiByJdbc(map, 30);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay60FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryDay60FirstInvestYearRoi(map);
		// return queryFirstInvestYearRoiByJdbc(map, 60);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay90FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryDay90FirstInvestYearRoi(map);
		// return queryFirstInvestYearRoiByJdbc(map, 90);
	}

	/**
	 * 通过jdbc查询数据
	 * 
	 * @return
	 */
	public List<ChannelRenewDataEntity> queryFirstInvestYearRoiByJdbc(Map<String, Object> map, int queryDay) {
		long l1 = System.currentTimeMillis();
		List<ChannelRenewDataEntity> retList = new ArrayList<ChannelRenewDataEntity>();
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String procedureSql = "call first_invest_year_roi_renew(? , ?)";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcHelper.callableQuery(procedureSql, map.get("startDate"), map.get("endDate"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long l2 = System.currentTimeMillis();
		System.err.println("++++++++++sql[" + procedureSql + "][" + map.get("startDate") + "," + map.get("endDate")
				+ "];耗时=" + (l2 - l1));

		transform(retList, list, queryDay);
		return retList;
	}

	public void transform(List<ChannelRenewDataEntity> retList, List<Map<String, Object>> list, int queryDay) {
		ChannelRenewDataEntity entity = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> dataMap = list.get(i);
			entity = new ChannelRenewDataEntity();
			entity.setChannelLabel(dataMap.get("channelLabel") + "");
			if (queryDay == 30) {
				entity.setDay30FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			if (queryDay == 60) {
				entity.setDay60FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			if (queryDay == 90) {
				entity.setDay90FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			retList.add(entity);
		}
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNumDay30(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNumDay30(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNumDay60(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNumDay60(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNumDay90(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNumDay90(map);
	}

	@Override
	public void insert(ChannelRenewDataEntity channelRenewDataEntity) {
		// TODO Auto-generated method stub
		channelRenewDataDao.insert(channelRenewDataEntity);
	}

	@Override
	public void delete(ChannelRenewDataEntity channelRenewDataEntity) {
		// TODO Auto-generated method stub
		channelRenewDataDao.delete(channelRenewDataEntity);
	}

	@Override
	public PageUtils query(Map<String, Object> params) throws ParseException {
		dimChannelService.createChanelCostTable(null);
		// 查询渠道列表信息
		List<DimChannelEntity> channelList = dimChannelService.queryOnlineChannelCostList(null);

		Map<String, String> channelDataMap = getChannelLabelKeyMap(channelList);
		System.err.println("+++++查询条件： " + params);
		params.put("endDate", params.get("date") + "");
		Object object = params.get("channelName");
		List<String> channelLabelList = new ArrayList<String>();

		if (object != null) {
			List<String> list = JSON.parseArray(object + "", String.class);
			channelLabelList = getChannelLabelsByName(channelList, list);
			params.put("channelLabelList", channelLabelList);
		} else {
			params.put("channelLabelList", new ArrayList<String>());
		}
		System.err.println("+++++查询条件： " + params);
		// 查询列表数据
		Query query = new Query(params);
		Map<String, Object> channelListMap = new ConcurrentHashMap<String, Object>();
		Map<String, Map<String, ChannelRenewDataEntity>> resultMap = new HashMap<String, Map<String, ChannelRenewDataEntity>>();
		// 查询并且返回各个指标数据
		buildQueryParamsAndQueryData(params, channelListMap, resultMap);
		// 将数据按照渠道聚合
		List<ChannelRenewDataEntity> list = unionData(channelDataMap, channelListMap, resultMap);
		// 过滤channelLabel
		List<ChannelRenewDataEntity> retList = new ArrayList<ChannelRenewDataEntity>();
		if (channelLabelList.size() == 0) {
			retList.addAll(list);
		} else {
			for (int i = 0; i < list.size(); i++) {
				ChannelRenewDataEntity entity = list.get(i);
				for (int j = 0; j < channelLabelList.size(); j++) {
					String label = channelLabelList.get(j);
					if ((label + "").trim().equals((entity.getChannelLabel() + "").trim())) {
						retList.add(entity);
					}
				}
			}
		}
		// 获取数据条数
		PageUtils pageUtil = new PageUtils(retList, retList.size(), query.getLimit(), query.getPage());
		for (int i = 0; i < retList.size(); i++) {
			// ChannelRenewDataEntity channelRenewDataEntity = retList.get(i);
			// service.delete(channelRenewDataEntity);
			// service.insert(channelRenewDataEntity);
		}
		return pageUtil;
	}

	private List<ChannelRenewDataEntity> unionData(Map<String, String> channelDataMap,
			Map<String, Object> channelListMap, Map<String, Map<String, ChannelRenewDataEntity>> resultMap) {
		List<ChannelRenewDataEntity> list = new ArrayList<ChannelRenewDataEntity>();

		Map<String, String> chanelTypeMap = dimChannelService.queryChanelTypeMap();

		Iterator<String> iterator = channelListMap.keySet().iterator();
		ChannelRenewDataEntity vo = null;
		while (iterator.hasNext()) {
			String key = iterator.next();
			vo = new ChannelRenewDataEntity();
			if (channelDataMap.containsKey(key)) {
				vo.setChannelName(channelDataMap.get(key));// 渠道名称
			} else {
				vo.setChannelName("未知");// 渠道名称
			}
			vo.setChannelLabel(key);// 渠道标签
			// 渠道类型
			if (chanelTypeMap.containsKey(key)) {
				vo.setChannelType(chanelTypeMap.get(key));
			} else {
				vo.setChannelType("未知");
			}
			// 30天 60 90 费用
			Map<String, ChannelRenewDataEntity> map1 = resultMap.get("result1");
			if (map1.containsKey(key)) {
				ChannelRenewDataEntity en = map1.get(key);
				vo.setDay30Cost(NumberUtil.keepPrecision(en.getDay30Cost(), 2));
				vo.setDay60Cost(NumberUtil.keepPrecision(en.getDay60Cost(), 2));
				vo.setDay90Cost(NumberUtil.keepPrecision(en.getDay90Cost(), 2));
			}
			// 上线时间
			Map<String, ChannelRenewDataEntity> map2 = resultMap.get("result2");
			if (map2.containsKey(key)) {
				ChannelRenewDataEntity en = map2.get(key);
				vo.setOnlineTime(en.getOnlineTime());
			}

			// 30日年化投资金额
			Map<String, ChannelRenewDataEntity> map3_30day = resultMap.get("result3_30day");
			if (map3_30day.containsKey(key)) {
				ChannelRenewDataEntity en = map3_30day.get(key);
				vo.setDay30YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
				vo.setDay30Amount(en.getAmount());
			}
			// 60日年化投资金额
			Map<String, ChannelRenewDataEntity> map3_60day = resultMap.get("result3_60day");
			if (map3_60day.containsKey(key)) {
				ChannelRenewDataEntity en = map3_60day.get(key);
				vo.setDay60YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
				vo.setDay60Amount(en.getAmount());
			}
			// 90日年化投资金额
			Map<String, ChannelRenewDataEntity> map3_90day = resultMap.get("result3_90day");
			if (map3_90day.containsKey(key)) {
				ChannelRenewDataEntity en = map3_90day.get(key);
				vo.setDay90YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
				vo.setDay90Amount(en.getAmount());
			}
			// 30日年化ROI
			// 60日年化ROI
			// 90日年化ROI
			if (vo.getDay30Cost() == 0) {
				vo.setDay30YearRoi(0);
			} else {
				vo.setDay30YearRoi(NumberUtil.keepPrecision(vo.getDay30YearAmount() / vo.getDay30Cost(), 2));
			}
			if (vo.getDay60Cost() == 0) {
				vo.setDay60YearRoi(0);
			} else {
				vo.setDay60YearRoi(NumberUtil.keepPrecision(vo.getDay60YearAmount() / vo.getDay60Cost(), 2));
			}
			if (vo.getDay90Cost() == 0) {
				vo.setDay90YearRoi(0);
			} else {
				vo.setDay90YearRoi(NumberUtil.keepPrecision(vo.getDay90YearAmount() / vo.getDay90Cost(), 2));
			}

			// 30日首投复投人数，首投复投金额，首投年化金额，
			// 30日复投率，30日金额复投率，30日人均首投年化金额
			Map<String, ChannelRenewDataEntity> result4_day30 = resultMap.get("result4_day30");
			if (result4_day30.containsKey(key)) {
				ChannelRenewDataEntity en = result4_day30.get(key);
				vo.setDay30FirstInvestUserNum(en.getDay30FirstInvestUserNum());
				vo.setDay30MultiInvestUserNum(en.getDay30MultiInvestUserNum());
				vo.setDay30FirstInvestAmount(NumberUtil.keepPrecision(en.getDay30FirstInvestAmount(), 2));
				vo.setDay30MultiInvestAmount(NumberUtil.keepPrecision(en.getDay30MultiInvestAmount(), 2));
				vo.setDay30FirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay30FirstInvestYearAmount(), 2));
				vo.setDay30MultiRate(en.getDay30MultiRate());
				vo.setDay30MultiRateText(NumberUtil.keepPrecision((double) en.getDay30MultiRate() * 100, 2) + "%");
				vo.setDay30MultiInvestAmountRate(en.getDay30MultiInvestAmountRate());
				vo.setDay30MultiInvestAmountRateText(
						NumberUtil.keepPrecision((double) en.getDay30MultiInvestAmountRate() * 100, 2) + "%");
				vo.setDay30PerFirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay30PerFirstInvestYearAmount(), 2));
			}
			// 60日首投复投人数，首投复投金额，首投年化金额，
			// 60日复投率，60日金额复投率，60日人均首投年化金额
			Map<String, ChannelRenewDataEntity> result4_day60 = resultMap.get("result4_day60");
			if (result4_day60.containsKey(key)) {
				ChannelRenewDataEntity en = result4_day60.get(key);
				vo.setDay60FirstInvestUserNum(en.getDay60FirstInvestUserNum());
				vo.setDay60MultiInvestUserNum(en.getDay60MultiInvestUserNum());
				vo.setDay60FirstInvestAmount(NumberUtil.keepPrecision(en.getDay60FirstInvestAmount(), 2));
				vo.setDay60MultiInvestAmount(NumberUtil.keepPrecision(en.getDay60MultiInvestAmount(), 2));
				vo.setDay60FirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay60FirstInvestYearAmount(), 2));
				vo.setDay60MultiRate(en.getDay60MultiRate());
				vo.setDay60MultiRateText(NumberUtil.keepPrecision((double) en.getDay60MultiRate() * 100, 2) + "%");
				vo.setDay60MultiInvestAmountRate(en.getDay60MultiInvestAmountRate());
				vo.setDay60MultiInvestAmountRateText(
						NumberUtil.keepPrecision((double) en.getDay60MultiInvestAmountRate() * 100, 2) + "%");
				vo.setDay60PerFirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay60PerFirstInvestYearAmount(), 2));
			}
			// 90日首投复投人数，首投复投金额，首投年化金额，
			// 90日复投率，90日金额复投率，90日人均首投年化金额
			Map<String, ChannelRenewDataEntity> result4_day90 = resultMap.get("result4_day90");
			if (result4_day90.containsKey(key)) {
				ChannelRenewDataEntity en = result4_day90.get(key);
				vo.setDay90FirstInvestUserNum(en.getDay90FirstInvestUserNum());
				vo.setDay90MultiInvestUserNum(en.getDay90MultiInvestUserNum());
				vo.setDay90FirstInvestAmount(NumberUtil.keepPrecision(en.getDay90FirstInvestAmount(), 2));
				vo.setDay90MultiInvestAmount(NumberUtil.keepPrecision(en.getDay90MultiInvestAmount(), 2));
				vo.setDay90FirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay90FirstInvestYearAmount(), 2));
				vo.setDay90MultiRate(en.getDay90MultiRate());
				vo.setDay90MultiRateText(NumberUtil.keepPrecision((double) en.getDay90MultiRate() * 100, 2) + "%");
				vo.setDay90MultiInvestAmountRate(en.getDay90MultiInvestAmountRate());
				vo.setDay90MultiInvestAmountRateText(
						NumberUtil.keepPrecision((double) en.getDay90MultiInvestAmountRate() * 100, 2) + "%");
				vo.setDay90PerFirstInvestYearAmount(NumberUtil.keepPrecision(en.getDay90PerFirstInvestYearAmount(), 2));
			}

			// 30日首投年化ROI
			// 60日首投年化ROI
			// 90日首投年化ROI
			if (vo.getDay30Cost() == 0) {
				vo.setDay30FirstInvestYearRoi(0);
			} else {
				vo.setDay30FirstInvestYearRoi(
						NumberUtil.keepPrecision((double) vo.getDay30FirstInvestYearAmount() / vo.getDay30Cost(), 4));
			}
			if (vo.getDay60Cost() == 0) {
				vo.setDay60FirstInvestYearRoi(0);
			} else {
				vo.setDay60FirstInvestYearRoi(
						NumberUtil.keepPrecision((double) vo.getDay60FirstInvestYearAmount() / vo.getDay60Cost(), 4));
			}
			if (vo.getDay90Cost() == 0) {
				vo.setDay90FirstInvestYearRoi(0);
			} else {
				vo.setDay90FirstInvestYearRoi(
						NumberUtil.keepPrecision((double) vo.getDay90FirstInvestYearAmount() / vo.getDay90Cost(), 4));
			}

			list.add(vo);
		}
		Collections.sort(list, new MyCompartor());
		return list;
	}

	/**
	 * 按渠道名字排序
	 * 
	 * @author Administrator
	 *
	 */
	class MyCompartor implements Comparator<ChannelRenewDataEntity> {
		@Override
		public int compare(ChannelRenewDataEntity o1, ChannelRenewDataEntity o2) {
			double m = o1.getDay30Cost() - o2.getDay30Cost();
			if (m == 0) {
				return 0;
			} else if (m > 0) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	/**
	 * 根据参数: 1.创建各个指标的查询参数 2.创建返回结果map
	 * 
	 * @param params
	 * @param resultMap
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void buildQueryParamsAndQueryData(Map<String, Object> params, Map<String, Object> channelListMap,
			Map<String, Map<String, ChannelRenewDataEntity>> resultMap) throws ParseException {
		String endDate = params.get("endDate") + "";
		List<String> channelLabelList = (List<String>) params.get("channelLabelList");
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("endDate", endDate);
		params1.put("channelLabelList", channelLabelList);
		params1.put("onlineDate", endDate);
		String timeFormat = "yyyy-MM-dd";
		params1.put("day30", DateUtil.getCurrDayBefore(endDate, -30, timeFormat));
		params1.put("day60", DateUtil.getCurrDayBefore(endDate, -60, timeFormat));
		params1.put("day90", DateUtil.getCurrDayBefore(endDate, -90, timeFormat));

		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("onlineDate", endDate);
		params2.put("channelLabelList", channelLabelList);

		// 30日60日90日，年化金额 ,传入不同参数
		Map<String, Object> params3_30day = new HashMap<String, Object>();
		params3_30day.put("onlineDate", endDate);
		params3_30day.put("startDate", endDate + " 00:00:00");
		params3_30day.put("endDate", DateUtil.getCurrDayBefore(endDate, -30, timeFormat) + " 23:59:59");
		params3_30day.put("channelLabelList", channelLabelList);

		Map<String, Object> params3_60day = new HashMap<String, Object>();
		params3_60day.put("onlineDate", endDate);
		params3_60day.put("startDate", endDate + " 00:00:00");
		params3_60day.put("endDate", DateUtil.getCurrDayBefore(endDate, -60, timeFormat) + " 23:59:59");
		params3_60day.put("channelLabelList", channelLabelList);

		Map<String, Object> params3_90day = new HashMap<String, Object>();
		params3_90day.put("onlineDate", endDate);
		params3_90day.put("startDate", endDate + " 00:00:00");
		params3_90day.put("endDate", DateUtil.getCurrDayBefore(endDate, -90, timeFormat) + " 23:59:59");
		params3_90day.put("channelLabelList", channelLabelList);

		// 首投复投人数，首投复投金额，首投复投年化金额，复投率，金额复投率，人均首投金额
		Map<String, Object> params4_day30 = new HashMap<String, Object>();
		params4_day30.putAll(params3_30day);
		Map<String, Object> params4_day60 = new HashMap<String, Object>();
		params4_day60.putAll(params3_60day);
		Map<String, Object> params4_day90 = new HashMap<String, Object>();
		params4_day90.putAll(params3_90day);

		Map<String, ChannelRenewDataEntity> result1 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result2 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_30day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_60day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_90day = new HashMap<String, ChannelRenewDataEntity>();

		Map<String, ChannelRenewDataEntity> result4_day30 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result4_day60 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result4_day90 = new HashMap<String, ChannelRenewDataEntity>();

		Thread t1 = new Thread(new ChannelRenewDataQueryThread(service, params1, channelListMap, result1, 1));
		Thread t2 = new Thread(new ChannelRenewDataQueryThread(service, params2, channelListMap, result2, 2));

		Thread t3_30day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_30day, channelListMap, result3_30day, 3));
		Thread t3_60day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_60day, channelListMap, result3_60day, 3));
		Thread t3_90day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_90day, channelListMap, result3_90day, 3));

		Thread t4_day30 = new Thread(new ChannelRenewDataQueryThread(service, params4_day30, null, result4_day30, 4));
		Thread t4_day60 = new Thread(new ChannelRenewDataQueryThread(service, params4_day60, null, result4_day60, 5));
		Thread t4_day90 = new Thread(new ChannelRenewDataQueryThread(service, params4_day90, null, result4_day90, 6));

		t1.start();
		t2.start();
		t3_30day.start();
		t3_60day.start();
		t3_90day.start();
		t4_day30.start();
		t4_day60.start();
		t4_day90.start();

		try {
			t1.join();
			t2.join();
			t3_30day.join();
			t3_60day.join();
			t3_90day.join();
			t4_day30.join();
			t4_day60.join();
			t4_day90.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			resultMap.put("result1", result1);
			resultMap.put("result2", result2);
			resultMap.put("result3_30day", result3_30day);
			resultMap.put("result3_60day", result3_60day);
			resultMap.put("result3_90day", result3_90day);
			resultMap.put("result4_day30", result4_day30);
			resultMap.put("result4_day60", result4_day60);
			resultMap.put("result4_day90", result4_day90);

		}
	}

	private Map<String, String> getChannelLabelKeyMap(List<DimChannelEntity> channelList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelName());
		}
		return dataMap;
	}

	private List<String> getChannelLabelsByName(List<DimChannelEntity> channelList, List<String> nameList) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			for (int j = 0; j < nameList.size(); j++) {
				if (dimChannelEntity.getChannelNameBack().equals(nameList.get(j))) {
					if (!list.contains(dimChannelEntity.getChannelLabel())) {
						list.add(dimChannelEntity.getChannelLabel());
					}
				}
			}

		}
		return list;
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("channelName", "渠道名称");
		headMap.put("channelLabel", "渠道标签");
		headMap.put("channelType", "渠道分类");

		headMap.put("day30Cost", "30天费用");
		headMap.put("day60Cost", "60天费用");
		headMap.put("day90Cost", "90天费用");

		headMap.put("onlineTime", "上线时间");

		headMap.put("day30YearAmount", "30日年化投资金额");
		headMap.put("day60YearAmount", "60日年化投资金额");
		headMap.put("day90YearAmount", "90日年化投资金额");

		headMap.put("day30YearRoi", "30日年化ROI");
		headMap.put("day60YearRoi", "60日年化ROI");
		headMap.put("day90YearRoi", "90日年化ROI");

		headMap.put("day30FirstInvestUserNum", "30日首投用户数");
		headMap.put("day60FirstInvestUserNum", "60日首投用户数");
		headMap.put("day90FirstInvestUserNum", "90日首投用户数");

		headMap.put("day30MultiInvestUserNum", "30日复投用户数");
		headMap.put("day60MultiInvestUserNum", "60日复投用户数");
		headMap.put("day90MultiInvestUserNum", "90日复投用户数");

		headMap.put("day30FirstInvestAmount", "30日首投金额");
		headMap.put("day60FirstInvestAmount", "60日首投金额");
		headMap.put("day90FirstInvestAmount", "90日首投金额");

		headMap.put("day30MultiInvestAmount", "30日复投金额");
		headMap.put("day60MultiInvestAmount", "60日复投金额");
		headMap.put("day90MultiInvestAmount", "90日复投金额");

		headMap.put("day30MultiRateText", "30日复投率");
		headMap.put("day60MultiRateText", "60日复投率");
		headMap.put("day90MultiRateText", "90日复投率");

		headMap.put("day30MultiInvestAmountRateText", "30日复投金额比");
		headMap.put("day60MultiInvestAmountRateText", "60日复投金额比");
		headMap.put("day90MultiInvestAmountRateText", "90日复投金额比");

		headMap.put("day30FirstInvestYearAmount", "30日首投年化金额");
		headMap.put("day60FirstInvestYearAmount", "60日首投年化金额");
		headMap.put("day90FirstInvestYearAmount", "90日首投年化金额");

		headMap.put("day30PerFirstInvestYearAmount", "30日人均首投年化金额");
		headMap.put("day60PerFirstInvestYearAmount", "60日人均首投年化金额");
		headMap.put("day90PerFirstInvestYearAmount", "90日人均首投年化金额");

		headMap.put("day30FirstInvestYearRoi", "30日首投年化ROI");
		headMap.put("day60FirstInvestYearRoi", "60日首投年化ROI");
		headMap.put("day90FirstInvestYearRoi", "90日首投年化ROI");
		return headMap;
	}
}
