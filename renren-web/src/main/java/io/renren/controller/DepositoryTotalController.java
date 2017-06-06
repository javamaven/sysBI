package io.renren.controller;

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
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportcgreport:list")
	public R list(@RequestBody Map<String, Object> params) {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");


		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<DepositoryTotalEntity> dmReportDailyDataList = dmReportCgReportService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		List<DepositoryTotalEntity> DepositoryTotalList = dmReportCgReportService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < DepositoryTotalList.size() ; i++) {
			DepositoryTotalEntity DepositoryTotal = new DepositoryTotalEntity();
			DepositoryTotal.setStatPeriod(DepositoryTotalList.get(i).getStatPeriod());
			DepositoryTotal.setSourcecaseno(DepositoryTotalList.get(i).getSourcecaseno());
			DepositoryTotal.setDepartment(DepositoryTotalList.get(i).getDepartment());


			DepositoryTotal.setProjectBelong(DepositoryTotalList.get(i).getProjectBelong());
			DepositoryTotal.setProjectType(DepositoryTotalList.get(i).getProjectType());
			DepositoryTotal.setCustomername(DepositoryTotalList.get(i).getCustomername());
			DepositoryTotal.setPayformoney(DepositoryTotalList.get(i).getPayformoney());
			DepositoryTotal.setLoanrate(DepositoryTotalList.get(i).getLoanrate());
			DepositoryTotal.setLoanyearlimit(DepositoryTotalList.get(i).getLoanyearlimit());
			DepositoryTotal.setPayforlimittime(DepositoryTotalList.get(i).getPayforlimittime());
			DepositoryTotal.setGiveoutmoneytime(DepositoryTotalList.get(i).getGiveoutmoneytime());
			DepositoryTotal.setWillgetmoneydate(DepositoryTotalList.get(i).getWillgetmoneydate());
			DepositoryTotal.setIscompleted(DepositoryTotalList.get(i).getIscompleted());
			DepositoryTotal.setSendDeadline(DepositoryTotalList.get(i).getSendDeadline());
			DepositoryTotal.setIsstamp(DepositoryTotalList.get(i).getIsstamp());


			va.add(DepositoryTotal);
		}
		Map<String, String> headMap = dmReportCgReportService.getExcelFields();

		String title = "存管报备总表";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

}
