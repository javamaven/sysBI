package io.renren.dao;

import io.renren.entity.PerformanceHisEntity;

import java.util.List;

/**
 * 历史绩效发放记录
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-02 09:26:54
 */
public interface PerformanceHisDao extends BaseDao<PerformanceHisEntity> {
    List<PerformanceHisEntity> queryExport();
}
