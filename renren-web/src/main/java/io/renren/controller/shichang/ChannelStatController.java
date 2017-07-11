package io.renren.controller.shichang;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.DimChannelEntity;
import io.renren.service.ChannelCostService;
import io.renren.service.DimChannelService;
import io.renren.service.shichang.ChannelStatService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 渠道统计报表
 */
@RestController
@RequestMapping("/channel/channel_stat")
public class ChannelStatController {
	@Autowired
	private ChannelCostService ChannelCostService;
	
	@Autowired
	private DimChannelService dimChannelService;
	
	@Autowired
	private ChannelStatService service;
	
	@Autowired
	DataSourceFactory dataSourceFactory;
	
	
	

	@ResponseBody
	@RequestMapping("/total")
	public R total(@RequestBody Map<String, Object> params) {
		String querySql = "";
		List<Map<String, Object>> list = null;
		try {
			String registerTimeCond = "";
			String registerStartTime = params.get("registerStartTime") + "";
			String firstInvestStartTime = params.get("firstInvestStartTime") + "";
			String registerEndTime = params.get("registerEndTime") + "";
			String firstInvestEndTime = params.get("firstInvestEndTime") + "";
			String channelLabel = params.get("channelName") + "";
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
			querySql = FileUtils.readFileToString(new File(path + File.separator + "sql/市场部渠道实时统计表头汇总.txt"));
			querySql = querySql.replace("${registerTimeCond}", registerTimeCond);
			querySql = querySql.replace("${firstInvestTimeCond}", firstInvestTimeCond);
			if(StringUtils.isEmpty(channelLabel) || "null".equals(channelLabel)){
				querySql = querySql.replace("${channelLabelCond}", "");
			}else{
				String[] split = channelLabel.split(",");
				List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
				Map<String, String> channelNameKeyMap = getChannelNameKeyMap(channelList);
				String channelString = "";
				for (int i = 0; i < split.length; i++) {
					Iterator<String> iterator = channelNameKeyMap.keySet().iterator();
					while(iterator.hasNext()){
						String next = iterator.next();
						if(next != null && next.contains(split[i])){
							channelString += "'" + channelNameKeyMap.get(next) + "',";
						}
					}
				}
				if(channelString.endsWith(",")){
					channelString = channelString.substring(0, channelString.length()-1);
				}
				querySql = querySql.replace("${channelLabelCond}", " and u1.activity_tag in("+channelString+") ");
			}
			long l1 = System.currentTimeMillis();
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
			list = util.query(querySql);
			long l2 = System.currentTimeMillis();
			System.err.println(querySql);
			System.err.println(" ++++耗时：" + (l2-l1));
		} catch (SQLException e) {
			System.err.println("++++++++error sql +++++++++++ " + querySql);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(list.size() == 0){
			return R.ok().put("total", null);
		}
		return R.ok().put("total", list.get(0));
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/detailList")
	public R detailList(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelLabel) {
		PageUtils pageUtil = service.queryDetailList(page, limit, registerStartTime, registerEndTime, firstInvestStartTime,
				firstInvestEndTime, channelLabel);
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelName) {
		PageUtils pageUtil = service.queryTotalList(page, limit, registerStartTime, registerEndTime, firstInvestStartTime,
				firstInvestEndTime, channelName);
		return R.ok().put("page", pageUtil);
	}



	@RequestMapping("/getChannel")
	public R getChannel() {
		// 查询渠道数据
		return R.ok().put("Channel", ChannelCostService.queryChannel());
	}


	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportTotalExcel")
	public void exportTotalExcel(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String registerStartTime = map.get("registerStartTime") + "";
		String firstInvestStartTime = map.get("firstInvestStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		String firstInvestEndTime = map.get("firstInvestEndTime") + "";
		String channelName = map.get("channelName") + "";
		PageUtils pageUtils = service.queryTotalList(1, 10000, registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime,
				channelName);
		JSONArray va = new JSONArray();
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtils.getList();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,Object> entity = dataList.get(i);
			va.add(entity);
		}
		
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("channel_name", "渠道名称");
		headMap.put("channel_label", "渠道标记");
		headMap.put("注册人数", "注册人数");
		
		headMap.put("首投人数", "首投人数");
		headMap.put("转化率", "转化率");
		headMap.put("注册人数", "注册人数");
		
		headMap.put("复投人数", "复投人数");
		headMap.put("复投率", "复投率");
		headMap.put("首投金额（万元）", "首投金额（万元）");
		
		headMap.put("累投金额（万元）", "累投金额（万元）");
		headMap.put("复投金额（万元）", "复投金额（万元）");
		headMap.put("首投年化投资金额（万元）", "首投年化投资金额（万元）");
		
		headMap.put("年化累投金额（万元）", "年化累投金额（万元）");
		headMap.put("年化复投金额（万元）", "年化复投金额（万元）");
		headMap.put("待收>100人数", "待收>100人数");
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
		String title = "渠道统计数据报表-("+registerStartTime+"-"+registerEndTime+")";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	List<DimChannelEntity> channelList = null;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportDetailExcel")
	public void exportDetailExcel(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String registerStartTime = map.get("registerStartTime") + "";
		String firstInvestStartTime = map.get("firstInvestStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		String firstInvestEndTime = map.get("firstInvestEndTime") + "";
		String channelName = map.get("channelName") + "";
		channelList = dimChannelService.queryChannelList(null);
		Map<String, String> channelNameKeyMap = getChannelNameKeyMap(channelList);
		String channelLabel = map.get("channelLabel") + "";
		if(StringUtils.isNotEmpty(channelName) && !"null".equals(channelName)){
			channelLabel =  channelNameKeyMap.get(channelName);
		}
		PageUtils pageUtils = service.queryDetailList(1, 1000000, registerStartTime, registerEndTime, firstInvestStartTime, firstInvestEndTime, channelLabel);
		JSONArray va = new JSONArray();
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) pageUtils.getList();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,Object> entity = dataList.get(i);
			va.add(entity);
		}
		
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("user_id", "用户ID");
		headMap.put("user_name", "用户名");
		headMap.put("channel_name", "渠道名称");
		
		headMap.put("activity_tag", "渠道标记");
		headMap.put("register_time", "注册时间");
		headMap.put("register_from", "操作平台");
		
		headMap.put("实名", "实名");
		headMap.put("绑卡", "绑卡");
		headMap.put("st_time", "首投时间");
		
		headMap.put("st_money", "首投金额（元）");
		headMap.put("days", "首投期限（天）");
		headMap.put("首投年化投资金额", "首投年化投资金额");
		
		headMap.put("复投金额", "复投金额");
		headMap.put("投资次数", "投资次数");
		headMap.put("累投金额", "累投金额");
		
		headMap.put("年化累投金额", "年化累投金额");
		headMap.put("年化复投金额", "年化复投金额");
		headMap.put("账户资产", "账户资产");
		
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
		String title = "渠道明细数据报表-("+registerStartTime+"-"+registerEndTime+")";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
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


}
