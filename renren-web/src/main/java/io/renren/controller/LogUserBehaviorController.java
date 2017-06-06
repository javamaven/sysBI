package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.LogUserBehaviorEntity;
import io.renren.service.LogUserBehaviorService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 用户行为日志
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
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="用户行为日志";
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("logUserBehavior:list")
	public R list(@RequestBody Map<String, Object> params){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");


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
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

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
		Map<String, String> headMap = logUserBehaviorService.getExcelFields();

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
