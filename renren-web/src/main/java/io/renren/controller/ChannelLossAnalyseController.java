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

import io.renren.controller.querythread.ChannelLossQueryThread;
import io.renren.entity.ChannelLossEntity;
import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelLossService;
import io.renren.service.DimChannelService;
import io.renren.util.NumberUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/loss")
public class ChannelLossAnalyseController extends AbstractController {
	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	private ChannelLossService service;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 查询渠道流失分析列表
	 */
	@RequestMapping("/queryChannelLossList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelInvestTimesList(@RequestBody Map<String, Object> params) {
		long startTime = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryList(null);
		Map<String, String> channelDataMap = getChannelLabelKeyMap(channelList);
		System.err.println("+++++查询条件： " + params);

		try {
			params.put("firstInvBeginDate", sdf1.format(dateSdf.parse(params.get("firstInvBeginDate") + "")) + " 00:00:00");
			params.put("firstInvEndDate", sdf1.format(dateSdf.parse(params.get("firstInvEndDate") + "")) + " 23:59:59");
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
		Map<String, Object> invest1TimeMap = new HashMap<String, Object>();
		invest1TimeMap.putAll(params);
		invest1TimeMap.put("investTime", 1);
		Map<String, Object> invest2TimeMap = new HashMap<String, Object>();
		invest2TimeMap.putAll(params);
		invest2TimeMap.put("investTime", 2);
		Map<String, Object> invest3TimeMap = new HashMap<String, Object>();
		invest3TimeMap.putAll(params);
		invest3TimeMap.put("investTime", 3);
		Map<String, Object> invest4TimeMap = new HashMap<String, Object>();
		invest4TimeMap.putAll(params);
		invest4TimeMap.put("investTime", 4);
		Map<String, Object> invest5TimeMap = new HashMap<String, Object>();
		invest5TimeMap.putAll(params);
		invest5TimeMap.put("investTime", 5);
		System.err.println("+++++查询条件： " + params);
		// 查询列表数据
		Query query = new Query(params);
		// 查询数据
		Map<String, ChannelLossEntity> result1 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result2 = new HashMap<String, ChannelLossEntity>();

		Map<String, ChannelLossEntity> result31 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result32 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result33 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result34 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result35 = new HashMap<String, ChannelLossEntity>();

		Map<String, ChannelLossEntity> result4 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result5 = new HashMap<String, ChannelLossEntity>();
//		Map<String, ChannelLossEntity> result6 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result7 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result8 = new HashMap<String, ChannelLossEntity>();
		Map<String, ChannelLossEntity> result9 = new HashMap<String, ChannelLossEntity>();
		Map<String, Object> channelListMap = new ConcurrentHashMap<String, Object>();
		Thread t1 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result1, 1));
		Thread t2 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result2, 2));

		Thread t31 = new Thread(new ChannelLossQueryThread(service, invest1TimeMap, invest1TimeMap, result31, 3));
		Thread t32 = new Thread(new ChannelLossQueryThread(service, invest2TimeMap, invest2TimeMap, result32, 3));
		Thread t33 = new Thread(new ChannelLossQueryThread(service, invest3TimeMap, invest3TimeMap, result33, 3));
		Thread t34 = new Thread(new ChannelLossQueryThread(service, invest4TimeMap, invest4TimeMap, result34, 3));
		Thread t35 = new Thread(new ChannelLossQueryThread(service, invest5TimeMap, invest5TimeMap, result35, 3));

		Thread t4 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result4, 4));
		Thread t5 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result5, 5));
