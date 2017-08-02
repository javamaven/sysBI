package io.renren.controller.yunying.analyse;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
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
import io.renren.util.NumberUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/analyse")
public class AnalyseController {
	
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;
	
	/**
	 * 净资金待收异动
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryDaishouYidong")
	public R queryDaishouYidong(@RequestBody Map<String, Object> params) {
		String reportType="当周待收用户分布";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/待收异动.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 大额提现用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryWithdrawuser")
	public R queryWithdrawuser(@RequestBody Map<String, Object> params) {
		String reportType="大额提现用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/大额提现用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 业务预警 列表查询
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBusinessAlarmList")
	public R queryBusinessAlarmList(Integer page, Integer limit, Map<String, Object> params, String statPeriod, String statType) {
		String reportType="业务预警分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		int start = (page - 1) * limit;
		int end = start + limit;
		String query_sql = "";
		String total_sql = "";
		List<Map<String, Object>> list = null;
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		String statTypeCond = "";
		if(StringUtils.isNotEmpty(statType)){
			statTypeCond = " and stat_type=" + statType;
		}
		int totalCount = 0;
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/业务预警列表.txt"));
			total_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/业务预警列表总行数.txt"));
			query_sql = query_sql.replace("${pageStart}", start + "");
			query_sql = query_sql.replace("${pageEnd}", end + "");
			query_sql = query_sql.replace("${statTypeCond}", statTypeCond);
			list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			
			total_sql = total_sql.replace("${statTypeCond}", statTypeCond);
			List<Map<String, Object>> totalList = new JdbcUtil(dataSourceFactory, "oracle26").query(total_sql, statPeriod);
			totalCount = Integer.parseInt(totalList.get(0).get("TOTAL") + "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PageUtils pageUtil = new PageUtils(list, totalCount, limit, page);

		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 业务预警
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBusinessAlarm")
	public R queryBusinessAlarm(@RequestBody Map<String, Object> params) {
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/业务预警.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 自然用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryZiranUserAnalyse")
	public R queryZiranUserAnalyse(@RequestBody Map<String, Object> params) {
		
		String reportType="自然用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/自然用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 高净值用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryChengshuUserAnalyse")
	public R queryChengshuUserAnalyse(@RequestBody Map<String, Object> params) {
		
		String reportType="成熟用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/成熟用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 沉默用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryNewUserAnalyse")
	public R queryNewUserAnalyse(@RequestBody Map<String, Object> params) {
		
		String reportType="新用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/新用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 沉默用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryChenmoUserAnalyse")
	public R queryChenmoUserAnalyse(@RequestBody Map<String, Object> params) {
		String reportType="沉默用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/沉默用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 高净值用户
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryHighUserAnalyse")
	public R queryHighUserAnalyse(@RequestBody Map<String, Object> params) {
		String reportType="高净值用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/高净值用户.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 大盘分析
	 * @param statPeriod
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryDapanAnalyse")
	public R queryDapanAnalyse(@RequestBody Map<String, Object> params) {
		String reportType="大盘分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		String query_sql = null;
		List<Map<String, Object>> dataList = null;
		String statPeriod = params.get("statPeriod") + "";
		if(StringUtils.isNotEmpty(statPeriod)){
			statPeriod = statPeriod.replace("-", "");
		}
		try {
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "sql/运营分析/大盘分析.txt"));
			
			String weekAgo = DateUtil.getCurrDayBefore(statPeriod,7, "yyyyMMdd");//一星期前
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, weekAgo);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql, statPeriod);
			dataList.add(list.get(0));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok().put("data_list", dataList);
	}
	
	/**
	 * 自然用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportZiranUser")
	public void exportZiranUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="自然用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R ziranUserData = queryZiranUserAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) ziranUserData.get("data_list");
		OutputStream os = null;
		String excelName = "自然用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("平台总投资用户"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1)); 
	        
	        HSSFCell c2 = row.createCell(2); 
	        c2.setCellValue(new HSSFRichTextString("平台总待收金额"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2)); 
	        
	        HSSFCell c3 = row.createCell(3); 
	        c3.setCellValue(new HSSFRichTextString("平台红包使用总金额"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3)); 
	        
	        HSSFCell c4 = row.createCell(4);   
	        c4.setCellValue(new HSSFRichTextString("自然用户数"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5)); 
	        
	        HSSFCell c6 = row.createCell(6);   
	        c6.setCellValue(new HSSFRichTextString("自然用户待收"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7)); 
	        
	        HSSFCell c8 = row.createCell(8);   
	        c8.setCellValue(new HSSFRichTextString("自然用户红包使用"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
	        
	        HSSFCell c10 = row.createCell(10); 
	        c10.setCellValue(new HSSFRichTextString("自然用户项目平均投资期限"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10)); 
	       
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(4);
	        c21.setCellValue(new HSSFRichTextString("自然用户数"));
	        HSSFCell c22 = row2.createCell(5);
	        c22.setCellValue(new HSSFRichTextString("占比"));
	        HSSFCell c23 = row2.createCell(6);
	        c23.setCellValue(new HSSFRichTextString("自然用户待收"));
	        HSSFCell c24 = row2.createCell(7);
	        c24.setCellValue(new HSSFRichTextString("占比"));
	        
	        HSSFCell c25 = row2.createCell(8);
	        c25.setCellValue(new HSSFRichTextString("自然用户红包使用"));
	        HSSFCell c26 = row2.createCell(9);
	        c26.setCellValue(new HSSFRichTextString("占比"));
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        c3.setCellStyle(style);
	        c4.setCellStyle(style);
	        c6.setCellStyle(style);
	        c8.setCellStyle(style);
	        c10.setCellStyle(style);
	        for (int i = 0; i < 11; i++) {
	            sheet.setColumnWidth(i, 17 * 256);//调整列宽
			}
        	String[] fields = {"日期", "平台总投资用户", "平台总待收金额", "平台红包使用总金额" , "自然用户数", "占比_自然用户数", "自然用户待收", 
					"占比_自然用户待收", "自然用户红包使用", "占比_自然用户红包使用", "自然用户项目平均投资期限"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else{
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 高净值用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportHighUser")
	public void exportHighUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="高净值用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R highUserData = queryHighUserAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) highUserData.get("data_list");
		OutputStream os = null;
		String excelName = "高净值用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("全量"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3)); 
	        HSSFCell c2 = row.createCell(4);   
	        c2.setCellValue(new HSSFRichTextString("[10W,20W)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6)); 
	        HSSFCell c3 = row.createCell(7);   
	        c3.setCellValue(new HSSFRichTextString("[20W,50W)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
	        
	        HSSFCell c4 = row.createCell(10);   
	        c4.setCellValue(new HSSFRichTextString("[50W,100W)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
	        
	        HSSFCell c5 = row.createCell(13);   
	        c5.setCellValue(new HSSFRichTextString("[100W,200W)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
	        
	        HSSFCell c6 = row.createCell(16);   
	        c6.setCellValue(new HSSFRichTextString("≥200W"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 18));
	        
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(1);
	        c21.setCellValue(new HSSFRichTextString("人数"));
	        HSSFCell c22 = row2.createCell(2);
	        c22.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c23 = row2.createCell(3);
	        c23.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        HSSFCell c24 = row2.createCell(4);
	        c24.setCellValue(new HSSFRichTextString("[10,20)人数"));
	        HSSFCell c25 = row2.createCell(5);
	        c25.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c26 = row2.createCell(6);
	        c26.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        HSSFCell c27 = row2.createCell(7);
	        c27.setCellValue(new HSSFRichTextString("[20,50)人数"));
	        HSSFCell c28 = row2.createCell(8);
	        c28.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c29 = row2.createCell(9);
	        c29.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        HSSFCell c210 = row2.createCell(10);
	        c210.setCellValue(new HSSFRichTextString("[50,100)人数"));
	        HSSFCell c211 = row2.createCell(11);
	        c211.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c212 = row2.createCell(12);
	        c212.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        HSSFCell c213 = row2.createCell(13);
	        c213.setCellValue(new HSSFRichTextString("[100,200)人数"));
	        HSSFCell c214 = row2.createCell(14);
	        c214.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c215 = row2.createCell(15);
	        c215.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        HSSFCell c216 = row2.createCell(16);
	        c216.setCellValue(new HSSFRichTextString("≥200人数"));
	        HSSFCell c217 = row2.createCell(17);
	        c217.setCellValue(new HSSFRichTextString("总投资金额"));
	        HSSFCell c218 = row2.createCell(18);
	        c218.setCellValue(new HSSFRichTextString("人均笔均红包使用"));
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        c3.setCellStyle(style);
	        c4.setCellStyle(style);
	        c5.setCellStyle(style);
	        c6.setCellStyle(style);
	        for (int i = 0; i < 19; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
        	String[] fields = {"日期", "人数", "总投资金额_万元", "人均笔均红包使用_ALL" , "人数_10_20", "总投资金额_10_20_万元", "人均笔均红包使用_10_20", 
					"人数_20_50", "总投资金额_20_50_万元", "人均笔均红包使用_20_50", "人数_50_100", "总投资金额_50_100_万元", "人均笔均红包使用_50_100", 
					"人数_100_200" , "总投资金额_100_200_万元", "人均笔均红包使用_100_200", "人数_200", "总投资金额_200_万元", "人均笔均红包使用_200"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else{
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 成熟用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportChenshuUser")
	public void exportChenshuUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="成熟用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R chenshuUserData = queryChengshuUserAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) chenshuUserData.get("data_list");
		OutputStream os = null;
		String excelName = "成熟用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
	        //创建标题行
			int rowIndex = 0;
	        HSSFRow row = sheet.createRow(rowIndex++); 
	        String[] fields_title = {"类别","日期","区间","人数","占比","环比"};
	        for (int i = 0; i < fields_title.length; i++) {
	        	 HSSFCell cell_ = row.createCell(i);
	        	 cell_.setCellValue(new HSSFRichTextString(fields_title[i]));
			}
	        
	        HSSFRow row2 = null;
	        int index = 0;
       	    Map<String, Object> weekAgo = dataList.get(0);
       	    Map<String, Object> curr = dataList.get(1);
       	    
       	    List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
       	    newList.add(curr);
       	    newList.add(weekAgo);
       	    //遍历待收
       	    String[] typeArr = {"待收", "平均投资金额"};
       	    for (int y = 0; y < typeArr.length; y++) {
				String type = typeArr[y];
				for (int z = 0; z < newList.size(); z++) {
					Map<String, Object> dataMap = newList.get(z);
					index = 1;
					row2 = sheet.createRow(rowIndex++);
					
					if(z == 0){//
						HSSFCell cell_ = row2.createCell(0);
						cell_.setCellValue(new HSSFRichTextString(type));
						cell_.setCellStyle(style);
						if(y == 0){
							sheet.addMergedRegion(new CellRangeAddress(1, 16, 0, 0)); 
						}else{
							sheet.addMergedRegion(new CellRangeAddress(17, 32, 0, 0)); 
						}
					}
					
					HSSFCell cell_1 = row2.createCell(index++);
					cell_1.setCellValue(new HSSFRichTextString(dataMap.get("日期") + ""));
					cell_1.setCellStyle(style);
					if(y == 0){
						if(z == 0){
							sheet.addMergedRegion(new CellRangeAddress(1, 8, 1, 1)); 
						}else{
							sheet.addMergedRegion(new CellRangeAddress(9, 16, 1, 1)); 
						}
					}else{
						if(z == 0){
							sheet.addMergedRegion(new CellRangeAddress(17, 24, 1, 1)); 
						}else{
							sheet.addMergedRegion(new CellRangeAddress(25, 32, 1, 1)); 
						}
					}
					
//					HSSFCell cell_2 = row2.createCell(index++);
//					cell_2.setCellValue(new HSSFRichTextString("全量（>5）"));
//					
//					HSSFCell cell_3 = row2.createCell(index++);
//					cell_3.setCellValue(new HSSFRichTextString(dataMap.get("全量用户") + ""));
//					
//					HSSFCell cell_4 = row2.createCell(index++);
//					cell_4.setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
//							Double.parseDouble(dataMap.get("占比_全量") + "")*100, 2) + "%"));
//					
//					HSSFCell cell_5 = row2.createCell(index++);
//					String field = "全量用户";
//					if(z == 0){
//						cell_5.setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
//								(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
//								Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
//					}else{
//						cell_5.setCellValue(new HSSFRichTextString("-"));
//					}
					//
					String[] arr1 = {"全量（>5）", "0","(0,5000)","[5000,1万)","[1万,2万)","[2万,5万)","[5万,10万)","≥10万"};
					String[] arr2 = {"全量用户","待收_0","待收_0_5000","待收_5000_10000","待收_10000_20000","待收_20000_50000","待收_50000_100000","待收_大于等于100000"};
					String[] arr3 = {"占比_全量","占比_待收_0","待收占比_待收_0_5000","占比_待收_5000_10000","占比_待收_10000_20000","占比_待收_20000_50000","占比_待收_50000_100000","占比_待收_大于等于100000"};
					
					String[] arr4 = {"(0,1千)","[1千,3千)","[3千,5千)","[5千,1万)","[1万,2万)","[2万,5万)","[5万,10万)","≥10万"};
					String[] arr5 = {"平均投资金额_0_1000","平均投资金额_1000_3000","平均投资金额_3000_5000","平均投资金额_5000_10000","平均投资金额_10000_20000","平均投资金额_20000_50000","平均投资金额_50000_100000","平均投资金额_大于等于100000"};
					String[] arr6 = {"占比_平均投资金额_0_1000","占比_平均投资金额_1000_3000","占比_平均投资金额_3000_5000","占比_平均投资金额_5000_10000","占比_平均投资金额_10000_20000","占比_平均投资金额_20000_50000","占比_平均投资金额_50000_100000","占比_平均投资金额_大于等于10万"};
					for (int i = 0; i < 8; i++) {
						index = 2;
						HSSFRow new_row = null;
						if(i == 0){
							new_row = row2;
						}else{
							new_row = sheet.createRow(rowIndex++); 
						}
						HSSFCell cell_22 = new_row.createCell(index++);
						if(y == 0){
							cell_22.setCellValue(new HSSFRichTextString(arr1[i]));
						}else{
							cell_22.setCellValue(new HSSFRichTextString(arr4[i]));
						}
						
						HSSFCell cell_32 = new_row.createCell(index++);
						if(y == 0){
							cell_32.setCellValue(new HSSFRichTextString(dataMap.get(arr2[i]) + ""));
						}else{
							cell_32.setCellValue(new HSSFRichTextString(dataMap.get(arr5[i]) + ""));
						}
						
						String text = "";
						if(y == 0){
							text = arr3[i];
						}else{
							text = arr6[i];
						}
						HSSFCell cell_42 = new_row.createCell(index++);
						if(StringUtils.isEmpty(dataMap.get(text) == null? "":dataMap.get(text).toString())){
							cell_42.setCellValue(new HSSFRichTextString("0%"));
						}else{
							cell_42.setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
									Double.parseDouble(dataMap.get(text) + "")*100, 2) + "%"));
						}
						
						HSSFCell cell_52 = new_row.createCell(index++);
						String field = "";
						if(y == 0){
							field = arr2[i];
						}else{
							field = arr5[i];
						}
						//环比
						if(z == 0){
							if(StringUtils.isEmpty(weekAgo.get(field) == null? "":weekAgo.get(field).toString())){
								cell_52.setCellValue(new HSSFRichTextString("0%"));
							}else{
								String key = NumberUtil.keepPrecision(
										(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
										Double.parseDouble(weekAgo.get(field)+""), 2) + "%";
								cell_52.setCellValue(new HSSFRichTextString(key));
							}
						}else{
							cell_52.setCellValue(new HSSFRichTextString("-"));
						}
					}
				}
			}
       	    
	        
       	    for (int i = 0; i < 6; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
       	    
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 新用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportNewUser")
	public void exportNewUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="新用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R newUserData = queryNewUserAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) newUserData.get("data_list");
		OutputStream os = null;
		String excelName = "新用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("全量（≤5）"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5)); 
	        HSSFCell c2 = row.createCell(6);   
	        c2.setCellValue(new HSSFRichTextString("投资次数：1"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 10)); 
	        HSSFCell c3 = row.createCell(11);   
	        c3.setCellValue(new HSSFRichTextString("投资次数：2"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 15));
	        
	        HSSFCell c4 = row.createCell(16);   
	        c4.setCellValue(new HSSFRichTextString("投资次数：3"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 20));
	        
	        HSSFCell c5 = row.createCell(21);   
	        c5.setCellValue(new HSSFRichTextString("投资次数：[4,5]"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 21, 25));
	        
	        HSSFRow row2 = sheet.createRow(1); 
	       
	        
	        String[] fields__ = {"用户数","高净值用户","总投资金额","总待收","平均红包金额"};
	        int index = 1;
	        for (int j = 0; j < 5; j++) {
	        	for (int i = 0; i < fields__.length; i++) {
	        		String field = fields__[i];
	        		HSSFCell cell_ = row2.createCell(index++);
	        		cell_.setCellValue(new HSSFRichTextString(field));
	        	}
			}
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        c3.setCellStyle(style);
	        c4.setCellStyle(style);
	        c5.setCellStyle(style);
        	String[] fields = {"日期", "用户数", "高净值用户数_全量", "总投资金额_全量" , "总待收_全量", "平均红包金额_全量", 
        			"用户数_1", "高净值用户数_1", "总投资金额_1", "总待收_1", "平均红包金额_1", 
        			"用户数_2", "高净值用户数_2", "总投资金额_2", "总待收_2", "平均红包金额_2", 
        			"用户数_3", "高净值用户数_3", "总投资金额_3", "总待收_3", "平均红包金额_3", 
					"用户数_4_5", "高净值用户数_4_5", "总投资金额_4_5", "总待收_4_5", "平均红包金额_4_5"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else{
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 沉默用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportChenmoUser")
	public void exportChenmoUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="沉默用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R chenmoData = queryChenmoUserAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) chenmoData.get("data_list");
		OutputStream os = null;
		String excelName = "沉默用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("全量"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3)); 
	        HSSFCell c2 = row.createCell(4);   
	        c2.setCellValue(new HSSFRichTextString("=30"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6)); 
	        HSSFCell c3 = row.createCell(7);   
	        c3.setCellValue(new HSSFRichTextString("(30,90)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
	        
	        HSSFCell c4 = row.createCell(10);   
	        c4.setCellValue(new HSSFRichTextString("=90"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
	        
	        HSSFCell c5 = row.createCell(13);   
	        c5.setCellValue(new HSSFRichTextString("(90,180)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
	        
	        HSSFCell c6 = row.createCell(16);   
	        c6.setCellValue(new HSSFRichTextString("=180"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 18));
	        
	        HSSFCell c7 = row.createCell(19);   
	        c7.setCellValue(new HSSFRichTextString(">180"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 19, 21));
	        
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(1);
	        c21.setCellValue(new HSSFRichTextString("人数"));
	        HSSFCell c22 = row2.createCell(2);
	        c22.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c23 = row2.createCell(3);
	        c23.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c24 = row2.createCell(4);
	        c24.setCellValue(new HSSFRichTextString("=30人数"));
	        HSSFCell c25 = row2.createCell(5);
	        c25.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c26 = row2.createCell(6);
	        c26.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c27 = row2.createCell(7);
	        c27.setCellValue(new HSSFRichTextString("(30,90)人数"));
	        HSSFCell c28 = row2.createCell(8);
	        c28.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c29 = row2.createCell(9);
	        c29.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c210 = row2.createCell(10);
	        c210.setCellValue(new HSSFRichTextString("=90人数"));
	        HSSFCell c211 = row2.createCell(11);
	        c211.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c212 = row2.createCell(12);
	        c212.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c213 = row2.createCell(13);
	        c213.setCellValue(new HSSFRichTextString("(90,180)人数"));
	        HSSFCell c214 = row2.createCell(14);
	        c214.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c215 = row2.createCell(15);
	        c215.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c216 = row2.createCell(16);
	        c216.setCellValue(new HSSFRichTextString("=180人数"));
	        HSSFCell c217 = row2.createCell(17);
	        c217.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c218 = row2.createCell(18);
	        c218.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        HSSFCell c219 = row2.createCell(19);
	        c219.setCellValue(new HSSFRichTextString(">180人数"));
	        HSSFCell c220 = row2.createCell(20);
	        c220.setCellValue(new HSSFRichTextString("高净值用户数"));
	        HSSFCell c221 = row2.createCell(21);
	        c221.setCellValue(new HSSFRichTextString("平均投资金额"));
	        
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        c3.setCellStyle(style);
	        c4.setCellStyle(style);
	        c5.setCellStyle(style);
	        c6.setCellStyle(style);
	        c7.setCellStyle(style);
	        for (int i = 0; i < 22; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
        	String[] fields = {"日期", "人数", "高净值用户数_全量", "平均投资金额_全量" , "人数_30", "高净值用户数_30", "平均投资金额_30", 
					"人数_30_90", "高净值用户数_30_90", "平均投资金额_30_90", "人数_90", "高净值用户数_90", "平均投资金额_90", 
					"人数_90_180" , "高净值用户数_90_180", "平均投资金额_90_180", "人数_180", "高净值用户数_180", "平均投资金额_180",
					"人数_大于180", "高净值用户数_大于180", "平均投资金额_大于180"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else{
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        			
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 大额提现用户导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportWithdrawUser")
	public void exportWithdrawUser(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="大额提现用户分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R withdrawData = queryWithdrawuser(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) withdrawData.get("data_list");
		OutputStream os = null;
		String excelName = "大额提现用户-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("当期提现总用户数"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1)); 
	        
	        HSSFCell c2 = row.createCell(2);   
	        c2.setCellValue(new HSSFRichTextString("大额提现用户数"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 8)); 
	        
	        
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(2);
	        c21.setCellValue(new HSSFRichTextString("当期总用户数"));
	        HSSFCell c22 = row2.createCell(3);
	        c22.setCellValue(new HSSFRichTextString("占当期提现用户比例"));
	        HSSFCell c23 = row2.createCell(4);
	        c23.setCellValue(new HSSFRichTextString("高净值用户"));
	        
	        HSSFCell c24 = row2.createCell(5);
	        c24.setCellValue(new HSSFRichTextString("沉默用户"));
	        HSSFCell c25 = row2.createCell(6);
	        c25.setCellValue(new HSSFRichTextString("新用户"));
	        HSSFCell c26 = row2.createCell(7);
	        c26.setCellValue(new HSSFRichTextString("成熟用户"));
	        
	        HSSFCell c27 = row2.createCell(8);
	        c27.setCellValue(new HSSFRichTextString("自然用户"));
	       
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        for (int i = 0; i < 9; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
        	String[] fields = {"日期", "当期提现总用户数", "当期总用户数", "占当期提现用户比例" , "高净值用户", "沉默用户", "新用户", 
					"成熟用户", "自然用户"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else{
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        			
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}

	/**
	 * 净资金待收异动导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportDaishouYidong")
	public void exportDaishouYidong(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="当周待收用户分布";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R daishouData = queryDaishouYidong(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) daishouData.get("data_list");
		OutputStream os = null;
		String excelName = "当周待收用户分布-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("待收金额"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1)); 
	        
	        HSSFCell c2 = row.createCell(2);   
	        c2.setCellValue(new HSSFRichTextString("待收资金用户分布(待收资金>0)"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 8)); 
	        
	        
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(2);
	        c21.setCellValue(new HSSFRichTextString("用户总数"));
	        HSSFCell c22 = row2.createCell(3);
	        c22.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c23 = row2.createCell(4);
	        c23.setCellValue(new HSSFRichTextString("沉默用户"));
	        
	        HSSFCell c24 = row2.createCell(5);
	        c24.setCellValue(new HSSFRichTextString("新用户"));
	        HSSFCell c25 = row2.createCell(6);
	        c25.setCellValue(new HSSFRichTextString("成熟用户"));
	        HSSFCell c26 = row2.createCell(7);
	        c26.setCellValue(new HSSFRichTextString("自然用户"));
	        
	        HSSFCell c27 = row2.createCell(8);
	        c27.setCellValue(new HSSFRichTextString("其他"));
	       
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        for (int i = 0; i < 9; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
        	String[] fields = {"日期", "净待收金额", "用户总数", "高净值用户" , "沉默用户", "新用户", 
					"成熟用户", "自然用户", "其他"}; 

	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	for (int j = 0; j < fields.length; j++) {
					String field = fields[j];
					row_.createCell(j).setCellValue(new HSSFRichTextString(dataMap.get(field) + ""));
				}
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 

        	for (int i = 0; i < fields.length; i++) {
        		String field = fields[i];
        		if(i == 0){
        			row_.createCell(i).setCellValue(new HSSFRichTextString("与上周环比"));
        		}else if(i == 1){//净待收金额
        			if(Double.parseDouble(weekAgo.get(field)+"") == 0){
        				row_.createCell(i).setCellValue(new HSSFRichTextString("0%"));
        			}else{
        				row_.createCell(i).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
            					(Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+""))*100/
            					Double.parseDouble(weekAgo.get(field)+""), 2) + "%"));
        			}
        		}else{
    				row_.createCell(i).setCellValue(new HSSFRichTextString(
    						Double.parseDouble(curr.get(field)+"")-Double.parseDouble(weekAgo.get(field)+"") + ""));
        		}
			}
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	/**
	 * 大盘分析导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@ResponseBody
	@RequestMapping("/exportDapanAnalyse")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="大盘分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);

		R dapanAnalyseData = queryDapanAnalyse(map);

		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dapanAnalyseData.get("data_list");
		OutputStream os = null;
		String excelName = "大盘分析-" + map.get("statPeriod");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			
	        //创建sheet页  
	        HSSFSheet sheet = wb.createSheet(excelName); 
	        
	        HSSFCellStyle style = wb.createCellStyle();
	        
	        //设置颜色
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
//	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
	        //边框填充
//	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//	        style.setFont(font);
	        style.setWrapText(false);
	        
			// 列头样式
			CellStyle cellStyle = wb.createCellStyle();
			/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
//			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        
	        //创建单元格  
	        HSSFRow row = sheet.createRow(0); 
	        sheet.autoSizeColumn(1, true);
	        HSSFCell c0 = row.createCell(0);
	        c0.setCellValue(new HSSFRichTextString("日期"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0)); 
	        
	        HSSFCell c1 = row.createCell(1); 
	        c1.setCellValue(new HSSFRichTextString("高净值用户"));  
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3)); 
	        HSSFCell c2 = row.createCell(4);   
	        c2.setCellValue(new HSSFRichTextString("沉默用户"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6)); 
	        HSSFCell c3 = row.createCell(7);   
	        c3.setCellValue(new HSSFRichTextString("新用户"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));
	        
	        HSSFCell c4 = row.createCell(10);   
	        c4.setCellValue(new HSSFRichTextString("成熟用户"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 12));
	        
	        HSSFCell c5 = row.createCell(13);   
	        c5.setCellValue(new HSSFRichTextString("自然用户"));
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 15));
	        
	        HSSFRow row2 = sheet.createRow(1); 
	        HSSFCell c21 = row2.createCell(1);
	        c21.setCellValue(new HSSFRichTextString("总投资用户	"));
	        HSSFCell c22 = row2.createCell(2);
	        c22.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c23 = row2.createCell(3);
	        c23.setCellValue(new HSSFRichTextString("占比"));
	        
	        HSSFCell c24 = row2.createCell(4);
	        c24.setCellValue(new HSSFRichTextString("沉默全量用户"));
	        HSSFCell c25 = row2.createCell(5);
	        c25.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c26 = row2.createCell(6);
	        c26.setCellValue(new HSSFRichTextString("占比"));
	        
	        HSSFCell c27 = row2.createCell(7);
	        c27.setCellValue(new HSSFRichTextString("全量新用户"));
	        HSSFCell c28 = row2.createCell(8);
	        c28.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c29 = row2.createCell(9);
	        c29.setCellValue(new HSSFRichTextString("占比"));
	        
	        HSSFCell c210 = row2.createCell(10);
	        c210.setCellValue(new HSSFRichTextString("成熟全量用户"));
	        HSSFCell c211 = row2.createCell(11);
	        c211.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c212 = row2.createCell(12);
	        c212.setCellValue(new HSSFRichTextString("占比"));
	        
	        HSSFCell c213 = row2.createCell(13);
	        c213.setCellValue(new HSSFRichTextString("自然全量用户"));
	        HSSFCell c214 = row2.createCell(14);
	        c214.setCellValue(new HSSFRichTextString("高净值用户"));
	        HSSFCell c215 = row2.createCell(15);
	        c215.setCellValue(new HSSFRichTextString("占比"));
	        
	        
	        c0.setCellStyle(style);
	        c1.setCellStyle(style);
	        c2.setCellStyle(style);
	        c3.setCellStyle(style);
	        c4.setCellStyle(style);
	        c5.setCellStyle(style);
	        
	        for (int i = 0; i < 16; i++) {
	            sheet.setColumnWidth(i, 15 * 256);//调整列宽
			}
	        
	        for (int i = 0; i < dataList.size(); i++) {
	        	Map<String, Object> dataMap = dataList.get(i);
	        	HSSFRow row_ = sheet.createRow(i + 2); 
	        	row_.createCell(0).setCellValue(new HSSFRichTextString(dataMap.get("日期") + ""));
	        	row_.createCell(1).setCellValue(new HSSFRichTextString(dataMap.get("总投资用户") + ""));
	        	row_.createCell(2).setCellValue(new HSSFRichTextString(dataMap.get("总高净值用户") + ""));
	        	row_.createCell(3).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(Double.parseDouble(dataMap.get("占比")+"")*100, 2) + "%"));
	        	row_.createCell(4).setCellValue(new HSSFRichTextString(dataMap.get("沉默全量用户") + ""));
	        	row_.createCell(5).setCellValue(new HSSFRichTextString(dataMap.get("沉默_高净值用户") + ""));
	        	row_.createCell(6).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(Double.parseDouble(dataMap.get("沉默_占比")+"")*100, 2) + "%"));
	        	row_.createCell(7).setCellValue(new HSSFRichTextString(dataMap.get("全量新用户") + ""));
	        	row_.createCell(8).setCellValue(new HSSFRichTextString(dataMap.get("新用户_高净值用户") + ""));
	        	row_.createCell(9).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(Double.parseDouble(dataMap.get("新用户_占比")+"")*100, 2) + "%"));
	        	row_.createCell(10).setCellValue(new HSSFRichTextString(dataMap.get("成熟全量用户") + ""));
	        	row_.createCell(11).setCellValue(new HSSFRichTextString(dataMap.get("成熟_高净值用户") + ""));
	        	row_.createCell(12).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(Double.parseDouble(dataMap.get("成熟_占比")+"")*100, 2) + "%"));
	        	row_.createCell(13).setCellValue(new HSSFRichTextString(dataMap.get("自然全量用户") + ""));
	        	row_.createCell(14).setCellValue(new HSSFRichTextString(dataMap.get("自然_高净值用户") + ""));
	        	row_.createCell(15).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(Double.parseDouble(dataMap.get("自然_占比")+"")*100, 2) + "%"));
			}
	        Map<String, Object> weekAgo = dataList.get(0);
	        Map<String, Object> curr = dataList.get(1);
        	HSSFRow row_ = sheet.createRow(4); 
        	row_.createCell(0).setCellValue(new HSSFRichTextString("与上周环比"));
        	double totalInvestUser_curr = Double.parseDouble(curr.get("总投资用户")+"");
        	double totalInvestUser_week = Double.parseDouble(weekAgo.get("总投资用户")+"");
        	row_.createCell(1).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision((totalInvestUser_curr-totalInvestUser_week)*100/totalInvestUser_week, 2) + "%"));
        	
        	double gjz_curr = Double.parseDouble(curr.get("总高净值用户")+"");
        	double gjz_week = Double.parseDouble(weekAgo.get("总高净值用户")+"");
        	row_.createCell(2).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision((gjz_curr-gjz_week)*100/gjz_week, 2) + "%"));
        	row_.createCell(3).setCellValue(new HSSFRichTextString(""));
        	
        	
        	row_.createCell(4).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("沉默全量用户")+"")-Double.parseDouble(weekAgo.get("沉默全量用户")+""))*100/
        			Double.parseDouble(weekAgo.get("沉默全量用户")+""), 2) + "%"));
        	row_.createCell(5).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("沉默_高净值用户")+"")-Double.parseDouble(weekAgo.get("沉默_高净值用户")+""))*100/
        			Double.parseDouble(weekAgo.get("沉默_高净值用户")+""), 2) + "%"));
        	row_.createCell(6).setCellValue(new HSSFRichTextString(""));
        	
        	row_.createCell(7).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("全量新用户")+"")-Double.parseDouble(weekAgo.get("全量新用户")+""))*100/
        			Double.parseDouble(weekAgo.get("全量新用户")+""), 2) + "%"));
        	row_.createCell(8).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("新用户_高净值用户")+"")-Double.parseDouble(weekAgo.get("新用户_高净值用户")+""))*100/
        			Double.parseDouble(weekAgo.get("新用户_高净值用户")+""), 2) + "%"));
        	row_.createCell(9).setCellValue(new HSSFRichTextString(""));
        	
        	row_.createCell(10).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("成熟全量用户")+"")-Double.parseDouble(weekAgo.get("成熟全量用户")+""))*100/
        			Double.parseDouble(weekAgo.get("成熟全量用户")+""), 2) + "%"));
        	row_.createCell(11).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("成熟_高净值用户")+"")-Double.parseDouble(weekAgo.get("成熟_高净值用户")+""))*100/
        			Double.parseDouble(weekAgo.get("成熟_高净值用户")+""), 2) + "%"));
        	row_.createCell(12).setCellValue(new HSSFRichTextString(""));
        	
        	row_.createCell(13).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("自然全量用户")+"")-Double.parseDouble(weekAgo.get("自然全量用户")+""))*100/
        			Double.parseDouble(weekAgo.get("自然全量用户")+""), 2) + "%"));
        	row_.createCell(14).setCellValue(new HSSFRichTextString(NumberUtil.keepPrecision(
        			(Double.parseDouble(curr.get("自然_高净值用户")+"")-Double.parseDouble(weekAgo.get("自然_高净值用户")+""))*100/
        			Double.parseDouble(weekAgo.get("自然_高净值用户")+""), 2) + "%"));
        	row_.createCell(15).setCellValue(new HSSFRichTextString(""));
	        
	        
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xls").getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null){
				os.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportBusinessAlarm")
	public void exportBusinessAlarm(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String reportType="业务预警分析";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String statPeriod = map.get("statPeriod") + "";
		String statType = map.get("statType") + "";
		R queryBusinessAlarmList = queryBusinessAlarmList(1, 1000000, map, statPeriod, statType);
		PageUtils page = (PageUtils) queryBusinessAlarmList.get("page");
		
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("USER_ID", "用户ID");
		headMap.put("CG_USER_ID", "存管ID");
		headMap.put("USERNAME", "用户名");
		headMap.put("PHONE", "电话");
		
		headMap.put("REALNAME", "姓名");
		headMap.put("availableAmount", "成功提现金额");
		headMap.put("MONEY_ALL", "账户资产权益额");
		
		headMap.put("MONEY_BALANCE", "账户余额");
		headMap.put("MONEY_WAIT", "当前待收金额");
		
		headMap.put("REGISTER_TIME", "注册日期");
		headMap.put("REG_TO_INVEST", "注册后首投间隔（分）");
		headMap.put("AVG_INV_TIME", "平均投资时间间隔（分）");
		
		headMap.put("AVG_INV_MONEY", "平均投资金额");
		headMap.put("INV_NUM", "投资笔数");
		headMap.put("USE_VOUCHE_NUM", "使用红包投资笔数");
		
		headMap.put("TRANSFER_NUM", "转让笔数");
		headMap.put("AVG_PERIOD", "标的平均投资期限");
		headMap.put("MONEY_RECHARGE", "累计充值金额");
		
		headMap.put("MONEY_WITHDRAW", "累计提现金额");
		headMap.put("提现金额_充值金额", "提现金额/充值金额");
		headMap.put("NO_INV", "距今未复投时长");
		
		headMap.put("RECOVER_ACCOUNT_WAIT", "未来30天内待收金额");
		headMap.put("USE_VOUCHE_MONEY", "使用红包总额");
		headMap.put("MONEY_WAIT_DEP", "存管版代收金额");
		
		headMap.put("isInternalTuijian", "待收金额");
		
		headMap.put("NOR_PERIOD_TYPE", "普通版本持有投资种类");
		
		String title = "业务预警-" + statPeriod;
		
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
