package io.renren.controller.channelmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import io.renren.entity.channelmanager.DmReportUserActivateDailyEntity;
import io.renren.entity.channelmanager.UserActiveInfoEntity;
import io.renren.service.channelmanager.DmReportUserActivateDailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 用户激活情况表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-04 17:57:21
 */
@Controller
@RequestMapping("/channel/manager")
public class DmReportUserActivateDailyController {
	@Autowired
	private DmReportUserActivateDailyService dmReportUserActivateDailyService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportuseractivatedaily:list")
	public R list(Integer page, Integer limit, Map<String, Object> params, String params2, String statPeriod,
			String afterInvestBalance_start, String afterInvestBalance_end, String startFirstInvestTime,
			String endFirstInvestTime, String startTotalMoney, String endTotalMoney, String startTotalInvestAmount,
			String endTotalInvestAmount, String startFirstInvestAmount, String endFirstInvestAmount,
			String startRegisterTime, String endRegisterTime, String bangCard, String realName, String channelName) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
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
		List<DmReportUserActivateDailyEntity> dmReportUserActivateDailyList = dmReportUserActivateDailyService
				.queryList(map);
		int total = dmReportUserActivateDailyService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportUserActivateDailyList, total, limit, page);

		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportuseractivatedaily:list")
	public void partExport(String params, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String,Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		String startFirstInvestTime = map.get("startFirstInvestTime") + "";
		if (StringUtils.isNotEmpty(startFirstInvestTime)) {
			map.put("startFirstInvestTime", startFirstInvestTime + " 00:00:00");
		}
		String endFirstInvestTime = map.get("endFirstInvestTime") + "";
		if (StringUtils.isNotEmpty(endFirstInvestTime)) {
			map.put("endFirstInvestTime", endFirstInvestTime + " 23:59:59");
		}

		String startRegisterTime = map.get("startRegisterTime") + "";
		if (StringUtils.isNotEmpty(startRegisterTime)) {
			map.put("startRegisterTime", startRegisterTime + " 00:00:00");
		}
		String endRegisterTime = map.get("endRegisterTime") + "";
		if (StringUtils.isNotEmpty(endRegisterTime)) {
			map.put("endRegisterTime", endRegisterTime + " 23:59:59");
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
		List<DmReportUserActivateDailyEntity> dataList = dmReportUserActivateDailyService.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportUserActivateDailyEntity entity = dataList.get(i);
			va.add(entity);
			if(i == 100){
				break;
			}
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "统计日期");
		headMap.put("userId", "用户ID");
		headMap.put("channelName", "渠道名称");
		headMap.put("channelMark", "渠道标记");
		headMap.put("registerTime", "注册时间");
		headMap.put("isRealname", "实名");
		headMap.put("isBinding", "绑卡");
		headMap.put("activateInvestTime", "激活投资时间");
		headMap.put("firstInvestTime", "首投时间");
		headMap.put("firstInvestBalance", "首投金额");
		headMap.put("firstInvestPeriod", "首投期限");

		headMap.put("afterInvestBalance", "复投金额");
		headMap.put("afterInvestNumber", "复投次数");
		headMap.put("totalInvestBalance", "累计投资金额");
		headMap.put("totalInvestNumber", "累计投资次数");
		headMap.put("changeInvestBalance", "债转投资金额");
		headMap.put("totalCapital", "帐户总资产");

		String title = "用户激活情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	@ResponseBody
	@RequestMapping("/totalList")
	@RequiresPermissions("dmreportuseractivatedaily:list")
	public R totalList(@RequestBody Map<String, Object> map) {
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		String startFirstInvestTime = map.get("startFirstInvestTime") + "";
		if (StringUtils.isNotEmpty(startFirstInvestTime)) {
			map.put("startFirstInvestTime", startFirstInvestTime + " 00:00:00");
		}
		String endFirstInvestTime = map.get("endFirstInvestTime") + "";
		if (StringUtils.isNotEmpty(endFirstInvestTime)) {
			map.put("endFirstInvestTime", endFirstInvestTime + " 23:59:59");
		}
		String startRegisterTime = map.get("startRegisterTime") + "";
		if (StringUtils.isNotEmpty(startRegisterTime)) {
			map.put("startRegisterTime", startRegisterTime + " 00:00:00");
		}
		String endRegisterTime = map.get("endRegisterTime") + "";
		if (StringUtils.isNotEmpty(endRegisterTime)) {
			map.put("endRegisterTime", endRegisterTime + " 23:59:59");
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
		UserActiveInfoEntity userActiveInfoEntity = dmReportUserActivateDailyService.queryTotalList(map);

		return R.ok().put("data", userActiveInfoEntity);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportuseractivatedaily:info")
	public R info(@PathVariable("statPeriod") Integer statPeriod) {
		DmReportUserActivateDailyEntity dmReportUserActivateDaily = dmReportUserActivateDailyService
				.queryObject(statPeriod);

		return R.ok().put("dmReportUserActivateDaily", dmReportUserActivateDaily);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportuseractivatedaily:save")
	public R save(@RequestBody DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyService.save(dmReportUserActivateDaily);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportuseractivatedaily:update")
	public R update(@RequestBody DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		dmReportUserActivateDailyService.update(dmReportUserActivateDaily);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportuseractivatedaily:delete")
	public R delete(@RequestBody Integer[] statPeriods) {
		dmReportUserActivateDailyService.deleteBatch(statPeriods);

		return R.ok();
	}

}
