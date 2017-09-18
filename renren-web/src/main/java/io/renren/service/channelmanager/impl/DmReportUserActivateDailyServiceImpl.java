package io.renren.service.channelmanager.impl;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.channelmanager.DmReportUserActivateDailyDao;
import io.renren.entity.DimChannelEntity;
import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;
import io.renren.service.DimChannelService;
import io.renren.service.channelmanager.DmReportUserActivateDailyService;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.Constant;
import io.renren.utils.PageUtils;

@Service("dmReportUserActivateDailyService")
public class DmReportUserActivateDailyServiceImpl implements DmReportUserActivateDailyService {
	@Autowired
	private DmReportUserActivateDailyDao dmReportUserActivateDailyDao;
	@Autowired
	private ChannelHeadManagerService channelHeadManagerService;
	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private DimChannelService dimChannelService;

	@Override
	public DmReportUserActivateDailyEntity queryObject(Integer statPeriod) {
		return dmReportUserActivateDailyDao.queryObject(statPeriod);
	}

	@Override
	public List<DmReportUserActivateDailyEntity> queryList(Map<String, Object> map) {
		return dmReportUserActivateDailyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		int queryTotal = dmReportUserActivateDailyDao.queryTotal(map);
		return queryTotal;
	}

	@Override
	public void save(DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyDao.save(dmReportUserActivateDaily);
	}

	@Override
	public void update(DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyDao.update(dmReportUserActivateDaily);
	}

	@Override
	public void delete(Integer statPeriod) {
		dmReportUserActivateDailyDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(Integer[] statPeriods) {
		dmReportUserActivateDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public UserActiveInfoEntity queryTotalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportUserActivateDailyDao.queryTotalList(map);
	}

	@Override
	public PageUtils query(Integer page, Integer limit, String statPeriod, String afterInvestBalance_start,
			String afterInvestBalance_end, String startFirstInvestTime, String endFirstInvestTime,
			String startTotalMoney, String endTotalMoney, String startTotalInvestAmount, String endTotalInvestAmount,
			String startFirstInvestAmount, String endFirstInvestAmount, String startRegisterTime,
			String endRegisterTime, String bangCard, String realName, String channelName, String registerFrom,String phone) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("afterInvestBalance_start", afterInvestBalance_start);
		map.put("afterInvestBalance_start", afterInvestBalance_start);
		map.put("afterInvestBalance_end", afterInvestBalance_end);
		map.put("registerFrom", registerFrom);
		map.put("phone", phone);
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		if (StringUtils.isNotEmpty(startFirstInvestTime)) {
			map.put("startFirstInvestTime", startFirstInvestTime + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(endFirstInvestTime)) {
			map.put("endFirstInvestTime", endFirstInvestTime + " 23:59:59");
		}
		map.put("startTotalMoney", startTotalMoney);
		map.put("endTotalMoney", endTotalMoney);

		map.put("startTotalInvestAmount", startTotalInvestAmount);
		map.put("endTotalInvestAmount", endTotalInvestAmount);
		map.put("startFirstInvestAmount", startFirstInvestAmount);
		map.put("endFirstInvestAmount", endFirstInvestAmount);

		if (StringUtils.isNotEmpty(startRegisterTime)) {
			map.put("startRegisterTime", startRegisterTime + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(endRegisterTime)) {
			map.put("endRegisterTime", endRegisterTime + " 23:59:59");
		}

		map.put("bangCard", bangCard);
		map.put("realName", realName);

		if (channelName == null || "".equals(channelName.toString().trim())) {
			map.put("channelName", new ArrayList<>());
		} else {
			channelName = channelName.toString().substring(0, channelName.toString().length() - 1);
			map.put("channelName", Arrays.asList(channelName.toString().split("\\^")));
		}
		
		setChannelAuth(map);

		System.err.println("++++++++++map: " + map);

		// 查询列表数据
		List<DmReportUserActivateDailyEntity> dmReportUserActivateDailyList = queryList(map);
		int total = queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportUserActivateDailyList, total, limit, page);
		return pageUtil;
	}
	
	/**
	 * 设置渠道权限查询条件
	 * @param map
	 * @param channelName
	 */
	private void setChannelAuth(Map<String, Object> map) {
		if(getUserId() != Constant.SUPER_ADMIN){//不是超级管理员
			boolean isMarketDirector = channelHeadManagerService.isMarketDirector();
			if(!isMarketDirector){
				List<String> labelList = channelHeadManagerService.queryChannelAuthByChannelHead("channel_name");
				System.err.println(labelList);
				String headString = "";
				if(labelList.size() > 0){
					map.put("channelNameAuth", labelList);
				}else{
					List<String> list = new ArrayList<String>();
					list.add("123^abc");
					map.put("channelNameAuth", list);
				}
				System.err.println("+++++++channelHead+++++" + headString);
			}
		}
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "统计日期");
		headMap.put("userId", "用户ID");
		headMap.put("username", "用户名称");
		headMap.put("channelName", "渠道名称");
		headMap.put("channelMark", "渠道标记");
		headMap.put("phone", "手机号码");
		headMap.put("registerTime", "注册时间");
		headMap.put("registerFrom", "操作平台");
		headMap.put("isRealname", "实名");
		headMap.put("isBinding", "绑卡");
		headMap.put("activateInvestTime", "激活投资时间");
		headMap.put("firstInvestTime", "首投时间");
		headMap.put("firstInvestBalance", "首投金额");
		headMap.put("firstTenderSubject", "首投项目类型");
		headMap.put("firstInvestPeriod", "首投期限");
		headMap.put("secAddtime", "首次复投时间");
		headMap.put("afterInvestBalance", "复投金额");
		headMap.put("afterTenderSubject", "复投项目类型");
		headMap.put("afterInvestNumber", "复投次数");
		headMap.put("totalInvestBalance", "累计投资金额");
		headMap.put("totalInvestNumber", "累计投资次数");
		headMap.put("changeInvestBalance", "债转投资金额");
		headMap.put("totalCapital", "帐户总资产");
		
		return headMap;
	}

