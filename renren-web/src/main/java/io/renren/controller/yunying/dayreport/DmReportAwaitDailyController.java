package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import io.renren.entity.yunying.dayreport.DmReportAccTransferEntity;
import io.renren.entity.yunying.dayreport.DmReportAwaitDailyEntity;
import io.renren.service.yunying.dayreport.DmReportAwaitDailyService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-05 10:50:43
 */
@Controller
@RequestMapping(value = "/yunying/dmreportawaitdaily")
public class DmReportAwaitDailyController {
	@Autowired
	private DmReportAwaitDailyService service;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="每日待收数据报告";
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportawaitdaily:list")
	public R list(Integer page, Integer limit, String statPeriod){

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		//查询列表数据
		List<DmReportAwaitDailyEntity> dmReportAwaitDailyList = service.queryList(map);
		int total = service.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportAwaitDailyList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportawaitdaily:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		// 查询列表数据
		List<DmReportAwaitDailyEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportAwaitDailyEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "每日待收数据报告";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportawaitdaily:info")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DmReportAwaitDailyEntity dmReportAwaitDaily = service.queryObject(statPeriod);
		
		return R.ok().put("dmReportAwaitDaily", dmReportAwaitDaily);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportawaitdaily:save")
	public R save(@RequestBody DmReportAwaitDailyEntity dmReportAwaitDaily){
		service.save(dmReportAwaitDaily);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportawaitdaily:update")
	public R update(@RequestBody DmReportAwaitDailyEntity dmReportAwaitDaily){
		service.update(dmReportAwaitDaily);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportawaitdaily:delete")
	public R delete(@RequestBody String[] statPeriods){
		service.deleteBatch(statPeriods);
		
		return R.ok();
	}

}
