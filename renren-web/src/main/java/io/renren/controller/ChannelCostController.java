package io.renren.controller;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.MarketChannelEntity;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.ChannelCostEntity;
import io.renren.service.ChannelCostService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 渠道成本统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-16 10:11:01
 */
@Controller
@RequestMapping("channelcost")
public class ChannelCostController {
	@Autowired
	private ChannelCostService ChannelCostService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("channelcost:list")
	public R list(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		//查询列表数据
		List<ChannelCostEntity> dmReportChannelCostList = ChannelCostService.queryList(query);
		int total = ChannelCostService.queryTotal(query);
		return R.ok().put("page", dmReportChannelCostList);
	}

	@RequestMapping("/getChannel")
	public R getChannel(){
		//查询渠道数据
		return R.ok().put("Channel", ChannelCostService.queryChannel());
	}
	@ResponseBody
	@RequestMapping("/totalList")
	@RequiresPermissions("channelcost:list")
	public R totalList(@RequestBody Map<String, Object> map) {
//		MyBatisSql sql = MyBatisSqlUtils.getMyBatisSql("io.renren.dao.ChannelChannelAllDao.queryChannel", null, sqlSessionFactory);
//		System.err.println("++++++++++++++++++++++++sql+++++++++++++++++++" + sql);

		String startTime = map.get("startTime") + "";
		if (StringUtils.isNotEmpty(startTime)) {
			map.put("startTime", startTime.replace("-",""));
		}
		String endTime = map.get("endTime") + "";
		if (StringUtils.isNotEmpty(endTime)) {
			map.put("endTime", endTime.replace("-",""));
		}
		Object channelName = map.get("channelName");
		if (channelName == null || "".equals(channelName.toString().trim())) {
			map.put("channelName", new ArrayList<>());
		} else {
			channelName = channelName.toString().substring(0, channelName.toString().length() - 1);
			map.put("channelName", Arrays.asList(channelName.toString().split("\\^")));
		}
		System.err.println("++++++++++map: " + map);

		// 查询列表数据
		ChannelCostEntity channelCostEntity = ChannelCostService.queryTotalList(map);

		return R.ok().put("data", channelCostEntity);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<ChannelCostEntity> MarketChannelList = ChannelCostService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < MarketChannelList.size() ; i++) {
			ChannelCostEntity MarketChannelUser = new ChannelCostEntity();
			MarketChannelUser.setStatPeriod(MarketChannelList.get(i).getStatPeriod());
			MarketChannelUser.setChannelName(MarketChannelList.get(i).getChannelName());
			MarketChannelUser.setChannelLabel(MarketChannelList.get(i).getChannelLabel());
			MarketChannelUser.setRegCou(MarketChannelList.get(i).getRegCou());
			MarketChannelUser.setRegCost(MarketChannelList.get(i).getRegCost());
			MarketChannelUser.setFirstinvestCou(MarketChannelList.get(i).getFirstinvestCou());
			MarketChannelUser.setFirstinvestMoney(MarketChannelList.get(i).getFirstinvestMoney());
			MarketChannelUser.setFirstCost(MarketChannelList.get(i).getFirstCost());
			MarketChannelUser.setFirstROI(MarketChannelList.get(i).getFirstROI());
			MarketChannelUser.setInvMoney(MarketChannelList.get(i).getInvMoney());
			MarketChannelUser.setNumROI(MarketChannelList.get(i).getNumROI());
			MarketChannelUser.setActualCost(MarketChannelList.get(i).getActualCost());


			va.add(MarketChannelUser);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("statPeriod","日期");
		headMap.put("channelName","渠道名称");
		headMap.put("channelLabel","渠道标签");
		headMap.put("regCou","注册人数");
		headMap.put("regCost","注册成本");
		headMap.put("firstinvestCou","首投人数");
		headMap.put("firstinvestMoney","首投金额");
		headMap.put("firstCost","首投成本");
		headMap.put("firstROI","首投ROI");
		headMap.put("invMoney","累投金额");
		headMap.put("numROI","累投ROI");
		headMap.put("actualCost","费用");


		String title = "渠道成本统计";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

}
