package io.renren.controller.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.entity.schedule.ScheduleReportTaskLogEntity;
import io.renren.service.schedule.ScheduleReportTaskLogService;
import io.renren.system.jdbc.JdbcHelper;
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
	private DruidDataSource dataSource;

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

	public void exportExcelX(String id, OutputStream out) {
		String path = "";
		String fileName = null;
		String fieldName = null;
		String querySql = "select file from schedule_report_task_log where id=" + id;
		FileOutputStream outputImage = null;
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		try {
			jdbcHelper.readImg(path, fileName, fieldName, querySql, outputImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Web导出后台方法 Controller调用
	 */
	public void downloadExcelFile(String id, String title, HttpServletResponse response) {

	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(int id, String title, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			exportExcelX(id + "", os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
