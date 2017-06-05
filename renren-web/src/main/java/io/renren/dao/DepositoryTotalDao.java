package io.renren.dao;

import io.renren.entity.DepositoryTotalEntity;

import java.util.List;

/**
 * 存管报备总表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-01 14:47:27
 */
public interface DepositoryTotalDao extends BaseDao<DepositoryTotalEntity> {
    List<DepositoryTotalEntity> queryExport();
}
