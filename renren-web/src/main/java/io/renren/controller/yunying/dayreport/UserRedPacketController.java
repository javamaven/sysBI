package io.renren.controller.yunying.dayreport;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

@Controller
@RequestMapping(value = "/yunying/userred")
public class UserRedPacketController {


	@Autowired
	private DataSourceFactory dataSourceFactory;

	/**
	 * 上传文件
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	@RequiresPermissions("phonesale:upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String[] fields = { "number", "user_name", "real_name", "register_time", "call_sale", "call_date",
					"call_result", "is_invest", "real_invest_amount", "sale_jiangli_amount", "year_invest_amount",
					"first_invest_time" };
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");

			// 清空表
			String tuncate_sql = "truncate table phone_sale_excel_data ";
			new JdbcUtil(dataSourceFactory, "oracle26").execute(tuncate_sql);
			// 插入表
			String sql = "insert into phone_sale_excel_data values(?,?,?,?,?,?)";
			List<List<Object>> dataList = getInsertDataList(list);
			new JdbcUtil(dataSourceFactory, "oracle26").batchInsert(sql, dataList);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		return R.ok();
	}

	/**
	 * P2P列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("phonesale:list")
	public R daylist(Integer page, Integer limit, String begin_time,String end_time,String huodong_name,
			String hongbao_name,String hongbao_id, String user_type,String channelName, 
			String userName,String userId) {
		int start = (page - 1) * limit;
		int end = start + limit;
		
		if (StringUtils.isNotEmpty(userId)) {
			userId=" AND ID like '%"+userId+"%'";
		}else{
			userId="";
		}

		if (StringUtils.isNotEmpty(huodong_name)) {
			huodong_name=" AND PURPOSE like '%"+huodong_name+"%'";
		}else{
			huodong_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name)) {
			hongbao_name=" AND NAME like '%"+hongbao_name+"%'";
		}else{
			hongbao_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id)) {
			hongbao_id=" AND WEIZHI like '%"+hongbao_id+"%'";
		}else{
			hongbao_id="";
		}
		if (StringUtils.isNotEmpty(user_type)) {
			user_type=" AND TYPE like '%"+user_type+"%'";
		}else{
			user_type="";
		}
		if (StringUtils.isNotEmpty(channelName)) {
			channelName=" AND channelName like '%"+channelName+"%'";
		}else{
			channelName="";
		}
		
		if (StringUtils.isNotEmpty(userName)) {
			userName=" AND REALNAME like '%"+userName+"%'";
		}else{
			userName="";
		}
		
		long l1 = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_mx.txt"));
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${userId}", userId);
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${huodong_name}", huodong_name);
			detail_sql = detail_sql.replace("${userName}", userName);
			detail_sql = detail_sql.replace("${channelName}", channelName);
			detail_sql = detail_sql.replace("${user_type}", user_type);
			detail_sql = detail_sql.replace("${hongbao_id}", hongbao_id);
			detail_sql = detail_sql.replace("${hongbao_name}", hongbao_name);
			detail_sql = detail_sql.replace("${pageStartSql}", " and RN > " + start + "");
			detail_sql = detail_sql.replace("${pageEndSql}", " and RN <= " + end + "");
			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = 0;
		
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_mx.txt"));
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${userId}", userId);
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${huodong_name}", huodong_name);
			detail_sql = detail_sql.replace("${userName}", userName);
			detail_sql = detail_sql.replace("${channelName}", channelName);
			detail_sql = detail_sql.replace("${user_type}", user_type);
			detail_sql = detail_sql.replace("${hongbao_id}", hongbao_id);
			detail_sql = detail_sql.replace("${hongbao_name}", hongbao_name);
			detail_sql = detail_sql.replace("${pageStartSql}", "");
			detail_sql = detail_sql.replace("${pageEndSql}", "");
			detail_sql = detail_sql.replace("${selectSql}", " COUNT(1) TOTAL ");
			List<Map<String, Object>> total_list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			Map<String, Object> map = total_list.get(0);
			total = Integer.parseInt(map.get("TOTAL").toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++红包明细查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/ddylist")
	public R daylist1(Integer page, Integer limit, String yingxiao_begin,String yingxiao_end,String touzi_begin,
			String touzi_end,String hongbao_begin, String hongbao_end,String channelName1, String userName1,
			String userType,String userId1) {
		long l1 = System.currentTimeMillis();
		int start = (page - 1) * limit;
		int end = start + limit;
		
		
		if (StringUtils.isNotEmpty(touzi_begin)) {
			touzi_begin=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin+"' and '"+touzi_end+"'";
		}else{
			touzi_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_begin)) {
			hongbao_begin=" AND USEABLEMONEY >='  "+hongbao_begin+"'";
		}else{
			hongbao_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_end)) {
			hongbao_end=" AND USEABLEMONEY <='  "+hongbao_end+"'";
		}else{
			hongbao_end="";
		}
		
		if (StringUtils.isNotEmpty(channelName1)) {
			channelName1=" AND channelName like '%"+channelName1+"%'";
		}else{
			channelName1="";
		}

		if (StringUtils.isNotEmpty(userName1)) {
			userName1=" AND userName like '%"+userName1+"%'";
		}else{
			userName1="";
		}
		
		if (StringUtils.isNotEmpty(userType)) {
			userType=" AND userType like '%"+userType+"%'";
		}else{
			userType="";
		}
		
		if (StringUtils.isNotEmpty(userId1)) {
			userId1=" AND ID like '%"+userId1+"%'";
		}else{
			userId1="";
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
			detail_sql = detail_sql.replace("${userType}",userType );
			detail_sql = detail_sql.replace("${userId1}",userId1 );
			detail_sql = detail_sql.replace("${userName1}",userName1 );
			detail_sql = detail_sql.replace("${pageStartSql}", " and RN > " + start + "");
			detail_sql = detail_sql.replace("${pageEndSql}", " and RN <= " + end + "");
			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = 0;
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
			detail_sql = detail_sql.replace("${userType}",userType );
			detail_sql = detail_sql.replace("${userId1}",userId1 );
			detail_sql = detail_sql.replace("${userName1}",userName1 );
			detail_sql = detail_sql.replace("${pageStartSql}", "");
			detail_sql = detail_sql.replace("${pageEndSql}", "");
			detail_sql = detail_sql.replace("${selectSql}", " COUNT(1) TOTAL ");
			List<Map<String, Object>> total_list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			Map<String, Object> map = total_list.get(0);
			total = Integer.parseInt(map.get("TOTAL").toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		long l2 = System.currentTimeMillis();

		System.err.println("++++++++asdsad查询耗时：" + (l2 - l1));
		return R.ok().put("page", pageUtil);
		
		
	}

	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String end_time = map.get("end_time") + "";
		String userId = map.get("userId") + "";
		String begin_time = map.get("begin_time") + "";
		String huodong_name = map.get("huodong_name") + "";
		String userName = map.get("userName") + "";
		String channelName = map.get("channelName") + "";
		String user_type = map.get("user_type") + "";
		String hongbao_id = map.get("hongbao_id") + "";
		String hongbao_name = map.get("hongbao_name") + "";
	
	
		
		if (StringUtils.isNotEmpty(userId)) {
			userId=" AND ID like '%"+userId+"%'";
		}else{
			userId="";
		}

		if (StringUtils.isNotEmpty(huodong_name)) {
			huodong_name=" AND PURPOSE like '%"+huodong_name+"%'";
		}else{
			huodong_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_name)) {
			hongbao_name=" AND NAME like '%"+hongbao_name+"%'";
		}else{
			hongbao_name="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_id)) {
			hongbao_id=" AND WEIZHI like '%"+hongbao_id+"%'";
		}else{
			hongbao_id="";
		}
		if (StringUtils.isNotEmpty(user_type)) {
			user_type=" AND TYPE like '%"+user_type+"%'";
		}else{
			user_type="";
		}
		if (StringUtils.isNotEmpty(channelName)) {
			channelName=" AND channelName like '%"+channelName+"%'";
		}else{
			channelName="";
		}
		
		if (StringUtils.isNotEmpty(userName)) {
			userName=" AND REALNAME like '%"+userName+"%'";
		}else{
			userName="";
		}
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_mx.txt"));
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${userId}", userId);
			detail_sql = detail_sql.replace("${begin_time}", begin_time);
			detail_sql = detail_sql.replace("${huodong_name}", huodong_name);
			detail_sql = detail_sql.replace("${userName}", userName);
			detail_sql = detail_sql.replace("${channelName}", channelName);
			detail_sql = detail_sql.replace("${user_type}", user_type);
			detail_sql = detail_sql.replace("${hongbao_id}", hongbao_id);
			detail_sql = detail_sql.replace("${hongbao_name}", hongbao_name);
			detail_sql = detail_sql.replace("${pageStartSql}", "");
			detail_sql = detail_sql.replace("${pageEndSql}", "");
			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String hongbao_begin = map.get("hongbao_begin") + "";
		String hongbao_end = map.get("hongbao_end") + "";
		String yingxiao_begin = map.get("yingxiao_begin") + "";
		String yingxiao_end = map.get("yingxiao_end") + "";
		String touzi_begin = map.get("touzi_begin") + "";
		String channelName1 = map.get("channelName1") + "";
		String userType = map.get("userType") + "";
		String userId1 = map.get("userId1") + "";
		String userName1 = map.get("userName1") + "";
		String touzi_end = map.get("touzi_end") + "";
		
	
		
		if (StringUtils.isNotEmpty(touzi_begin)) {
			touzi_begin=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin+"' and '"+touzi_end+"'";
		}else{
			touzi_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_begin)) {
			hongbao_begin=" AND USEABLEMONEY >='  "+hongbao_begin+"'";
		}else{
			hongbao_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_end)) {
			hongbao_end=" AND USEABLEMONEY <='  "+hongbao_end+"'";
		}else{
			hongbao_end="";
		}
		
		if (StringUtils.isNotEmpty(channelName1)) {
			channelName1=" AND channelName like '%"+channelName1+"%'";
		}else{
			channelName1="";
		}

		if (StringUtils.isNotEmpty(userName1)) {
			userName1=" AND userName like '%"+userName1+"%'";
		}else{
			userName1="";
		}
		
		if (StringUtils.isNotEmpty(userType)) {
			userType=" AND userType like '%"+userType+"%'";
		}else{
			userType="";
		}
		
		if (StringUtils.isNotEmpty(userId1)) {
			userId1=" AND ID like '%"+userId1+"%'";
		}else{
			userId1="";
		}
		
		
		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
			detail_sql = detail_sql.replace("${userType}",userType );
			detail_sql = detail_sql.replace("${userId1}",userId1 );
			detail_sql = detail_sql.replace("${userName1}",userName1 );
			detail_sql = detail_sql.replace("${pageStartSql}", "");
			detail_sql = detail_sql.replace("${pageEndSql}", "");
			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList2.addAll(list);
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
		Map<String, String> headMap = getDayListExcelFields();
		String title = "用户收到红包明细";
		
		// 查询列表数据
		JSONArray va2 = new JSONArray();
		for (int i = 0; i < resultList2.size(); i++) {
			va2.add(resultList2.get(i));
		}
		Map<String, String> headMap2 = getDayListExcelFields2();
		String title2 = "用户获得红包累计情况";

		List<String> titleList = new ArrayList<>();
		titleList.add(title);
		titleList.add(title2);

		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
		headMapList.add(headMap);
		headMapList.add(headMap2);

		
		List<JSONArray> ja = new ArrayList<JSONArray>();
		ja.add(va);
		ja.add(va2);

		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
		
	}

	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void exportQingKuangListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String hongbao_begin = map.get("hongbao_begin") + "";
		String hongbao_end = map.get("hongbao_end") + "";
		String yingxiao_begin = map.get("yingxiao_begin") + "";
		String yingxiao_end = map.get("yingxiao_end") + "";
		String touzi_begin = map.get("touzi_begin") + "";
		String channelName1 = map.get("channelName1") + "";
		String userType = map.get("userType") + "";
		String userId1 = map.get("userId1") + "";
		String userName1 = map.get("userName1") + "";
		String touzi_end = map.get("touzi_end") + "";
		
	
		
		if (StringUtils.isNotEmpty(touzi_begin)) {
			touzi_begin=" AND to_char(a.addtime,'yyyy-mm-dd') between '"+touzi_begin+"' and '"+touzi_end+"'";
		}else{
			touzi_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_begin)) {
			hongbao_begin=" AND USEABLEMONEY >='  "+hongbao_begin+"'";
		}else{
			hongbao_begin="";
		}
		
		if (StringUtils.isNotEmpty(hongbao_end)) {
			hongbao_end=" AND USEABLEMONEY <='  "+hongbao_end+"'";
		}else{
			hongbao_end="";
		}
		
		if (StringUtils.isNotEmpty(channelName1)) {
			channelName1=" AND channelName like '%"+channelName1+"%'";
		}else{
			channelName1="";
		}

		if (StringUtils.isNotEmpty(userName1)) {
			userName1=" AND userName like '%"+userName1+"%'";
		}else{
			userName1="";
		}
		
		if (StringUtils.isNotEmpty(userType)) {
			userType=" AND userType like '%"+userType+"%'";
		}else{
			userType="";
		}
		
		if (StringUtils.isNotEmpty(userId1)) {
			userId1=" AND ID like '%"+userId1+"%'";
		}else{
			userId1="";
		}
		
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/user_red_packer_qk.txt"));
			detail_sql = detail_sql.replace("${hongbao_begin}",hongbao_begin );
			detail_sql = detail_sql.replace("${hongbao_end}",hongbao_end );
			detail_sql = detail_sql.replace("${yingxiao_begin}",yingxiao_begin );
			detail_sql = detail_sql.replace("${yingxiao_end}",yingxiao_end );
			detail_sql = detail_sql.replace("${touzi_begin}",touzi_begin );
			detail_sql = detail_sql.replace("${channelName1}",channelName1 );
			detail_sql = detail_sql.replace("${userType}",userType );
			detail_sql = detail_sql.replace("${userId1}",userId1 );
			detail_sql = detail_sql.replace("${userName1}",userName1 );
			detail_sql = detail_sql.replace("${pageStartSql}", "");
			detail_sql = detail_sql.replace("${pageEndSql}", "");
			detail_sql = detail_sql.replace("${selectSql}", " a.* ");
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
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
		String title = "用户获得红包累计情况";
		headMap = getDayListExcelFields2();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("D_DATE", "查询日期");
		headMap.put("ID", "用户id");
		headMap.put("USERNAME", "用户名");
		headMap.put("REALNAME", "用户姓名");
		headMap.put("PHONE", "用户手机号");
		headMap.put("PURPOSE", "用户参与活动描述");
		headMap.put("NAME", "红包名称");
		headMap.put("WEIZHI", "红包模板id");
		headMap.put("HBTIME", "参与活动日期");
		headMap.put("HBMONEY", "收到红包金额");
		headMap.put("USEMONEY", "使用红包金额");
		headMap.put("RATE", "红包抵扣率");
		
		headMap.put("ANNUALIZED", "是否现金红包");
		headMap.put("RETIME", "获取红包时间");
		headMap.put("ENDTIME", "红包失效时间");
		headMap.put("USERTIMES", "红包使用次数");
		headMap.put("TENDER", "使用红包投资金额");
		headMap.put("SUMTENDER", "使用红包年化投资金额");
		headMap.put("ROI", "ROI");
		
		headMap.put("NROI", "年化ROI");
		headMap.put("AW", "用户待收金额");
		headMap.put("TYPE", "用户分类标签");
		headMap.put("CHANNELNAME", "渠道名称");
		return headMap;

	}
	
	private Map<String, String> getDayListExcelFields2() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("D_DAY", "查询日期");
		headMap.put("ID", "用户id");
		headMap.put("USERNAME", "用户名");
		headMap.put("REALNAME", "用户姓名");
		headMap.put("PHONE", "用户手机号");
		headMap.put("ATI_TIME", "最近一次营销日期");
		headMap.put("DIF", "最近一次营销距今天数");
		headMap.put("COUT_HB", "获得红包个数");
		headMap.put("COUNTU", "使用红包个数");
		headMap.put("GMONEY", "获得红包总金额");
		headMap.put("USEMONEY", "使用红包金额");
		headMap.put("USEABLEMONEY", "剩余可用红包金额");
		
		headMap.put("MONEYI", "使用红包金额占比");
		headMap.put("USERTIME", "使用红包个数占比");
		headMap.put("TIMEI", "投资使用红包次数占比");
		headMap.put("TENDER", "使用红包投资金额");
		headMap.put("SUMTENDER", "使用红包年化投资金额");
		headMap.put("ROI", "使用红包投资ROI");
		headMap.put("NROI", "使用红包年化投资ROI");
		
		headMap.put("AW", "用户待收金额");
		
		
		headMap.put("CHANNELNAME", "渠道名称");
		headMap.put("TYPE", "用户分类标签");
		return headMap;

	}
	




	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");

	private List<List<Object>> getInsertDataList(List<Map<String, Object>> excelList) {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		List<Object> list = null;
		for (int i = 0; i < excelList.size(); i++) {
			list = new ArrayList<Object>();
			Map<String, Object> map = excelList.get(i);
			list.add(map.get("user_name") + "");
			list.add(2);
			try {
				list.add(dateSdf.parse(map.get("call_date") + ""));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			list.add(map.get("call_result") + "");// 打电话结果
			list.add(map.get("call_sale") + "");// 电销人员
			list.add(null);

			dataList.add(list);
		}
		return dataList;
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
	 * 电销日报月报：列表分页列表
	 * 
	 * @author Administrator
	 *
	 */
	class QueryListThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> resultList;
		private int start;
		private int end;
		private String reportType;// day , month
		private String investEndTime;

