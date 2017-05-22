package io.renren.controller;

import java.io.IOException;
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

import io.renren.entity.ChannelStftInfoEntity;
import io.renren.service.ChannelStftInfoService;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@RestController
@RequestMapping("/channel/stft")
public class ChannelStftInfoController extends AbstractController {

	@Autowired
	private ChannelStftInfoService service;

	/**
	 * 查询渠道首投复投信息
	 */
	@RequestMapping("/stftInfo2")
	@RequiresPermissions("channel:channelAll:list")
	public R stftInfo2(@RequestBody Map<String, Object> params) {
		
		
		return null;
	}

	/**
	 * 查询渠道首投复投信息
	 */
	@RequestMapping("/stftInfo")
	@RequiresPermissions("channel:channelAll:list")
	public R stftInfo(@RequestBody Map<String, Object> params) {
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

		List<ChannelStftInfoEntity> dataList = JSON.parseArray(list, ChannelStftInfoEntity.class);
		JSONArray dataArray = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			ChannelStftInfoEntity entity = dataList.get(i);
			dataArray.add(entity);
		}
		Map<String, String> headMap = service.getExcelFields();
		String title = "渠道首投复投情况";
		ExcelUtil.downloadExcelFile(title, headMap, dataArray, response);
	}

}
