package io.renren.dao;

import io.renren.entity.MarketChannelEntity;

import java.util.List;
import java.util.Map;

/**
 * 市场部每日渠道数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
public interface MarketChannelDao extends BaseDao<MarketChannelEntity> {
    List<MarketChannelEntity> queryExport();
}
