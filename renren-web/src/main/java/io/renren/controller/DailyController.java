package io.renren.controller;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.DailyEntity;
import io.renren.service.DailyService;
import io.renren.service.LabelTagManagerService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

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
	@Autowired
	private LabelTagManagerService labelTagManagerService;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="日报";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//	static class Page{
//		private int total;
//		private List<?> rows;
//		public int getTotal() {
//			return total;
//		}
//		public void setTotal(int total) {
//			this.total = total;
//		}
//		public List<?> getRows() {
//			return rows;
//		}
//		public void setRows(List<?> rows) {
//			this.rows = rows;
//		}
//		public Page(int total, List<?> rows) {
//			super();
//			this.total = total;
//			this.rows = rows;
//		}
//
//	}


	/**
	 * 列表
	 */

	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit,String begin_time, String end_time){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		
		
		//查询列表数据
		List<DailyEntity> dmReportDailyDataList = dailyDataService.queryList(map);
		int total = dailyDataService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDailyDataList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
//	public Page list(Map<String, Object> params, Integer page, Integer limit, String reg_begindate,String reg_enddate) {
//		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
//		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
//		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
//		params.put("page", (page - 1) * limit);
//		params.put("limit", limit);
//		params.put("reg_begindate", reg_begindate);
//		params.put("reg_enddate", reg_enddate);
//
//		//查询列表数据
//		Query query = new Query(params);
//
//		//查询列表数据
//		List< DailyEntity> dmReportDailyDataList = dailyDataService.queryList(query);
//		int total = dailyDataService.queryTotal(params);
//		Page page1 = new Page(total, dmReportDailyDataList);
//		return page1;
//
//
//	}

	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params,HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		Map<String,Object> map = JSON.parseObject(params, Map.class);
//
		List<DailyEntity> ProjectSumList = dailyDataService.queryList(map);
		JSONArray va = new JSONArray();

		for (int i = 0; i < ProjectSumList.size(); i++) {
			DailyEntity entity = ProjectSumList.get(i);
			va.add(entity);
		}

		Map<String, String> headMap = dailyDataService.getExcelFields();

		String title = "日报指标汇总";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}



}
