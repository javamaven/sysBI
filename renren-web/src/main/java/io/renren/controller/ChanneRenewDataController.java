package io.renren.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.renren.controller.querythread.ChannelRenewDataQueryThread;
import io.renren.entity.ChannelRenewDataEntity;
import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelRenewDataService;
import io.renren.service.DimChannelService;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/renew")
public class ChanneRenewDataController extends AbstractController {
	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	private ChannelRenewDataService service;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat datesdf2 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 查询渠道流失分析列表
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("/queryChannelRenewDataList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelRenewDataList(@RequestBody Map<String, Object> params) throws ParseException {
		long startTime = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryList(null);
		Map<String, String> channelDataMap = getChannelLabelKeyMap(channelList);
		System.err.println("+++++查询条件： " + params);
		params.put("endDate", DateUtil.formatDate(params.get("date") + ""));
		Object object = params.get("channelName");
		if (object != null) {
			List<String> list = JSON.parseArray(object + "", String.class);
			params.put("channelLabelList", getChannelLabelsByName(channelList, list));
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
		// 获取数据条数
		PageUtils pageUtil = new PageUtils(list, list.size(), query.getLimit(), query.getPage());

		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 根据参数: 1.创建各个指标的查询参数 2.创建返回结果map
	 * 
	 * @param params
	 * @param resultMap
	 * @return
	 * @throws ParseException
	 */
	private void buildQueryParamsAndQueryData(Map<String, Object> params, Map<String, Object> channelListMap,
			Map<String, Map<String, ChannelRenewDataEntity>> resultMap) throws ParseException {
		String endDate = params.get("endDate") + "";
		List<String> channelLabelList = (List<String>) params.get("channelLabelList");
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("endDate", endDate);
		params1.put("channelLabelList", channelLabelList);
		params1.put("day30", DateUtil.getCurrDayBefore(endDate, 30));
		params1.put("day60", DateUtil.getCurrDayBefore(endDate, 60));
		params1.put("day90", DateUtil.getCurrDayBefore(endDate, 90));

		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("channelLabelList", channelLabelList);

		Map<String, Object> params3_30day = new HashMap<String, Object>();
		params3_30day.put("startDate",
				datesdf2.format(dateSdf.parse(DateUtil.getCurrDayBefore(endDate, 30))) + " 00:00:00");
		params3_30day.put("endDate", datesdf2.format(dateSdf.parse(endDate)) + " 23:59:59");
		params3_30day.put("channelLabelList", channelLabelList);

		Map<String, Object> params3_60day = new HashMap<String, Object>();
		params3_60day.put("startDate",
				datesdf2.format(dateSdf.parse(DateUtil.getCurrDayBefore(endDate, 60))) + " 00:00:00");
		params3_60day.put("endDate", datesdf2.format(dateSdf.parse(endDate)) + " 23:59:59");
		params3_60day.put("channelLabelList", channelLabelList);

		Map<String, Object> params3_90day = new HashMap<String, Object>();
		params3_90day.put("startDate",
				datesdf2.format(dateSdf.parse(DateUtil.getCurrDayBefore(endDate, 90))) + " 00:00:00");
		params3_90day.put("endDate", datesdf2.format(dateSdf.parse(endDate)) + " 23:59:59");
		params3_90day.put("channelLabelList", channelLabelList);

		Map<String, Object> params4 = new HashMap<String, Object>();
		params4.putAll(params1);

		Map<String, Object> params5_30day_first = new HashMap<String, Object>();
		params5_30day_first.putAll(params3_30day);
		params5_30day_first.put("rownum", 1);// 首投

		Map<String, Object> params5_60day_first = new HashMap<String, Object>();
		params5_60day_first.putAll(params3_60day);
		params5_60day_first.put("rownum", 1);// 首投

		Map<String, Object> params5_90day_first = new HashMap<String, Object>();
		params5_90day_first.putAll(params3_90day);
		params5_90day_first.put("rownum", 1);// 首投

		Map<String, Object> params5_30day_second = new HashMap<String, Object>();
		params5_30day_second.putAll(params3_30day);
		params5_30day_second.put("rownum", 2);// 复投

		Map<String, Object> params5_60day_second = new HashMap<String, Object>();
		params5_60day_second.putAll(params3_60day);
		params5_60day_second.put("rownum", 2);// 复投

		Map<String, Object> params5_90day_second = new HashMap<String, Object>();
		params5_90day_second.putAll(params3_90day);
		params5_90day_second.put("rownum", 2);// 复投

		Map<String, Object> params6_30day_first = new HashMap<String, Object>();
		params6_30day_first.putAll(params3_30day);
		params6_30day_first.put("rownum", 1);// 首投

		Map<String, Object> params6_60day_first = new HashMap<String, Object>();
		params6_60day_first.putAll(params3_60day);
		params6_60day_first.put("rownum", 1);// 首投

		Map<String, Object> params6_90day_first = new HashMap<String, Object>();
		params6_90day_first.putAll(params3_90day);
		params6_90day_first.put("rownum", 1);// 首投

		Map<String, ChannelRenewDataEntity> result1 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result2 = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_30day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_60day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result3_90day = new HashMap<String, ChannelRenewDataEntity>();

		Map<String, ChannelRenewDataEntity> result4 = new HashMap<String, ChannelRenewDataEntity>();

		Map<String, ChannelRenewDataEntity> result5_30day_first = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result5_60day_first = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result5_90day_first = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result5_30day_second = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result5_60day_second = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result5_90day_second = new HashMap<String, ChannelRenewDataEntity>();

		Map<String, ChannelRenewDataEntity> result6_30day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result6_60day = new HashMap<String, ChannelRenewDataEntity>();
		Map<String, ChannelRenewDataEntity> result6_90day = new HashMap<String, ChannelRenewDataEntity>();

		Thread t1 = new Thread(new ChannelRenewDataQueryThread(service, params1, channelListMap, result1, 1));
		Thread t2 = new Thread(new ChannelRenewDataQueryThread(service, params2, channelListMap, result2, 2));

		Thread t3_30day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_30day, channelListMap, result3_30day, 3));
		Thread t3_60day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_60day, channelListMap, result3_60day, 3));
		Thread t3_90day = new Thread(
				new ChannelRenewDataQueryThread(service, params3_90day, channelListMap, result3_90day, 3));

		Thread t4 = new Thread(new ChannelRenewDataQueryThread(service, params4, null, result4, 4));

		Thread t5_30day_first = new Thread(
				new ChannelRenewDataQueryThread(service, params5_30day_first, channelListMap, result5_30day_first, 5));
		Thread t5_60day_first = new Thread(
				new ChannelRenewDataQueryThread(service, params5_60day_first, channelListMap, result5_60day_first, 5));
		Thread t5_90day_first = new Thread(
				new ChannelRenewDataQueryThread(service, params5_90day_first, channelListMap, result5_90day_first, 5));
		Thread t5_30day_second = new Thread(new ChannelRenewDataQueryThread(service, params5_30day_second,
				channelListMap, result5_30day_second, 5));
		Thread t5_60day_second = new Thread(new ChannelRenewDataQueryThread(service, params5_60day_second,
				channelListMap, result5_60day_second, 5));
		Thread t5_90day_second = new Thread(new ChannelRenewDataQueryThread(service, params5_90day_second,
				channelListMap, result5_90day_second, 5));

		Thread t6_30day = new Thread(
				new ChannelRenewDataQueryThread(service, params6_30day_first, channelListMap, result6_30day, 6));
		Thread t6_60day = new Thread(
				new ChannelRenewDataQueryThread(service, params6_60day_first, channelListMap, result6_60day, 6));
		Thread t6_90day = new Thread(
				new ChannelRenewDataQueryThread(service, params6_90day_first, channelListMap, result6_90day, 6));
		t1.start();
		t2.start();
		t3_30day.start();
		t3_60day.start();
		t3_90day.start();
		t4.start();
		t5_30day_first.start();
		t5_60day_first.start();
		t5_90day_first.start();
		t5_30day_second.start();
		t5_60day_second.start();
		t5_90day_second.start();
		t6_30day.start();
		t6_60day.start();
		t6_90day.start();
		try {
			t1.join();
			t2.join();
			t3_30day.join();
			t3_60day.join();
			t3_90day.join();
			t4.join();
			t5_30day_first.join();
			t5_60day_first.join();
			t5_90day_first.join();
			t5_30day_second.join();
			t5_60day_second.join();
			t5_90day_second.join();
			t6_30day.join();
			t6_60day.join();
			t6_90day.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			resultMap.put("result1", result1);
			resultMap.put("result2", result2);
			resultMap.put("result3_30day", result3_30day);
			resultMap.put("result3_60day", result3_60day);
			resultMap.put("result3_90day", result3_90day);
			resultMap.put("result4", result4);
			resultMap.put("result5_30day_first", result5_30day_first);
			resultMap.put("result5_60day_first", result5_60day_first);
			resultMap.put("result5_90day_first", result5_90day_first);
			resultMap.put("result5_30day_second", result5_30day_second);
			resultMap.put("result5_60day_second", result5_60day_second);
			resultMap.put("result5_90day_second", result5_90day_second);
			resultMap.put("result6_30day", result6_30day);
			resultMap.put("result6_60day", result6_60day);
			resultMap.put("result6_90day", result6_90day);
		}
	}

	/**
	 * '2017-04-25 23:59:59'
	 * 
	 * @param currDayBefore
	 * @return
	 */
	private String formate(String date) {
		// TODO Auto-generated method stub
		try {
			return datesdf2.format(dateSdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<ChannelRenewDataEntity> unionData(Map<String, String> channelDataMap,
			Map<String, Object> channelListMap, Map<String, Map<String, ChannelRenewDataEntity>> resultMap) {
		List<ChannelRenewDataEntity> list = new ArrayList<ChannelRenewDataEntity>();

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
			}
			// 60日年化投资金额
			Map<String, ChannelRenewDataEntity> map3_60day = resultMap.get("result3_60day");
			if (map3_60day.containsKey(key)) {
				ChannelRenewDataEntity en = map3_60day.get(key);
				vo.setDay60YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
			}
			// 90日年化投资金额
			Map<String, ChannelRenewDataEntity> map3_90day = resultMap.get("result3_90day");
			if (map3_90day.containsKey(key)) {
				ChannelRenewDataEntity en = map3_90day.get(key);
				vo.setDay90YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
			}
			

			// 30 60 90年化ROI
			Map<String, ChannelRenewDataEntity> result4 = resultMap.get("result4");
			if (result4.containsKey(key)) {
				ChannelRenewDataEntity en = result4.get(key);
				vo.setDay30YearRoi(NumberUtil.keepPrecision(en.getDay30YearRoi(), 2));
				vo.setDay60YearRoi(NumberUtil.keepPrecision(en.getDay60YearRoi(), 2));
				vo.setDay90YearRoi(NumberUtil.keepPrecision(en.getDay90YearRoi(), 2));
			}
			// 30首投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_30day_first = resultMap.get("result5_30day_first");
			if (result5_30day_first.containsKey(key)) {
				ChannelRenewDataEntity en = result5_30day_first.get(key);
				vo.setDay30FirstInvestUserNum(en.getFirstInvestUserNum());
				vo.setDay30YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
			}
			// 60首投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_60day_first = resultMap.get("result5_60day_first");
			if (result5_60day_first.containsKey(key)) {
				ChannelRenewDataEntity en = result5_60day_first.get(key);
				vo.setDay60FirstInvestUserNum(en.getFirstInvestUserNum());
				vo.setDay60YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
			}
			// 90首投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_90day_first = resultMap.get("result5_90day_first");
			if (result5_90day_first.containsKey(key)) {
				ChannelRenewDataEntity en = result5_90day_first.get(key);
				vo.setDay90FirstInvestUserNum(en.getFirstInvestUserNum());
				vo.setDay90YearAmount(NumberUtil.keepPrecision(en.getYearAmount(), 2));
			}

			// 30复投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_30day_second = resultMap.get("result5_30day_second");
			if (result5_30day_second.containsKey(key)) {
				ChannelRenewDataEntity en = result5_30day_second.get(key);
				vo.setDay30MultiInvestUserNum(NumberUtil.keepPrecision(en.getFirstInvestUserNum(), 2));
			}
			// 60复投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_60day_second = resultMap.get("result5_60day_second");
			if (result5_60day_second.containsKey(key)) {
				ChannelRenewDataEntity en = result5_60day_second.get(key);
				vo.setDay60MultiInvestUserNum(NumberUtil.keepPrecision(en.getFirstInvestUserNum(), 2));
			}
			// 90复投用户，年化投资金额
			Map<String, ChannelRenewDataEntity> result5_90day_second = resultMap.get("result5_90day_second");
			if (result5_90day_second.containsKey(key)) {
				ChannelRenewDataEntity en = result5_90day_second.get(key);
				vo.setDay90MultiInvestUserNum(NumberUtil.keepPrecision(en.getFirstInvestUserNum(), 2));
			}

			// 30日年化ROI
			Map<String, ChannelRenewDataEntity> result6_30day = resultMap.get("result6_30day");
			if (result6_30day.containsKey(key)) {
				ChannelRenewDataEntity en = result6_30day.get(key);
				vo.setDay30YearRoi(NumberUtil.keepPrecision(en.getFirstInvestYearRoi(), 2));
			}
			// 60日年化ROI
			Map<String, ChannelRenewDataEntity> result6_60day = resultMap.get("result6_60day");
			if (result6_60day.containsKey(key)) {
				ChannelRenewDataEntity en = result6_60day.get(key);
				vo.setDay60YearRoi(NumberUtil.keepPrecision(en.getFirstInvestYearRoi(), 2));
			}
			// 90日年化ROI
			Map<String, ChannelRenewDataEntity> result6_90day = resultMap.get("result6_90day");
			if (result6_90day.containsKey(key)) {
				ChannelRenewDataEntity en = result6_90day.get(key);
				vo.setDay90YearRoi(NumberUtil.keepPrecision(en.getFirstInvestYearRoi(), 2));
			}

			list.add(vo);
		}
		return list;
	}

	private Map<String, String> getChannelLabelKeyMap(List<DimChannelEntity> channelList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			dataMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelNameBack());
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

}
