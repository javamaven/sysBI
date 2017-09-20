package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/Bgdaishou")
public class BgDaishouController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="待收";

	
	/**
	 *待收
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(@RequestBody Map<String, Object> params) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(), new Date(), "查看", reportType, " ");

		String startDay = params.get("startDay") + "";
		String endDay = params.get("endDay") + "";
		if (StringUtils.isNotEmpty(startDay)) {
			startDay = startDay.replace("-", "");
		}
		if (StringUtils.isNotEmpty(endDay)) {
			endDay = endDay.replace("-", "");
		}

		List<List<Map<String, Object>>> resultList = new ArrayList<>();
		List<String> dayList = new ArrayList<>();
		dayList.add(startDay);
		String currDay = "";
		currDay = startDay;
		while (true) {
			if(currDay.equals(endDay)){
				break;
			}
			currDay = DateUtil.getCurrDayBefore(currDay, -1, "yyyyMMdd");
			dayList.add(currDay);
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			for (int i = 0; i < dayList.size(); i++) {
				String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/test.txt"));
				detail_sql = detail_sql.replace("${day}", dayList.get(i));
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.ok().put("data", resultList);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String invest_end_time = map.get("invest_end_time") + "";
		String invest_month_time = map.get("invest_month_time") + "";
		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/yxP2P.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${investMonthTime}", invest_month_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "越秀P2P数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TYPE", "分类");
		headMap.put("NUM", "人数(穿透)");
		headMap.put("SUM", "借款余额(穿透)");
		headMap.put("BORROW_USER", "人数(非穿透)");
		headMap.put("BORROW_CAPITAL", "借款余额(非穿透)");
		headMap.put("NUMM", "人数(总)");
		headMap.put("SUMM", "借款余额(总)");
		headMap.put("AVGG", "人均借款余额(万)");
		headMap.put("NUMS", "出借人数(总)");
		headMap.put("AVGS", "平均借款期限(天)");
		headMap.put("AVGLI", "平均借款利率(万)");
		headMap.put("YUQI", "逾期");
		return headMap;

	}

	@ResponseBody
	@RequestMapping("/ZDS")
	public R ZDS() {
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/Ds.txt"));
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long l2 = System.currentTimeMillis();

		System.err.println("++++++++查询耗时：" + (l2 - l1));
		return R.ok().put("data", resultList);
	}
	



}
