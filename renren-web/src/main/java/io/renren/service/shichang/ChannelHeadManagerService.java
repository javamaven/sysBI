package io.renren.service.shichang;

import java.util.List;
import java.util.Map;

import io.renren.entity.shichang.ChannelHeadManagerEntity;

/**
 * 渠道分类-手动维护
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-10 10:03:42
 */
public interface ChannelHeadManagerService {
	
	ChannelHeadManagerEntity queryObject(String channelLabel);
	
	List<Map<String,Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ChannelHeadManagerEntity entity);
	
	void update(ChannelHeadManagerEntity entity);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(String[] channelLabels);

	void insert(Map<String, Object> map);

	ChannelHeadManagerEntity queryByChannelHead(ChannelHeadManagerEntity entity);
	
	List<String> queryAuthByChannelHead();//查询当前账号所能查看的渠道
	List<String> queryChannelAuthByChannelHead(String key);//key:channel_label,channel_name
	
	boolean isMarketDirector();//是否是市场部总监
}
