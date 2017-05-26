package io.renren.controller.schedule;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-19 10:19:30
 */
@Controller
@RequestMapping("/schedule/schedulereporttasklog")
public class ScheduleReportTaskLogController {
	@Autowired
	private ScheduleReportTaskLogService scheduleReportTaskLogService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("schedulereporttasklog:list")
	// public R list(Integer page, Integer limit){
	public R list(@RequestParam Map<String, Object> params) {

		Query query = new Query(params);

		// 查询列表数据
		List<ScheduleReportTaskLogEntity> scheduleReportTaskLogList = scheduleReportTaskLogService.queryList(query);
		int total = scheduleReportTaskLogService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(scheduleReportTaskLogList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("schedulereporttasklog:list")
	public R info(@PathVariable("id") Integer id) {
		ScheduleReportTaskLogEntity scheduleReportTaskLog = scheduleReportTaskLogService.queryObject(id);

		return R.ok().put("scheduleReportTaskLog", scheduleReportTaskLog);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("schedulereporttasklog:list")
	public R save(@RequestBody ScheduleReportTaskLogEntity scheduleReportTaskLog) {
		scheduleReportTaskLogService.save(scheduleReportTaskLog);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("schedulereporttasklog:list")
	public R update(@RequestBody ScheduleReportTaskLogEntity scheduleReportTaskLog) {
		scheduleReportTaskLogService.update(scheduleReportTaskLog);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("schedulereporttasklog:list")
	public R delete(@RequestBody Integer[] ids) {
		scheduleReportTaskLogService.deleteBatch(ids);

		return R.ok();
	}

}
