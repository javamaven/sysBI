package io.renren.service.yunying.dayreport.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportVipSituationDao;
import io.renren.entity.yunying.dayreport.DmReportVipSituationEntity;
import io.renren.service.yunying.dayreport.DmReportVipSituationService;

@Service("dmReportVipSituationService")
public class DmReportVipSituationServiceImpl implements DmReportVipSituationService {
	@Autowired
	private DmReportVipSituationDao dmReportVipSituationDao;

	@Override
	public DmReportVipSituationEntity queryObject(BigDecimal statPeriod) {
		return dmReportVipSituationDao.queryObject(statPeriod);
	}

	@Override
	public List<DmReportVipSituationEntity> queryList(Map<String, Object> map) {
		List<DmReportVipSituationEntity> list = dmReportVipSituationDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportVipSituationEntity entity = list.get(i);
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
		return dmReportVipSituationDao.queryTotal(map);
	}

	@Override
	public void save(DmReportVipSituationEntity dmReportVipSituation) {
		dmReportVipSituationDao.save(dmReportVipSituation);
	}

	@Override
	public void update(DmReportVipSituationEntity dmReportVipSituation) {
		dmReportVipSituationDao.update(dmReportVipSituation);
	}

	@Override
	public void delete(BigDecimal statPeriod) {
		dmReportVipSituationDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(BigDecimal[] statPeriods) {
		dmReportVipSituationDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "时间");

		headMap.put("owner", "所属人");
		headMap.put("dayUserCou", "当天新增投资人数");
		headMap.put("dayTenderCou", "当天新增投资次数");

		headMap.put("dayTenderY", "当天新增年化投资金额");
		headMap.put("dayTender", "当天新增投资金额");

		return headMap;
	}

}
