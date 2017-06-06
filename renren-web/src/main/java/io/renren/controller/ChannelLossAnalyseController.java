package io.renren.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import io.renren.entity.ChannelLossEntity;
import io.renren.service.ChannelLossService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/loss")
public class ChannelLossAnalyseController extends AbstractController {
	@Autowired
	private ChannelLossService service;

	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="渠道流失分析";

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 查询渠道流失分析列表
	 */
	@RequestMapping("/queryChannelLossList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelInvestTimesList(@RequestBody Map<String, Object> params) {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		long startTime = System.currentTimeMillis();
		PageUtils pageUtil = service.query(params);
		long endTime = System.currentTimeMillis();
		System.err.println("++++++++++++++++++++++++++++++++++查询总耗时：" + (endTime - startTime));
		return R.ok().put("page", pageUtil);
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	@RequiresPermissions("channel:channelAll:list")
	public void partExport(String list, HttpServletRequest request, HttpServletResponse response) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		List<ChannelLossEntity> dataList = JSON.parseArray(list, ChannelLossEntity.class);
		JSONArray va = new JSONArray();
		//
		for (int i = 0; i < dataList.size(); i++) {
			ChannelLossEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();
		String title = "渠道流失分析";
		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
