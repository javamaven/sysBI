package io.renren.service;

import java.util.List;
import java.util.Map;
import io.renren.entity.MarketChannelEntity;
/**
 * 市场部每日渠道数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
public interface MarketChannelService {

	List<MarketChannelEntity> queryList(Map<String, Object> map);
	int queryTotal(Map<String, Object> map);
}
