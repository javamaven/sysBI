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

import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;
import io.renren.entity.yunying.dayreport.DmReportCashDataEntity;
import io.renren.service.yunying.dayreport.DmReportCashDataService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 11:27:30
 */
@Controller
@RequestMapping(value = "/yunying/dmreportcashdata")
public class DmReportCashDataController {
	@Autowired
	private DmReportCashDataService service;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportcashdata:list")
	public R list(Integer page, Integer limit, String statPeriod){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		//查询列表数据
		List<DmReportCashDataEntity> dmReportCashDataList = service.queryList(map);
		int total = service.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportCashDataList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportcashdata:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportCashDataEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportCashDataEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "每日提现用户数据报告";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportcashdata:info")
	public R info(@PathVariable("statPeriod") BigDecimal statPeriod){
		DmReportCashDataEntity dmReportCashData = service.queryObject(statPeriod);
		
		return R.ok().put("dmReportCashData", dmReportCashData);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportcashdata:save")
	public R save(@RequestBody DmReportCashDataEntity dmReportCashData){
		service.save(dmReportCashData);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportcashdata:update")
	public R update(@RequestBody DmReportCashDataEntity dmReportCashData){
		service.update(dmReportCashData);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportcashdata:delete")
	public R delete(@RequestBody BigDecimal[] statPeriods){
		service.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
