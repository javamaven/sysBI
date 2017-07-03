package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;
import io.renren.service.yunying.dayreport.DmReportDepSalesService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-29 16:30:26
 */
@Controller
@RequestMapping("dmreportdepsales")
public class DmReportDepSalesController {
	@Autowired
	private DmReportDepSalesService dmReportDepSalesService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportdepsales:list")
	public R list(Integer page, Integer limit,String statPeriod){
		
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryList(map);
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list2")
	@RequiresPermissions("dmreportdepsales:list")
	public R list2(Integer page, Integer limit,String statPeriod){
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryLists(map);
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/list3")
	@RequiresPermissions("dmreportdepsales:list")
	public R list3(Integer page, Integer limit,String statPeriod){
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListss(map);
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportDepSalesEntity> dataList = dmReportDepSalesService.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportDepSalesEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields();

		String title = "各类产品的实际销售情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel1")
	public void partExport1(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportDepSalesEntity> dataList = dmReportDepSalesService.queryLists(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportDepSalesEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields1();

		String title = "底层资产供应情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void partExport2(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportDepSalesEntity> dataList = dmReportDepSalesService.queryListss(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportDepSalesEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields2();

		String title = "理财计划留存情况（单位：万）";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
}
