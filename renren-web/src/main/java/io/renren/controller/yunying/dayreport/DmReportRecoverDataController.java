package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.math.BigDecimal;
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

import io.renren.entity.yunying.dayreport.DmReportRecoverDataEntity;
import io.renren.service.yunying.dayreport.DmReportRecoverDataService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 14:33:45
 */
@Controller
@RequestMapping(value = "/yunying/dmreportrecoverdata")
public class DmReportRecoverDataController {
	@Autowired
	private DmReportRecoverDataService service;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportrecoverdata:list")
	public R list(Integer page, Integer limit, String statPeriod){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		//查询列表数据
		List<DmReportRecoverDataEntity> dmReportRecoverDataList = service.queryList(map);
		int total = service.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportRecoverDataList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportacctransfer:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportRecoverDataEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportRecoverDataEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "每日回款用户数据报告";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportrecoverdata:info")
	public R info(@PathVariable("statPeriod") BigDecimal statPeriod){
		DmReportRecoverDataEntity dmReportRecoverData = service.queryObject(statPeriod);
		
		return R.ok().put("dmReportRecoverData", dmReportRecoverData);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportrecoverdata:save")
	public R save(@RequestBody DmReportRecoverDataEntity dmReportRecoverData){
		service.save(dmReportRecoverData);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportrecoverdata:update")
	public R update(@RequestBody DmReportRecoverDataEntity dmReportRecoverData){
		service.update(dmReportRecoverData);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportrecoverdata:delete")
	public R delete(@RequestBody BigDecimal[] statPeriods){
		service.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
