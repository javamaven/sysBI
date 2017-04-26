package io.renren.service;

import io.renren.entity.LabelTagManagerEntity;

import java.util.List;
import java.util.Map;

/**
 * 标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-11 19:46:06
 */
public interface LabelTagManagerService {

	LabelTagManagerEntity queryObject(Integer id);

	List<LabelTagManagerEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(LabelTagManagerEntity labelTagManager);

	void saveDetail(LabelTagManagerEntity labelTagManager);

	void deleteAll(Map<String, Object> map);

	int exists(Map<String, Object> map);

	String querySysUser(Long id);
}
