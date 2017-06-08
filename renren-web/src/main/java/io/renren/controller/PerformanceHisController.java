package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DepositoryTotalEntity;
import io.renren.entity.PerformanceHisEntity;
import io.renren.service.PerformanceHisService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 历史绩效发放记录
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:54
 */
@Controller
@RequestMapping("dmreportperformledgerhis")
public class PerformanceHisController {
	@Autowired
	private PerformanceHisService dmReportPerformLedgerHisService;
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="历史绩效发放记录表";

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
	@RequiresPermissions("dmreportperformledgerhis:list")
	public Page list(Map<String, Object> params, Integer page, Integer limit, String statPeriod) {

		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		params.put("page", (page - 1) * limit);
		params.put("limit", limit);
		params.put("statPeriod", statPeriod);
		//查询列表数据
		Query query = new Query(params);
		int total = dmReportPerformLedgerHisService.queryTotal(params);
		//查询列表数据
		List< PerformanceHisEntity> dmReportDailyDataList = dmReportPerformLedgerHisService.queryList(query);

		Page page1 = new Page(total, dmReportDailyDataList);
//		PageUtils pageUtils = new PageUtils(dmReportDailyDataList, total, query.getLimit(), query.getPage());
//		return R.ok().put("page", page1);
		return page1;


	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		List<PerformanceHisEntity> PerformanceHisList = dmReportPerformLedgerHisService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < PerformanceHisList.size() ; i++) {
			PerformanceHisEntity PerformanceHis = new PerformanceHisEntity();
			PerformanceHis.setStatPeriod(PerformanceHisList.get(i).getStatPeriod());
			PerformanceHis.setDevelopmanagername(PerformanceHisList.get(i).getDevelopmanagername());
			PerformanceHis.setDepartment(PerformanceHisList.get(i).getDepartment());

			PerformanceHis.setExpectedPerformance(PerformanceHisList.get(i).getExpectedPerformance());
			PerformanceHis.setActualPerformance(PerformanceHisList.get(i).getActualPerformance());


			va.add(PerformanceHis);
		}
		Map<String, String> headMap = dmReportPerformLedgerHisService.getExcelFields();

		String title = "历史绩效发放记录";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}


}