	private Map<String, String> getChannelMap(List<DimChannelEntity> channelList) {
		Map<String,String> channelMap = new HashMap<>();
		for (int i = 0; i < channelList.size(); i++) {
			DimChannelEntity dimChannelEntity = channelList.get(i);
			channelMap.put(dimChannelEntity.getChannelLabel(), dimChannelEntity.getChannelNameBack());
		}
		return channelMap;
	}

	private List<String> getChannelLabelsByName(List<DimChannelEntity> channelList , List<String> nameList) {
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
	public PageUtils queryRealTimePage(int page, int limit, String startRegisterTime, String endRegisterTime,
			Object channelName) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		int start = (page - 1) * limit;
		map.put("limit", limit);
		String registerTimeCond = "";
		if (StringUtils.isNotEmpty(startRegisterTime)) {
			map.put("startRegisterTime", startRegisterTime + " 00:00:00");
			registerTimeCond += " and s.registerTime >= '" + startRegisterTime + " 00:00:00" + "' ";
		}
		if (StringUtils.isNotEmpty(endRegisterTime)) {
			map.put("endRegisterTime", endRegisterTime + " 23:59:59");
			registerTimeCond += " and s.registerTime <= '" + endRegisterTime + " 23:59:59" + "' ";
		}

		if (channelName == null || "".equals(channelName.toString().trim())) {
			map.put("channelName", new ArrayList<>());
		} else {
			channelName = channelName.toString().substring(0, channelName.toString().length() - 1);
			map.put("channelName", Arrays.asList(channelName.toString().split("\\^")));
		}
		
		setChannelAuth(map);

		System.err.println("++++++++++map: " + map);
		List<DimChannelEntity> channelList = dimChannelService.queryChannelList(null);
		Map<String,String> channelMap = getChannelMap(channelList);
		Object channelObj = map.get("channelName");
		String channelLabelCond = "";
		if(channelObj != null){
			List<String> channelNameList = (List<String>) channelObj;
			List<String> channelLabels = getChannelLabelsByName(channelList, channelNameList);
			for (int i = 0; i < channelLabels.size(); i++) {
				String channelLabel = channelLabels.get(i);
				channelLabelCond += "'" + channelLabel + "',";
			}
			if(channelLabelCond.endsWith(",")){
				channelLabelCond = channelLabelCond.substring(0, channelLabelCond.length() - 1);
			}
		}
		if(channelLabelCond.length() > 0){
			channelLabelCond = " and channelLabel in (" + channelLabelCond + ")";
		}
		String path = this.getClass().getResource("/").getPath();
		List<Map<String,Object>> dataList = null;
		List<Map<String,Object>> totalList = null;
		String querySql = null;
		String totalSql = null;
		try {
			querySql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/用户激活实时查询.txt"));
			totalSql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/用户激活实时查询总数.txt"));
			querySql = querySql.replace("${channelLabelCond}", channelLabelCond);
			querySql = querySql.replace("${registerTimeCond}", registerTimeCond);
			totalSql = totalSql.replace("${channelLabelCond}", channelLabelCond);
			totalSql = totalSql.replace("${registerTimeCond}", registerTimeCond);
			dataList = new JdbcUtil(dataSourceFactory, "mysql").query(querySql, start, limit);
			totalList = new JdbcUtil(dataSourceFactory, "mysql").query(totalSql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		// 查询列表数据
//		List<DmReportUserActivateDailyEntity> dmReportUserActivateDailyList = queryList(map);
		Map<String, Object> totalMap = totalList.get(0);
		Object totalObj = totalMap.get("total");
		int total = 0;
		if(totalObj != null){
			total = Integer.parseInt(totalObj.toString());
		}

		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> dataMap = dataList.get(i);
			String channelLabel = dataMap.get("channelLabel") + "";
			dataMap.put("channelName", channelMap.get(channelLabel));
		}
		PageUtils pageUtil = new PageUtils(dataList, total, limit, page);
		return pageUtil;
	}

}
