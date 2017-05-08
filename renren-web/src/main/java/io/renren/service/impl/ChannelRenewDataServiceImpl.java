package io.renren.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.dao.ChannelRenewDataDao;
import io.renren.entity.ChannelRenewDataEntity;
import io.renren.service.ChannelRenewDataService;
import io.renren.system.jdbc.JdbcHelper;

@Service("ChannelRenewDataService")
public class ChannelRenewDataServiceImpl implements ChannelRenewDataService {
	@Autowired
	private DruidDataSource dataSource;

	@Autowired
	private ChannelRenewDataDao channelRenewDataDao;

	@Override
	public List<ChannelRenewDataEntity> queryChannelCost(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryChannelCost(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryOnlineTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryOnlineTime(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearAmount(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryYearRoi(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestUserNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestUserNum(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryFirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelRenewDataDao.queryFirstInvestYearRoi(map);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay30FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return channelRenewDataDao.queryDay30FirstInvestYearRoi(map);
//		return queryFirstInvestYearRoiByJdbc(map, 30);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay60FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return channelRenewDataDao.queryDay60FirstInvestYearRoi(map);
//		return queryFirstInvestYearRoiByJdbc(map, 60);
	}

	@Override
	public List<ChannelRenewDataEntity> queryDay90FirstInvestYearRoi(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return channelRenewDataDao.queryDay90FirstInvestYearRoi(map);
//		return queryFirstInvestYearRoiByJdbc(map, 90);
	}

	/**
	 * 通过jdbc查询数据
	 * 
	 * @return
	 */
	public List<ChannelRenewDataEntity> queryFirstInvestYearRoiByJdbc(Map<String, Object> map, int queryDay) {
		long l1 = System.currentTimeMillis();
		List<ChannelRenewDataEntity> retList = new ArrayList<ChannelRenewDataEntity>();
		JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
		String procedureSql = "call first_invest_year_roi_renew(? , ?)";
		List<Map<String, Object>> list = null;
		try {
			list = jdbcHelper.callableQuery(procedureSql, map.get("startDate"), map.get("endDate"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long l2 = System.currentTimeMillis();
		System.err.println("++++++++++sql[" + procedureSql + "][" + map.get("startDate") + "," + map.get("endDate")
				+ "];耗时=" + (l2 - l1));

		transform(retList, list, queryDay);
		return retList;
	}

	public void transform(List<ChannelRenewDataEntity> retList, List<Map<String, Object>> list, int queryDay) {
		ChannelRenewDataEntity entity = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> dataMap = list.get(i);
			entity = new ChannelRenewDataEntity();
			entity.setChannelLabel(dataMap.get("channelLabel") + "");
			if (queryDay == 30) {
				entity.setDay30FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			if (queryDay == 60) {
				entity.setDay60FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			if (queryDay == 90) {
				entity.setDay90FirstInvestYearRoi(Double.valueOf(dataMap.get("firstInvestYearRoi") + ""));
			}
			retList.add(entity);
		}
	}
	

}
