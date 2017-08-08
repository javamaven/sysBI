package io.renren.service.channelmanager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.channelmanager.DmReportUserActivateDailyDao;
import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;
import io.renren.service.channelmanager.DmReportUserActivateDailyService;
import io.renren.utils.PageUtils;

@Service("dmReportUserActivateDailyService")
public class DmReportUserActivateDailyServiceImpl implements DmReportUserActivateDailyService {
	@Autowired
	private DmReportUserActivateDailyDao dmReportUserActivateDailyDao;

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
		return dmReportUserActivateDailyDao.queryTotal(map);
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

		System.err.println("++++++++++map: " + map);

		// 查询列表数据
		List<DmReportUserActivateDailyEntity> dmReportUserActivateDailyList = queryList(map);
		int total = queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportUserActivateDailyList, total, limit, page);
		return pageUtil;
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "统计日期");
		headMap.put("userId", "用户ID");
		headMap.put("username", "用户名称");
		headMap.put("channelName", "渠道名称");
		headMap.put("channelMark", "渠道标记");
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
		headMap.put("phone", "手机号码");
		return headMap;
	}

}
