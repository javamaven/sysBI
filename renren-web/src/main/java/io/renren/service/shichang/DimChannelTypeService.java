package io.renren.service.shichang;

import java.util.List;
import java.util.Map;

import io.renren.entity.shichang.DimChannelTypeEntity;

/**
 * 渠道分类-手动维护
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-10 10:03:42
 */
public interface DimChannelTypeService {
	
	DimChannelTypeEntity queryObject(String channelLabel);
	
	List<Map<String,Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DimChannelTypeEntity dimChannelType);
	
	void update(DimChannelTypeEntity dimChannelType);
	
	void delete(String channelLabel);
	
	void deleteBatch(String[] channelLabels);
}
