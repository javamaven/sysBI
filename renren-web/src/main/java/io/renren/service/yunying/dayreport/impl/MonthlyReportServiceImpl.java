package io.renren.service.yunying.dayreport.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.MonthlyReportDao;
import io.renren.entity.yunying.dayreport.MonthlyReportEntity;
import io.renren.service.yunying.dayreport.MonthlyReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.utils.PageUtils;

@Service("Service")
public class MonthlyReportServiceImpl implements MonthlyReportService {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private MonthlyReportDao monthlyReportDao;

	@Override
	public MonthlyReportEntity queryObject(BigDecimal statPeriod) {
		return monthlyReportDao.queryObject(statPeriod);
	}

	@Override
	public List<MonthlyReportEntity> queryList(Map<String, Object> map) {
		return monthlyReportDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return monthlyReportDao.queryTotal(map);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("STAT_TYPE", "指标明细");
		headMap.put("STAT_NUM", "指标");
		return headMap;
	}
	@Override
	public Map<String, String> getExcelFields2() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("REALNAME", "名字");
		headMap.put("TENDER_CAPITAL", "金额(万元)");
		return headMap;
	}

	@Override
	public PageUtils queryList(Integer page, Integer limit, String statPeriod) {
//		if (StringUtils.isNotEmpty(invest_end_time)) {
//			invest_end_time = invest_end_time.replace("-", "");
//		}
		int year = Integer.parseInt(statPeriod.substring(0,4));
		int month = Integer.parseInt(statPeriod.substring(6,7));
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);
		lastDayOfMonth = lastDayOfMonth.replace("-", "");
		String firstDay= statPeriod.replace("-", "")+"01";

	
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/P2P.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investStatTime}", firstDay);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		return pageUtil;
	}
	@Override
	public PageUtils queryList1(Integer page, Integer limit, String statPeriod) {
		int year = Integer.parseInt(statPeriod.substring(0,4));
		int month = Integer.parseInt(statPeriod.substring(6,7));
		String lastDayOfMonth = DateUtil.getLastDayOfMonth(year, month);
		lastDayOfMonth = lastDayOfMonth.replace("-", "");
		String firstDay= statPeriod.replace("-", "")+"01";

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/five.txt"));
			detail_sql = detail_sql.replace("${investEndTime}", lastDayOfMonth);
			detail_sql = detail_sql.replace("${investStatTime}", firstDay);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int total = resultList.size();
		PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
		return pageUtil;
	}
	
}
