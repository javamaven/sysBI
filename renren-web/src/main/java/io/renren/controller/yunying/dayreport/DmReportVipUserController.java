package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.yunying.dayreport.DmReportVipUserEntity;
import io.renren.service.UserBehaviorService;
import io.renren.service.yunying.dayreport.DmReportVipUserService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MapUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-15 14:07:28
 */
@Controller
@RequestMapping(value = "/yunying/dmreportvipuser")
public class DmReportVipUserController {
	@Autowired
	private DmReportVipUserService service;
	
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="VIP用户明细信息";
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	/**
	 * 
	 */

	/**
	 * 电销vip数据用户导入
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
//			STAT_PERIOD	USER_ID	OLD_USER_ID	CG_USER_ID	USERNAME	REALNAME	PHONE	AWAIT	LV	OWNER	IS_HIGH_VALUE 200w_AWAIT
			String[] fields = {"STAT_PERIOD","OLD_USER_ID","CG_USER_ID","OLD_USERNAME","USERNAME","REALNAME","SEX","OLD_PHONE",
					"PHONE","AWAIT","TOTAL_RECEIPT","LV","OWNER","BALANCE","REG_TIME","LOGIN_TIME","LAST_RECOVER_TIME","LAST_RECOVER_MONEY",
					"LAST_RECHARGE_TIME","LAST_RECHARGE_MONEY","RECHARGE_MONEY_C","VOUCHER_MONEY","LAST_CASH_TIME",
					"LAST_CASH_MONEY","CASH_MONEY_C","INV_COU","AVG_PERIOD","MONTH_TENDER","MONTH_TENDER_Y",
					"MONTH_TENDER_COU","DAY_TENDER","DAY_TENDER_Y","DAY_TENDER_COU","200W_AWAIT"};
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");

			// 清空表
			String tuncate_sql = "truncate table dm_report_vip_user_5 ";
			new JdbcUtil(dataSourceFactory, "oracle26").execute(tuncate_sql);
			// 插入表
			String sql = "insert into dm_report_vip_user_5 values(?,?,?,?,?,?,?,?,?,?,?,?)";
			List<List<Object>> dataList = getInsertDataList(list);
			new JdbcUtil(dataSourceFactory, "oracle26").batchInsert(sql, dataList);
			String unpdateUserId = "update dm_report_vip_user_5 v set user_id = (select u.user_id from edw_user_basic u where 1=1 and u.cg_user_id=v.cg_user_id or u.old_user_id=v.old_user_id)";
			new JdbcUtil(dataSourceFactory, "oracle26").execute(unpdateUserId);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	
	private List<List<Object>> getInsertDataList(List<Map<String, Object>> excelList) {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		List<Object> list = null;
		//只需导入字段STAT_PERIOD	USER_ID	OLD_USER_ID	CG_USER_ID	USERNAME	REALNAME	PHONE	AWAIT	LV	OWNER	IS_HIGH_VALUE
		String STAT_PERIOD = "";
		Map<String,Object> dayMap = new HashMap<>();
		for (int i = 0; i < excelList.size(); i++) {
			list = new ArrayList<Object>();
			Map<String, Object> map = excelList.get(i);
//			System.err.println(map);
			String statPeriod = map.get("STAT_PERIOD") + "";
			statPeriod = statPeriod.replace("-", "");
			if(StringUtils.isEmpty(STAT_PERIOD)){//后面所有的统计日期，全取第一行的
				STAT_PERIOD = statPeriod;
			}
			int oldUserid = 0;
			try {
				oldUserid = (int)Double.parseDouble(map.get("OLD_USER_ID") + "");
			} catch (Exception e) {
				STAT_PERIOD = "";
				e.printStackTrace();
				continue;
			}
//			if(!dayMap.containsKey(STAT_PERIOD)){
//				dayMap.put(STAT_PERIOD, STAT_PERIOD);
//			}
			list.add(STAT_PERIOD);
			list.add(null);//user_id为null
			if(StringUtils.isEmpty(map.get("OLD_USER_ID") + "")){
				list.add(null);
			}else{
				list.add(oldUserid);
			}
			if(StringUtils.isEmpty(map.get("CG_USER_ID") + "")){
				list.add(null);
			}else{
				list.add((int)Double.parseDouble(map.get("CG_USER_ID") + ""));
			}
			
			list.add(map.get("OLD_USERNAME") + "");//名单用户名
			list.add(map.get("REALNAME") + "");
			list.add(map.get("OLD_PHONE") + "");
			
			list.add(map.get("AWAIT") + "");
			String lv = MapUtil.getValue(map, "LV");
			if(StringUtils.isEmpty(lv)){
				list.add(0);
			}else{
				list.add((int)Double.parseDouble(map.get("LV") + ""));
			}
			
			list.add(map.get("OWNER") + "");
			
			list.add(null);
			String await_200w = MapUtil.getValue(map, "200W_AWAIT");
			if(StringUtils.isEmpty(await_200w)){
				list.add("");
			}else{
				list.add(await_200w);
			}

//			String sql = "insert into dm_report_vip_user_5 values(?,?,?,?,?,?,?,?,?,?,?,?)";
//			if(i > 4000){
//				try {
//					new JdbcUtil(dataSourceFactory, "oracle26").execute(sql, list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),
//							list.get(6),list.get(7),list.get(8),list.get(9),list.get(10),list.get(11));
//				} catch (SQLException e) {
//					System.err.println(list + " ;i=" + i);
//					e.printStackTrace();
//				}
//			}
			dataList.add(list);
		}
		System.err.println(dayMap);
		return dataList;
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
	public R list(Integer page, Integer limit,String statPeriod){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		//查询列表数据
		List<DmReportVipUserEntity> dmReportVipUserList = service.queryList(map);
		int total = service.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportVipUserList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportVipUserEntity> dataList = service.queryList(map);
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportVipUserEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "VIP用户数据报告";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
}
