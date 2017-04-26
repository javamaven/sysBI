package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.dao.LabelTagManagerDao;
import io.renren.entity.LabelTagManagerEntity;
import io.renren.service.LabelTagManagerService;



@Service("labeltagMainService")
public class LabelTagManagerServiceImpl implements LabelTagManagerService {
	@Autowired
	private LabelTagManagerDao labelTagManagerDao;

	@Override
	public LabelTagManagerEntity queryObject(Integer id){
		return labelTagManagerDao.queryObject(id);
	}


	@Override
	public List<LabelTagManagerEntity> queryList(Map<String, Object> map){
		return labelTagManagerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return labelTagManagerDao.queryTotal(map);
	}

	@Override
	public void save(LabelTagManagerEntity labelTagManager) {
		labelTagManagerDao.save(labelTagManager);
	}

	@Override
	public void saveDetail(LabelTagManagerEntity labelTagManager) {
		labelTagManagerDao.saveDetail(labelTagManager);
	}

	@Override
	public String querySysUser(Long id) {
		return labelTagManagerDao.querySysUser(id);
	}

	@Override
	public void deleteAll(Map<String, Object> map){
		labelTagManagerDao.deleteAll(map);
	}

	@Override
	public int exists(Map<String, Object> map){
		return labelTagManagerDao.exists(map);
	}



}
