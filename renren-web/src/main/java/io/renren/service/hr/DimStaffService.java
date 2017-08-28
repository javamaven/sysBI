package io.renren.service.hr;

import java.util.List;
import java.util.Map;

import io.renren.entity.hr.DimStaffEntity;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-28 10:12:47
 */
public interface DimStaffService {
	
	DimStaffEntity queryObject(String realname);
	
	List<DimStaffEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DimStaffEntity dimStaff);
	
	void update(DimStaffEntity dimStaff);
	
	void delete(String realname);
	
	void deleteBatch(String[] realnames);
}
