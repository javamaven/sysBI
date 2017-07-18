package io.renren.controller.channelmanager;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
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

import static io.renren.utils.ShiroUtils.getUserId;

/**
 * 用户激活情况表
 * 
 * @date 2017-05-04 17:57:21
 */
@Controller
@RequestMapping("/channel/manager")
public class DmReportUserActivateDailyController {
	@Autowired
	private DmReportUserActivateDailyService service;
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="用户激活情况表";

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
			String startRegisterTime, String endRegisterTime, String bangCard,String phone,
			String realName, String channelName, String registerFrom) {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		PageUtils pageUtil = service.query(page, limit, statPeriod, afterInvestBalance_start, afterInvestBalance_end,
				startFirstInvestTime, endFirstInvestTime, startTotalMoney, endTotalMoney, startTotalInvestAmount,
				endTotalInvestAmount, startFirstInvestAmount, endFirstInvestAmount, startRegisterTime, endRegisterTime,
				bangCard, realName, channelName, registerFrom,phone);

		return R.ok().put("page", pageUtil);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportuseractivatedaily:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
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

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		// 查询列表数据
		List<DmReportUserActivateDailyEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportUserActivateDailyEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

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
		UserActiveInfoEntity userActiveInfoEntity = service.queryTotalList(map);

		return R.ok().put("data", userActiveInfoEntity);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportuseractivatedaily:info")
	public R info(@PathVariable("statPeriod") Integer statPeriod) {
		DmReportUserActivateDailyEntity dmReportUserActivateDaily = service.queryObject(statPeriod);

		return R.ok().put("dmReportUserActivateDaily", dmReportUserActivateDaily);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportuseractivatedaily:save")
	public R save(@RequestBody DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		service.save(dmReportUserActivateDaily);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportuseractivatedaily:update")
	public R update(@RequestBody DmReportUserActivateDailyEntity dmReportUserActivateDaily) {
		service.update(dmReportUserActivateDaily);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportuseractivatedaily:delete")
	public R delete(@RequestBody Integer[] statPeriods) {
		service.deleteBatch(statPeriods);

		return R.ok();
	}

}
