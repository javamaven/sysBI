package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.entity.PerformanceHisEntity;
import io.renren.entity.UserBehaviorEntity;
import io.renren.entity.yunying.dayreport.DmReportCashDataEntity;
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
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;

/**
 * 市场部每日渠道数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
@RestController
@RequestMapping("/black/userBehavior")
public class UserBehaviorController {
	@Autowired
	private UserBehaviorService userBehaviorService;


	/**
	 * 列表
	 */
	@RequestMapping("/first")
	 @RequiresPermissions("Behavior:list")
	public R list(@RequestBody Map<String, Object> params) {



		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<UserBehaviorEntity> UserBehaviorDataList = userBehaviorService.queryList(query);

		return R.ok().put("page", UserBehaviorDataList);

	}

	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {

		List<UserBehaviorEntity> UserBehaviorList = userBehaviorService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < UserBehaviorList.size() ; i++) {
			UserBehaviorEntity  UserBehavior = new UserBehaviorEntity();
			UserBehavior.setStatDate(UserBehaviorList.get(i).getStatDate());
			UserBehavior.setUserName(UserBehaviorList.get(i).getUserName());
			UserBehavior.setDeleteTimes(UserBehaviorList.get(i).getDeleteTimes());
			UserBehavior.setReportType(UserBehaviorList.get(i).getReportType());
			UserBehavior.setExportTimes(UserBehaviorList.get(i).getExportTimes());
			UserBehavior.setEditTimes(UserBehaviorList.get(i).getEditTimes());
			UserBehavior.setSeeTimes(UserBehaviorList.get(i).getSeeTimes());

			va.add(UserBehavior);
		}
		Map<String, String> headMap = userBehaviorService.getExcelFields();

		String title = "用户报表访问情况";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}


//	@ResponseBody
//	@RequestMapping("/exportExcel")
//	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		Map<String, Object> map = JSON.parseObject(params, Map.class);
//		String beginStatDate = map.get("beginStatDate") + "";
//		if (StringUtils.isNotEmpty(beginStatDate)) {
//			map.put("beginStatDate", beginStatDate);
//		}
//
//		// 查询列表数据
//		List<UserBehaviorEntity> dataList = userBehaviorService.queryList(map);
//		JSONArray va = new JSONArray();
//		for (int i = 0; i < dataList.size(); i++) {
//			UserBehaviorEntity entity = dataList.get(i);
//			va.add(entity);
//		}
//		Map<String, String> headMap = userBehaviorService.getExcelFields();
//
//		String title = "用户使用报表情况";
//
//		ExcelUtil.downloadExcelFile(title, headMap, va, response);
//	}
}
