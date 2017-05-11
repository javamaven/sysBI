package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DailyEntity;
import io.renren.entity.UserBehaviorEntity;
import io.renren.service.DailyService;
import io.renren.service.LabelTagManagerService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;

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
	//private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 列表
	 */
	@RequestMapping("/black")
	 @RequiresPermissions("curly:list")
	public R list(@RequestBody Map<String, Object> params) {

		//获取用户ID
		long ID = getUserId();
		// 获取用户账号
		String userName = labelTagManagerService.querySysUser(ID);
		UserBehaviorEntity logUserBehavior = new UserBehaviorEntity();
		logUserBehavior.setUserName(userName);
		logUserBehavior.setCreateTime(new Date());
		logUserBehavior.setTYPE("查看");
		logUserBehavior.setReportType(""+reportType);
		logUserBehavior.setEXECSQL("\tDROP TABLE IF EXISTS TMP;\n" +
				"\t\tCREATE TEMPORARY TABLE TMP AS\n" +
				"\t\tSELECT\n" +
				"\t\tSTAT_PERIOD,\n" +
				"\t\tINDICATORS_NAME,\n" +
				"\t\tindicators_value,\n" +
				"\t\tsequential,\n" +
				"\t\tcompared,\n" +
				"\t\tmonth_mean_value,\n" +
				"\t\tmonth_mean_value_than,\n" +
				"\t\tNUM\n" +
				"\t\tFROM test.DIM_REPORT_DAILY\n" +
				"\t\tWHERE 1=1 ");

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(logUserBehavior);



		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<DailyEntity> dmReportDailyDataList = dailyDataService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}

//	public R insert(@RequestBody Map<String, Map<String,Object>> map) {
//		long struUserId = getUserId();
//		// 获取用户账号
//		String userName = labelTagManagerService.querySysUser(struUserId);
//		UserBehaviorEntity logUserBehavior = new UserBehaviorEntity();
//		logUserBehavior.setUserName(userName);
//		logUserBehavior.setCreateTime(new Date());
//		logUserBehavior.setTYPE("查看");
//		logUserBehavior.setReportType(""+reportType);
//		logUserBehavior.setEXECSQL("000");
//
//
//		return R.ok();
//	}

	@ResponseBody
	@RequestMapping("/partExport")
	@RequiresPermissions("curly:list")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {
		//获取用户ID
		long ID = getUserId();
		// 获取用户账号
		String userName = labelTagManagerService.querySysUser(ID);
		UserBehaviorEntity logUserBehavior = new UserBehaviorEntity();
		logUserBehavior.setUserName(userName);
		logUserBehavior.setCreateTime(new Date());
		logUserBehavior.setTYPE("导出");
		logUserBehavior.setReportType(""+reportType);
		logUserBehavior.setEXECSQL("ddd ");
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(logUserBehavior);


		List<DailyEntity> DailyList = dailyDataService.queryExports();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < DailyList.size() ; i++) {
			DailyEntity DailyUser = new DailyEntity();
			DailyUser.setStatPeriod(DailyList.get(i).getStatPeriod());
			DailyUser.setIndicatorsName(DailyList.get(i).getIndicatorsName());
			DailyUser.setIndicatorsValue(DailyList.get(i).getIndicatorsValue());
			DailyUser.setSequential(DailyList.get(i).getSequential());
			DailyUser.setCompared(DailyList.get(i).getCompared());
			DailyUser.setMonthMeanValue(DailyList.get(i).getMonthMeanValue());
			DailyUser.setMonthMeanValueThan(DailyList.get(i).getMonthMeanValueThan());
			va.add(DailyUser);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("statPeriod","日期");
		headMap.put("indicatorsName","指标名字");
		headMap.put("indicatorsValue","指标值");
		headMap.put("sequential","环比");
		headMap.put("compared","同比");
		headMap.put("monthMeanValue","30天均值");
		headMap.put("monthMeanValueThan","30天均值比");

		String title = "日报指标汇总";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}



}
