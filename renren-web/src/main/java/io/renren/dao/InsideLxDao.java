package io.renren.dao;

import io.renren.entity.InsideLxEntity;

import java.util.List;

/**
 * 员工拉新统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-26 09:27:51
 */
public interface InsideLxDao extends BaseDao<InsideLxEntity> {
    List<InsideLxEntity> queryExport();
}
