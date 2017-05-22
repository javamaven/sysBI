package io.renren.dao;

import io.renren.entity.ChannelCostEntity;

import java.util.List;
import java.util.Map;

/**
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
public interface ChannelCostDao extends BaseDao<ChannelCostEntity> {
    List<ChannelCostEntity> queryChannel();
    ChannelCostEntity queryTotalList(Map<String, Object> map);
    List<ChannelCostEntity> queryExport();
}
