package io.renren.service.schedule.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import io.renren.util.FileUtil;
import io.renren.utils.ExcelUtil;

public class JobUtil {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 生成附件
	 * 
	 * @param dataArray
	 * @param excelTitile
	 * @param fileName
	 * @param fieldMap
	 * @return
	 * @throws IOException
	 */

	public String buildAttachFile(JSONArray dataArray, String excelTitile, String fileName,
			Map<String, String> fieldMap) throws IOException {
		String path = this.getClass().getResource("/").getPath();
//		String CurrentClassFilePath = JobUtil.class.getResource("").getPath();  
		if(path.endsWith("classes")){
			path = path.replace("classes", "");
		}
		if(path.endsWith("classes/")){
			path = path.replace("classes/", "");
		}
		if(path.endsWith("WEB-INF/")){
			path = path.replace("WEB-INF/", "");
		}
		
		File dir = new File(path + "/attach-temp");
		String time = sdf.format(new Date());
		String filePath = path + "/attach-temp" + File.separator + time + ".xlsx";
		File file = new File(filePath);
		FileOutputStream outputStream = null;
		try {
			if (!FileUtil.dirExists(dir)) {
				dir.mkdir();
			}
			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			ExcelUtil.createExcelFile(excelTitile, fieldMap, dataArray, outputStream);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}

	/**
	 * 获取异常的堆栈信息
	 * 
	 * @param t
	 * @return
	 */
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

}
