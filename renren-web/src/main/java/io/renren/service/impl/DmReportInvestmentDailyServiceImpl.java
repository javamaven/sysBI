package io.renren.service.impl;

import static io.renren.utils.ShiroUtils.getUserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.DmReportInvestmentDailyDao;
import io.renren.entity.DmReportInvestmentDailyEntity;
import io.renren.service.DmReportInvestmentDailyService;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.utils.Constant;
import io.renren.utils.PageUtils;

@Service("dmReportInvestmentDailyService")
public class DmReportInvestmentDailyServiceImpl implements DmReportInvestmentDailyService {
	@Autowired
	private DmReportInvestmentDailyDao dmReportInvestmentDailyDao;
	@Autowired
	ChannelHeadManagerService channelHeadManagerService;

	@Override
	public DmReportInvestmentDailyEntity queryObject(String statPeriod) {
		return dmReportInvestmentDailyDao.queryObject(statPeriod);
	}

	@Override
	public List<DmReportInvestmentDailyEntity> queryList(Map<String, Object> map) {
		return dmReportInvestmentDailyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return dmReportInvestmentDailyDao.queryTotal(map);
	}

	@Override
	public void save(DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		dmReportInvestmentDailyDao.save(dmReportInvestmentDaily);
	}

	@Override
	public void update(DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		dmReportInvestmentDailyDao.update(dmReportInvestmentDaily);
	}

	@Override
	public void delete(String statPeriod) {
		dmReportInvestmentDailyDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(String[] statPeriods) {
		dmReportInvestmentDailyDao.deleteBatch(statPeriods);
	}

	@Override
	public DmReportInvestmentDailyEntity queryTotalList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return dmReportInvestmentDailyDao.queryTotalList(map);
	}

	@Override
	public PageUtils query(Integer page, Integer limit, String userId, String userName, String channelId,
			String channelName, String investStartTime, String investEndTime, String operPlatform, String withProType) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("channelId", channelId);
		map.put("operPlatform", operPlatform);
		map.put("withProType", withProType);
		if (StringUtils.isNotEmpty(investStartTime)) {
			map.put("investStartTime", investStartTime + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(investEndTime)) {
			map.put("investEndTime", investEndTime + " 23:59:59");
		}
		if (channelName == null || "".equals(channelName.toString().trim())) {
			map.put("channelName", new ArrayList<>());
		} else {
			channelName = channelName.toString().substring(0, channelName.toString().length() - 1);
			map.put("channelName", Arrays.asList(channelName.toString().split("\\^")));
		}
		setChannelAuth(map);
		// 查询列表数据
		List<DmReportInvestmentDailyEntity> dmReportInvestmentDailyList = queryList(map);
		int total = queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportInvestmentDailyList, total, limit, page);
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
				if(labelList.size() > 0){
					map.put("channelNameAuth", labelList);
				}else{
					List<String> list = new ArrayList<String>();
					list.add("123^abc");
					map.put("channelNameAuth", list);
				}
			}
		}
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("userId", "用户ID");
		headMap.put("username", "用户名称");
		headMap.put("channelId", "渠道ID");
		headMap.put("channelName", "渠道名称");
		headMap.put("activityTag", "渠道标记");
		headMap.put("tenderFrom", "操作平台");
		headMap.put("addtime", "投资时间");
		headMap.put("borrowType", "涉及项目类型");
		headMap.put("pid", "投资记录ID");
		headMap.put("projectName", "涉及项目名称");
		headMap.put("tenderCapital", "涉及项目本金");
		headMap.put("borrowPeriod", "涉及项目期限");
		headMap.put("recoverAccountWait", "当前持有总金额");
		headMap.put("stage", "目前状态");
		headMap.put("registerTime", "注册时间");


		return headMap;
	}

}
