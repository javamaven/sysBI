package io.renren.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.ChannelRenewDataEntity;
import io.renren.service.ChannelRenewDataService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/renew")
public class ChanneRenewDataController extends AbstractController {
	@Autowired
	private ChannelRenewDataService service;

	@Autowired
	private DruidDataSource dataSource;

	@Autowired
	private DataSourceFactory dataSourceFactory;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 查询渠道流失分析列表
	 * 
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping("/queryTest")
	@RequiresPermissions("channel:channelAll:list")
	public R queryTest(@RequestBody Map<String, Object> params) throws ParseException, SQLException {
		long l1 = System.currentTimeMillis();
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = util.query("select * from excel_data limit 10");

		// JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		// String procedureSql = "call first_invest_year_roi_renew_day90(? ,
		// ?)";
		// List<Map<String, Object>> list =
		// jdbcHelper.callableQuery(procedureSql, "2017-02-02 00:00:00",
		// "2017-05-03 23:59:59");
		long l2 = System.currentTimeMillis();
		System.err.println("list=" + list);
		System.err.println("耗时=" + (l2 - l1));

		return null;
	}

	/**
	 * 查询渠道流失分析列表
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("/queryChannelRenewDataList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelRenewDataList(@RequestBody Map<String, Object> params) throws ParseException {
		long startTime = System.currentTimeMillis();
		PageUtils pageUtil = service.query(params);
		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));

		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("channel:channelAll:list")
	public void partExport(String list, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<ChannelRenewDataEntity> dataList = JSON.parseArray(list, ChannelRenewDataEntity.class);
		JSONArray va = new JSONArray();
		//
		for (int i = 0; i < dataList.size(); i++) {
			ChannelRenewDataEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();

		String title = "渠道续费数据提取汇总";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
