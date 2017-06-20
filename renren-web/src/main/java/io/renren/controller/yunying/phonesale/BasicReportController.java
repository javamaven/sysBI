package io.renren.controller.yunying.phonesale;

import java.io.IOException;
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
public class BasicReportController {
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

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		PageUtils page = service.queryList(1, 10000, registerStartTime, registerEndTime, 1, 10000);
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String title = "当天注册未投资用户-" + registerEndTime.substring(0 ,13);

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
