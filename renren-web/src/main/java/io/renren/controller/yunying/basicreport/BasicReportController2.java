package io.renren.controller.yunying.basicreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/basicreport")
public class BasicReportController2 {
	@Autowired
	private BasicReportService service;

	@ResponseBody
	@RequestMapping("/registNotInvest")
	public R registNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {

		int start = (page - 1) * limit;
		int end = start + limit;
		PageUtils pageUtil = service.queryList(page, limit, registerStartTime, registerEndTime, start, end);

		return R.ok().put("page", pageUtil);

	}

	/**
	 * 注册三天未投资用户数据
	 * 
	 * @param page
	 * @param limit
	 * @param registerStartTime
	 * @param registerEndTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/registThreeDaysNotInvest")
	public R registThreeDaysNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {

		int start = (page - 1) * limit;
		int end = start + limit;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registerStartTime", registerStartTime);
		params.put("registerEndTime", registerEndTime);
		List<Map<String, Object>> list = service.queryRegisterThreeDaysNotInvestList(params);

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			if (end > list.size()) {
				retList.addAll(list.subList(start, list.size()));
			} else {
				retList.addAll(list.subList(start, end));
			}
		}
		if (retList.size() > 0) {
			service.getAmontByUserId(retList);// 统计用户的账户余额
		}

		PageUtils pageUtil = new PageUtils(list, list.size(), limit, page);

		return R.ok().put("page", pageUtil);

	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		PageUtils page = service.queryList(0, 10000, registerStartTime, registerEndTime, 0, 10000);
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String Hour = registerEndTime.substring(11 , 13);
		String title = "注册一小时未投资用户-W-" + month + day + "_" + Hour + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportRegisterThreeDaysExcel")
	public void exportRegisterThreeDaysExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerEndTime = map.get("registerEndTime") + "";
		List<Map<String, Object>> dataList = service.queryRegisterThreeDaysNotInvestList(map);
		service.getAmontByUserId(dataList);// 统计用户的账户余额
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String title = "注册3天未投资用户-W-" + month + day + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
