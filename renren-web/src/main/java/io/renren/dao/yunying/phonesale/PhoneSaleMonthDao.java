package io.renren.dao.yunying.phonesale;

import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.yunying.phonesale.PhoneSaleMonthEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-15 16:41:23
 */
public interface PhoneSaleMonthDao extends BaseDao<PhoneSaleMonthEntity> {
	
	public void batchInsertPhoneSaleMonthData(List<Map<String, Object>> dataList);
}