//		Thread t6 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result6, 6));
		Thread t7 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result7, 7));
		Thread t8 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result8, 8));
		Thread t9 = new Thread(new ChannelLossQueryThread(service, params, channelListMap, result9, 9));
		try {
			t1.start();
			t2.start();
			t31.start();
			t32.start();
			t33.start();
			t34.start();
			t35.start();
			t4.start();
			t5.start();
//			t6.start();
			t7.start();
			t8.start();
			t9.start();
			long l1 = System.currentTimeMillis();
			t1.join();
			long l2 = System.currentTimeMillis();
			t2.join();
			long l3 = System.currentTimeMillis();
			t31.join();
			t32.join();
			t33.join();
			t34.join();
			t35.join();
			long l4 = System.currentTimeMillis();
			t4.join();
			long l5 = System.currentTimeMillis();
			t5.join();
			long l6 = System.currentTimeMillis();
//			t6.join();
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
		List<ChannelLossEntity> list = unionChannelLossData(channelDataMap, channelListMap, result1, result2, result31,
				result32, result33, result34, result35, result4, result5, null, result7, result8, result9);// 将数据按照渠道聚合
		// 获取数据条数
		PageUtils pageUtil = new PageUtils(list, list.size(), query.getLimit(), query.getPage());

		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtil);
	}

	private List<ChannelLossEntity> unionChannelLossData(Map<String, String> channelDataMap,
			Map<String, Object> channelListMap, Map<String, ChannelLossEntity> result1,
			Map<String, ChannelLossEntity> result2, Map<String, ChannelLossEntity> result31,
			Map<String, ChannelLossEntity> result32, Map<String, ChannelLossEntity> result33,
			Map<String, ChannelLossEntity> result34, Map<String, ChannelLossEntity> result35,
			Map<String, ChannelLossEntity> result4, Map<String, ChannelLossEntity> result5,
			Map<String, ChannelLossEntity> result6, Map<String, ChannelLossEntity> result7,
			Map<String, ChannelLossEntity> result8, Map<String, ChannelLossEntity> result9) {
		List<ChannelLossEntity> list = new ArrayList<ChannelLossEntity>();

		Iterator<String> iterator = channelListMap.keySet().iterator();
		ChannelLossEntity vo = null;
		while (iterator.hasNext()) {
			String key = iterator.next();
			vo = new ChannelLossEntity();
			if (channelDataMap.containsKey(key)) {
				vo.setChannelName(channelDataMap.get(key));// 渠道名称
			} else {
				vo.setChannelName("未知");// 渠道名称
			}
			vo.setChannelLabel(key);// 渠道标签

			if (result1.containsKey(key)) {
				ChannelLossEntity en = result1.get(key);
				vo.setRegisterUserNum(en.getRegisterUserNum());// 1:注册人数
			}
			if (result2.containsKey(key)) {
				ChannelLossEntity en = result2.get(key);
				vo.setFirstInvestUserNum(en.getFirstInvestUserNum());// 2：首投人数
				vo.setFirstInvestAmount(NumberUtil.keepPrecision(en.getFirstInvestAmount(), 2));
			}
			if (result31.containsKey(key)) {
				ChannelLossEntity en = result31.get(key);
				vo.setInvestOneTimeUserNum(en.getInvestUserNum()); // 投资1次人数
			}
			if (result32.containsKey(key)) {
				ChannelLossEntity en = result32.get(key);
				vo.setInvestTwoTimeUserNum(en.getInvestUserNum());// 投资2次人数
			}
			if (result33.containsKey(key)) {
				ChannelLossEntity en = result33.get(key);
				vo.setInvestThreeTimeUserNum(en.getInvestUserNum());// 投资3次人数
			}
			if (result34.containsKey(key)) {
				ChannelLossEntity en = result34.get(key);
				vo.setInvestFourTimeUserNum(en.getInvestUserNum());// 投资4次人数
			}
			if (result35.containsKey(key)) {
				ChannelLossEntity en = result35.get(key);
				vo.setInvestNTimeUserNum(en.getInvestUserNum());// 投资n次人数
			}

			// 4：首次投资金额
			if (result4.containsKey(key)) {
				ChannelLossEntity en = result4.get(key);
				vo.setFirstInvestAmount(NumberUtil.keepPrecision(en.getFirstInvestAmount(), 2));
			}
			// 5：累积投资金额,累计投资年化金额
			if (result5.containsKey(key)) {
				ChannelLossEntity en = result5.get(key);
				vo.setInvestAmount(NumberUtil.keepPrecision(en.getInvestAmount(), 2));
				vo.setInvestYearAmount(NumberUtil.keepPrecision(en.getInvestYearAmount(), 2));
			}
			// 6：累计投资年化金额
//			if (result6.containsKey(key)) {
//				ChannelLossEntity en = result6.get(key);
//				vo.setInvestYearAmount(NumberUtil.keepPrecision(en.getInvestYearAmount(), 2));
//			}
			// 7：首投使用红包金额
			if (result7.containsKey(key)) {
				ChannelLossEntity en = result7.get(key);
				vo.setFirstInvestUseRedMoney(NumberUtil.keepPrecision(en.getFirstInvestUseRedMoney(), 2));
			}
			// 人均首投使用红包金额=首投使用红包金额/首投人数
			if (vo.getFirstInvestUserNum() != 0) {
				vo.setPerFirstInvestUseRedMoney(NumberUtil
						.keepPrecision((double) vo.getFirstInvestUseRedMoney() / vo.getFirstInvestUserNum(), 2));
			}
			// 8：累积红包金额
			if (result8.containsKey(key)) {
				ChannelLossEntity en = result8.get(key);
				vo.setTotalUseRedMoney(NumberUtil.keepPrecision(en.getTotalUseRedMoney(), 2));
				vo.setUseRedMoneyUserNum(en.getUseRedMoneyUserNum());
			}
			// 人均累计使用红包金额
			if (vo.getUseRedMoneyUserNum() != 0) {
				vo.setPerTotalUseRedMoney(
						NumberUtil.keepPrecision((double) vo.getTotalUseRedMoney() / vo.getUseRedMoneyUserNum(), 2));
			}
			//9.点点赚投资天数 , 点点赚平均投资金额
			if (result9.containsKey(key)) {
				ChannelLossEntity en = result9.get(key);
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
