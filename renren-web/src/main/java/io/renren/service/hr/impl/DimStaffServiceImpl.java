package io.renren.service.hr.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.hr.DimStaffDao;
import io.renren.entity.hr.DimStaffEntity;
import io.renren.service.hr.DimStaffService;



@Service("dimStaffService")
public class DimStaffServiceImpl implements DimStaffService {
	@Autowired
	private DimStaffDao dimStaffDao;
	
	@Override
	public DimStaffEntity queryObject(String cardId){
		return dimStaffDao.queryObject(cardId);
	}
	
	@Override
	public List<DimStaffEntity> queryList(Map<String, Object> map){
		return dimStaffDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dimStaffDao.queryTotal(map);
	}
	
	@Override
	public void save(DimStaffEntity dimStaff){
		dimStaffDao.save(dimStaff);
	}
	
	@Override
	public void update(DimStaffEntity dimStaff){
		dimStaffDao.update(dimStaff);
	}
	
	@Override
	public void delete(String realname){
		dimStaffDao.delete(realname);
	}
	
	@Override
	public void deleteBatch(String[] realnames){
		dimStaffDao.deleteBatch(realnames);
	}
	
}
