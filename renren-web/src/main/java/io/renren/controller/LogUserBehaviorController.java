package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.LogUserBehaviorEntity;
import io.renren.service.LogUserBehaviorService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
@RestController
@RequestMapping("logUserBehavior")
public class LogUserBehaviorController {
	@Autowired
	private LogUserBehaviorService logUserBehaviorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("logUserBehavior:list")
	public R list(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		//查询列表数据
		List<LogUserBehaviorEntity> logUserBehaviorList = logUserBehaviorService.queryList(query);
		int total = logUserBehaviorService.queryTotal(query);
		return R.ok().put("page", logUserBehaviorList);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<LogUserBehaviorEntity> MarketChannelList = logUserBehaviorService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < MarketChannelList.size() ; i++) {
			LogUserBehaviorEntity logUserBehaviorEntity = new LogUserBehaviorEntity();
			logUserBehaviorEntity.setUserID(MarketChannelList.get(i).getUserID());
			logUserBehaviorEntity.setUserName(MarketChannelList.get(i).getUserName());
			logUserBehaviorEntity.setChannlName(MarketChannelList.get(i).getChannlName());
			logUserBehaviorEntity.setChannlMark(MarketChannelList.get(i).getChannlMark());
			logUserBehaviorEntity.setActionTime(MarketChannelList.get(i).getActionTime());
			logUserBehaviorEntity.setActionPlatform(MarketChannelList.get(i).getActionPlatform());
			logUserBehaviorEntity.setAction(MarketChannelList.get(i).getAction());
			logUserBehaviorEntity.setProjectType(MarketChannelList.get(i).getProjectType());
			logUserBehaviorEntity.setProjectAmount(MarketChannelList.get(i).getProjectAmount());


			va.add(logUserBehaviorEntity);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("userID","用户ID");
		headMap.put("userName","用户名");
		headMap.put("channlName","渠道名称");
		headMap.put("channlMark","渠道标记");
		headMap.put("actionTime","操作时间");
		headMap.put("actionPlatform","操作平台");
		headMap.put("action","行为");
		headMap.put("projectType","涉及项目类型");
		headMap.put("projectAmount","涉及项目本金");

		String title = "用户行为日志";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}
	@ResponseBody
	@RequestMapping("/getActionPlatform")
	public R getActionPlatform(){
		//查询渠道数据
		return R.ok().put("ActionPlatform", logUserBehaviorService.queryActionPlatform());
	}

	@ResponseBody
	@RequestMapping("/getAction")
	public R getAction(){
		//查询渠道数据
		return R.ok().put("Action", logUserBehaviorService.queryAction());
	}


	
}