		public QueryListThread(DataSourceFactory dataSourceFactory, List<Map<String, Object>> resultList, int start,
				int end, String reportType, String investEndTime) {
			this.dataSourceFactory = dataSourceFactory;
			this.resultList = resultList;
			this.start = start;
			this.end = end;
			this.reportType = reportType;
			this.investEndTime = investEndTime == null ? "" : investEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				if ("day".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_day_detail_sql.txt"));
				} else if ("month".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_month_detail_sql.txt"));
				}
				// 分页查询
				detail_sql = detail_sql.replace("${selectSql}", "*");
				detail_sql = detail_sql.replace("${pageStartSql}", "and RN >= " + start);
				detail_sql = detail_sql.replace("${pageEndSql}", "and ROWNUM <= " + end);
				detail_sql = detail_sql.replace("${investEndTime}", investEndTime);
				List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
				resultList.addAll(list);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 电销月报：列表总行数
	 * 
	 * @author Administrator
	 *
	 */
	class QueryListTotalThread implements Runnable {
		private DataSourceFactory dataSourceFactory;
		private List<Map<String, Object>> totalList;
		private String reportType;
		private String investEndTime;

		public QueryListTotalThread(DataSourceFactory dataSourceFactory, List<Map<String, Object>> totalList,
				String reportType, String investEndTime) {
			this.dataSourceFactory = dataSourceFactory;
			this.totalList = totalList;
			this.reportType = reportType;
			this.investEndTime = investEndTime == null ? "" : investEndTime;
		}

		@Override
		public void run() {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = null;
			try {
				if ("day".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_day_detail_sql.txt"));
				} else if ("month".equals(reportType)) {
					detail_sql = FileUtil
							.readAsString(new File(path + File.separator + "phone_sale_month_detail_sql.txt"));
				}
				detail_sql = detail_sql.replace("${selectSql}", "count(1) total");
				detail_sql = detail_sql.replace("${pageStartSql}", "");
				detail_sql = detail_sql.replace("${pageEndSql}", "");
				detail_sql = detail_sql.replace("${investEndTime}", investEndTime);
				totalList.addAll(new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
