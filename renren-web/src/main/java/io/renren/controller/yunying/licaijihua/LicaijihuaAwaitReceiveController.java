package io.renren.controller.yunying.licaijihua;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.controller.AbstractController;
import io.renren.service.UserBehaviorService;
import io.renren.service.licaijihua.LicaijihuaAwaitReceiveService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@RestController
@RequestMapping("/yunying/licaijihua")
public class LicaijihuaAwaitReceiveController extends AbstractController {
	
	String reportType = "理财计划待收存量";

	@Autowired
	UserBehaviorService userBehaviorService;
	@Autowired
	LicaijihuaAwaitReceiveService licaijihuaAwaitReceiveService;
	
	@ResponseBody
	@RequestMapping("/list")
	public R list(Integer page, Integer limit, String statPeriod) {
		Map<String,Object> params = new HashMap<>();
		params.put("statPeriod", statPeriod);
		params.put("page", page);
		params.put("limit", limit);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看", reportType," ");
		long startTime = System.currentTimeMillis();
		PageUtils pageUtils = licaijihuaAwaitReceiveService.query(params);
		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtils);
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/export")
	public void phoneSaleCgUserListExport(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出", reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		map.put("page", 1);
		map.put("limit", 100000);
		PageUtils pageUtils = licaijihuaAwaitReceiveService.query(map);
		
		// 查询列表数据
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) pageUtils.getList();
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			va.add(dataList.get(i));
		}
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("PLAN_TYPE","计划类型");
		headMap.put( "BORROW_PERIOD","计划期限");
		headMap.put("CAPITAL","待收本金总量");
		
		headMap.put("AWAIT_RATE","总量占比");
		headMap.put("HUOQI_CAPITAL","活期待收总量");
		headMap.put( "HUOQI_RATE","活期待收占比");
		headMap.put("M_JOIN","当月成交总量");
		
		headMap.put("D_JOIN","当日成交总量");
		headMap.put("M_EXIT","当月退出总量");
		headMap.put( "D_EXIT","当日退出总量");
		headMap.put("EXIT_RATE","当日退出率");
		
		String title = reportType;
		try {
			ExcelUtil.exportExcel(title, headMap, va, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
