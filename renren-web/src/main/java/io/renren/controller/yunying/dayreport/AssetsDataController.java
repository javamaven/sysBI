package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/assets")
public class AssetsDataController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="资产供应与成交";

	
	/**
	 * 用户注册来源
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit ,String period ,String sortorder ,String order) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String paixu="";
		if (StringUtils.isNotEmpty(sortorder)) {
			paixu="ORDER BY "+sortorder +" "+order;
		}
		
		
		String month="";
		
		if (StringUtils.isNotEmpty(period)) {
			period = period.replace("-", "");
			month = period.substring(0,6);
		}
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
//			DecimalFormat df = new DecimalFormat("#,###.00"); 
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/资产供应与成交.txt"));
			detail_sql = detail_sql.replace("${month}", month);
			detail_sql = detail_sql.replace("${period}", period);
			detail_sql = detail_sql.replace("${paixu}", paixu);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
//			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> map = list.get(i);
//				map.get(key)
//			}
//			
			
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("+++++++资产供应与成交数据查询耗时：" + (l2 - l1));
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
		String period = map.get("period") + "";
		String sortorder=map.get("sortorder")+"";
		String order=map.get("order")+"";
		R r=daylist(1, 1000000, period,sortorder , order);
		PageUtils pageUtil = (PageUtils) r.get("page");	
		
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "用户注册来源";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("PERIOD", "资产期限");
		headMap.put("M_PUB", "当月资产供应");
		headMap.put("SUPPLY_RATE", "期限占比");
		headMap.put("D_PUB", "当日资产供应");
		headMap.put("M_PUB_FIN", "供应理财计划资产");
		headMap.put("SUPPLY_FIN_RATE", "期限占比");
		headMap.put("M_INV", "当月成交");
		
		headMap.put("INV_RATE", "期限占比");
		headMap.put("D_INV", "当日成交");
		headMap.put("M_SAN", "当月散标成交");
		headMap.put("D_SAN", "当日散标成交");
		headMap.put("M_FIN", "当月理财计划成交");
		headMap.put("D_FIN", "当日理财计划成交");
	

		return headMap;

	}





}
