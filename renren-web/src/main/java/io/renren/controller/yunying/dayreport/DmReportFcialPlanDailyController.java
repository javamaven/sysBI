package io.renren.controller.yunying.dayreport;

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
import io.renren.entity.yunying.dayreport.DmReportFcialPlanDailyEntity;
import io.renren.service.yunying.dayreport.DmReportFcialPlanDailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import static io.renren.utils.ShiroUtils.getUserId;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 11:04:27
 */
@Controller
@RequestMapping(value = "/yunying/dmreportfcialplandaily")
public class DmReportFcialPlanDailyController {
	@Autowired
	private DmReportFcialPlanDailyService service;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="每日理财计划基本数据";

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportfcialplandaily:list")
	public R list(Integer page, Integer limit, String statPeriod) {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportFcialPlanDailyEntity> dmReportFcialPlanDailyList = service.queryList(map);
		int total = service.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportFcialPlanDailyList, total, limit, page);

		return R.ok().put("page", pageUtil);
	}
	

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportfcialplandaily:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		// 查询列表数据
		List<DmReportFcialPlanDailyEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportFcialPlanDailyEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "每日理财计划基本数据";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}


	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportfcialplandaily:info")
	public R info(@PathVariable("statPeriod") String statPeriod) {
		DmReportFcialPlanDailyEntity dmReportFcialPlanDaily = service.queryObject(statPeriod);

		return R.ok().put("dmReportFcialPlanDaily", dmReportFcialPlanDaily);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportfcialplandaily:save")
	public R save(@RequestBody DmReportFcialPlanDailyEntity dmReportFcialPlanDaily) {
		service.save(dmReportFcialPlanDaily);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportfcialplandaily:update")
	public R update(@RequestBody DmReportFcialPlanDailyEntity dmReportFcialPlanDaily) {
		service.update(dmReportFcialPlanDaily);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportfcialplandaily:delete")
	public R delete(@RequestBody String[] statPeriods) {
		service.deleteBatch(statPeriods);

		return R.ok();
	}

}
