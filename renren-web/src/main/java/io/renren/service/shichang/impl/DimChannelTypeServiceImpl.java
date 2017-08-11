package io.renren.service.shichang.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.shichang.DimChannelTypeDao;
import io.renren.entity.shichang.DimChannelTypeEntity;
import io.renren.service.shichang.DimChannelTypeService;



@Service("dimChannelTypeService")
public class DimChannelTypeServiceImpl implements DimChannelTypeService {
	@Autowired
	private DimChannelTypeDao dimChannelTypeDao;
	
	@Override
	public DimChannelTypeEntity queryObject(String channelLabel){
		return dimChannelTypeDao.queryObject(channelLabel);
	}
	
	@Override
	public List<Map<String,Object>> queryList(Map<String, Object> map){
		return dimChannelTypeDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dimChannelTypeDao.queryTotal(map);
	}
	
	@Override
	public void save(DimChannelTypeEntity dimChannelType){
		dimChannelTypeDao.save(dimChannelType);
	}
	
	@Override
	public void update(DimChannelTypeEntity dimChannelType){
		dimChannelTypeDao.update(dimChannelType);
	}
	
	@Override
	public void delete(String channelLabel){
		dimChannelTypeDao.delete(channelLabel);
	}
	
	@Override
	public void deleteBatch(String[] channelLabels){
		dimChannelTypeDao.deleteBatch(channelLabels);
	}
	
}
