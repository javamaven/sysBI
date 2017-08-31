package io.renren.controller.shichang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.shichang.DmReportDalilyMarketingEntity;
import io.renren.service.shichang.DmReportDalilyMarketingService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-31 14:16:57
 */
@Controller
@RequestMapping("dmreportdalilymarketing")
public class DmReportDalilyMarketingController {
	@Autowired
	private DmReportDalilyMarketingService dmReportDalilyMarketingService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/dayList")
	public R dayList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
//		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryList(map);
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryDayList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/monthList")
	public R monthList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryMonthList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/totalList")
	public R totalList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryTotalList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DmReportDalilyMarketingEntity dmReportDalilyMarketing = dmReportDalilyMarketingService.queryObject(statPeriod);
		
		return R.ok().put("dmReportDalilyMarketing", dmReportDalilyMarketing);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingService.save(dmReportDalilyMarketing);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingService.update(dmReportDalilyMarketing);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] statPeriods){
		dmReportDalilyMarketingService.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
