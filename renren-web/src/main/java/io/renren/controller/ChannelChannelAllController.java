package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 渠道总体情况
 *
 * @author lujianfeng
 * @email lujianfeng@mindai.com
 * @date 2017年3月29日17:29:13
 */
@RestController
@RequestMapping("/channel/channelAll")
public class ChannelChannelAllController extends AbstractController {
	@Autowired
	private ChannelChannelAllService channelChannelAllService;

	/**
	 * 所有列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("channel:channelAll:list")
	public R list(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		//查询数据
		List<ChannelChannelAllEntity> channelList = channelChannelAllService.queryList(query);
		//获取数据条数
		int total = channelChannelAllService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(channelList, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取图表数据
	 */
	@RequestMapping("/mainChart")
	@RequiresPermissions("channel:channelAll:mainChart")
	public R mainChart(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		//查询数据
		List<ChannelChannelAllEntity> chartMain = channelChannelAllService.queryMainChart(query);

		return R.ok().put("mainChart", chartMain);
	}

	/**
	 * 获取渠道数据
	 */
	@RequestMapping("/getChannel")
	public R getChannel(){
		//查询渠道数据
		return R.ok().put("Channel", channelChannelAllService.queryChannel());
	}

}
