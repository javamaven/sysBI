package io.renren.controller.shichang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.SysUserEntity;
import io.renren.entity.shichang.ChannelHeadManagerEntity;
import io.renren.service.SysUserService;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 渠道负责人架构关系管理
 * 
 */
@Controller
@RequestMapping("channel/channelHead")
public class ChannelHeadController {
	@Autowired
	private ChannelHeadManagerService service;
	@Autowired
	private SysUserService sysUserService;
	
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String channelName, String channelType, String channelLabel){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		if(StringUtils.isNotEmpty(channelName)){
			map.put("channelName", channelName);
		}
		if(StringUtils.isNotEmpty(channelType)){
			map.put("channelType", channelType);
		}
		if(StringUtils.isNotEmpty(channelLabel)){
			map.put("channelLabel", channelLabel);
		}
		//查询列表数据
		List<Map<String,Object>> dimChannelTypeList = service.queryList(map);
		int total = service.queryTotal(map);
		PageUtils pageUtil = new PageUtils(dimChannelTypeList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{channelLabel}")
	public R info(@PathVariable("channelLabel") String channelLabel){
		channelLabel = channelLabel.replace("(重复渠道)", "");
		ChannelHeadManagerEntity dimChannelType = service.queryObject(channelLabel);
		
		return R.ok().put("dimChannelType", dimChannelType);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody ChannelHeadManagerEntity entity){
		
		SysUserEntity queryByUserName = sysUserService.queryByUserName(entity.getSysAccount());
		if(queryByUserName == null){
			return R.error().put("msg", "经分系统帐号不存在");
		}
		
		ChannelHeadManagerEntity retEntity = service.queryByChannelHead(entity);
		if(retEntity != null){
			return R.error().put("msg", "渠道负责人已存在");
		}
		if("-1".equals(entity.getParentChannelHead())){
			entity.setParentChannelHeadId("-1");
		}
		service.save(entity);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody ChannelHeadManagerEntity entity){
//		dimChannelType.setChannelLabel(dimChannelType.getChannelLabel().replace("(重复渠道)", ""));
		service.update(entity);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody Map<String,Object> params){
		service.delete(params);
		return R.ok();
	}
	
}
