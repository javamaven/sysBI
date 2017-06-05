package io.renren.dao;

import io.renren.entity.ProjectSumEntity;

import java.util.List;

/**
 * 项目总台帐
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 15:41:58
 */
public interface ProjectSumDao extends BaseDao<ProjectSumEntity> {
    List<ProjectSumEntity> queryExport();
}
