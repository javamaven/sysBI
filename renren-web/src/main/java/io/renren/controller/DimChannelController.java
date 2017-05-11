package io.renren.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.service.DimChannelService;
import io.renren.utils.R;

/**
 * 渠道总体情况
 */
@RestController
@RequestMapping("/channel")
public class DimChannelController extends AbstractController {
	@Autowired
	private DimChannelService dimChannelService;

	@RequestMapping("/queryChannelName")
	public R getChannel() {
		// 查询渠道数据
		return R.ok().put("Channel", dimChannelService.queryChannelName());
	}

}
