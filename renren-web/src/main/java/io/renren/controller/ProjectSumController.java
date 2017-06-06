package io.renren.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DmReportFinRepaymentdetailEntity;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
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
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportfinrepaymentsum:list")
	public R list(@RequestBody Map<String, Object> params) {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<ProjectSumEntity> dmReportDailyDataList = dmReportFinRepaymentsumService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		List<ProjectSumEntity> ProjectSumList = dmReportFinRepaymentsumService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < ProjectSumList.size() ; i++) {
			ProjectSumEntity ProjectSum = new ProjectSumEntity();
			ProjectSum.setStatPeriod(ProjectSumList.get(i).getStatPeriod());
			ProjectSum.setOrgsimplename(ProjectSumList.get(i).getOrgsimplename());
			ProjectSum.setProducttype(ProjectSumList.get(i).getProducttype());
			ProjectSum.setSubproducttype(ProjectSumList.get(i).getSubproducttype());
			ProjectSum.setSourcecaseno(ProjectSumList.get(i).getSourcecaseno());


			ProjectSum.setDepartment(ProjectSumList.get(i).getDepartment());
			ProjectSum.setDevelopmanagername(ProjectSumList.get(i).getDevelopmanagername());
			ProjectSum.setWorkername(ProjectSumList.get(i).getWorkername());

			ProjectSum.setCustomername(ProjectSumList.get(i).getCustomername());
			ProjectSum.setPayformoney(ProjectSumList.get(i).getPayformoney());
			ProjectSum.setPayformoneyout(ProjectSumList.get(i).getPayformoneyout());

			ProjectSum.setLoanyearlimit(ProjectSumList.get(i).getLoanyearlimit());
			ProjectSum.setPayforlimittime(ProjectSumList.get(i).getPayforlimittime());
			ProjectSum.setGiveoutmoneytime(ProjectSumList.get(i).getGiveoutmoneytime());
			ProjectSum.setWillgetmoneydate(ProjectSumList.get(i).getWillgetmoneydate());


			ProjectSum.setTotalRateAmount(ProjectSumList.get(i).getTotalRateAmount());
			ProjectSum.setInterestRate(ProjectSumList.get(i).getInterestRate());
			ProjectSum.setOtherRate(ProjectSumList.get(i).getOtherRate());
			ProjectSum.setCapitalCost(ProjectSumList.get(i).getCapitalCost());

			ProjectSum.setOtherRateAmount(ProjectSumList.get(i).getOtherRateAmount());
			ProjectSum.setRemain(ProjectSumList.get(i).getRemain());
			ProjectSum.setReinterest(ProjectSumList.get(i).getReinterest());
			ProjectSum.setRebackmain(ProjectSumList.get(i).getRebackmain());
			ProjectSum.setRebackinterest(ProjectSumList.get(i).getRebackinterest());
			ProjectSum.setWaitCapital(ProjectSumList.get(i).getWaitCapital());

			ProjectSum.setWaitInterest(ProjectSumList.get(i).getWaitInterest());
			ProjectSum.setReamercedmoney3(ProjectSumList.get(i).getReamercedmoney3());
			ProjectSum.setReamercedmoney(ProjectSumList.get(i).getReamercedmoney());


			ProjectSum.setType(ProjectSumList.get(i).getType());
			ProjectSum.setCapitalSource(ProjectSumList.get(i).getCapitalSource());

			ProjectSum.setRealgetmoneydate(ProjectSumList.get(i).getRealgetmoneydate());
			ProjectSum.setRebackservice(ProjectSumList.get(i).getRebackservice());
			ProjectSum.setRepaymentWay(ProjectSumList.get(i).getRepaymentWay());
			ProjectSum.setReamercedmoney(ProjectSumList.get(i).getReamercedmoney());
			ProjectSum.setCarNoLocation(ProjectSumList.get(i).getCarNoLocation());

			ProjectSum.setCapitalDelistCompany(ProjectSumList.get(i).getCapitalDelistCompany());
			ProjectSum.setExchange1(ProjectSumList.get(i).getExchange1());
			ProjectSum.setExchange2(ProjectSumList.get(i).getExchange2());
			ProjectSum.setBorrowers(ProjectSumList.get(i).getBorrowers());



			va.add(ProjectSum);
		}
		Map<String, String> headMap = dmReportFinRepaymentsumService.getExcelFields();

		String title = "项目总台帐";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

	
}
