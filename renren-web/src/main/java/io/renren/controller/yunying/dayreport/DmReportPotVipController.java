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

import io.renren.entity.yunying.dayreport.DmReportBasicDailyEntity;
import io.renren.entity.yunying.dayreport.DmReportPotVipEntity;
import io.renren.service.UserBehaviorService;
import io.renren.service.yunying.dayreport.DmReportPotVipService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 潜力VIP数据表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-07 16:24:59
 */
@Controller
@RequestMapping("dmreportpotvip")
public class DmReportPotVipController {
	@Autowired
	private DmReportPotVipService dmReportPotVipService;
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="潜力VIP数据";
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportpotvip:list")
	public R list(Integer page, Integer limit,String statPeriod){
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(statPeriod)){
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		//查询列表数据
		List<DmReportPotVipEntity> dmReportPotVipList = dmReportPotVipService.queryList(map);
		int total = dmReportPotVipService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportPotVipList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("dmreportpotvip:list")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		// 查询列表数据
		List<DmReportPotVipEntity> dataList = dmReportPotVipService.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportPotVipEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportPotVipService.getExcelFields();

		String title = "潜力VIP数据";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dmreportpotvip:info")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DmReportPotVipEntity dmReportPotVip = dmReportPotVipService.queryObject(statPeriod);
		
		return R.ok().put("dmReportPotVip", dmReportPotVip);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dmreportpotvip:save")
	public R save(@RequestBody DmReportPotVipEntity dmReportPotVip){
		dmReportPotVipService.save(dmReportPotVip);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dmreportpotvip:update")
	public R update(@RequestBody DmReportPotVipEntity dmReportPotVip){
		dmReportPotVipService.update(dmReportPotVip);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dmreportpotvip:delete")
	public R delete(@RequestBody String[] statPeriods){
		dmReportPotVipService.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
