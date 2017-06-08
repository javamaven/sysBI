package io.renren.controller.schedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.schedule.ScheduleReportTaskEntity;
import io.renren.service.schedule.ScheduleReportTaskService;
import io.renren.util.CronExpressionUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 报表推送任务配置表
 * 
 */
@Controller
@RequestMapping("/schedule/schedulereporttask")
public class ScheduleReportTaskController {
	@Autowired
	private ScheduleReportTaskService scheduleReportTaskService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("schedulereporttask:list")
	public R list(Integer page, Integer limit, String taskName) {
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("taskNameLike", taskName);

		// 查询列表数据
		List<ScheduleReportTaskEntity> scheduleReportTaskList = scheduleReportTaskService.queryList(map);
		for (int i = 0; i < scheduleReportTaskList.size(); i++) {
			ScheduleReportTaskEntity entity = scheduleReportTaskList.get(i);
			try {
				if("0".equals(entity.getIsRunning())){
					entity.setNextRunTime("-");
					continue;
				}
				String time = CronExpressionUtil.nextRunTime(entity.getSendRate());
				entity.setNextRunTime(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int total = scheduleReportTaskService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(scheduleReportTaskList, total, limit, page);

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("schedulereporttask:info")
	public R info(@PathVariable("id") Integer id) {
		ScheduleReportTaskEntity scheduleReportTask = scheduleReportTaskService.queryObject(id);

		return R.ok().put("scheduleReportTask", scheduleReportTask);
	}

	/**
	 * 启动任务
	 */
	@ResponseBody
	@RequestMapping("/startTask")
	@RequiresPermissions("schedulereporttask:save")
	public R startTask(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTaskService.startTask(scheduleReportTask);
		return R.ok();
	}

	/**
	 * 停止任务
	 */
	@ResponseBody
	@RequestMapping("/stopTask")
	@RequiresPermissions("schedulereporttask:save")
	public R stopTask(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		scheduleReportTaskService.stopTask(scheduleReportTask);
		return R.ok();
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/existsTaskName")
	@RequiresPermissions("schedulereporttask:save")
	public R existsTaskName(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", scheduleReportTask.getTaskName());
		List<ScheduleReportTaskEntity> queryList = scheduleReportTaskService.queryList(map);
		return R.ok().put("total", queryList.size());
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/addTask")
	@RequiresPermissions("schedulereporttask:save")
	public R addTask(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", scheduleReportTask.getTaskName());
		String cron = scheduleReportTask.getSendRate();
		if (!CronExpressionUtil.isRight(cron)) {
			return R.ok().put("cron", "error");
		}

		List<ScheduleReportTaskEntity> queryList = scheduleReportTaskService.queryList(map);
		if (queryList.size() == 0) {
			scheduleReportTaskService.save(scheduleReportTask);
		}
		return R.ok().put("total", queryList.size());
	}
	
	/**
	 * 获取Crontab执行时间
	 */
	@ResponseBody
	@RequestMapping("/getRunTime")
	public R getRunTime(@RequestBody Map<String,Object> params) {
//		String selectType = params.get("selectType") + "";
		String date = params.get("date") + "";
		List<String> ret = new ArrayList<String>();
		try {
			ret = CronExpressionUtil.nextRunTimes(date, 5);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return R.ok().put("data", ret);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("schedulereporttask:save")
	public R save(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", scheduleReportTask.getTaskName());
		String cron = scheduleReportTask.getSendRate();
		if (!CronExpressionUtil.isRight(cron)) {
			return R.ok().put("cron", "error");
		}

		List<ScheduleReportTaskEntity> queryList = scheduleReportTaskService.queryList(map);
		if (queryList.size() > 0) {
			return R.ok().put("total", queryList.size());
		}
		scheduleReportTaskService.save(scheduleReportTask);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("schedulereporttask:update")
	public R update(@RequestBody ScheduleReportTaskEntity scheduleReportTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", (scheduleReportTask.getTaskName() + "").trim());
		map.put("existsName", scheduleReportTask.getId());
		String cron = scheduleReportTask.getSendRate();
		if (!CronExpressionUtil.isRight(cron)) {
			return R.ok().put("cron", "error");
		}

		List<ScheduleReportTaskEntity> queryList = scheduleReportTaskService.queryList(map);
		if (queryList.size() > 0) {
			return R.ok().put("total", queryList.size());
		}

		scheduleReportTaskService.update(scheduleReportTask);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("schedulereporttask:delete")
	public R delete(@RequestBody Integer[] ids) {
		scheduleReportTaskService.deleteBatch(ids);

		return R.ok();
	}

}
