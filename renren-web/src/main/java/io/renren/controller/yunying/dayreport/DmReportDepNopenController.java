package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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

import io.renren.entity.yunying.dayreport.DmReportDepNopenEntity;
import io.renren.service.UserBehaviorService;
import io.renren.service.yunying.dayreport.DmReportDepNopenService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-26 14:36:17
 */
@Controller
@RequestMapping("dmreportdepnopen")
public class DmReportDepNopenController {
	@Autowired
	private DmReportDepNopenService dmReportDepNopenService;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private String reportType = "普通版有待收但是未开通存的账户数据";

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportdepnopen:list")
	public R list(Integer page, Integer limit, String statPeriod) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		System.out.println(statPeriod);
		// 查询列表数据
		List<DmReportDepNopenEntity> dmReportDepNopenList = dmReportDepNopenService.queryList(map);
		int total = dmReportDepNopenService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportDepNopenList, total, limit, page);

		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportdepnopen:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(), new Date(), "导出", reportType, " ");
		// 查询列表数据
		List<DmReportDepNopenEntity> dataList = dmReportDepNopenService.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportDepNopenEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepNopenService.getExcelFields();

		String title = "普通版有待收但是未开通存的账户数据";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportdepnopen:info")
	public R info(@PathVariable("statPeriod") String statPeriod) {
		DmReportDepNopenEntity dmReportDepNopen = dmReportDepNopenService.queryObject(statPeriod);

		return R.ok().put("dmReportDepNopen", dmReportDepNopen);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportdepnopen:save")
	public R save(@RequestBody DmReportDepNopenEntity dmReportDepNopen) {
		dmReportDepNopenService.save(dmReportDepNopen);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportdepnopen:update")
	public R update(@RequestBody DmReportDepNopenEntity dmReportDepNopen) {
		dmReportDepNopenService.update(dmReportDepNopen);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportdepnopen:delete")
	public R delete(@RequestBody String[] statPeriods) {
		dmReportDepNopenService.deleteBatch(statPeriods);

		return R.ok();
	}

}
