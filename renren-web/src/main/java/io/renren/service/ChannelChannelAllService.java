package io.renren.service;

import io.renren.entity.ChannelChannelAllEntity;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 * 
 * @author lujianfeng
 * @email lujianfeng@mindai.com
 * @date 2017年3月29日17:29:13
 */

public interface ChannelChannelAllService {
		
	/**
	 * 查询列表
	 */
	List<ChannelChannelAllEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 查询图表数据
	 */
	List<ChannelChannelAllEntity> queryMainChart(Map<String, Object> map);

	/**
	 * 查询渠道
	 * @return
	 */
	List<ChannelChannelAllEntity> queryChannel();

}
