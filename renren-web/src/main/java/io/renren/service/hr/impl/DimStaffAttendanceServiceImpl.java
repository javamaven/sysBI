package io.renren.service.hr.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.hr.DimStaffAttendanceDao;
import io.renren.entity.hr.DimStaffAttendanceEntity;
import io.renren.service.hr.DimStaffAttendanceService;



@Service("dimStaffAttendanceService")
public class DimStaffAttendanceServiceImpl implements DimStaffAttendanceService {
	@Autowired
	private DimStaffAttendanceDao dimStaffAttendanceDao;
	
	@Override
	public DimStaffAttendanceEntity queryObject(DimStaffAttendanceEntity dimStaffAttendance){
		return dimStaffAttendanceDao.queryObject(dimStaffAttendance);
	}
	
	@Override
	public List<DimStaffAttendanceEntity> queryList(Map<String, Object> map){
		return dimStaffAttendanceDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dimStaffAttendanceDao.queryTotal(map);
	}
	
	@Override
	public void save(DimStaffAttendanceEntity dimStaffAttendance){
		dimStaffAttendanceDao.save(dimStaffAttendance);
	}
	
	@Override
	public void update(DimStaffAttendanceEntity dimStaffAttendance){
		dimStaffAttendanceDao.update(dimStaffAttendance);
	}
	
	@Override
	public void delete(String realname){
		dimStaffAttendanceDao.delete(realname);
	}
	
	@Override
	public void deleteBatch(String[] realnames){
		dimStaffAttendanceDao.deleteBatch(realnames);
	}

	@Override
	public void batchInsert(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		dimStaffAttendanceDao.batchInsert(list);
	}
	
}
