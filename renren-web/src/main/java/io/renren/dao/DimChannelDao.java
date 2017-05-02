package io.renren.dao;

import java.util.List;

import io.renren.entity.DimChannelEntity;


public interface DimChannelDao extends BaseDao<DimChannelEntity> {
	/**
	 * 获取渠道数据
	 * @return
	 */
	List<DimChannelEntity> queryChannel();
	
	/**
	 * 获取渠道成本数据
	 * @return
	 */
	List<DimChannelEntity> queryChannelCostList();
}
