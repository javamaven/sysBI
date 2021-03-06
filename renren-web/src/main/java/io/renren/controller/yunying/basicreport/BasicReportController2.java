package io.renren.controller.yunying.basicreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
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
import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/basicreport")
public class BasicReportController2 {
	@Autowired
	private BasicReportService service;
	
	
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;
	
	/**
	 * 新规则电销数据推送
	 * @param page
	 * @param limit
	 * * @param type 免费，付费，cps，邀请
	 * @param registerStartTime
	 * @param registerEndTime
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/phoneSaleDataSend")
	public R phoneSaleDataSend(Integer page, Integer limit, String type, String registerStartTime, String registerEndTime) throws Exception {
		String reportType="电销数据推送";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		Map<String, Object> params = new HashMap<>();
		params.put("registerStartTime", registerStartTime);
		params.put("registerEndTime", registerEndTime);
		params.put("type", type);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		int start = (page - 1) * limit;
		int end = start + limit;
		PageUtils pageUtil = null;
		if("free_channel".equals(type)){
//			pageUtil = service.queryList(page, limit, registerStartTime, registerEndTime, start, end, "list");
			pageUtil = service.queryFreeChannelList(page, limit, registerStartTime, registerEndTime, start, end, "list");
		}else if("pay_channel".equals(type) || "cps_channel".equals(type)){
			list = service.queryPayOrCpsChannelList(params);
		}else if("invited_channel".equals(type)){
			pageUtil = service.queryInvitedChannelList(page, limit, registerStartTime, registerEndTime, start, end, "list");
		}else if("pay_channel_weixin".equals(type)){
			list = service.queryPayOrCpsChannelList(params);
		}else if("pay_channel_sem_xinxiliu".equals(type)){
			pageUtil = service.queryXinxiLiuList(page, limit, registerStartTime, registerEndTime, start, end);
		}else if("pay_channel_app_fenfa".equals(type)){
			list = service.queryPayOrCpsChannelList(params);
		}

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			if (end > list.size()) {
				retList.addAll(list.subList(start, list.size()));
			} else {
				retList.addAll(list.subList(start, end));
			}
		}
		if("pay_channel".equals(type) || "cps_channel".equals(type) || "pay_channel_weixin".equals(type) || "pay_channel_app_fenfa".equals(type)){
			pageUtil = new PageUtils(retList, list.size(), limit, page);
		}
		if (retList.size() > 0) {
			service.getAmontByUserId(retList);// 统计用户的账户余额
		}
		return R.ok().put("page", pageUtil);

	}

	/**
	 * 新规则电销数据推送excel导出09-11
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportPhoneSaleSendDataExcel")
	public void exportPhoneSaleSendDataExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String reportType="电销数据推送";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		String type = map.get("type") + "";
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		PageUtils pageUtil = null;
		if("free_channel".equals(type)){
//			pageUtil = service.queryList(page, limit, registerStartTime, registerEndTime, start, end, "list");
			pageUtil = service.queryFreeChannelList(1, 100000, registerStartTime, registerEndTime, 0, 100000, "list");
			dataList = (List<Map<String, Object>>) pageUtil.getList();
		}else if("pay_channel".equals(type) || "cps_channel".equals(type)){
			dataList = service.queryPayOrCpsChannelList(map);
		}else if("invited_channel".equals(type)){
			pageUtil = service.queryInvitedChannelList(1, 100000, registerStartTime, registerEndTime, 0, 100000, "list");
			dataList = (List<Map<String, Object>>) pageUtil.getList();
		}else if("pay_channel_weixin".equals(type)){
			dataList = service.queryPayOrCpsChannelList(map);
		}else if("pay_channel_sem_xinxiliu".equals(type)){
			pageUtil = service.queryXinxiLiuList(1, 100000, registerStartTime, registerEndTime, 0, 100000);
			dataList = (List<Map<String, Object>>) pageUtil.getList();
		}else if("pay_channel_app_fenfa".equals(type)){
			dataList = service.queryPayOrCpsChannelList(map);
		}
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String year = registerEndTime.substring(0 , 4);
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String Hour = registerEndTime.substring(11 , 13);
		String title = "";
		//将导出的数据记录到电销推送记录表
		insertPhoneSaleData(year+month+day+Hour, dataList, type);
		if("free_channel".equals(type)){
			title = "注册1小时未投资用户(免费渠道)-W-" + month + day + "_" + Hour + "-" + dataList.size();
		}else if("pay_channel".equals(type)){
			title = "注册7天未投资用户(付费渠道)-W-" + month + day + "-" + dataList.size();
		}else if("invited_channel".equals(type)){
			title = "注册5天未投资用户(邀请渠道)-W-" + month + day + "-" + dataList.size();
		}else if("cps_channel".equals(type)){
			title = "注册7天未投资用户(CPS渠道)-W-" + month + day + "-" + dataList.size();
		}else if("pay_channel_weixin".equals(type)){
			title = "注册7天未投资用户(付费-微信公众号)-W-" + month + day + "-" + dataList.size();
		}else if("pay_channel_sem_xinxiliu".equals(type)){
			title = "注册1小时未投资用户(付费-SEM)-W-" + month + day + "_" + Hour + "-" + dataList.size();
		}else if("pay_channel_app_fenfa".equals(type)){
			title = "注册2天未投资用户(付费-应用分发市场)-W-" + month + day + "-" + dataList.size();
		}
		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	private void insertPhoneSaleData(String dataTime, List<Map<String, Object>> dataList, String type) {
		try {
			List<Map<String, String>> insert_data = new ArrayList<Map<String,String>>();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> insert_map = new HashMap<String, String>();
				Map<String, Object> map = dataList.get(i);
				insert_map.put("data", JSON.toJSONString(map));
				insert_map.put("cg_user_id", map.get("用户ID") + "");
				insert_map.put("type", type);
				insert_map.put("data_time", dataTime);
				insert_data.add(insert_map);
			}
			if(insert_data.size() > 0){
				service.batchInsertPhoneSaleJobSendData(insert_data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/queryPhoneSaleCgUserList")
	public R queryPhoneSaleCgUserList(Integer page, Integer limit, String date, String type,String isKaitongCg) {
		String reportType="筛选存管版用户";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("type", type);
		map.put("isKaitongCg", isKaitongCg);
		if (StringUtils.isNotEmpty(date)) {
			map.put("date", date);
		}
		// 查询列表数据
		List<Map<String, Object>> retList = service.queryPhoneSaleCgUserList(map);
		
		int total = service.queryPhoneSaleCgUserTotal(map);
		PageUtils pageUtil = null;
		if("1".equals(type)){
			total = retList.size();
			List<Map<String, Object>> dataList = new ArrayList<>();
			if(total <= limit){
				dataList.addAll(retList);
			}else{
				int fromIndex = (page - 1) * limit;
				int toIndex = fromIndex + limit;
				dataList.addAll(retList.subList(fromIndex, toIndex));
			}
			pageUtil = new PageUtils(dataList , total, limit, page);
		}else{
			 pageUtil = new PageUtils(retList, total, limit, page);
		}
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 电销筛选存管版用户数据 导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/phoneSaleCgUserListExport")
	public void phoneSaleCgUserListExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="筛选存管版用户";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		
		String type = map.get("type") + "";
		map.put("offset", 0);
		map.put("limit", 100000);
		
		// 查询列表数据
		List<Map<String, Object>> dataList = service.queryPhoneSaleCgUserList(map);
		List<String> userIdList = new ArrayList<String>();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map2 = dataList.get(i);
			userIdList.add((int)Double.parseDouble(map2.get("user_id") + "") + "");
			va.add(map2);
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("phone","电话");
		headMap.put( "user_name","用户名");
		headMap.put("real_name","用户姓名");
		
		headMap.put("call_date","电销日期");
		headMap.put("give_date","数据提供日期");
		headMap.put( "cg_user_id","存管ID");
		headMap.put("depository_open_time","存管开户时间");
		String date = DateUtil.getCurrDayStr();

		String title = "";
		if("1".equals(type)){
			title = "外包电销(首投后3天未复投)已筛选数据-" + date;
		}else if("2".equals(type)){
			title = "外包电销(外呼3天后未投资)已筛选数据-" + date;
		}else if("3".equals(type)){
			title = "沉默客户已筛存管ID用户-" + date;
		}
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
			map.put("idList", userIdList);
			if("1".equals(type)){
				UUID randomUUID = UUID.randomUUID();
				for (int i = 0; i < dataList.size(); i++) {
					Map<String, Object> map__ = dataList.get(i);
					map__.put("user_id", (int)Double.parseDouble(map__.get("user_id") + "") + "");
					map__.put("uuid", randomUUID.toString());
					map__.put("type", type);
				}
				service.batchInsertPhoneSaleCgUser(dataList);
			}
			service.updatePhoneSaleCgUserList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传文件(筛选开通存管版用户)
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/importPhoneSaleUser")
	public R upload(@RequestParam("file") MultipartFile file, String type) {
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String originalFilename = file.getOriginalFilename();
			if("1".equals(type) && originalFilename.contains("首投后3天未复投")){
			}else if("2".equals(type) && originalFilename.contains("外呼3天后未投资")){
			}else if("3".equals(type) && originalFilename.contains("沉默客户")){
			}else{
				return R.error("导入文件不正确");
			}
			String[] fields = { "user_id", "phone", "user_name", "real_name", "call_date", "give_date"};
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");
			UUID randomUUID = UUID.randomUUID();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				map.put("user_id", (int)Double.parseDouble(map.get("user_id") + "") + "");
				map.put("uuid", randomUUID.toString());
				map.put("type", type);
			}
			service.batchInsertPhoneSaleCgUser(list);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}
	
	/**
	 * MultipartFile 转换成File
	 * 
	 * @param multfile
	 *            原文件类型
	 * @return File
	 * @throws IOException
	 */
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
	 * 电销外呼申请历史数据（注册未投资用户） 导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/phoneSaleHistoryListExport")
	public void phoneSaleHistoryListExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="注册未投资";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		int limitNum = 100000;
		String limitNumStr = map.get("limitNum") + "";
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		if(StringUtils.isNotEmpty(limitNumStr)){
			limitNum = Integer.parseInt(limitNumStr);
		}
		R retData = phoneSaleHistory(1, limitNum, registerStartTime, registerEndTime, limitNumStr);
		PageUtils page = (PageUtils) retData.get("page");
		
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
//		 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("存管版用户ID", "存管版用户ID");
		headMap.put("用户名", "用户名");
		headMap.put("电话", "电话");
		
