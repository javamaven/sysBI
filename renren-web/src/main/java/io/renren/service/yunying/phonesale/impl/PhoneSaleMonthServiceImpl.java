package io.renren.service.yunying.phonesale.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.phonesale.PhoneSaleMonthDao;
import io.renren.entity.yunying.phonesale.PhoneSaleMonthEntity;
import io.renren.service.yunying.phonesale.PhoneSaleMonthService;

@Service("phoneSaleMonthService")
public class PhoneSaleMonthServiceImpl implements PhoneSaleMonthService {
	@Autowired
	private PhoneSaleMonthDao phoneSaleMonthDao;

	@Override
	public PhoneSaleMonthEntity queryObject(String userName) {
		return phoneSaleMonthDao.queryObject(userName);
	}

	@Override
	public List<PhoneSaleMonthEntity> queryList(Map<String, Object> map) {
		return phoneSaleMonthDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return phoneSaleMonthDao.queryTotal(map);
	}

	@Override
	public void save(PhoneSaleMonthEntity phoneSaleMonth) {
		phoneSaleMonthDao.save(phoneSaleMonth);
	}

	@Override
	public void update(PhoneSaleMonthEntity phoneSaleMonth) {
		phoneSaleMonthDao.update(phoneSaleMonth);
	}

	@Override
	public void delete(String userName) {
		phoneSaleMonthDao.delete(userName);
	}

	@Override
	public void deleteBatch(String[] userNames) {
		phoneSaleMonthDao.deleteBatch(userNames);
	}

	@Override
	public void batchInsertPhoneSaleMonthData(List<Map<String, Object>> dataList) {
		// TODO Auto-generated method stub
		phoneSaleMonthDao.batchInsertPhoneSaleMonthData(dataList);
	}

}
