package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.yunying.dayreport.DmReportActiveChannelCostService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 09:27:12
 */
@Controller
@RequestMapping(value = "/yunying/dmreportactivechannelcost")
public class DmReportActiveChannelCostController {
	@Autowired
	private DmReportActiveChannelCostService service;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String statPeriod) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		int start = (page - 1) * limit;
		int end = start + limit;
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod);
		}
		// 查询列表数据
		// List<DmReportActiveChannelCostEntity> dmReportActiveChannelCostList =
		// service.queryList(map);
		List<Map<String, Object>> costList = service.queryCostList(map);
//		int total = service.queryTotal(map);
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		if (costList.size() > 0) {
			if (end > costList.size()) {
				retList.addAll(costList.subList(start, costList.size()));
			} else {
				retList.addAll(costList.subList(start, end));
			}
		}
		
		PageUtils pageUtil = new PageUtils(retList, costList.size(), limit, page);

		return R.ok().put("page", pageUtil);
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod);
		}
		// 查询列表数据
//		List<DmReportActiveChannelCostEntity> dataList = service.queryList(map);
		List<Map<String, Object>> dataList = service.queryCostList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "活动渠道成本数据报告";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
