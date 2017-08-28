package io.renren.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelUtil {
	public static String NO_DEFINE = "no_define";// 未定义的字段
	public static String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";// 默认日期格式
	public static int DEFAULT_COLOUMN_WIDTH = 17;

	/**
	 * 导出Excel 97(.xls)格式 ，少量数据
	 *
	 * @param title
	 *            标题行
	 * @param headMap
	 *            属性-列名
	 * @param jsonArray
	 *            数据集
	 * @param datePattern
	 *            日期格式，null则用默认日期格式
	 * @param colWidth
	 *            列宽 默认 至少17个字节
	 * @param out
	 *            输出流
	 */
	public static void exportExcel(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern,
			int colWidth, OutputStream out) {
		if (datePattern == null)
			datePattern = DEFAULT_DATE_PATTERN;
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		workbook.createInformationProperties();
		workbook.getDocumentSummaryInformation().setCompany("*****公司");
		SummaryInformation si = workbook.getSummaryInformation();
		si.setAuthor("nono"); // 填加xls文件作者信息
		si.setApplicationName("导出程序"); // 填加xls文件创建程序信息
		si.setLastAuthor("最后保存者信息"); // 填加xls文件最后保存者信息
		si.setComments("nono is a programmer!"); // 填加xls文件作者信息
		si.setTitle("POI导出Excel"); // 填加xls文件标题信息
		si.setSubject("POI导出Excel");// 填加文件主题信息
		si.setCreateDateTime(new Date());
		// 表头样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 20);
		titleFont.setBoldweight((short) 700);
		titleStyle.setFont(titleFont);
		// 列头样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFFont headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(headerFont);
		// 单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFFont cellFont = workbook.createFont();
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(cellFont);
		// 生成一个(带标题)表格
		HSSFSheet sheet = workbook.createSheet();
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("nono");
		// 设置列宽
		int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		int[] arrColWidth = new int[headMap.size()];
		// 产生表格标题行,以及设置列宽
		String[] properties = new String[headMap.size()];
		String[] headers = new String[headMap.size()];
		int ii = 0;
		for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
			String fieldName = iter.next();

			properties[ii] = fieldName;
			headers[ii] = fieldName;

			int bytes = fieldName.getBytes().length;
			arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
			sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
			ii++;
		}
		// 遍历集合数据，产生数据行
		int rowIndex = 0;
		for (Object obj : jsonArray) {
			if (rowIndex == 65535 || rowIndex == 0) {
				if (rowIndex != 0)
					sheet = workbook.createSheet();// 如果数据超过了，则在第二页显示

				HSSFRow titleRow = sheet.createRow(0);// 表头 rowIndex=0
				titleRow.createCell(0).setCellValue(title);
				titleRow.getCell(0).setCellStyle(titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

				HSSFRow headerRow = sheet.createRow(1); // 列头 rowIndex =1
				for (int i = 0; i < headers.length; i++) {
					headerRow.createCell(i).setCellValue(headers[i]);
					headerRow.getCell(i).setCellStyle(headerStyle);

				}
				rowIndex = 2;// 数据内容从 rowIndex=2开始
			}
			JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
			HSSFRow dataRow = sheet.createRow(rowIndex);
			for (int i = 0; i < properties.length; i++) {
				HSSFCell newCell = dataRow.createCell(i);

				Object o = jo.get(properties[i]);
				String cellValue = "";
				if (o == null)
					cellValue = "";
//				else if (o instanceof Date)
//					cellValue = new SimpleDateFormat(datePattern).format(o);
				else
					cellValue = o.toString();

				newCell.setCellValue(cellValue);
				newCell.setCellStyle(cellStyle);
			}
			rowIndex++;
		}
		// 自动调整宽度
		/*
		 * for (int i = 0; i < headers.length; i++) { sheet.autoSizeColumn(i); }
		 */
		try {
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出Excel 2007 OOXML (.xlsx)格式
	 *
	 * @param title
	 *            标题行
	 * @param headMap
	 *            属性-列头
	 * @param jsonArray
	 *            数据集
	 * @param datePattern
	 *            日期格式，传null值则默认 年月日
	 * @param colWidth
	 *            列宽 默认 至少17个字节
	 * @param out
	 *            输出流
	 */
	public static void exportExcelX(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern,
			int colWidth, OutputStream out, Map<String, String> typeMap) {
		if (datePattern == null)
			datePattern = DEFAULT_DATE_PATTERN;
		// 声明一个工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存
		workbook.setCompressTempFiles(true);
		// 表头样式
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) titleFont).setFontHeightInPoints((short) 20);
		((org.apache.poi.ss.usermodel.Font) titleFont).setBoldweight((short) 700);
		titleStyle.setFont((org.apache.poi.ss.usermodel.Font) titleFont);
		// 列头样式
		CellStyle headerStyle = workbook.createCellStyle();
		/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) headerFont).setFontHeightInPoints((short) 12);
		((org.apache.poi.ss.usermodel.Font) headerFont).setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont((org.apache.poi.ss.usermodel.Font) headerFont);
		// 单元格样式
		CellStyle cellStyle = workbook.createCellStyle();
		/* cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) cellFont).setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont((org.apache.poi.ss.usermodel.Font) cellFont);
		// 生成一个(带标题)表格
		SXSSFSheet sheet = workbook.createSheet();
		// 设置列宽
		int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		int[] arrColWidth = new int[headMap.size()];
		// 产生表格标题行,以及设置列宽
		String[] properties = new String[headMap.size()];
		String[] headers = new String[headMap.size()];
		int ii = 0;
		for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
			String fieldName = iter.next();

			properties[ii] = fieldName;
			headers[ii] = headMap.get(fieldName);

			int bytes = fieldName.getBytes().length;
			arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
			sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
			ii++;
		}
		// 遍历集合数据，产生数据行
		int rowIndex = 0;
		for (Object obj : jsonArray) {
			if (rowIndex == 1000000 || rowIndex == 0) {
				if (rowIndex != 0)
					sheet = workbook.createSheet();// 如果数据超过了，则在第二页显示

				SXSSFRow titleRow = sheet.createRow(0);// 表头 rowIndex=0
				titleRow.createCell(0).setCellValue(title);
				titleRow.getCell(0).setCellStyle(titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

				SXSSFRow headerRow = sheet.createRow(1); // 列头 rowIndex =1
				for (int i = 0; i < headers.length; i++) {
					headerRow.createCell(i).setCellValue(headers[i]);
					headerRow.getCell(i).setCellStyle(headerStyle);

				}
				rowIndex = 2;// 数据内容从 rowIndex=2开始
			}
			JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
			SXSSFRow dataRow = sheet.createRow(rowIndex);
			for (int i = 0; i < properties.length; i++) {
				SXSSFCell newCell = dataRow.createCell(i);

				Object o = jo.get(properties[i]);
				if (o == null){
					newCell.setCellValue("");
//				}else if (o instanceof Date){
//					newCell.setCellValue(new SimpleDateFormat(datePattern).format(o));
				}else if (o instanceof Float){
					BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					newCell.setCellValue(setScale.floatValue());
				}else if (o instanceof Double){
					BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
					newCell.setCellValue(setScale.doubleValue());
				}else if(o instanceof Integer){
					newCell.setCellValue(Integer.parseInt(o.toString()));
				}else if(o instanceof BigDecimal){
					BigDecimal big = new BigDecimal(o.toString());
					int intValue = big.intValue();
					double doubleValue = big.doubleValue();
					if(doubleValue > intValue){
						newCell.setCellValue(doubleValue);
					}else{
						newCell.setCellValue(intValue);
					}
				}else{
					if(typeMap != null){
						if(typeMap.containsKey(i + "")){//判断该列字段是否指定了字段类型
							String type = typeMap.get(i + "");
							if("int".equals(type)){
								newCell.setCellValue(Integer.parseInt(o.toString()));
							}else if("double".equals(type)){
								BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								newCell.setCellValue(setScale.doubleValue());
							}else if("float".equals(type)){
								BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
								newCell.setCellValue(setScale.floatValue());
							}
						}else{
							newCell.setCellValue(o.toString());
						}
					}else{
						newCell.setCellValue(o.toString());
					}
				}
				
				newCell.setCellStyle(cellStyle);
			}
			rowIndex++;
		}
		// 自动调整宽度
		/*
		 * for (int i = 0; i < headers.length; i++) { sheet.autoSizeColumn(i); }
		 */
		try {
			workbook.write(out);
			workbook.close();
			workbook.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportExcelX(List<String> title, List<Map<String, String>> headMapList, List<JSONArray> jsonArrayList, String datePattern,
			int colWidth, OutputStream out) {
		if (datePattern == null)
			datePattern = DEFAULT_DATE_PATTERN;
		// 声明一个工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存
		workbook.setCompressTempFiles(true);
		// 表头样式
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) titleFont).setFontHeightInPoints((short) 20);
		((org.apache.poi.ss.usermodel.Font) titleFont).setBoldweight((short) 700);
		titleStyle.setFont((org.apache.poi.ss.usermodel.Font) titleFont);
		// 列头样式
		CellStyle headerStyle = workbook.createCellStyle();
		/* headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) headerFont).setFontHeightInPoints((short) 12);
		((org.apache.poi.ss.usermodel.Font) headerFont).setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont((org.apache.poi.ss.usermodel.Font) headerFont);
		// 单元格样式
		CellStyle cellStyle = workbook.createCellStyle();
		/* cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); */
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
		((org.apache.poi.ss.usermodel.Font) cellFont).setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont((org.apache.poi.ss.usermodel.Font) cellFont);
		// 生成一个(带标题)表格
		SXSSFSheet sheet = null;
		// 设置列宽
		int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		
		for (int z = 0; z < headMapList.size(); z++) {
			sheet = workbook.createSheet(title.get(z));// 如果数据超过了，则在第二页显示
			Map<String, String> headMap = headMapList.get(z);
			int[] arrColWidth = new int[headMap.size()];
			// 产生表格标题行,以及设置列宽
			String[] properties = new String[headMap.size()];
			String[] headers = new String[headMap.size()];
			int ii = 0;
			for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
				String fieldName = iter.next();
				
				properties[ii] = fieldName;
				headers[ii] = headMap.get(fieldName);
				
				int bytes = fieldName.getBytes().length;
				arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
				sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
				ii++;
			}
			
			
			// 遍历集合数据，产生数据行
			int rowIndex = 0;
			JSONArray jsonArray = jsonArrayList.get(z);
			for (Object obj : jsonArray) {
				if (rowIndex == 1000000 || rowIndex == 0) {
					SXSSFRow titleRow = sheet.createRow(0);// 表头 rowIndex=0
					titleRow.createCell(0).setCellValue(title.get(z));
					titleRow.getCell(0).setCellStyle(titleStyle);
					
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
					
					SXSSFRow headerRow = sheet.createRow(1); // 列头 rowIndex =1
					for (int i = 0; i < headers.length; i++) {
						headerRow.createCell(i).setCellValue(headers[i]);
						headerRow.getCell(i).setCellStyle(headerStyle);

					}
					rowIndex = 2;// 数据内容从 rowIndex=2开始
				}
				JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
				SXSSFRow dataRow = sheet.createRow(rowIndex);
				for (int i = 0; i < properties.length; i++) {
					SXSSFCell newCell = dataRow.createCell(i);

					Object o = jo.get(properties[i]);
					if (o == null){
						newCell.setCellValue("");
//					}else if (o instanceof Date){
//						newCell.setCellValue(new SimpleDateFormat(datePattern).format(o));
					}else if (o instanceof Float){
						BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						newCell.setCellValue(setScale.floatValue());
					}else if (o instanceof Double){
						BigDecimal setScale = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
						newCell.setCellValue(setScale.doubleValue());
					}else if(o instanceof Integer){
						newCell.setCellValue(Integer.parseInt(o.toString()));
					}else if(o instanceof BigDecimal){
						BigDecimal big = new BigDecimal(o.toString());
						int intValue = big.intValue();
						double doubleValue = big.doubleValue();
						if(doubleValue > intValue){
							newCell.setCellValue(doubleValue);
						}else{
							newCell.setCellValue(intValue);
						}
					}else{
						newCell.setCellValue(o.toString());
					}
					
					newCell.setCellStyle(cellStyle);
				}
				rowIndex++;
			}
			
		}
	
		// 自动调整宽度
		/*
		 * for (int i = 0; i < headers.length; i++) { sheet.autoSizeColumn(i); }
		 */
		try {
			workbook.write(out);
			workbook.close();
			workbook.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Web导出后台方法 Controller调用
	 */
	public static void createExcelFile(String title, Map<String, String> headMap, JSONArray dataArray,
			FileOutputStream outputStream) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(title, headMap, dataArray, null, 0, os, null);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 如果失败，则抛出异常
	 * @param title
	 * @param headMap
	 * @param ja
	 * @param response
	 * @throws Exception
	 */
	public static void exportExcel(String title, Map<String, String> headMap, JSONArray ja,
			HttpServletResponse response) throws Exception {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(title, headMap, ja, null, 0, os, null);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * Web导出后台方法 Controller调用
	 */
	public static void downloadExcelFile(List<String> titleList, List<Map<String, String>> headMap, List<JSONArray> ja,
			HttpServletResponse response) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(titleList, headMap, ja, null, 0, os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((titleList.get(0) + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadExcelFile(String excelName,List<String> tabTitileList, List<Map<String, String>> headMap, List<JSONArray> ja,
			HttpServletResponse response) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(tabTitileList, headMap, ja, null, 0, os);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((excelName + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Web导出后台方法 Controller调用
	 */
	public static void downloadExcelFile(String title, Map<String, String> headMap, JSONArray ja,
			HttpServletResponse response) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(title, headMap, ja, null, 0, os, null);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param title
	 * @param headMap
	 * @param ja
	 * @param response
	 * @param types 指定每一列的数据类型
	 */
	public static void downloadExcelFile(String title, Map<String, String> headMap, JSONArray ja,
			HttpServletResponse response, Map<String, String> typeMap) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtil.exportExcelX(title, headMap, ja, null, 0, os, typeMap);
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
			response.setContentLength(content.length);
			ServletOutputStream outputStream = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buff = new byte[8192];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);

			}
			bis.close();
			bos.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 下边是在controller里边调用的案例及十万条数据测试
	 * 
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "partExport") public void
	 * partExport(HttpServletResponse response) throws IOException{ int count =
	 * 10; JSONArray ja = new JSONArray(); for(int i=0;i<10;i++){ Student s =
	 * new Student(); s.setName("POI"+i); s.setAge(i); s.setBirthday(new
	 * Date()); s.setHeight(i); s.setWeight(i); s.setSex(i/2==0?false:true);
	 * ja.add(s); } Map<String,String> headMap = new
	 * LinkedHashMap<String,String>(); headMap.put("name","姓名");
	 * headMap.put("age","年龄"); headMap.put("birthday","生日");
	 * headMap.put("height","身高"); headMap.put("weight","体重");
	 * headMap.put("sex","性别");
	 * 
	 * String title = "测试";
	 * 
	 * ExcelUtil.downloadExcelFile(title,headMap,ja,response);
	 * 
	 * }
	 * 
	 * class Student { private String name; private int age; private Date
	 * birthday; private float height; private double weight; private boolean
	 * sex;
	 * 
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 * 
	 * public Integer getAge() { return age; }
	 * 
	 * public Date getBirthday() { return birthday; }
	 * 
	 * public void setBirthday(Date birthday) { this.birthday = birthday; }
	 * 
	 * public float getHeight() { return height; }
	 * 
	 * public void setHeight(float height) { this.height = height; }
	 * 
	 * public double getWeight() { return weight; }
	 * 
	 * public void setWeight(double weight) { this.weight = weight; }
	 * 
	 * public boolean isSex() { return sex; }
	 * 
	 * public void setSex(boolean sex) { this.sex = sex; }
	 * 
	 * public void setAge(Integer age) { this.age = age; } }
	 */
	
	
	
	
    public static Map<String, Object> parseExcel(File file,String fileName, String[] fields) {

        // 1.准备返回的变量
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String message = "success";
        List<Map<String, Object>> stones = new ArrayList<Map<String, Object>>();

        boolean isE2007 = false; // 判断是否是excel2007格式
        if (fileName != null && fileName.endsWith("xlsx")) {
            isE2007 = true;
        }
        isE2007 = true;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        // 2.准备workbook
        // 同时支持Excel 2003、2007
        File excelFile = null;
        if(file != null){
        	excelFile = file;
        }else{
        	excelFile = new File(fileName); // 创建文件对象
        }
        Workbook workbook = null;
        // 根据文件格式(2003或者2007)来初始化
        try {
            FileInputStream is = new FileInputStream(excelFile); // 文件流
            if (isE2007) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3.遍历集合，组装结果
        int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
        // 遍历每个Sheet
        for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(s);
            int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
            Map<String, Object> map = null;
            // 遍历每一行
            for (int r = 1; r < rowCount; r++) {
                map = new HashMap<String ,Object>();
                Row row = sheet.getRow(r);
                if(row == null){
                	continue;
                }
                int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
//                int cellCount = row.getLastCellNum();
//                System.err.println("第"+ r + "行，列数为：" + cellCount);
//                System.err.println("第"+ r + "行，列数为：" + cellCount);
                // 遍历每一列
                for (int c = 0; c < fields.length; c++) {
                	Cell cell = null;
                	try {
                		cell = row.getCell(c);
					} catch (Exception e) {
						continue;
					}
                    if(cell == null){
                    	continue;
                    }
                    int cellType = cell.getCellType();
                    String cellStringValue = null;
                    switch (cellType) {
                    case Cell.CELL_TYPE_STRING: // 文本
                        cellStringValue = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC: // 数字、日期
                        if (DateUtil.isCellDateFormatted(cell)) {
                            cellStringValue = fmt.format(cell.getDateCellValue()); // 日期型
                        } else {
                        	Object inputValue = null;// 单元格值
                            cellStringValue = String.valueOf(cell.getNumericCellValue()); // 数字
                            Long longVal = Math.round(cell.getNumericCellValue());
                            Double doubleVal = cell.getNumericCellValue();
                            if(Double.parseDouble(longVal + ".0") == doubleVal){   //判断是否含有小数位.0
                                inputValue = longVal;
                            }else{
                                inputValue = doubleVal;
                            }
                            DecimalFormat df = new DecimalFormat("#.##");    //格式化为四位小数，按自己需求选择；
                            cellStringValue = String.valueOf(df.format(inputValue));
//                            if (cellStringValue.contains("E")) {
//                                cellStringValue = String.valueOf(new Double(cell.getNumericCellValue()).longValue()); // 数字
//                                cellStringValue = String.valueOf(new Double(cell.getNumericCellValue()).longValue()); // 数字
//                                cellStringValue = String.valueOf(new Double(cell.getNumericCellValue()).longValue()); // 数字
//                            }
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN: // 布尔型
                        cellStringValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_BLANK: // 空白
                        cellStringValue = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_ERROR: // 错误
                        cellStringValue = "错误";
                        break;
                    case Cell.CELL_TYPE_FORMULA: // 公式
                    	try {
                    		cellStringValue = cell.getNumericCellValue() + "";
						} catch (Exception e) {
							cellStringValue = "错误";
						}
                        break;
                    default:
                        cellStringValue = "错误";
                    }

                    if (cellStringValue.equals("错误")) {
                        message = "解析Excel时发生错误，第[" + (s + 1) + "]sheet，第[" + (row.getRowNum() + 1) + "]行，第[" + (c + 1)
                                + "]列解析错误";
                        resultMap.put("message", message);
                        return resultMap;
                    }
                    cellStringValue = cellStringValue.trim();
                    map.put(fields[c], cellStringValue);
                }
                stones.add(map);
            }
        }
        resultMap.put("message", message);
        resultMap.put("list", stones);
        return resultMap;
    }
	
	
}