package io.renren.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.PerformanceHisEntity;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.PerformanceParameterEntity;
import io.renren.service.PerformanceParameterService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 绩效台帐-分配表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:58
 */
@Controller
@RequestMapping("dmreportperformanceledger")
public class PerformanceParameterController {
	@Autowired
	private PerformanceParameterService dmReportPerformanceLedgerService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportperformanceledger:list")
	public R list(@RequestBody Map<String, Object> params) {


		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<PerformanceParameterEntity> dmReportDailyDataList = dmReportPerformanceLedgerService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<PerformanceParameterEntity> PerformanceParameterList = dmReportPerformanceLedgerService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < PerformanceParameterList.size() ; i++) {
			PerformanceParameterEntity PerformanceParameter = new PerformanceParameterEntity();
			PerformanceParameter.setStatPeriod(PerformanceParameterList.get(i).getStatPeriod());

			PerformanceParameter.setDevelopmanagername(PerformanceParameterList.get(i).getDevelopmanagername());
			PerformanceParameter.setDepartment(PerformanceParameterList.get(i).getDepartment());
			PerformanceParameter.setPayformoneyout(PerformanceParameterList.get(i).getPayformoneyout());
			PerformanceParameter.setGrossProfit(PerformanceParameterList.get(i).getGrossProfit());
			PerformanceParameter.setSalaryCost(PerformanceParameterList.get(i).getSalaryCost());
			PerformanceParameter.setReimbursement(PerformanceParameterList.get(i).getReimbursement());
			PerformanceParameter.setRentShare(PerformanceParameterList.get(i).getRentShare());
			PerformanceParameter.setNetMargin(PerformanceParameterList.get(i).getNetMargin());
			PerformanceParameter.setCommissionRatio(PerformanceParameterList.get(i).getCommissionRatio());
			PerformanceParameter.setAvailablePerformance(PerformanceParameterList.get(i).getAvailablePerformance());
			PerformanceParameter.setRiskReserve(PerformanceParameterList.get(i).getRiskReserve());
			PerformanceParameter.setSettledAmount(PerformanceParameterList.get(i).getSettledAmount());
			PerformanceParameter.setSettledAmtRate(PerformanceParameterList.get(i).getSettledAmtRate());
			PerformanceParameter.setExpectedPerformance(PerformanceParameterList.get(i).getExpectedPerformance());


			va.add(PerformanceParameter);
		}
		Map<String, String> headMap = dmReportPerformanceLedgerService.getExcelFields();

		String title = "绩效台帐";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

	
}
