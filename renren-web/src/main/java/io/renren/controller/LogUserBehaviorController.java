package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.LogUserBehaviorEntity;
import io.renren.service.LogUserBehaviorService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
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

	static class Page{
		private int total;
		private List<?> rows;
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public List<?> getRows() {
			return rows;
		}
		public void setRows(List<?> rows) {
			this.rows = rows;
		}
		public Page(int total, List<?> rows) {
			super();
			this.total = total;
			this.rows = rows;
		}

	}
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("logUserBehavior:list")
	public Page list(Map<String, Object> params, Integer page, Integer limit, String userID,String userName,
					 String start_action_time,String end_action_time,String actionPlatform,String action) {
		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		// List jsonarray = JSON.parseObject(actionPlatform.class);

//		List<String> actionPlatformList = JSON.parseArray(actionPlatform + "", String.class);
//		System.out.println("==========================="+actionPlatformList.size());
		params.put("page", (page - 1) * limit);
		params.put("limit", limit);
		params.put("userID", userID);
		params.put("userName", userName);
		params.put("start_action_time", start_action_time);
		params.put("end_action_time", end_action_time);
		params.put("action", JSON.parseArray(action + "", String.class));
		params.put("actionPlatform", JSON.parseArray(actionPlatform + "", String.class));

		//查询列表数据
		Query query = new Query(params);

		//查询列表数据
		List<LogUserBehaviorEntity> dmReportDailyDataList = logUserBehaviorService.queryList(query);
		int total = logUserBehaviorService.queryTotal(params);
		Page page1 = new Page(total, dmReportDailyDataList);
		return page1;


	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params,HttpServletResponse response, HttpServletRequest request) throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		Map<String,Object> map = JSON.parseObject(params, Map.class);
//
		List<LogUserBehaviorEntity> ProjectSumList = logUserBehaviorService.queryList(map);
		JSONArray va = new JSONArray();

		for (int i = 0; i < ProjectSumList.size(); i++) {
			LogUserBehaviorEntity entity = ProjectSumList.get(i);
			va.add(entity);
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
