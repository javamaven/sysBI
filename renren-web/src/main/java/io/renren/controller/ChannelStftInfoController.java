package io.renren.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.controller.querythread.ChannelStFtInfoQueryThread;
import io.renren.entity.ChannelStftInfoEntity;
import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelStftInfoService;
import io.renren.service.DimChannelService;
import io.renren.util.NumberUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/stft")
public class ChannelStftInfoController extends AbstractController {

	@Autowired
	private ChannelStftInfoService service;

	@Autowired
	private DimChannelService dimChannelService;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	/**
	 * 查询渠道首投复投信息
	 */
	@RequestMapping("/stftInfo")
	@RequiresPermissions("channel:channelAll:list")
	public R stftInfo(@RequestBody Map<String, Object> params) {
		long startTime = System.currentTimeMillis();
		List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
		Map<String, String> channelDataMap = getChannelLabelKeyMap(channelList);
		System.err.println("+++++查询条件： " + params);
		List<String> channelLabelList = new ArrayList<String>();
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
				channelLabelList = getChannelLabelsByName(channelList, list);
				params.put("channelLabelList", channelLabelList);
			} else {
				channelLabelList = new ArrayList<String>();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.err.println("+++++查询条件： " + params);
		// 查询列表数据
		Query query = new Query(params);
		// 查询数据
		Map<String, ChannelStftInfoEntity> result1 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result2 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result3 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result4 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result5 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result6 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result7 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result8 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result9 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result10 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, ChannelStftInfoEntity> result11 = new HashMap<String, ChannelStftInfoEntity>();
		Map<String, Object> channelListMap = new ConcurrentHashMap<String, Object>();
		Thread t1 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result1, 1));
		Thread t2 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result2, 2));
		Thread t3 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result3, 3));
		Thread t4 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result4, 4));
		Thread t5 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result5, 5));
		Thread t6 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result6, 6));// queryFirstInvestUserAmount
		Thread t7 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result7, 7));
		Thread t8 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result8, 8));
		Thread t9 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result9, 9));
		Thread t10 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result10, 10));
		Thread t11 = new Thread(new ChannelStFtInfoQueryThread(service, params, channelListMap, result11, 11));// 用户投资人数
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		t11.start();
		try {
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
			t10.join();
			long l11 = System.currentTimeMillis();
			t11.join();
			long l12 = System.currentTimeMillis();
			System.err.println("+++++++++++++耗时1：" + (l2 - l1));
			System.err.println("+++++++++++++耗时2：" + (l3 - l2));
			System.err.println("+++++++++++++耗时3：" + (l4 - l3));
			System.err.println("+++++++++++++耗时4：" + (l5 - l4));
			System.err.println("+++++++++++++耗时5：" + (l6 - l5));
			System.err.println("+++++++++++++耗时6：" + (l7 - l6));
			System.err.println("+++++++++++++耗时7：" + (l8 - l7));
			System.err.println("+++++++++++++耗时8：" + (l9 - l8));
			System.err.println("+++++++++++++耗时9：" + (l10 - l9));
			System.err.println("+++++++++++++耗时10：" + (l11 - l10));
			System.err.println("+++++++++++++耗时11：" + (l12 - l11));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<ChannelStftInfoEntity> list = unionChannelStftInfoData(channelDataMap, channelListMap, result1, result2,
				result3, result4, result5, result6, result7, result8, result9, result10, result11);// 将数据按照渠道聚合

		// 获取数据条数
		// 过滤channelLabel
		List<ChannelStftInfoEntity> retList = new ArrayList<ChannelStftInfoEntity>();
		if (channelLabelList.size() == 0) {
			retList.addAll(list);
		} else {
			for (int i = 0; i < list.size(); i++) {
				ChannelStftInfoEntity entity = list.get(i);
				for (int j = 0; j < channelLabelList.size(); j++) {
					String label = channelLabelList.get(j);
					if ((label + "").trim().equals((entity.getChannelLabel() + "").trim())) {
						retList.add(entity);
					}
				}
			}
		}

		PageUtils pageUtil = new PageUtils(retList, retList.size(), query.getLimit(), query.getPage());

		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtil);
	}

	private List<ChannelStftInfoEntity> unionChannelStftInfoData(Map<String, String> channelDataMap,
			Map<String, Object> channelListMap, Map<String, ChannelStftInfoEntity> result1,
			Map<String, ChannelStftInfoEntity> result2, Map<String, ChannelStftInfoEntity> result3,
			Map<String, ChannelStftInfoEntity> result4, Map<String, ChannelStftInfoEntity> result5,
			Map<String, ChannelStftInfoEntity> result6, Map<String, ChannelStftInfoEntity> result7,
			Map<String, ChannelStftInfoEntity> result8, Map<String, ChannelStftInfoEntity> result9,
			Map<String, ChannelStftInfoEntity> result10, Map<String, ChannelStftInfoEntity> result11) {
		// TODO Auto-generated method stub

		List<ChannelStftInfoEntity> list = new ArrayList<ChannelStftInfoEntity>();

		Iterator<String> iterator = channelListMap.keySet().iterator();
		ChannelStftInfoEntity vo = null;

		while (iterator.hasNext()) {
			String key = iterator.next();
			vo = new ChannelStftInfoEntity();
			if (channelDataMap.containsKey(key)) {
				vo.setChannelName(channelDataMap.get(key));// 渠道名称
				if ("经纪人邀请2.0".equals(vo.getChannelName())) {
					vo.setChannelType("经济人邀请"); // 渠道分类
				} else {
					vo.setChannelType("非经济人邀请"); // 渠道分类
				}
			} else {
				vo.setChannelName("未知");// 渠道名称
				vo.setChannelType("未知");
			}
			vo.setChannelLabel(key);// 渠道标签
			// 1.注册人数
			if (result1.containsKey(key)) {
				ChannelStftInfoEntity en = result1.get(key);
				vo.setRegisterUserNum(en.getRegisterUserNum());// 1:注册人数
			}
			// 2.首投人数，首投金额,首投年化投资金额 人均首投
			if (result2.containsKey(key)) {
				ChannelStftInfoEntity en = result2.get(key);
				vo.setFirstInvestUserNum(en.getFirstInvestUserNum());// 2：首投人数
				vo.setFirstInvestAmount(NumberUtil.keepPrecision(en.getFirstInvestAmount(), 2));// 首投金额
				vo.setFirstInvestYearAmount(NumberUtil.keepPrecision(en.getFirstInvestYearAmount(), 2));// 首投年化金额
				vo.setFirstInvestPer(NumberUtil.keepPrecision(en.getFirstInvestPer(), 2));// 人均首投=首投金额/首投人数
			}
			// 用户年华投资金额
			if (result3.containsKey(key)) {
				ChannelStftInfoEntity en = result3.get(key);
				vo.setUserInvestYearAmount(NumberUtil.keepPrecision(en.getUserInvestYearAmount(), 2));
			}
			// // 人均首投=首投金额/首投人数
			// if (vo.getFirstInvestUserNum() == 0) {
			// vo.setFirstInvestPer(0);
			// } else {
			// vo.setFirstInvestPer(
			// NumberUtil.keepPrecision((double) vo.getFirstInvestAmount() /
			// vo.getFirstInvestUserNum(), 2));
			// }
			// 转化率 （公式=“首投人数”/“注册人数”）
			if (vo.getRegisterUserNum() == 0) {
				vo.setConversionRate(0);
				vo.setConversionRateText("0.0%");
			} else {
				vo.setConversionRate((double) vo.getFirstInvestUserNum() / vo.getRegisterUserNum());

				vo.setConversionRateText(NumberUtil.keepPrecision(vo.getConversionRate() * 100, 2) + "%");
			}

			// 10，项目投资金额 ，项目投资人数
			if (result10.containsKey(key)) {
				ChannelStftInfoEntity en = result10.get(key);
				vo.setProInvestAmount(NumberUtil.keepPrecision(en.getProInvestAmount(), 2));// 项目投资金额
				vo.setProInvestUser(en.getProInvestUser());// 项目投资人数
			}
			// 首投用户项目投资金额 ，
			if (result9.containsKey(key)) {
				ChannelStftInfoEntity en = result9.get(key);
				vo.setFirstInvestProAmount(NumberUtil.keepPrecision(en.getFirstInvestProAmount(), 2));
			}
			// 复投人数
			if (result8.containsKey(key)) {
				ChannelStftInfoEntity en = result8.get(key);
				vo.setMultipleUser(en.getMultipleUser());// 复投人数
			}
			// 复投率=复投人数/首投人数
			if (vo.getFirstInvestUserNum() == 0) {
				vo.setMultipleRate(0);
				vo.setMultipleRateText("0.0%");
			} else {
				vo.setMultipleRate((double) vo.getMultipleUser() / vo.getFirstInvestUserNum());
				vo.setMultipleRateText(NumberUtil.keepPrecision(vo.getMultipleRate() * 100, 2) + "%");
			}

			// 4.项目复投金额 ,项目复投人数
			if (result4.containsKey(key)) {
				ChannelStftInfoEntity en = result4.get(key);
				vo.setProMultiInvestAmount(NumberUtil.keepPrecision(en.getProMultiInvestAmount(), 2));
				vo.setProMultiInvestUser(en.getProMultiInvestUser());
			}
			// 5.首投用户投资金额
			if (result5.containsKey(key)) {
				ChannelStftInfoEntity en = result5.get(key);
				vo.setFirstInvestUserAmount(NumberUtil.keepPrecision(en.getFirstInvestUserAmount(), 2));
			}
			// 6.用户投资金额,
			if (result6.containsKey(key)) {
				ChannelStftInfoEntity en = result6.get(key);
				vo.setUserInvestAmount(NumberUtil.keepPrecision(en.getUserInvestAmount(), 2));
			}
			// 11.用户投资人数
			if (result11.containsKey(key)) {
				ChannelStftInfoEntity en = result11.get(key);
				vo.setUserInvestNum(en.getUserInvestNum());
			}
			// 7。首投平均期限
			if (result7.containsKey(key)) {
				ChannelStftInfoEntity en = result7.get(key);
				vo.setFirstInvestPerTime(NumberUtil.keepPrecision(en.getFirstInvestPerTime(), 2));
			}

			// 首投用户平均项目投资金额 （公式=“首投用户项目投资金额”/“首投人数”）
			if (vo.getFirstInvestUserNum() == 0) {
				vo.setFirstInvestUserPerProAmount(0);
			} else {
				vo.setFirstInvestUserPerProAmount(NumberUtil
						.keepPrecision((double) vo.getFirstInvestProAmount() / vo.getFirstInvestUserNum(), 2));
			}

			// 平均项目投资金额 （公式=“项目投资金额”/对应渠道在“统计周期”进行项目投资的人数）
			if (vo.getProInvestUser() == 0) {
				vo.setPerProInvestAmont(0);
			} else {
				vo.setPerProInvestAmont(
						NumberUtil.keepPrecision((double) vo.getProInvestAmount() / vo.getProInvestUser(), 2));
			}
			// 平均项目复投金额 （公式=“项目复投金额”/“项目复投人数”）
			if (vo.getProMultiInvestUser() == 0) {
				vo.setPerProMultiInvestAmount(0);
			} else {
				vo.setPerProMultiInvestAmount(NumberUtil
						.keepPrecision((double) vo.getProMultiInvestAmount() / vo.getProMultiInvestUser(), 2));
			}
			// 首投用户平均投资金额 （K列“投资金额”/D列“首投”人数）
			if (vo.getFirstInvestUserNum() == 0) {
				vo.setFirstInvestUserPerAmount(0);
			} else {
				vo.setFirstInvestUserPerAmount(NumberUtil
						.keepPrecision((double) vo.getFirstInvestUserAmount() / vo.getFirstInvestUserNum(), 2));
			}
			// 平均投资金额 （公式=“用户投资金额”/（对应渠道标签下，在统计周期内有投资用户数），考虑项目、债转、点点赚买入）
			if (vo.getUserInvestNum() == 0) {
				vo.setPerInvestAmount(0);
			} else {
				vo.setPerInvestAmount(
						NumberUtil.keepPrecision((double) vo.getUserInvestAmount() / vo.getUserInvestNum(), 2));
			}
			// 金额复投率 金额复投率=复投金额/首投金额
			if (vo.getFirstInvestAmount() == 0) {
				vo.setAmountMultiRate(0);
				vo.setAmountMultiRateText("0.0%");
			} else {
				vo.setAmountMultiRate(
						(double) (vo.getUserInvestAmount() - vo.getFirstInvestAmount()) / vo.getFirstInvestAmount());
				vo.setAmountMultiRateText(NumberUtil.keepPrecision(vo.getAmountMultiRate() * 100, 2) + "%");
			}
			list.add(vo);
		}
		if(list.size() > 0){
			Collections.sort(list, new MyCompartor());
		}
		return list;
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("channel:channelAll:list")
	public void partExport(String list, HttpServletRequest request, HttpServletResponse response) throws IOException {

		List<ChannelStftInfoEntity> dataList = JSON.parseArray(list, ChannelStftInfoEntity.class);
		JSONArray va = new JSONArray();
		//
		for (int i = 0; i < dataList.size(); i++) {
			ChannelStftInfoEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("channelName", "渠道名称");
		headMap.put("channelLabel", "渠道标签");
		headMap.put("channelType", "渠道分类");
		headMap.put("registerUserNum", "注册人数");
		headMap.put("firstInvestUserNum", "首投人数");
		headMap.put("firstInvestAmount", "首投金额");
		headMap.put("firstInvestYearAmount", "首投年化投资金额");
		headMap.put("firstInvestPer", "人均首投");
		headMap.put("conversionRateText", "转化率");
		headMap.put("multipleUser", "复投人数");
		headMap.put("multipleRateText", "复投率");
		headMap.put("firstInvestProAmount", "首投用户项目投资金额");
		headMap.put("proInvestAmount", "项目投资金额");
		headMap.put("proMultiInvestAmount", "项目复投金额");
		headMap.put("firstInvestUserAmount", "首投用户投资金额");
		headMap.put("userInvestAmount", "用户投资金额");
		headMap.put("userInvestYearAmount", "用户年化投资金额");
		headMap.put("firstInvestPerTime", "首投平均期限");
		headMap.put("firstInvestUserPerProAmount", "首投用户平均项目投资金额");
		headMap.put("perProInvestAmont", "平均项目投资金额");
		headMap.put("perProMultiInvestAmount", "平均项目复投金额");
		headMap.put("firstInvestUserPerAmount", "首投用户平均投资金额");
		headMap.put("perInvestAmount", "平均投资金额");
		headMap.put("amountMultiRateText", "金额复投率");

		String title = "渠道首投复投情况";


		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	/**
	 * 按渠道名字排序
	 * 
	 * @author Administrator
	 *
	 */
	class MyCompartor implements Comparator<ChannelStftInfoEntity> {
		@Override
		public int compare(ChannelStftInfoEntity o1, ChannelStftInfoEntity o2) {
			return (o1.getChannelName()+"").compareTo(o2.getChannelName());
		}
	}
}
