package io.renren.service.yunying.dayreport.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportActiveChannelCostDao;
import io.renren.entity.yunying.dayreport.DmReportActiveChannelCostEntity;
import io.renren.service.yunying.dayreport.DmReportActiveChannelCostService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;

@Service("dmReportActiveChannelCostService")
public class DmReportActiveChannelCostServiceImpl implements DmReportActiveChannelCostService {
	@Autowired
	private DmReportActiveChannelCostDao dmReportActiveChannelCostDao;

	@Override
	public DmReportActiveChannelCostEntity queryObject(BigDecimal statPeriod) {
		return dmReportActiveChannelCostDao.queryObject(statPeriod);
	}

	@Autowired
	private DataSourceFactory dataSourceFactory;

	@Override
	public List<Map<String, Object>> queryCostList(Map<String, Object> map) {
		String query_sql = null;
		List<Map<String, Object>> list = null;
		try {
			String statPeriod = map.get("statPeriod") + "";
			String path = this.getClass().getResource("/").getPath();
			query_sql = FileUtil.readAsString(new File(path + File.separator + "/sql/活动渠道成本数据报告.txt"));
			if(StringUtils.isNotEmpty(statPeriod) && !"null".equals(statPeriod)){
				query_sql = query_sql.replace("${day}", " and to_char(a.use_time, 'yyyy-mm-dd')='" + statPeriod + "'");
			}else{
				query_sql = query_sql.replace("${day}", "");
			}
			list = new JdbcUtil(dataSourceFactory, "oracle26").query(query_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<DmReportActiveChannelCostEntity> queryList(Map<String, Object> map) {
		List<DmReportActiveChannelCostEntity> list = dmReportActiveChannelCostDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportActiveChannelCostEntity entity = list.get(i);
			String statPeriod = entity.getStatPeriod();
			if (statPeriod.length() == 8) {
				String year = statPeriod.substring(0, 4);
				String month = statPeriod.substring(4, 6);
				String day = statPeriod.substring(6, 8);
				entity.setStatPeriod(year + "-" + month + "-" + day);
			}
		}
		return list;
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return dmReportActiveChannelCostDao.queryTotal(map);
	}

	@Override
	public void save(DmReportActiveChannelCostEntity dmReportActiveChannelCost) {
		dmReportActiveChannelCostDao.save(dmReportActiveChannelCost);
	}

	@Override
	public void update(DmReportActiveChannelCostEntity dmReportActiveChannelCost) {
		dmReportActiveChannelCostDao.update(dmReportActiveChannelCost);
	}

	@Override
	public void delete(BigDecimal statPeriod) {
		dmReportActiveChannelCostDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(BigDecimal[] statPeriods) {
		dmReportActiveChannelCostDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("STATPERIOD", "日期");

		headMap.put("CODE", "渠道标签");
		headMap.put("NAME", "渠道名称");
		headMap.put("COST", "推广成本");
		headMap.put("COSTSOURCE", "成本归属部门");

		return headMap;
	}

}
