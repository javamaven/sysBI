package io.renren.controller.shichang;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import io.renren.entity.shichang.DimChannelCostNewEntity;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.service.shichang.DimChannelCostNewService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.Constant;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-08 10:16:36
 */
@Controller
@RequestMapping("dimchannelcostnew")
public class DimChannelCostNewController {
	@Autowired
	private DimChannelCostNewService dimChannelCostNewService;
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	DruidDataSource dataSource;
	
	@Autowired
	private ChannelHeadManagerService service;
	
	/**
	 * 上传文件
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		int total_record = 0;
		int sucess_record = 0;
		List<String> errorList = new ArrayList<String>();
		//1、先删除
		String deleteSql = "delete from dim_channel_cost_new where stat_period=? and channel_label=?";
		//2、插入
		String insertSql = "insert into dim_channel_cost_new values(?,?,?,?)";
		
		try {
			if (file.isEmpty()) {
				throw new RRException("上传文件不能为空");
			}
			String[] fields = { "stat_period", "channel_label", "cost", "recharge"};
			Map<String, Object> retMap = ExcelUtil.parseExcel(multipartToFile(file), null, fields);
			List<Map<String, Object>> list = (List<Map<String, Object>>) retMap.get("list");
			total_record = list.size();//导入总行数
			//查出所有有权限的渠道
			List<String> labelList = new ArrayList<>();
			if(getUserId() != Constant.SUPER_ADMIN && !service.isMarketDirector()){//不是超级管理员
				labelList = service.queryChannelAuthByChannelHead("channel_label");
				System.err.println("+++++++++负责渠道："+labelList);
			}
			//遍历导入的渠道是否有权限
			//有权限则先删后增
			for (int i = 0; i < list.size(); i++) {
				try {
					Map<String, Object> map = list.get(i);
					String stat_period = map.get("stat_period").toString().trim();
					stat_period = stat_period.replace("-", "");
					String cost = map.get("cost") + "";
					String recharge = map.get("recharge") + "";
					String channel_label = map.get("channel_label").toString().trim();
					if(recharge.equals("null")){
						recharge = "";
					}
					if(getUserId() != Constant.SUPER_ADMIN && !service.isMarketDirector()){
						for (int j = 0; j < labelList.size(); j++) {//判断是否有权限
							if(labelList.get(j).trim().equals(channel_label)){
								//删除
								new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql, stat_period, channel_label);
								//插入
								new JdbcUtil(dataSourceFactory, "oracle26").execute(insertSql, stat_period, channel_label,
										cost, recharge);
								sucess_record++;
							}
						}
					}else{
						//删除
						new JdbcUtil(dataSourceFactory, "oracle26").execute(deleteSql, stat_period, channel_label);
						//插入
						new JdbcUtil(dataSourceFactory, "oracle26").execute(insertSql, stat_period, channel_label,
								cost, recharge);
						sucess_record++;
					}
					
				} catch (Exception e) {
					errorList.add(e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error(e.getMessage());
		}
		// 同步数据到mysql成本表
		String querySql = "select * from dim_channel_cost_new  ";
		String truncateSql = "truncate table dim_channel_cost_new ";
		List<Map<String, Object>> dataList = null;
		try {
			new JdbcHelper(dataSource).execute(truncateSql);
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(querySql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		dimChannelCostNewService.batchInsert(dataList);
		/*String batchInsertSql = "insert into dim_channel_cost_new values ";
		String valuesStr = "";
		int record = 0;
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);
			String stat_period = map.get("STAT_PERIOD").toString().trim();
			String channel_label = map.get("CHANNEL_LABEL").toString().trim();
			valuesStr += "('"+stat_period+"','"+channel_label+"',";
			String cost = map.get("COST") == null? "":map.get("COST").toString();
			if(map.get("COST") == null || map.get("COST").toString().trim().equals("")){
				valuesStr += null + ",";
			}else{
				valuesStr += cost + ",";
			}
			String recharge = map.get("RECHARGE") == null? "":map.get("RECHARGE").toString();
			if(map.get("RECHARGE") == null || map.get("RECHARGE").toString().trim().equals("")){
				valuesStr += null + "),";
			}else{
				valuesStr += recharge + "),";
			}
			record++;
			if(record == 100){
				record = 0;
				valuesStr = valuesStr.substring(0, valuesStr.length() - 1);
				try {
					new JdbcHelper(dataSource).execute(batchInsertSql + valuesStr);
					valuesStr = "";
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			if(valuesStr.length() > 0){
				valuesStr = valuesStr.substring(0, valuesStr.length() - 1);
				new JdbcHelper(dataSource).execute(batchInsertSql + valuesStr);
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}*/
		return R.ok().put("total_record", total_record).put("sucess_record", sucess_record).put("error_list", errorList);
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		List<Map<String,Object>> dataList = dimChannelCostNewService.queryList(map);
		
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = dimChannelCostNewService.getExcelFields();
		String title = "渠道费用信息";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		int start = (page - 1) * limit;
		int end = start + limit;
		
		//查询列表数据
		List<Map<String,Object>> dimChannelCostNewList = dimChannelCostNewService.queryList(map);
//		int total = dimChannelCostNewService.queryTotal(map);
		
		List<Map<String,Object>> retList = new ArrayList<>();
		if(dimChannelCostNewList.size() > end){
			retList.addAll(dimChannelCostNewList.subList(start, end));
		}else{
			retList.addAll(dimChannelCostNewList.subList(start, dimChannelCostNewList.size()));
		}
		PageUtils pageUtil = new PageUtils(retList , dimChannelCostNewList.size(), limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	@RequiresPermissions("dimchannelcostnew:info")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DimChannelCostNewEntity dimChannelCostNew = dimChannelCostNewService.queryObject(statPeriod);
		
		return R.ok().put("dimChannelCostNew", dimChannelCostNew);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("dimchannelcostnew:save")
	public R save(@RequestBody DimChannelCostNewEntity dimChannelCostNew){
		dimChannelCostNewService.save(dimChannelCostNew);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("dimchannelcostnew:update")
	public R update(@RequestBody DimChannelCostNewEntity dimChannelCostNew){
		dimChannelCostNewService.update(dimChannelCostNew);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("dimchannelcostnew:delete")
	public R delete(@RequestBody String[] statPeriods){
		dimChannelCostNewService.deleteBatch(statPeriods);
		
		return R.ok();
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
}
