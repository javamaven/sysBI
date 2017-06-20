package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportVipUserDao;
import io.renren.entity.yunying.dayreport.DmReportVipUserEntity;
import io.renren.service.yunying.dayreport.DmReportVipUserService;

@Service("dmReportVipUserService")
public class DmReportVipUserServiceImpl implements DmReportVipUserService {
	@Autowired
	private DmReportVipUserDao dmReportVipUserDao;

	@Override
	public List<DmReportVipUserEntity> queryList(Map<String, Object> map) {

		List<DmReportVipUserEntity> list = dmReportVipUserDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportVipUserEntity entity = list.get(i);
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
		return dmReportVipUserDao.queryTotal(map);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "时间");

		headMap.put("oldUserId", "用户ID");
		headMap.put("cgUserId", "存管ID");
		headMap.put("oldUsername", "名单用户名");

		headMap.put("username", "用户名");
		headMap.put("realname", "姓名");
		headMap.put("oldPhone", "名单电话号码");

		headMap.put("phone", "电话号码");
		headMap.put("await", "名单总待收");
		headMap.put("totalReceipt", "当前总待收");
		
		headMap.put("lv", "等级划分");
		headMap.put("owner", "所属人");
		headMap.put("balance", "账户余额");

		headMap.put("regTime", "注册时间");
		headMap.put("loginTime", "最近登录时间");
		headMap.put("lastRecoverTime", "最近一次回款日期");

		headMap.put("lastRecoverMoney", "最近一次回款金额");
		headMap.put("lastRechargeTime", "最近一次充值日期");
		headMap.put("lastRechargeMoney", "最近一次充值金额");

		headMap.put("rechargeMoneyC", "累计充值金额");
		headMap.put("voucherMoney", "账户红包");
		headMap.put("lastCashTime", "最近一次提现日期");

		headMap.put("lastCashMoney", "最近一次提现金额");
		headMap.put("cashMoneyC", "累计提现金额");
		headMap.put("invCou", "投资次数");

		headMap.put("avgPeriod", "平均投资期限");
		headMap.put("monthTender", "当月投资金额");
		headMap.put("monthTenderY", "当月年华金额");

		headMap.put("monthTenderCou", "当月投资次数");
		headMap.put("dayTender", "当天投资金额");
		headMap.put("dayTenderY", "当天年华金额");

		headMap.put("dayTenderCou", "当天投资次数");

		return headMap;
	}

}
