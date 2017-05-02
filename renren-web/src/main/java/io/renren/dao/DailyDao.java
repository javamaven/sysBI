package io.renren.dao;

import io.renren.entity.DailyEntity;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-12 09:06:43
 */
public interface DailyDao extends BaseDao<DailyEntity> {
    List<DailyEntity> queryExports();
}
