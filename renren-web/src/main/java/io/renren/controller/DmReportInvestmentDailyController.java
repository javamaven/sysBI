package io.renren.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.DmReportInvestmentDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;
import io.renren.service.DmReportInvestmentDailyService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 用户投资情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-12 11:16:53
 */
@Controller
@RequestMapping("dmreportinvestmentdaily")
public class DmReportInvestmentDailyController {
	@Autowired
	private DmReportInvestmentDailyService dmReportInvestmentDailyService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportinvestmentdaily:list")
	public R list(Integer page, Integer limit, String userId, String userName, String channelId, String channelName,
			String investStartTime, String investEndTime, String operPlatform, String withProType, String statPeriod) {
		System.err.println("+++++++statPeriod++++++" + statPeriod);
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
		// 查询列表数据
		List<DmReportInvestmentDailyEntity> dmReportInvestmentDailyList = dmReportInvestmentDailyService.queryList(map);
		int total = dmReportInvestmentDailyService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportInvestmentDailyList, total, limit, page);

		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/totalList")
	@RequiresPermissions("dmreportinvestmentdaily:list")
	public R totalList(@RequestBody Map<String, Object> map) {
		System.err.println("++++++++++map: " + map);
		String investStartTime = map.get("investStartTime") + "";
		if (StringUtils.isNotEmpty(investStartTime )) {
			map.put("investStartTime", investStartTime + " 00:00:00");
		}
		String investEndTime = map.get("investEndTime") + "";
		if (StringUtils.isNotEmpty(investEndTime)) {
			map.put("investEndTime", investEndTime + " 23:59:59");
		}
		Object channelName = map.get("channelName");
		if (channelName == null || "".equals(channelName.toString().trim())) {
			map.put("channelName", new ArrayList<>());
		} else {
			channelName = channelName.toString().substring(0, channelName.toString().length() - 1);
			map.put("channelName", Arrays.asList(channelName.toString().split("\\^")));
		}
		// 查询列表数据
		DmReportInvestmentDailyEntity dmReportInvestmentDailyEntity = dmReportInvestmentDailyService.queryTotalList(map);

		return R.ok().put("data", dmReportInvestmentDailyEntity);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportinvestmentdaily:info")
	public R info(@PathVariable("statPeriod") String statPeriod) {
		DmReportInvestmentDailyEntity dmReportInvestmentDaily = dmReportInvestmentDailyService.queryObject(statPeriod);

		return R.ok().put("dmReportInvestmentDaily", dmReportInvestmentDaily);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportinvestmentdaily:save")
	public R save(@RequestBody DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		dmReportInvestmentDailyService.save(dmReportInvestmentDaily);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportinvestmentdaily:update")
	public R update(@RequestBody DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		dmReportInvestmentDailyService.update(dmReportInvestmentDaily);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportinvestmentdaily:delete")
	public R delete(@RequestBody String[] statPeriods) {
		dmReportInvestmentDailyService.deleteBatch(statPeriods);

		return R.ok();
	}

}
