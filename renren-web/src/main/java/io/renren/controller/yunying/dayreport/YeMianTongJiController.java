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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.EasyUiPage;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;

@Controller
@RequestMapping(value = "/yunying/yemiantongji")
public class YeMianTongJiController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="页面访问统计";

	
	/**
	 * 页面访问统计
	 */
	@ResponseBody
//	@RequestMapping("/list")
	@RequestMapping(value = "/list", method = RequestMethod.POST) 
	public EasyUiPage daylist(@RequestBody Map<String, Object> params) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		String stat_time=params.get("beginTime")+"";
		String end_time=params.get("endTime")+"";
		String bianhao=params.get("bianhao")+"";
		List<Map<String, Object>> num = (List<Map<String, Object>>) params.get("table_num");
		List<Map<String, Object>> sum = (List<Map<String, Object>>) params.get("table_sum");
		List<String> paramsList = new ArrayList<>();
		System.out.println(num);
		for (int i = 0; i < num.size(); i++) {
			Map<String, Object> map = num.get(i);
			Object object = map.get("Input");
			Object object2 = map.get("Select");
			if (object== null||"".equals(object.toString().trim())) {	
				continue;
			}else {
				
			if ("包含".equals(object2)) {
				String tianjian= "AND CURURL like ('%"+object+"%') ";
				System.out.println(tianjian);
				paramsList.add(tianjian);
			}else {
				String tianjian2= "AND CURURL not like ('%"+object+"%') ";
				System.out.println(tianjian2);
				paramsList.add(tianjian2);
			}
		
		}
		}
		
		for (int i = 0; i < sum.size(); i++) {
			Map<String, Object> map = sum.get(i);
			Object object = map.get("Input");
			Object object2 = map.get("Select");
			if (object== null||"".equals(object.toString().trim())) {
				continue;
			}else {
			if ("包含".equals(object2)) {
				String tianjian= "AND PREURL like ('%"+object+"%') ";
				System.out.println(tianjian);
				paramsList.add(tianjian);
			}else {
				String tianjian2= "AND PREURL not like ('%"+object+"%') ";
				System.out.println(tianjian2);
				paramsList.add(tianjian2);
			}
		}
		}
		
		int page = 1;
	    int limit = 100;
		if (StringUtils.isNotEmpty(stat_time)) {
			stat_time = stat_time.replace("-", "");
			end_time = end_time.replace("-", "");
			 String time="AND TIME_DAY BETWEEN " +stat_time + " AND " + end_time;
			 paramsList.add(time);
		}
		
		if (StringUtils.isNotEmpty(bianhao)) {
			bianhao="AND TYPE="+bianhao;
			paramsList.add(bianhao);
		}
		
		  
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String queryCond = "";
			for (int i = 0; i < paramsList.size(); i++) {
				queryCond += paramsList.get(i);
			}
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/页面访问统计.txt"));
			detail_sql = detail_sql.replace("${queryCond}", queryCond);
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

		System.err.println("++++++++页面访问统计数据查询耗时：" + (l2 - l1));
		
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
		Map<String,Object> map = JSON.parseObject(params, Map.class);
		String beginTime = map.get("beginTime") + "";
		String endTime = map.get("endTime") + "";
		String bianhao=map.get("bianhao")+"";
		EasyUiPage r=daylist(map);
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) r.getRows();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "页面访问统计数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("TIME_DAY", "日期");
		headMap.put("PV", "PV");
		headMap.put("UV", "UV");
		return headMap;

	}





}
