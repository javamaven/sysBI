package io.renren.dao;

import io.renren.entity.PerformanceParameterEntity;

import java.util.List;

/**
 * 绩效台帐-分配表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:58
 */
public interface PerformanceParameterDao extends BaseDao<PerformanceParameterEntity> {
    List<PerformanceParameterEntity> queryExport();
}
