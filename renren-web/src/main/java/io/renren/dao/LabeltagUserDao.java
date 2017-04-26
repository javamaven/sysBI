package io.renren.dao;

import io.renren.entity.LabelTagManagerEntity;
import io.renren.entity.LabeltagUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-13 14:08:32
 */
public interface LabeltagUserDao extends BaseDao<LabeltagUserEntity> {
    List<String> queryLabelTagType(Map<String, Object> map);
    List<LabeltagUserEntity> queryList(Map<String, Object> map);
    String queryStrSql(Map<String, Object> map);
    List<LabelTagManagerEntity> queryLabelList(Map<String, Object> map);
    List<LabeltagUserEntity> queryExport();
}
