package io.renren.service.yunying.dayreport.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportDdzRemainDao;
import io.renren.entity.yunying.dayreport.DmReportDdzRemainEntity;
import io.renren.service.yunying.dayreport.DmReportDdzRemainService;

@Service("dmReportDdzRemainService")
public class DmReportDdzRemainServiceImpl implements DmReportDdzRemainService {
	@Autowired
	private DmReportDdzRemainDao dmReportDdzRemainDao;

	@Override
	public DmReportDdzRemainEntity queryObject(BigDecimal statPeriod) {
		return dmReportDdzRemainDao.queryObject(statPeriod);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<DmReportDdzRemainEntity> queryList(Map<String, Object> map) {

		List<DmReportDdzRemainEntity> list = dmReportDdzRemainDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportDdzRemainEntity entity = list.get(i);
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
		return dmReportDdzRemainDao.queryTotal(map);
	}

	@Override
	public void save(DmReportDdzRemainEntity dmReportDdzRemain) {
		dmReportDdzRemainDao.save(dmReportDdzRemain);
	}

	@Override
	public void update(DmReportDdzRemainEntity dmReportDdzRemain) {
		dmReportDdzRemainDao.update(dmReportDdzRemain);
	}

	@Override
	public void delete(BigDecimal statPeriod) {
		dmReportDdzRemainDao.delete(statPeriod);
	}

	@Override
	public void deleteBatch(BigDecimal[] statPeriods) {
		dmReportDdzRemainDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");

		headMap.put("username", "用户名");
		headMap.put("realname", "用户姓名");
		headMap.put("phone", "电话");

		headMap.put("regTime", "注册时间");
		headMap.put("availableAmount", "点点赚余额");
		headMap.put("cou", "点点赚持有天数");

		headMap.put("xmInvMoney", "项目累计投资额");
		headMap.put("isInternal", "是否内部员工");
		headMap.put("isInternalTuijian", "用户是否内部员工邀请");

		return headMap;
	}
}
