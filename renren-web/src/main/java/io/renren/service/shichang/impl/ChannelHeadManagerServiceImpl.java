package io.renren.service.shichang.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.shichang.ChannelHeadManagerDao;
import io.renren.entity.shichang.ChannelHeadManagerEntity;
import io.renren.service.shichang.ChannelHeadManagerService;



@Service("channelHeadManagerService")
public class ChannelHeadManagerServiceImpl implements ChannelHeadManagerService {
	@Autowired
	private ChannelHeadManagerDao channelHeadManagerDao;
	
	@Override
	public List<Map<String,Object>> queryList(Map<String, Object> map){
		return channelHeadManagerDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return channelHeadManagerDao.queryTotal(map);
	}
	
	@Override
	public void save(ChannelHeadManagerEntity entity){
		channelHeadManagerDao.save(entity);
	}
	
	@Override
	public void update(ChannelHeadManagerEntity entity){
		channelHeadManagerDao.update(entity);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		channelHeadManagerDao.delete(map);
	}
	
	@Override
	public void deleteBatch(String[] channelLabels){
		channelHeadManagerDao.deleteBatch(channelLabels);
	}

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		channelHeadManagerDao.insert(map);
	}

	@Override
	public ChannelHeadManagerEntity queryObject(String channelLabel) {
		// TODO Auto-generated method stub
		return channelHeadManagerDao.queryObject(channelLabel);
	}
	
	@Override
	public ChannelHeadManagerEntity queryByChannelHead(ChannelHeadManagerEntity entity) {
		// TODO Auto-generated method stub
		return channelHeadManagerDao.queryByChannelHead(entity);
	}
	
}
