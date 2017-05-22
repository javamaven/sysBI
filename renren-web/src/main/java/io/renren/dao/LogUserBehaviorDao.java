package io.renren.dao;

import io.renren.entity.LogUserBehaviorEntity;

import java.util.List;

/**
 * 渠道成本统计表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
public interface LogUserBehaviorDao extends BaseDao<LogUserBehaviorEntity> {
    List<LogUserBehaviorEntity> queryExport();
    List<LogUserBehaviorEntity> queryAction();
    List<LogUserBehaviorEntity> queryActionPlatform();
}
