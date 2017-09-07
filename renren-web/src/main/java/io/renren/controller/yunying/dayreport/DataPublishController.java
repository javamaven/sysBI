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

import com.alibaba.druid.sql.ast.statement.SQLIfStatement.Else;
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
@RequestMapping(value = "/yunying/pilu")
public class DataPublishController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="官网数据披露";


	
	/**
	 * 数据披露列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R daylist(Integer page, Integer limit, String invest_end_time,String invest_month_time) {
		
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		if (StringUtils.isNotEmpty(invest_month_time)) {
			invest_month_time = invest_month_time.replace("-", "");
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish.txt"));
			List<Map<String, Object>> list2 = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list2);
			String currDate =sdf.format(new Date());

			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			if (year!=-1) {
				if (year==2017) {
					for (int i = 7; i <= month; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();

					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
					}
				}else {
					for (int i = 7; i <= 12; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth((year-(year-2017)), i);
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
				}
			
			}
				if (year>2017) {
					int years=year;
					int months=0;
					for (int j = 0; j <years-2017; j++) {
					
						
						 year=year-(year-2018-j);//2018
						 if (year>=2018) {
							 if (year!=years) {
								 months=12;
							}else {
								  months=month;
							}
						 }
			 for (int i = 1; i <= months; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
				int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
				String nian1=nian+"";
				String yue1=yue+"";
				System.err.println("查询日期" + lastDayOfMonth);
//				String path = this.getClass().getResource("/").getPath();
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				detail_sql = detail_sql.replace("${nian1}", nian1);
				detail_sql = detail_sql.replace("${yue1}", yue1);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			}
				year=years+1;
			}
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++越秀P2P查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist")
	@RequiresPermissions("phonesale:list")
	public R daylist1(Integer page, Integer limit, String investEndTime) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {

			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectfuzhu.txt"));
			List<Map<String, Object>> list2 = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list2);
			String currDate =sdf.format(new Date());
			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			if (year!=-1) {
				if (year==2017) {
					for (int i = 7; i <= month; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
						String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();

					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectHK.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
					}
				}else {
					for (int i = 7; i <= 12; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth((year-(year-2017)), i);
						String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectfuzhuHK.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
				}
			
			}
				if (year>2017) {
					int years=year;
					int months=0;
					for (int j = 0; j <years-2017; j++) {
					
						
						 year=year-(year-2018-j);//2018
						 if (year>=2018) {
							 if (year!=years) {
								 months=12;
							}else {
								  months=month;
							}
						 }
			 for (int i = 1; i <= months; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
				int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
				int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
				String nian1=nian+"";
				String yue1=yue+"";
				System.err.println("查询日期" + lastDayOfMonth);
//				String path = this.getClass().getResource("/").getPath();
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
				detail_sql = detail_sql.replace("${nian1}", nian1);
				detail_sql = detail_sql.replace("${yue1}", yue1);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			}
				year=years+1;
			}
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++项目情况-按还款方式查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}

	
	
	@ResponseBody
	@RequestMapping("/biaolilv")
	@RequiresPermissions("phonesale:list")
	public R biaolilv(Integer page, Integer limit, String investEndTime) {
		
		SimpleDateFormat sdff = new SimpleDateFormat("yyyyMMdd");
		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}
		
		String currDate =sdff.format(new Date());
		String lastDayOfMonth="";
		System.out.println(currDate);
		int nian=Integer.parseInt(currDate.substring(0,4));
		int yue=Integer.parseInt(currDate.substring(4,6));
		int day1=Integer.parseInt(currDate.substring(7,8));
		System.out.println("++++++++++"+day1+"++++++++++");
		if (day1<5) {
			yue=yue-1;
			 lastDayOfMonth = DateUtil.getLastDayOfMonth(nian, yue);
			 lastDayOfMonth=lastDayOfMonth.replace("-", "");
			 currDate=lastDayOfMonth;
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_biaolilv.txt"));
//			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${currDate}", currDate);
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

		System.err.println("++++++++表利率查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/biaoqixian")
	@RequiresPermissions("phonesale:list")
	public R biaoqixian(Integer page, Integer limit, String investEndTime) {
		
		SimpleDateFormat sdff = new SimpleDateFormat("yyyyMMdd");
		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}
		
		String currDate =sdff.format(new Date());
		String lastDayOfMonth="";
		System.out.println(currDate);
		int nian=Integer.parseInt(currDate.substring(0,4));
		int yue=Integer.parseInt(currDate.substring(4,6));
		int day1=Integer.parseInt(currDate.substring(7,8));
		System.out.println("++++++++++"+day1+"++++++++++");
		if (day1<5) {
			yue=yue-1;
			 lastDayOfMonth = DateUtil.getLastDayOfMonth(nian, yue);
			 lastDayOfMonth=lastDayOfMonth.replace("-", "");
			 currDate=lastDayOfMonth;
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/biao_qixian.txt"));
//			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${currDate}", currDate);
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

		System.err.println("++++++++表期限查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/jiekuan")
	@RequiresPermissions("phonesale:list")
	public R jiekuan(Integer page, Integer limit, String investEndTime) {
		
		SimpleDateFormat sdff = new SimpleDateFormat("yyyyMMdd");
		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}
		
		String currDate =sdff.format(new Date());
		String lastDayOfMonth="";
		System.out.println(currDate);
		int nian=Integer.parseInt(currDate.substring(0,4));
		int yue=Integer.parseInt(currDate.substring(4,6));
		int day1=Integer.parseInt(currDate.substring(7,8));
		System.out.println("++++++++++"+day1+"++++++++++");
		if (day1<5) {
			yue=yue-1;
			 lastDayOfMonth = DateUtil.getLastDayOfMonth(nian, yue);
			 lastDayOfMonth=lastDayOfMonth.replace("-", "");
			 currDate=lastDayOfMonth;
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_jiekuan.txt"));
//			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${currDate}", currDate);
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

		System.err.println("++++++++借款金额查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/touzi")
	@RequiresPermissions("phonesale:list")
	public R touzi(Integer page, Integer limit, String investEndTime) {
		
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotEmpty(investEndTime)) {
			investEndTime = investEndTime.replace("-", "");
		}
		
		String currDate =sdff.format(new Date());
		String lastDayOfMonth="";
		System.out.println(currDate);
		int nian=Integer.parseInt(currDate.substring(0,4));
		int yue=Integer.parseInt(currDate.substring(6,7));
		int day1=Integer.parseInt(currDate.substring(8,10));
		System.out.println("++++++++++"+day1+"++++++++++");
		if (day1<5) {
			yue=yue-1;
			 lastDayOfMonth = DateUtil.getLastDayOfMonth(nian, yue);
//			 lastDayOfMonth=lastDayOfMonth.replace("-", "");
			 currDate=lastDayOfMonth;
		}
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_touzi.txt"));
//			detail_sql = detail_sql.replace("${investEndTime}", invest_end_time);
			detail_sql = detail_sql.replace("${currDate}", currDate);
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

		System.err.println("++++++++投资人数查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/nianli")
	@RequiresPermissions("phonesale:list")
	public R nianlin(Integer page, Integer limit, String investEndTime) {

		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_nianlin.txt"));
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

		System.err.println("++++++++年龄分布查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/xingbie")
	@RequiresPermissions("phonesale:list")
	public R xingbie(Integer page, Integer limit, String investEndTime) {

		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_xingbie.txt"));
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

		System.err.println("++++++++性别分布查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/touzinianling")
	@RequiresPermissions("phonesale:list")
	public R touzinianling(Integer page, Integer limit, String investEndTime) {

		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_touzinianling.txt"));
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

		System.err.println("++++++++投资年龄分布查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	@ResponseBody
	@RequestMapping("/touzixingbie")
	@RequiresPermissions("phonesale:list")
	public R touzixingbie(Integer page, Integer limit, String investEndTime) {

		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/pilu_touzixingbie.txt"));
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

		System.err.println("++++++++投资性别性别分布查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/chanpin")
	public R chanpin(Integer page, Integer limit, String investEndTime) {

		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/chanpin.txt"));
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

		System.err.println("++++++++产品组合查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
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
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish.txt"));
			List<Map<String, Object>> list2 = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list2);
			String currDate =sdf.format(new Date());

			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			if (year!=-1) {
				if (year==2017) {
					for (int i = 7; i <= month; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();

					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
					}
				}else {
					for (int i = 7; i <= 12; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth((year-(year-2017)), i);
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
				}
			
			}
				if (year>2017) {
					int years=year;
					int months=0;
					for (int j = 0; j <years-2017; j++) {
					
						
						 year=year-(year-2018-j);//2018
						 if (year>=2018) {
							 if (year!=years) {
								 months=12;
							}else {
								  months=month;
							}
						 }
			 for (int i = 1; i <= months; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
				int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
				String nian1=nian+"";
				String yue1=yue+"";
				System.err.println("查询日期" + lastDayOfMonth);
//				String path = this.getClass().getResource("/").getPath();
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				detail_sql = detail_sql.replace("${nian1}", nian1);
				detail_sql = detail_sql.replace("${yue1}", yue1);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			}
				year=years+1;
			}
			}
			}
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
		String title = "累计成交量";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportMonthListExcel2(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {

			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectfuzhu.txt"));
			List<Map<String, Object>> list2 = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list2);
			String currDate =sdf.format(new Date());
			int year = Integer.parseInt(currDate.substring(0,4));
			int month = Integer.parseInt(currDate.substring(4,6));
			System.err.println(month);
			if (year!=-1) {
				if (year==2017) {
					for (int i = 7; i <= month; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
						String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();

					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectHK.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
					}
				}else {
					for (int i = 7; i <= 12; i++) {
						System.err.println("查询" + i + "月数据");
						String lastDayOfMonth = DateUtil.getLastDayOfMonth((year-(year-2017)), i);
						String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
						int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
						int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
						String nian1=nian+"";
						String yue1=yue+"";
						System.err.println("查询日期" + lastDayOfMonth);
//					String path = this.getClass().getResource("/").getPath();
					detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/projectfuzhuHK.txt"));
					detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
					detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
					detail_sql = detail_sql.replace("${nian1}", nian1);
					detail_sql = detail_sql.replace("${yue1}", yue1);
					List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
					resultList.addAll(list);
				}
			
			}
				if (year>2017) {
					int years=year;
					int months=0;
					for (int j = 0; j <years-2017; j++) {
					
						
						 year=year-(year-2018-j);//2018
						 if (year>=2018) {
							 if (year!=years) {
								 months=12;
							}else {
								  months=month;
							}
						 }
			 for (int i = 1; i <= months; i++) {
				System.err.println("查询" + i + "月数据");
				String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, i);
				String firstMonthDay=(lastDayOfMonth.substring(0,7))+"-01";
				int nian=Integer.parseInt(lastDayOfMonth.substring(0,4));
				int yue=Integer.parseInt(lastDayOfMonth.substring(5,7));
				String nian1=nian+"";
				String yue1=yue+"";
				System.err.println("查询日期" + lastDayOfMonth);
//				String path = this.getClass().getResource("/").getPath();
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/DataPublish2018.txt"));
				detail_sql = detail_sql.replace("${lastDayOfMonth}", lastDayOfMonth);
				detail_sql = detail_sql.replace("${firstMonthDay}", firstMonthDay);
				detail_sql = detail_sql.replace("${nian1}", nian1);
				detail_sql = detail_sql.replace("${yue1}", yue1);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			}
				year=years+1;
			}
			}
			}
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
		String title = "项目情况-按还款方式";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("YEAR", "年份");
		headMap.put("MONTH", "月份");
		headMap.put("SUM", "平台累计交易额（亿元）");
		return headMap;

	}	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("DMONTH", "年份");
		headMap.put("MONTH", "月份");
		headMap.put("TIAN", "按天付息");
		headMap.put("YUE", "按月付息");
		headMap.put("DAOQI", "到期还本还息");
		headMap.put("BENXI", "等额本息");
		return headMap;

	}


	
	
}
