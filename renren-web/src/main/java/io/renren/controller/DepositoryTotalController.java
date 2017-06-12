package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DepositoryTotalEntity;
import io.renren.service.DepositoryTotalService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
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
 * 存管报备总表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-01 14:47:27
 */
@Controller
@RequestMapping("dmreportcgreport")
public class DepositoryTotalController {
	@Autowired
	private DepositoryTotalService dmReportCgReportService;

	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="存管报备总表";
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
	@RequiresPermissions("dmreportcgreport:list")

	public Page list(Map<String, Object> params, Integer page, Integer limit, String statPeriod,String sourcecaseno,
					 String customername,String iscompleted,String isstamp ) {

		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		params.put("page", (page - 1) * limit);
		params.put("limit", limit);
		params.put("statPeriod", statPeriod);
		params.put("sourcecaseno", sourcecaseno);
		params.put("customername", customername);
		params.put("iscompleted", iscompleted);
		params.put("isstamp", isstamp);
		//查询列表数据
		Query query = new Query(params);
		int total = dmReportCgReportService.queryTotal(params);
		//查询列表数据
		List< DepositoryTotalEntity> dmReportDailyDataList = dmReportCgReportService.queryList(query);

		Page page1 = new Page(total, dmReportDailyDataList);
		return page1;

	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params,HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		Map<String,Object> map = JSON.parseObject(params, Map.class);
		List<DepositoryTotalEntity> PerformanceHisList = dmReportCgReportService.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < PerformanceHisList.size(); i++) {
			DepositoryTotalEntity entity = PerformanceHisList.get(i);
			va.add(entity);
		}

		Map<String, String> headMap = dmReportCgReportService.getExcelFields();

		String title = "存管报备总表";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);

	}

}
