package io.renren.service.shichang;

import java.util.List;
import java.util.Map;

import io.renren.entity.shichang.DimChannelCostNewEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-08 10:16:36
 */
public interface DimChannelCostNewService {
	
	DimChannelCostNewEntity queryObject(String statPeriod);
	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DimChannelCostNewEntity dimChannelCostNew);
	
	void update(DimChannelCostNewEntity dimChannelCostNew);
	
	void delete(String statPeriod);
	
	void deleteBatch(String[] statPeriods);

	Map<String, String> getExcelFields();

	void batchInsert(List<Map<String, Object>> dataList);
}
