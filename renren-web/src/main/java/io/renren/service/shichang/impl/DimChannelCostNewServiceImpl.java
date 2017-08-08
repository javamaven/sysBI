package io.renren.service.shichang.impl;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.dao.shichang.DimChannelCostNewDao;
import io.renren.entity.shichang.DimChannelCostNewEntity;
import io.renren.service.shichang.DimChannelCostNewService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;



@Service("dimChannelCostNewService")
public class DimChannelCostNewServiceImpl implements DimChannelCostNewService {
	@Autowired
	DruidDataSource dataSource;
	@Autowired
	DataSourceFactory dataSourceFactory;
	@Autowired
	private DimChannelCostNewDao dimChannelCostNewDao;
	
	@Override
	public DimChannelCostNewEntity queryObject(String statPeriod){
		return dimChannelCostNewDao.queryObject(statPeriod);
	}

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		List<Map<String, Object>> dataList = null;
		String path = this.getClass().getResource("/").getPath();
		try {
			String detail_sql = null;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/市场部/市场部费用录入.txt"));
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
		// return dimChannelCostNewDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dimChannelCostNewDao.queryTotal(map);
	}
	
	@Override
	public void save(DimChannelCostNewEntity dimChannelCostNew){
		dimChannelCostNewDao.save(dimChannelCostNew);
	}
	
	@Override
	public void update(DimChannelCostNewEntity dimChannelCostNew){
		dimChannelCostNewDao.update(dimChannelCostNew);
	}
	
	@Override
	public void delete(String statPeriod){
		dimChannelCostNewDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dimChannelCostNewDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("CHANNEL_NAME", "渠道名称");
		headMap.put("CHANNEL_LABEL", "渠道标记");
		headMap.put("RECHARGE", "累计充值");
		
		headMap.put("COST", "累计费用");
		headMap.put("BALANCE", "渠道账户余额");
		headMap.put("LAST_RECHARGE_TIME", "最近充值记录");
		
		headMap.put("LAST_COST_TIME", "最近费用记录");
		headMap.put("LAST_REG_TIME", "最近注册天数");
		
		return headMap;
	}

	@Override
	public void batchInsert(List<Map<String, Object>> dataList) {
		// TODO Auto-generated method stub
		dimChannelCostNewDao.batchInsert(dataList);
	}
	
}
