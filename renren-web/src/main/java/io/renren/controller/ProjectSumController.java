package io.renren.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.ProjectSumEntity;
import io.renren.service.ProjectSumService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 项目总台帐
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 15:41:58
 */
@Controller
@RequestMapping("dmreportfinrepaymentsum")
public class ProjectSumController {
	@Autowired
	private ProjectSumService dmReportFinRepaymentsumService;
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="项目总台帐";

	static class Page{
		private int total;
		private List<?> rows;
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public List<?> getRows() {
			return rows;
		}
		public void setRows(List<?> rows) {
			this.rows = rows;
		}
		public Page(int total, List<?> rows) {
			super();
			this.total = total;
			this.rows = rows;
		}

	}
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportfinrepaymentsum:list")
	public Page list(Map<String, Object> params, Integer page, Integer limit, String statPeriod,String sourcecaseno,
					 String customername,String giveoutmoneytime,String willgetmoneydate) {
		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		params.put("page", (page - 1) * limit);
		params.put("limit", limit);
		params.put("statPeriod", statPeriod);
		params.put("sourcecaseno", sourcecaseno);
		params.put("customername", customername);
		params.put("giveoutmoneytime", giveoutmoneytime);
		params.put("willgetmoneydate", willgetmoneydate);

		//查询列表数据
		Query query = new Query(params);
		int total = dmReportFinRepaymentsumService.queryTotal(params);
		//查询列表数据
		List< ProjectSumEntity> dmReportDailyDataList = dmReportFinRepaymentsumService.queryList(query);

		Page page1 = new Page(total, dmReportDailyDataList);
		return page1;


	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");


		Map<String,Object> map = JSON.parseObject(params, Map.class);
//		String statPeriod = map.get("statPeriod") + "";
//		if (StringUtils.isNotEmpty(statPeriod)) {
//			map.put("statPeriod", statPeriod);
//		}
		List<ProjectSumEntity> ProjectSumList = dmReportFinRepaymentsumService.queryList(map);
		JSONArray va = new JSONArray();

		for (int i = 0; i < ProjectSumList.size(); i++) {
			ProjectSumEntity entity = ProjectSumList.get(i);
			va.add(entity);
		}

		Map<String, String> headMap = dmReportFinRepaymentsumService.getExcelFields();

		String title = "项目总台帐";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

	
}
