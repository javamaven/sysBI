package io.renren.dao.shichang;

import java.util.List;
import java.util.Map;

import io.renren.dao.BaseDao;
import io.renren.entity.shichang.DimChannelTypeEntity;

/**
 * 渠道分类-手动维护
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-10 10:03:42
 */
public interface DimChannelTypeDao extends BaseDao<DimChannelTypeEntity> {

	List<Map<String,Object>> queryListMap(Map<String, Object> map);
	
}
