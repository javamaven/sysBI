package io.renren.controller;

import io.renren.entity.ChannelChannelAllEntity;
import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 市场部每日渠道数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
@RestController
@RequestMapping("market")
public class MarketChannelController {
	@Autowired
	private MarketChannelService marketChannelDataService;


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("marketChannel:list")
	public R list(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		
		//查询列表数据
		List<MarketChannelEntity> dmReportChannelDataList = marketChannelDataService.queryList(query);
		int total = marketChannelDataService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(dmReportChannelDataList, total,  query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	

	
}
