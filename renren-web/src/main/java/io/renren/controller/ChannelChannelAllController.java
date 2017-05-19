package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
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
	 * 所有列表
	 */
	@ResponseBody
	@RequestMapping("/exportList")
	@RequiresPermissions("channel:channelAll:list")
	public void exportList(String param,HttpServletRequest request, HttpServletResponse response)throws IOException {
		//查询列表数据

		Map<String,Object> map = JSON.parseObject(param, Map.class);
		Query query = new Query(map);
		//查询数据
		List<ChannelChannelAllEntity> channelList = channelChannelAllService.queryList(query);
		JSONArray va = new JSONArray();
		for (int i = 0; i < channelList.size(); i++) {
			ChannelChannelAllEntity entity = channelList.get(i);
			va.add(entity);
		}

		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("channelName","渠道");
		headMap.put("investTimes","投资次数");
		headMap.put("stayPer","留存率");
		headMap.put("investUsers","投资用户数");
		headMap.put("moneyVoucherPer","次均红包金额");
		headMap.put("moneyInvestPer","次均投资金额");
		headMap.put("moneyInvsetYearPer","平均投资期限");
		headMap.put("borrowPeriodPer","平均投资间隔");

		String title = "渠道分次投资情况";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);


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
