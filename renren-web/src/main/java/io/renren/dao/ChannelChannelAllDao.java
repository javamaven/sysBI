package io.renren.dao;

import io.renren.entity.ChannelChannelAllEntity;

import java.util.List;
import java.util.Map;

/**
 * 渠道总体情况
 * 
 * @author lujianfeng
 * @email lujianfeng@mindai.com
 * @date 2017年3月29日18:01:543
 */
public interface ChannelChannelAllDao extends BaseDao<ChannelChannelAllEntity> {

	/**
	 * 获取图标数据
	 *
	 */
	List<ChannelChannelAllEntity> queryMainChart(Map<String, Object> map);

	/**
	 * 获取渠道数据
	 * @return
	 */
	List<ChannelChannelAllEntity> queryChannel();

	List<ChannelChannelAllEntity> queryChannelHead();

}
