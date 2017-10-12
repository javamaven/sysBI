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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MapUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.EasyUiPage;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/yunyingdaily")
public class YunYingDailyController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="运营每日数据";

	
	/**
	 * 运营每日数据列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public EasyUiPage daylist(Integer page, Integer limit, String beginTime, String endTime) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		page = 1;
	    limit = 100;
		if (StringUtils.isNotEmpty(beginTime)) {
			beginTime = beginTime.replace("-", "");
			endTime = endTime.replace("-", "");
			
		}
		   String time="AND do.STAT_PERIOD BETWEEN " +beginTime + " AND " + endTime;
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营每日数据.txt"));
			detail_sql = detail_sql.replace("${time}", time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++运营每日数据查询耗时：" + (l2 - l1));
//		return R.ok().put("page", pageUtil);
		
		   EasyUiPage pageObj = new EasyUiPage();
		    pageObj.setRows(resultList);
		    pageObj.setTotal(resultList.size());
		    return pageObj;
	}
	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String beginTime = map.get("beginTime") + "";
		String endTime = map.get("endTime") + "";
		EasyUiPage r=daylist(1, 1000000, beginTime,  endTime);
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) r.getRows();
		

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "运营每日数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("STAT_PERIOD", "日期");
		headMap.put("ADD_INV", "净增投资");
		headMap.put("INV", "投资金额");
		headMap.put("SAN_INV", "散标投资");
		headMap.put("TRAN_INV", "债转投资");
		headMap.put("FIN_INV", "理财计划投资");
		headMap.put("PROJECT_SCALE", "点点赚余额");
		headMap.put("NUM", "投资人数");
		
		headMap.put("INV_NUM", "投资次数");
		headMap.put("RE_AMOUNT", "充值金额");
		headMap.put("WI_AMOUNT", "提现金额");
		headMap.put("CT", "净充值");
		headMap.put("SAN_REPAY_CAPITAL", "散标回款本金");
		headMap.put("SAN_REPAY", "散标回款");
		headMap.put("EXITS", "理财计划退出本金");
		
		headMap.put("VOUCHE", "红包成本");
		headMap.put("INTEREST_EXTRA", "加息成本");
		headMap.put("REBATE_MONEY", "返利成本");
		headMap.put("AWAIT_NUM", "有待收人数");
		
		headMap.put("OLD_AWAIT", "旧版待收本金");
		headMap.put("AWAIT", "待收本金");
		headMap.put("REPAY_ACCOUNT_WAIT", "待收金额");
		headMap.put("BORROW_ACCOUNT", "底层资产成交");
		headMap.put("YEAR_BORROW_ACCOUNT", "底层资产年化");
		headMap.put("M_REPAY_ACCOUNT_WAIT", "本月新增年后还款待收");
		headMap.put("Y_REPAY_ACCOUNT_WAIT", "年后还款待收");
		
		headMap.put("SCHEDULE_MONEY", "可排期项目");
		headMap.put("NO_MATCH_CAPITIL_WAIT", "理财计划预约资金");
		
		
		

		return headMap;

	}





}
