package io.renren.dao.shichang;

import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.shichang.DimChannelCostNewEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-08 10:16:36
 */
public interface DimChannelCostNewDao extends BaseDao<DimChannelCostNewEntity> {

	void batchInsert(List<Map<String, Object>> dataList);
	
}
