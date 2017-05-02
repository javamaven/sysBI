package io.renren.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;

import io.renren.controller.querythread.ChannelInvestTimesQueryThread;
import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelInvestTimesService;
import io.renren.service.DimChannelService;
import io.renren.util.NumberUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/invest")
public class ChannelInvestTimesController extends AbstractController {
	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	private ChannelInvestTimesService service;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 查询渠道首投复投信息
	 */
	@RequestMapping("/queryChannelInvestTimesList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelInvestTimesList(@RequestBody Map<String, Object> params) {
		long startTime = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
		Map<String, String> channelDataMap = getChannelLabelKeyMap(channelList);
		System.err.println("+++++查询条件： " + params);
		try {
			params.put("regBeginDate", sdf1.format(dateSdf.parse(params.get("regBeginDate") + "")) + " 00:00:00");
			params.put("regEndDate", sdf1.format(dateSdf.parse(params.get("regEndDate") + "")) + " 23:59:59");
			if (StringUtils.isEmpty(params.get("invEndDate") + "")) {
				params.put("invEndDate", dateTimeSdf.format(new Date()));
			} else {
				params.put("invEndDate", sdf1.format(dateSdf.parse(params.get("invEndDate") + "")) + " 23:59:59");
			}
			Object object = params.get("channelName");
			if (object != null) {
				List<String> list = JSON.parseArray(object + "", String.class);
				List<String> nameList = getChannelLabelsByName(channelList, list);
				params.put("channelLabelList", nameList);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.err.println("+++++查询条件： " + params);
		// 查询列表数据
		Query query = new Query(params);
		// 查询数据
		Map<String, ChannelInvestTimesEntity> result1 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result2 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result3 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result4 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result5 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result6 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result7 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result8 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, ChannelInvestTimesEntity> result9 = new HashMap<String, ChannelInvestTimesEntity>();
		Map<String, Object> channelListMap = new ConcurrentHashMap<String, Object>();
		Thread t1 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result1, 1));
		Thread t2 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result2, 2));
		Thread t3 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result3, 3));
		Thread t4 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result4, 4));
		Thread t5 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result5, 5));
		Thread t6 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result6, 6));
		Thread t7 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result7, 7));
		Thread t8 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result8, 8));
		Thread t9 = new Thread(new ChannelInvestTimesQueryThread(service, params, channelListMap, result9, 9));
		try {
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t5.start();
			t6.start();
			t7.start();
			t8.start();
			t9.start();
			long l1 = System.currentTimeMillis();
			t1.join();
			long l2 = System.currentTimeMillis();
			t2.join();
			long l3 = System.currentTimeMillis();
			t3.join();
			long l4 = System.currentTimeMillis();
			t4.join();
			long l5 = System.currentTimeMillis();
			t5.join();
			long l6 = System.currentTimeMillis();
			t6.join();
			long l7 = System.currentTimeMillis();
			t7.join();
			long l8 = System.currentTimeMillis();
			t8.join();
			long l9 = System.currentTimeMillis();
			t9.join();
			long l10 = System.currentTimeMillis();
			System.err.println("+++++++++++++耗时1：" + (l2 - l1));
			System.err.println("+++++++++++++耗时2：" + (l3 - l2));
			System.err.println("+++++++++++++耗时3：" + (l4 - l3));
			System.err.println("+++++++++++++耗时4：" + (l5 - l4));
			System.err.println("+++++++++++++耗时5：" + (l6 - l5));
			System.err.println("+++++++++++++耗时6：" + (l7 - l6));
			System.err.println("+++++++++++++耗时7：" + (l8 - l7));
			System.err.println("+++++++++++++耗时8：" + (l9 - l8));
			System.err.println("+++++++++++++耗时9：" + (l10 - l9));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<ChannelInvestTimesEntity> list = unionChannelInvestTimesData(channelDataMap, channelListMap, result1,
				result2, result3, result4, result5, result6, result7, result8, result9);// 将数据按照渠道聚合
		// 获取数据条数
		PageUtils pageUtil = new PageUtils(list, list.size(), query.getLimit(), query.getPage());

		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtil);
	}

	private List<ChannelInvestTimesEntity> unionChannelInvestTimesData(Map<String, String> channelDataMap,
			Map<String, Object> channelListMap, Map<String, ChannelInvestTimesEntity> result1,
			Map<String, ChannelInvestTimesEntity> result2, Map<String, ChannelInvestTimesEntity> result3,
			Map<String, ChannelInvestTimesEntity> result4, Map<String, ChannelInvestTimesEntity> result5,
			Map<String, ChannelInvestTimesEntity> result6, Map<String, ChannelInvestTimesEntity> result7,
			Map<String, ChannelInvestTimesEntity> result8, Map<String, ChannelInvestTimesEntity> result9) {
		List<ChannelInvestTimesEntity> list = new ArrayList<ChannelInvestTimesEntity>();

		Iterator<String> iterator = channelListMap.keySet().iterator();
		ChannelInvestTimesEntity vo = null;
		while (iterator.hasNext()) {
			String key = iterator.next();
			vo = new ChannelInvestTimesEntity();
			if (channelDataMap.containsKey(key)) {
				vo.setChannelName(channelDataMap.get(key));// 渠道名称
			} else {
				vo.setChannelName("未知");// 渠道名称
			}
			vo.setChannelLabel(key);// 渠道标签
			if (result1.containsKey(key)) {
				ChannelInvestTimesEntity en = result1.get(key);
				vo.setRegisterUserNum(en.getRegisterUserNum());
			}
			if (result2.containsKey(key)) {
				ChannelInvestTimesEntity en = result2.get(key);
				vo.setFirstInvestUserNum(en.getFirstInvestUserNum());
				vo.setFirstInvestAmount(NumberUtil.keepPrecision(en.getFirstInvestAmount(), 2));
			}
			if (result3.containsKey(key)) {
				ChannelInvestTimesEntity en = result3.get(key);
				vo.setInvestTimes(en.getInvestTimes());
			}
			if (result4.containsKey(key)) {
				ChannelInvestTimesEntity en = result4.get(key);
				vo.setInvestUserNum(en.getInvestUserNum());
			}
			if (result5.containsKey(key)) {
				ChannelInvestTimesEntity en = result5.get(key);
				vo.setInvestAmount(NumberUtil.keepPrecision(en.getInvestAmount(), 2));
			}
			if (result6.containsKey(key)) {
				ChannelInvestTimesEntity en = result6.get(key);
				vo.setInvestYearAmount(NumberUtil.keepPrecision(en.getInvestYearAmount(), 2));
			}
			if (result7.containsKey(key)) {
				ChannelInvestTimesEntity en = result7.get(key);
				vo.setFirstInvestRedMoney(NumberUtil.keepPrecision(en.getFirstInvestRedMoney(), 2));
			}
			// 人均首投使用红包金额=首投使用红包金额/首投人数
			if (vo.getFirstInvestUserNum() != 0) {
				vo.setPerFirstInvestRedMoney(
						NumberUtil.keepPrecision((double) vo.getFirstInvestRedMoney() / vo.getFirstInvestUserNum(), 2));
			}
			if (result8.containsKey(key)) {
				ChannelInvestTimesEntity en = result8.get(key);
				vo.setAllRedMoney(NumberUtil.keepPrecision(en.getAllRedMoney(), 2));
			}

			// 点点赚投资天数 未知如何计算 ，点点赚平均投资金额
			if (result9.containsKey(key)) {
				ChannelInvestTimesEntity en = result9.get(key);
				vo.setDdzInvestDays(en.getDdzInvestDays());
				vo.setDdzPerInvestAmount(NumberUtil.keepPrecision(en.getDdzPerInvestAmount(), 2));
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
