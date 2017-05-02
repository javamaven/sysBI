package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DailyEntity;
import io.renren.service.DailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 市场部每日渠道数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
@RestController
@RequestMapping("/channel/daily")
public class DailyController {
	@Autowired
	private DailyService dailyDataService;


	/**
	 * 列表
	 */
	@RequestMapping("/black")
	 @RequiresPermissions("curly:list")
	public R list(@RequestBody Map<String, Object> params) {
		//查询列表数据
		Query query = new Query(params);

		//查询列表数据
		List<DailyEntity> dmReportDailyDataList = dailyDataService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	@RequiresPermissions("curly:list")
	public void partExport(HttpServletResponse response) throws IOException {
		List<DailyEntity> DailyList = dailyDataService.queryExports();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < DailyList.size() ; i++) {
			DailyEntity DailyUser = new DailyEntity();
			DailyUser.setStatPeriod(DailyList.get(i).getStatPeriod());
			DailyUser.setIndicatorsName(DailyList.get(i).getIndicatorsName());
			DailyUser.setIndicatorsValue(DailyList.get(i).getIndicatorsValue());
			DailyUser.setSequential(DailyList.get(i).getSequential());
			DailyUser.setCompared(DailyList.get(i).getCompared());
			DailyUser.setMonthMeanValue(DailyList.get(i).getMonthMeanValue());
			DailyUser.setMonthMeanValueThan(DailyList.get(i).getMonthMeanValueThan());
			va.add(DailyUser);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("statPeriod","日期");
		headMap.put("indicatorsName","指标名字");
		headMap.put("indicatorsValue","指标值");
		headMap.put("sequential","环比");
		headMap.put("compared","同比");
		headMap.put("monthMeanValue","30天均值");
		headMap.put("monthMeanValueThan","30天均值比");

		String title = "日报指标汇总";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

}
