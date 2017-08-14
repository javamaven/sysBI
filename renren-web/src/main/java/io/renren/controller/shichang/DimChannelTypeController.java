package io.renren.controller.shichang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.shichang.DimChannelTypeEntity;
import io.renren.service.shichang.DimChannelTypeService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 渠道分类-手动维护
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-10 10:03:42
 */
@Controller
@RequestMapping("dimchanneltype")
public class DimChannelTypeController {
	@Autowired
	private DimChannelTypeService dimChannelTypeService;
	
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
		List<Map<String,Object>> dimChannelTypeList = dimChannelTypeService.queryList(map);
		int total = dimChannelTypeService.queryTotal(map);
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
		DimChannelTypeEntity dimChannelType = dimChannelTypeService.queryObject(channelLabel);
		
		return R.ok().put("dimChannelType", dimChannelType);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody DimChannelTypeEntity dimChannelType){
		dimChannelTypeService.save(dimChannelType);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody DimChannelTypeEntity dimChannelType){
		dimChannelType.setChannelLabel(dimChannelType.getChannelLabel().replace("(重复渠道)", ""));
		dimChannelTypeService.update(dimChannelType);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] channelLabels){
		String[] deletes = new String[channelLabels.length];
		//有重复的渠道,两条记录完全一样，id和时间都一样，没办法保留一条
		
		for (int i = 0; i < deletes.length; i++) {
			deletes[i] = channelLabels[i].replace("(重复渠道)", "");
		}
		//1、查询
		Map<String, Object> map = new HashMap<>();
		map.put("channelLabel", deletes[0]);
		List<Map<String, Object>> queryList = dimChannelTypeService.queryList(map);
		//2、删除
		dimChannelTypeService.deleteBatch(deletes);
		//3、插入
		dimChannelTypeService.insert(queryList.get(0));
		return R.ok();
	}
	
}
