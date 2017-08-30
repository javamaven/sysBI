package io.renren.controller.hr;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.hr.DimStaffAttendanceEntity;
import io.renren.entity.hr.DimStaffEntity;
import io.renren.service.hr.DimStaffAttendanceService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MapUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;


/**
 * 考勤信息记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-29 10:08:21
 */
@Controller
@RequestMapping("/hr/worktime/")
public class DimStaffAttendanceController {
	@Autowired
	private DimStaffAttendanceService dimStaffAttendanceService;
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	DruidDataSource dataSource;
	
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	public void batchInsertIo(List<List<Object>> dataList) {
		try {

			int COMMIT_SIZE = 3000;
			String DATA = "";
			Connection conn = new JdbcHelper(dataSource).getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("load data local infile 'sql.csv' into table DIM_STAFF_ATTENDANCE_tmp fields terminated by ','");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < dataList.size(); i++) {
				List<Object> list = dataList.get(i);
				for (int j = 0; j < list.size(); j++) {
					if(j == list.size() - 1){
						sb.append(list.get(j) + "\n");
					}else{
						sb.append(list.get(j) + ",");
					}
				}
				if (i % COMMIT_SIZE == 0) {
					InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
					((com.mysql.jdbc.Statement) pstmt).setLocalInfileInputStream(is);
					pstmt.execute();
					conn.commit();
					sb.setLength(0);
				}
			}
			InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
			((com.mysql.jdbc.Statement) pstmt).setLocalInfileInputStream(is);
			pstmt.execute();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 上传文件
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		long t0 = System.currentTimeMillis();
		int total_record = 0;
		int sucess_record = 0;
		List<String> errorList = new ArrayList<String>();
		List<List<Object>> insertList = new ArrayList<>();
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String[] fields = { "realname", "post","attend_time", "clock_time", "clock_result"};
			
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");
			total_record = list.size();//导入总行数
//			UUID randomUUID = UUID.randomUUID();
			int batch = 0;
			List<Map<String, Object>> maxList = new JdbcHelper(dataSource).query("select IFNULL(max(batch),0) BATCH from DIM_STAFF_ATTENDANCE");
			if(maxList != null && maxList.size() > 0){
				Map<String, Object> map = maxList.get(0);
				batch = Integer.parseInt(map.get("BATCH")+"") + 1;
			}
			String[] items = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				try {
					List<Object> data = new ArrayList<>();
					Map<String, Object> map = list.get(i);
					map.put("batch", batch);
					String realname = MapUtil.getValue(map, "realname");
					String post = MapUtil.getValue(map, "post");
					String attend_time = MapUtil.getValue(map, "attend_time");
					String clock_time = MapUtil.getValue(map, "clock_time");
					String clockResult = MapUtil.getValue(map, "clock_result");
					Date attend_time_date = null;
					Date clock_time_date = null;
					if(StringUtils.isNotEmpty(attend_time)){
						attend_time += ":00";
						java.util.Date parse = dateSdf.parse(attend_time);
						attend_time_date = new Date(parse.getTime());
					}
					items[i] = realname + "^" + post + "^" + attend_time;
					if(StringUtils.isNotEmpty(clock_time)){
//						clock_time += ":00";
						java.util.Date parse = dateSdf.parse(clock_time);
						clock_time_date =  new Date(parse.getTime());
					}
					map.put("attend_time", attend_time_date);
					map.put("clock_time", clock_time_date);
					
					data.add(realname);
					data.add(post);
					data.add(attend_time_date);
					data.add(clock_time_date);
					data.add(clockResult);
					data.add(batch);
					insertList.add(data);
//					new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql, realname, post, attend_time_date);
//					DimStaffAttendanceEntity entity = new DimStaffAttendanceEntity(realname, post, clockResult, 
//							attend_time, clock_time);
					
					
					/*
//					DimStaffEntity dimStaff = new DimStaffEntity(realname, cardId, phone, department, part, post, ifBoss,
//							workTime, leaveTime);
					//1。没有则insert
					//2.已存在则update
					if(StringUtils.isEmpty(realname) || StringUtils.isEmpty(post) || StringUtils.isEmpty(attend_time)){
						continue;
					}
					DimStaffAttendanceEntity queryObject = dimStaffAttendanceService.queryObject(entity);
//					List<Map<String, Object>> dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(selectSql, realname, post, attend_time_date);
//					if(dataList == null || dataList.size() == 0){
//						dimStaffService.save(dimStaff);
					if(queryObject == null){
						dimStaffAttendanceService.save(entity);
						//插入到41oracle
						new JdbcUtil(dataSourceFactory, "oracle26").execute(insertSql, realname, post,
								attend_time_date, clock_time_date, clockResult);
					}else{
						dimStaffAttendanceService.update(entity);
						new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql, realname, post, attend_time_date);
						DimStaffAttendanceEntity queryObject_new = dimStaffAttendanceService.queryObject(entity);
						new JdbcUtil(dataSourceFactory, "oracle26").execute(insertSql, queryObject_new.getRealname(), queryObject_new.getPost(),
								queryObject_new.getAttendTime(), queryObject_new.getClockTime(), queryObject_new.getClockResult());
					}
					*/
					sucess_record++;
				} catch (Exception e) {
					errorList.add(e.getMessage());
//					e.printStackTrace();
					continue;
				}
			}
//			long t1 = System.currentTimeMillis();
//			dimStaffAttendanceService.deleteBatch(items);
//			long t2 = System.currentTimeMillis();
//			dimStaffAttendanceService.batchInsert(list);
//			batchInsertMysql(insertList);
			long t3 = System.currentTimeMillis();
			batchInsertOracle(insertList);
			long t4 = System.currentTimeMillis();
//			batchInsertIo(insertList);
//			long t5 = System.currentTimeMillis();
			new Thread(new OracleToMysql(dataSourceFactory)).start();
//			System.err.println("++++++++删除mysql耗时++++++++++" + (t2-t1));
//			System.err.println("++++++++插入mysql耗时++++++++++" + (t3-t2));
//			System.err.println("++++++++插入mysql耗时IO++++++++++" + (t5-t4));
			System.err.println("++++++++插入oracle耗时++++++++++" + (t4-t3));
			System.err.println("++++++++导入总耗时++++++++++" + (t4-t0));
			//insert into dim_staff_attendance (realname,post, attend_time, clock_time, clock_result) values ( ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ? )
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok().put("total_record", total_record).put("sucess_record", sucess_record).put("error_list", errorList);
	}
	
	class OracleToMysql implements Runnable{
		private List<List<Object>> insertList = new ArrayList<>();
		private DataSourceFactory dataSourceFactory;
		
		public OracleToMysql(DataSourceFactory dataSourceFactory){
			this.dataSourceFactory = dataSourceFactory;
		}

		@Override
		public void run() {
			long l1 = System.currentTimeMillis();
			String sql = "select * from dim_staff_attendance ";
			try {
				List<Map<String,Object>> dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(sql);
				for (int i = 0; i < dataList.size(); i++) {
					List<Object> list = new ArrayList<>();
					Map<String, Object> map = dataList.get(i);
					list.add(map.get("REALNAME"));
					list.add(map.get("POST"));
					list.add(map.get("ATTEND_TIME"));
					list.add(map.get("CLOCK_TIME"));
					list.add(map.get("CLOCK_RESULT"));
					list.add(map.get("BATCH"));
					insertList.add(list);
				}
				batchInsertMysql(insertList);
				long l2 = System.currentTimeMillis();
				System.err.println("++++oracle同步到mysql耗时+++" + (l2-l1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	private void batchDeleteOracle(String[] items) {
		// TODO Auto-generated method stub
		String deleteSql = "delete from dim_staff_attendance d where 1=1 and d.realname || '^' ||d.post || '^' || to_char(d.attend_time, 'yyyy-MM-dd hh24:mi:ss') in (";
		
		try {
			int delete_size = 0;
			String inCond = "";
			for (int i = 0; i < items.length; i++) {
				inCond += "'" + items[i] + "',";
				delete_size++;
				if(delete_size == 1000){
					inCond = inCond.substring(0, inCond.length() - 1);
					deleteSql += inCond + ")";
					System.err.println(deleteSql);
					new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql);
					delete_size = 0;
					inCond = "";
				}
			}
			if(delete_size > 0){
				inCond = inCond.substring(0, inCond.length() - 1);
				deleteSql += inCond + ")";
				System.err.println(deleteSql);
				new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void batchInsertOracle(List<List<Object>> dataList) {
		// TODO Auto-generated method stub
		String truncateSql = "truncate table dim_staff_attendance_tmp";
		String insertSql = "insert into dim_staff_attendance_tmp values(?,?,?,?,?,?) ";//插入临时表，然后执行sql更新数据
		String insertIntoSql = 
				"insert into DIM_STAFF_ATTENDANCE  " +
				"select * from DIM_STAFF_ATTENDANCE_TMP t where 1=1   " +
				"and not exists (  " +
				"    select d.realname || d.post || to_char(d.attend_time, 'yyyy-MM-dd hh24:mm:ss') from DIM_STAFF_ATTENDANCE d  " +
				"    where  d.realname || d.post || to_char(d.attend_time, 'yyyy-MM-dd hh24:mm:ss')=t.realname || t.post || to_char(t.attend_time, 'yyyy-MM-dd hh24:mm:ss')  " +
				")  ";
		try {
			new JdbcUtil(dataSourceFactory, "oracle26").execute(truncateSql);
			new JdbcUtil(dataSourceFactory, "oracle26").batchInsert(insertSql, dataList);
			new JdbcUtil(dataSourceFactory, "oracle26").execute(insertIntoSql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void batchInsertMysql(List<List<Object>> dataList) {
		// TODO Auto-generated method stub
		String insertSql = "insert into dim_staff_attendance values(?,?,?,?,?,?) ";
		try {
			
			new JdbcHelper(dataSource).execute("truncate table dim_staff_attendance");
			new JdbcHelper(dataSource).batchInsert(insertSql, dataList);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private File multipartToFile(MultipartFile multfile) throws IOException {
		CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
		// 这个myfile是MultipartFile的
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		// 手动创建临时文件
		if (file.length() < 2048) {
			File tmpFile = new File(
					System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + file.getName());
			multfile.transferTo(tmpFile);
			return tmpFile;
		}
		return file;
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String month){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		List<Map<String, Object>> dataList = queryOracleDataList("staff", month);
		PageUtils pageUtil = new PageUtils(dataList , dataList.size(), limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/partmentlist")
	public R partmentlist(Integer page, Integer limit, String month){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		List<Map<String, Object>> dataList = queryOracleDataList("partment", month);
		PageUtils pageUtil = new PageUtils(dataList , dataList.size(), limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 查询oracle的员工考勤信息dim_staff_attendance
	 * @param type staff：员工；partment：部门
	 * @param month 
	 * @return
	 */
	private List<Map<String, Object>> queryOracleDataList(String type, String month) {
		String path = this.getClass().getResource("/").getPath();
		String query_sql = null;
		List<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		try {
			if ("staff".equals(type)) {
				query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/人力资源/考勤信息-员工.txt"));
			} else if ("partment".equals(type)) {
				query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/人力资源/考勤信息-部门.txt"));
			}
			retList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, month);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{realname}")
	public R info(@PathVariable("realname") String realname){
//		DimStaffAttendanceEntity dimStaffAttendance = dimStaffAttendanceService.queryObject(realname);
		
		return R.ok().put("dimStaffAttendance", null);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody DimStaffAttendanceEntity dimStaffAttendance){
		dimStaffAttendanceService.save(dimStaffAttendance);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody DimStaffAttendanceEntity dimStaffAttendance){
		dimStaffAttendanceService.update(dimStaffAttendance);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] realnames){
		dimStaffAttendanceService.deleteBatch(realnames);
		
		return R.ok();
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String month = map.get("month") + "";
		String type = map.get("type") + "";
		JSONArray va = new JSONArray();
		List<Map<String, Object>> dataList = queryOracleDataList(type, month);
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> entity = dataList.get(i);
			va.add(entity);
		}
		
		
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		
		getExcelFields(headMap, type);
		
		String title = "";
		if("staff".equals(type)){
			title = "员工"+month+"考勤信息";
		}else if("partment".equals(type)){
			title = "部门"+month+"考勤信息";
		}

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	private void getExcelFields(Map<String, String> headMap, String type) {
		if("staff".equals(type)){
			headMap.put("REALNAME", "员工姓名");
			headMap.put("DEPARTMENT", "部门");
			headMap.put("POST", "职位");
			
			headMap.put("IF_BOSS", "是否副总级及以上职位");
			
			headMap.put("IF_BOSS", "是否副总监及以上职位");
			headMap.put("OVERTIME", "月度总加班工时");
			headMap.put("DAY_OVERTIME", "日均加班工时");
			
			headMap.put("LATETIME", "月度总迟到工时");
			headMap.put("LATE_TIMES", "月度迟到次数");
			headMap.put("RANK", "加班工时排名");
		}else if("partment".equals(type)){
			headMap.put("DEPARTMENT", "部门");
			headMap.put("CLOCK_NUM", "考勤人数");
			
			headMap.put("M_OVERTIME", "月度总加班工时");
			headMap.put("M_PER_OVERTIME", "月度人均加班工时");
			
			headMap.put("D_PE_OVERTIME", "日人均加班工时");
			headMap.put("M_PER_LATETIME", "月度人均迟到工时");
			headMap.put("M_PER_LATE_TIMES", "月度人均迟到次数");
			
			headMap.put("MOST_OVERTIME_STAFF", "加班最长员工");
			headMap.put("RANK", "人均加班工时排名");
		}
		
	}
}
