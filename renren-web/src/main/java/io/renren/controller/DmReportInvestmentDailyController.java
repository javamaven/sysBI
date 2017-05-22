package io.renren.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.DmReportInvestmentDailyEntity;
import io.renren.service.DmReportInvestmentDailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 用户投资情况表
 * 
 * @date 2017-05-12 11:16:53
 */
@Controller
@RequestMapping("dmreportinvestmentdaily")
public class DmReportInvestmentDailyController {
	@Autowired
	private DmReportInvestmentDailyService service;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportinvestmentdaily:list")
	public R list(Integer page, Integer limit, String userId, String userName, String channelId, String channelName,
			String investStartTime, String investEndTime, String operPlatform, String withProType, String statPeriod) {
		PageUtils pageUtil = service.query(page, limit, userId, userName, channelId, channelName, investStartTime,
				investEndTime, operPlatform, withProType);
		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/totalList")
	@RequiresPermissions("dmreportinvestmentdaily:list")
	public R totalList(@RequestBody Map<String, Object> map) {
		String investStartTime = map.get("investStartTime") + "";
		if (StringUtils.isNotEmpty(investStartTime)) {
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
		DmReportInvestmentDailyEntity dmReportInvestmentDailyEntity = service.queryTotalList(map);

		return R.ok().put("data", dmReportInvestmentDailyEntity);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportinvestmentdaily:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String investStartTime = map.get("investStartTime") + "";
		if (StringUtils.isNotEmpty(investStartTime)) {
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
		System.err.println("++++++++++map: " + map);

		// 查询列表数据
		List<DmReportInvestmentDailyEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportInvestmentDailyEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "用户投资情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportinvestmentdaily:info")
	public R info(@PathVariable("statPeriod") String statPeriod) {
		DmReportInvestmentDailyEntity dmReportInvestmentDaily = service.queryObject(statPeriod);

		return R.ok().put("dmReportInvestmentDaily", dmReportInvestmentDaily);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportinvestmentdaily:save")
	public R save(@RequestBody DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		service.save(dmReportInvestmentDaily);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportinvestmentdaily:update")
	public R update(@RequestBody DmReportInvestmentDailyEntity dmReportInvestmentDaily) {
		service.update(dmReportInvestmentDaily);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportinvestmentdaily:delete")
	public R delete(@RequestBody String[] statPeriods) {
		service.deleteBatch(statPeriods);

		return R.ok();
	}

}
