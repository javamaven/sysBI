package io.renren.service.impl;

import io.renren.dao.LabeltagUserDao;
import io.renren.entity.LabelTagManagerEntity;
import io.renren.entity.LabeltagUserEntity;
import io.renren.service.LabeltagUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("LabeltagUserService")
public class LabeltagUserServiceImpl implements LabeltagUserService {
	@Autowired
	private LabeltagUserDao labeltagUserDao;
	
	@Override
	public LabeltagUserEntity queryObject(Integer userId){
		return labeltagUserDao.queryObject(userId);
	}
	
	@Override
	public List<LabeltagUserEntity> queryList(Map<String, Object> map){
		return labeltagUserDao.queryList(map);
	}
	
	@Override
	public int queryTotal(){
		return labeltagUserDao.queryTotal();
	}

	@Override
	public List<String> queryLabelTagType(Map<String, Object> map) {
		return labeltagUserDao.queryLabelTagType(map);
	}

	@Override
	public List<LabelTagManagerEntity> queryLabelList(Map<String, Object> map) {
		return labeltagUserDao.queryLabelList(map);
	}

	@Override
	public String queryStrSql(Map<String, Object> map){
		return labeltagUserDao.queryStrSql(map);
	}

	@Override
	public List<LabeltagUserEntity> queryExport() {
		return labeltagUserDao.queryExport();
	}
}
