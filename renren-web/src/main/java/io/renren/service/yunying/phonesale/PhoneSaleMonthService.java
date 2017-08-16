package io.renren.service.yunying.phonesale;

import java.util.List;
import java.util.Map;

import io.renren.entity.yunying.phonesale.PhoneSaleMonthEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-15 16:41:23
 */
public interface PhoneSaleMonthService {
	
	PhoneSaleMonthEntity queryObject(String userName);
	
	List<PhoneSaleMonthEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PhoneSaleMonthEntity phoneSaleMonth);
	
	void update(PhoneSaleMonthEntity phoneSaleMonth);
	
	void delete(String userName);
	
	void deleteBatch(String[] userNames);
	
	public void batchInsertPhoneSaleMonthData(List<Map<String, Object>> dataList);
}
