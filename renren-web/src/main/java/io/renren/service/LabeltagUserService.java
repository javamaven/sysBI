package io.renren.service;

import io.renren.entity.LabelTagManagerEntity;
import io.renren.entity.LabeltagUserEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 用户标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-13 14:08:32
 */
public interface LabeltagUserService {

	LabeltagUserEntity queryObject(Integer userId);
	
	List<LabeltagUserEntity> queryList(Map<String, Object> map);
	
	int queryTotal();

	List<String> queryLabelTagType(Map<String, Object> map);

	List<LabelTagManagerEntity> queryLabelList(Map<String, Object> map);

	String queryStrSql(Map<String, Object> map);

	List<LabeltagUserEntity> queryExport();
}
