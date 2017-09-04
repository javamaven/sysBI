package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/nine")
public class NextNineDaysController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="未来回款与理财计划解锁情况";
	

	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String end_time,String begin_time) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String beforeDay="";
		String beforeNineDay="";
		String beforeDaywu="";
		int sumday=0;
		int oneDay=0; 
		String beforeOneDay="";
		String day="";
		if (StringUtils.isNotEmpty(begin_time)) {
//			beforeDay = begin_time.replace("-", "");
			beforeDay = DateUtil.getCurrDayBefore(begin_time, 1, "yyyy-MM-dd");
			beforeDaywu = beforeDay.replace("-", "");
			sumday=DateUtil.differentDaysByMillisecond(begin_time, end_time);
			day=sumday+"";
			oneDay=sumday-1;
			beforeOneDay=oneDay+"";
			beforeNineDay = DateUtil.getCurrDayBefore(beforeDay, oneDay, "yyyy-MM-dd");
			beforeNineDay=beforeNineDay.replace("-", "");
//			String lastyeartime=Integer.parseInt(lastYearTime)-1+lastYearTime2;
		}

		
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/nextNineDays.txt"));
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${day}", day);
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${beforeDay}", beforeDay);
			detail_sql = detail_sql.replace("${beforeNineDay}", beforeNineDay);
			detail_sql = detail_sql.replace("${beforeDaywu}", beforeDaywu);
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

		System.err.println("++++++++未来回款情况查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}


	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String begin_time = map.get("begin_time") + "";
		String end_time = map.get("end_time") + "";


		long l1 = System.currentTimeMillis();
      
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

	
		
		
		String beforeDay="";
		String beforeNineDay="";
		String beforeDaywu="";
		int sumday=0;
		int oneDay=0;
		String beforeOneDay="";
		String day="";
		if (StringUtils.isNotEmpty(begin_time)) {
//			beforeDay = begin_time.replace("-", "");
			beforeDay = DateUtil.getCurrDayBefore(begin_time, 1, "yyyy-MM-dd");
			beforeDaywu = beforeDay.replace("-", "");
			sumday=DateUtil.differentDaysByMillisecond(begin_time, end_time);
			day=sumday+"";
			oneDay=sumday-1;
			beforeOneDay=oneDay+"";
			beforeNineDay = DateUtil.getCurrDayBefore(beforeDay, oneDay, "yyyy-MM-dd");
			beforeNineDay=beforeNineDay.replace("-", "");
//			String lastyeartime=Integer.parseInt(lastYearTime)-1+lastYearTime2;
		}



		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/nextNineDays.txt"));
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${day}", day);
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${beforeDay}", beforeDay);
			detail_sql = detail_sql.replace("${beforeNineDay}", beforeNineDay);
			detail_sql = detail_sql.replace("${beforeDaywu}", beforeDaywu);
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
		String title = "未来回款情况";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME", "回款日期");
		headMap.put("PTB_REPAY_ACCOUNT_WAIT", "普通版回款");
		headMap.put("CGB_REPAY_ACCOUNT_WAIT", "存管版回款");
		headMap.put("REPAY_ACCOUNT_WAIT", "总回款");
		headMap.put("UNLOCK_MONEY", "理财计划解锁金额");
//		headMap.put("LJ_UNLOCK_MONEY", "累计解锁未退出金额");
		return headMap;

	}


	
}
