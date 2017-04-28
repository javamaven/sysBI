package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.DimChannelEntity;

public interface DimChannelService {
	
	
	List<DimChannelEntity> queryList(Map<String, Object> map);
	
	
	Map<String,String> queryListAsLabelMap(Map<String, Object> map);
	
	
}
