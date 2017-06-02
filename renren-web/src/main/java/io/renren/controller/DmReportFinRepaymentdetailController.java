package io.renren.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.InsideLxEntity;
import io.renren.entity.MarketChannelEntity;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.service.DmReportFinRepaymentdetailService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 项目台帐明细
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 10:07:16
 */
@Controller
@RequestMapping("dmreportfinrepaymentdetail")
public class DmReportFinRepaymentdetailController {
	@Autowired
	private DmReportFinRepaymentdetailService dmReportFinRepaymentdetailService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportfinrepaymentdetail:list")
	public R list(@RequestBody Map<String, Object> params) {


		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<DmReportFinRepaymentdetailEntity> dmReportDailyDataList = dmReportFinRepaymentdetailService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<DmReportFinRepaymentdetailEntity> DmReportFinRepaymentdetailList = dmReportFinRepaymentdetailService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < DmReportFinRepaymentdetailList.size() ; i++) {
			DmReportFinRepaymentdetailEntity DmReportFinRepaymentdetail = new DmReportFinRepaymentdetailEntity();
			DmReportFinRepaymentdetail.setStatPeriod(DmReportFinRepaymentdetailList.get(i).getStatPeriod());
			DmReportFinRepaymentdetail.setSourcecaseno(DmReportFinRepaymentdetailList.get(i).getSourcecaseno());
			DmReportFinRepaymentdetail.setCustomername(DmReportFinRepaymentdetailList.get(i).getCustomername());
			DmReportFinRepaymentdetail.setPayformoney(DmReportFinRepaymentdetailList.get(i).getPayformoney());
			DmReportFinRepaymentdetail.setPayformoneyout(DmReportFinRepaymentdetailList.get(i).getPayformoneyout());
			DmReportFinRepaymentdetail.setTotpmts(DmReportFinRepaymentdetailList.get(i).getTotpmts());
			DmReportFinRepaymentdetail.setReindex(DmReportFinRepaymentdetailList.get(i).getReindex());
			DmReportFinRepaymentdetail.setPlanrepaydate(DmReportFinRepaymentdetailList.get(i).getPlanrepaydate());
			DmReportFinRepaymentdetail.setRealredate(DmReportFinRepaymentdetailList.get(i).getRealredate());
			DmReportFinRepaymentdetail.setRemain(DmReportFinRepaymentdetailList.get(i).getRemain());
			DmReportFinRepaymentdetail.setReinterest(DmReportFinRepaymentdetailList.get(i).getReinterest());
			DmReportFinRepaymentdetail.setRebackmain(DmReportFinRepaymentdetailList.get(i).getRebackmain());
			DmReportFinRepaymentdetail.setRebackinterest(DmReportFinRepaymentdetailList.get(i).getRebackinterest());
			DmReportFinRepaymentdetail.setReamercedmoney(DmReportFinRepaymentdetailList.get(i).getReamercedmoney());
			DmReportFinRepaymentdetail.setReamercedmoney3(DmReportFinRepaymentdetailList.get(i).getReamercedmoney3());
			DmReportFinRepaymentdetail.setRealgetmoneydate(DmReportFinRepaymentdetailList.get(i).getRealgetmoneydate());


			va.add(DmReportFinRepaymentdetail);
		}
		Map<String, String> headMap = dmReportFinRepaymentdetailService.getExcelFields();

		String title = "项目台帐明细";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}


}
