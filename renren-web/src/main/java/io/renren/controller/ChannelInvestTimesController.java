package io.renren.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.service.ChannelInvestTimesService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/invest")
public class ChannelInvestTimesController extends AbstractController {
	@Autowired
	private ChannelInvestTimesService service;

	SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 查询渠道首投复投信息
	 */
	@RequestMapping("/queryChannelInvestTimesList")
	@RequiresPermissions("channel:channelAll:list")
	public R queryChannelInvestTimesList(@RequestBody Map<String, Object> params) {
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

		List<ChannelInvestTimesEntity> dataList = JSON.parseArray(list, ChannelInvestTimesEntity.class);
		JSONArray va = new JSONArray();
		//
		for (int i = 0; i < dataList.size(); i++) {
			ChannelInvestTimesEntity entity = dataList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();
		String title = "渠道投资次数分析";
		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

}
