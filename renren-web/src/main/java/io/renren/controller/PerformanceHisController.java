package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DepositoryTotalEntity;
import io.renren.entity.PerformanceHisEntity;
import io.renren.service.PerformanceHisService;
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
import java.util.List;
import java.util.Map;


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
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportperformledgerhis:list")
	public R list(@RequestBody Map<String, Object> params) {


		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List< PerformanceHisEntity> dmReportDailyDataList = dmReportPerformLedgerHisService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


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
