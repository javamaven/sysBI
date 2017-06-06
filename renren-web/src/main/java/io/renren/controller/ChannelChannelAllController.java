package io.renren.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

import static io.renren.utils.ShiroUtils.getUserId;

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
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="渠道分次投资情况";
	/**
	 * 所有列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("channel:channelAll:list")
	public R list(@RequestBody Map<String, Object> params){

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
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
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String,Object> map = JSON.parseObject(param, Map.class);
		Query query = new Query(map);
		//查询数据
		List<ChannelChannelAllEntity> channelList = channelChannelAllService.queryList(query);
		JSONArray va = new JSONArray();
		for (int i = 0; i < channelList.size(); i++) {
			ChannelChannelAllEntity entity = channelList.get(i);
			va.add(entity);
		}

		Map<String, String> headMap = channelChannelAllService.getExcelFields();

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
