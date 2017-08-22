package io.renren.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.service.DimChannelService;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.utils.Constant;
import io.renren.utils.R;

/**
 * 渠道总体情况
 */
@RestController
@RequestMapping("/channel")
public class DimChannelController extends AbstractController {
	@Autowired
	private DimChannelService dimChannelService;
	@Autowired
	private ChannelHeadManagerService service;

	@RequestMapping("/queryChannelName")
	public R getChannel() {
		// 查询渠道数据
		return R.ok().put("Channel", dimChannelService.queryChannelName());
	}
	
	
	@RequestMapping("/queryChannelNameByAuth")
	public R queryChannelNameByAuth() {
		// 查询渠道数据
		if(service.isMarketDirector() || getUserId() == Constant.SUPER_ADMIN){//市场部总监，超级管理员
			return R.ok().put("Channel", dimChannelService.queryChannelName());
		}else{
			List<String> dataList = service.queryChannelAuthByChannelHead("channel_name");
			List<Map<String,Object>> ret = new ArrayList<>();
			for (int i = 0; i < dataList.size(); i++) {
				String channelName = dataList.get(i);
				Map<String,Object> map =  new HashMap<String, Object>();
				map.put("channelName", channelName);
				ret.add(map);
			}
			return R.ok().put("Channel", ret);
		}
	}

}