		headMap.put("注册时间", "注册时间");
		headMap.put("用户来源", "用户来源");
		headMap.put("实名认证", "实名认证");
		
		headMap.put("红包类型", "红包类型");
		headMap.put("有效期", "有效期");
		
		
		headMap.put("真实姓名", "真实姓名");
		headMap.put("普通版账户余额", "普通版账户余额");
		headMap.put("存管版账户余额", "存管版账户余额");
		
		headMap.put("普通版用户ID", "普通版用户ID");
		headMap.put("存管版是否开户", "存管版是否开户");
		headMap.put("用户ID", "用户ID");
		
		String title = "外包外呼-注册后未投资用户";
		
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
			//保存已经导出的数据到表phone_sale_history_export
			service.batchInsertPhoneSaleData(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 电销外呼申请历史数据（注册未投资用户）
	 * @param page
	 * @param limit
	 * @param registerStartTime
	 * @param registerEndTime
	 * @param limitNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/phoneSaleHistoryList")
	public R phoneSaleHistory(Integer page, Integer limit, String registerStartTime, String registerEndTime, String limitNum) {
		String reportType="注册未投资";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		
		int size = 0;
		if(StringUtils.isNotEmpty(limitNum)){
			size = Integer.parseInt(limitNum);
		}
		if(size < limit && size > 0){
			limit = size;
		}
		int start = (page - 1) * limit;
		int end = start + limit;
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		try {
			registerStartTime += " 00:00:00";
			registerEndTime += " 23:59:59";
			Thread queryThread = new Thread(new QueryPhoneSaleHistoryListThread("list", dataSourceFactory, resultList,
					start, end, registerStartTime, registerEndTime));
			Thread totalThread = new Thread(new QueryPhoneSaleHistoryListThread("total", dataSourceFactory, totalList,
					start, end, registerStartTime, registerEndTime));
			queryThread.start();
			totalThread.start();
			queryThread.join();
			totalThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int total = Integer.parseInt(totalList.get(0).get("TOTAL").toString());
		if(total > size && size >0){
			total = size;
		}
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);

		return R.ok().put("page", pageUtil);

	}
	
	class QueryPhoneSaleHistoryListThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> list;
		private String statType;
		private int start;
		private int end;
		private String registerStartTime;
		private String registerEndTime;

		public QueryPhoneSaleHistoryListThread(String statType, DataSourceFactory dataSourceFactory, List<Map<String, Object>> list,
				int start, int end, String registerStartTime, String registerEndTime) {
			this.statType = statType;
			this.dataSourceFactory = dataSourceFactory;
			this.list = list;
			this.start = start;
			this.end = end;
			this.registerStartTime = registerStartTime == null ? "" : registerStartTime;
			this.registerEndTime = registerEndTime == null ? "" : registerEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/外包外呼历史数据提取.txt"));
				
				if("list".equals(statType)){
					detail_sql = detail_sql.replace("${selectSql}", " * ");
					detail_sql = detail_sql.replace("${pageStartSql}", " and RN >= " + start);
					detail_sql = detail_sql.replace("${pageEndSql}", " and ROWNUM <= " + end);
					
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, registerStartTime, registerEndTime));
				}else if("total".equals(statType)){
					detail_sql = detail_sql.replace("${selectSql}", "count(1) total");
					detail_sql = detail_sql.replace("${pageStartSql}","");
					detail_sql = detail_sql.replace("${pageEndSql}", "");
					list.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql, registerStartTime, registerEndTime));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	@ResponseBody
	@RequestMapping("/registNotInvest")
	public R registNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {
		String reportType="注册一小时未投资用户";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		
		int start = (page - 1) * limit;
		int end = start + limit;
		PageUtils pageUtil = service.queryList(page, limit, registerStartTime, registerEndTime, start, end, "list");

		return R.ok().put("page", pageUtil);

	}
	
	@ResponseBody
	@RequestMapping("/firstInvestNotMulti")
	public R firstInvestNotMulti(Integer page, Integer limit, String statPeriod) {
		String reportType="本月首投3天未复投用户";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("limit", limit);
		map.put("statPeriod", statPeriod);
		PageUtils pageUtil = service.queryFirstInvestNotMultiList(map);
		return R.ok().put("page", pageUtil);

	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportFirstInvestNotMultiExcel")
	public void exportFirstInvestNotMultiExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="本月首投3天未复投用户";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		PageUtils page = service.queryFirstInvestNotMultiList(map);
		
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFirstInvestNotMultiFields();
		String title = "本月注册首投后三天未复投用户";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	/**
	 * 注册三天未投资用户数据
	 * 
	 * @param page
	 * @param limit
	 * @param registerStartTime
	 * @param registerEndTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/registThreeDaysNotInvest")
	public R registThreeDaysNotInvest(Integer page, Integer limit, String registerStartTime, String registerEndTime) {
		String reportType="注册三天未投资";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
		int start = (page - 1) * limit;
		int end = start + limit;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registerStartTime", registerStartTime);
		params.put("registerEndTime", registerEndTime);
		List<Map<String, Object>> list = service.queryRegisterThreeDaysNotInvestList(params);

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (list.size() > 0) {
			if (end > list.size()) {
				retList.addAll(list.subList(start, list.size()));
			} else {
				retList.addAll(list.subList(start, end));
			}
		}
		if (retList.size() > 0) {
			service.getAmontByUserId(retList);// 统计用户的账户余额
		}

		PageUtils pageUtil = new PageUtils(list, list.size(), limit, page);

		return R.ok().put("page", pageUtil);

	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="注册一小时未投资";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerStartTime = map.get("registerStartTime") + "";
		String registerEndTime = map.get("registerEndTime") + "";
		PageUtils page = service.queryList(1, 10000, registerStartTime, registerEndTime, 0, 10000, "list");
		List<Map<String, String>> dataList = (List<Map<String, String>>) page.getList();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String Hour = registerEndTime.substring(11 , 13);
		String title = "注册一小时未投资用户-W-" + month + day + "_" + Hour + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	
	/**
	 * 注册三天未投资导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportRegisterThreeDaysExcel")
	public void exportRegisterThreeDaysExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String reportType="注册三天未投资";
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String registerEndTime = map.get("registerEndTime") + "";
		List<Map<String, Object>> dataList = service.queryRegisterThreeDaysNotInvestList(map);
		service.getAmontByUserId(dataList);// 统计用户的账户余额
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = service.getExcelFields();
		String month = registerEndTime.substring(5 , 7);
		String day = registerEndTime.substring(8 , 10);
		String title = "注册3天未投资用户-W-" + month + day + "-" + dataList.size();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
