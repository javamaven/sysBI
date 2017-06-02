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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;
import io.renren.entity.yunying.dayreport.DmReportFcialPlanDailyEntity;
import io.renren.service.yunying.dayreport.DmReportBasicDailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 15:24:09
 */
@Controller
@RequestMapping(value = "/yunying/dmreportbasicdaily")
public class DmReportBasicDailyController {
	@Autowired
	private DmReportBasicDailyService service;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportbasicdaily:list")
	public R list(Integer page, Integer limit, String statPeriod){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		//查询列表数据
		List<DmReportBasicDailyEntity> dmReportBasicDailyList = service.queryList(map);
		int total = service.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportBasicDailyList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportbasicdaily:info")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DmReportBasicDailyEntity dmReportBasicDaily = service.queryObject(statPeriod);
		
		return R.ok().put("dmReportBasicDaily", dmReportBasicDaily);
	}
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportbasicdaily:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportBasicDailyEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportBasicDailyEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "每日基本数据";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportbasicdaily:save")
	public R save(@RequestBody DmReportBasicDailyEntity dmReportBasicDaily){
		service.save(dmReportBasicDaily);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportbasicdaily:update")
	public R update(@RequestBody DmReportBasicDailyEntity dmReportBasicDaily){
		service.update(dmReportBasicDaily);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportbasicdaily:delete")
	public R delete(@RequestBody String[] statPeriods){
		service.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
