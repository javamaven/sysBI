package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.DimChannelEntity;

public interface DimChannelService {
	
	
	List<DimChannelEntity> queryChannelList(Map<String, Object> map);
	
	
	Map<String,String> queryListAsLabelMap(Map<String, Object> map);
	
	
	/**
	 * 获取渠道成本数据
	 * @return
	 */
	List<DimChannelEntity> queryChannelCostList();
	
	
	Map<String,String> queryChanelTypeMap();
	
}
