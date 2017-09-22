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

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/tuhao")
public class TuHaoUserController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="高净值用户";

	
	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit) {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		int start = (page - 1) * limit;
		int end = start + limit;
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			Thread thread1 = new Thread(new QueryThread("list", dataSourceFactory, resultList, start, end));
			Thread thread2 = new Thread(new QueryThread("total", dataSourceFactory, totalList, 0,0));
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();
//		
//		int total = resultList.size();
//		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
//		long l2 = System.currentTimeMillis();

		System.err.println("+++++++高净值用户查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	
	class QueryThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;


		public QueryThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list, int start, int end) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				
				if("list".equals(statType)){
					
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/土豪用户_list.txt"));
					
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, start, end));
				}else if("total".equals(statType)){
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/土豪用户_total.txt"));
		
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		R r=daylist(1, 1000000);
		PageUtils pageUtil = (PageUtils) r.get("page");	
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) pageUtil.getList();

		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "高净值用户数据";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("USER_ID", "用户id");
		headMap.put("OLD_USER_ID", "旧版用户id");
		headMap.put("CG_USER_ID", "存管id");
		headMap.put("PHONE", "用户手机号");
		headMap.put("USERNAME", "用户名");
		headMap.put("REALNAME", "用户真实姓名");
		headMap.put("IS_STALL", "是否员工");
		
		headMap.put("SEX", "性别");
		headMap.put("REGISTER_TIME", "注册时间");
		headMap.put("TEND_TIMES", "投资总次数");
		headMap.put("DIF_DATE", "最近一次投资离现在天数");
		headMap.put("SUM_CAPITAL", "总投资金额");
		headMap.put("SUM_VOUCHE", "总使用红包金额");
		headMap.put("IS_VIP", "是否高净值");
		
		headMap.put("AGE", "用户年龄");
		headMap.put("ID_R_180", "近180天内到期待收比例");
		headMap.put("ID_R_30", "近30天内到期待收比例");
		headMap.put("R_30", "近30天内到期待收金额");
		headMap.put("IS_YLS", "是否容易流失");
		headMap.put("IS_NEW", "是否为新数据");
		headMap.put("AW", "提数日期最新待收");

		return headMap;

	}





}
