package io.renren.dao;

import io.renren.entity.LabelTagManagerEntity;

import java.util.Map;

/**
 * 标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-11 19:46:06
 */
public interface LabelTagManagerDao extends BaseDao<LabelTagManagerEntity> {

    /**
     * 获取渠道数据
     * @return
     */
   String querySysUser(Long id);

   void saveDetail(LabelTagManagerEntity labelTagManager);

   void deleteAll (Map<String, Object> map);
   int exists (Map<String, Object> map);

}
