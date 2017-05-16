package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.ChannelChannelAllEntity;
import io.renren.entity.DailyEntity;
import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
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
import java.util.Date;
import java.util.LinkedHashMap;
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
@RequestMapping("market")
public class MarketChannelController {
	@Autowired
	private MarketChannelService marketChannelDataService;


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("marketChannel:list")
	public R list(@RequestBody Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		
		//查询列表数据
		List<MarketChannelEntity> dmReportChannelDataList = marketChannelDataService.queryList(query);
		int total = marketChannelDataService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(dmReportChannelDataList, total,  query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<MarketChannelEntity> MarketChannelList = marketChannelDataService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < MarketChannelList.size() ; i++) {
			MarketChannelEntity MarketChannelUser = new MarketChannelEntity();
			MarketChannelUser.setStatPeriod(MarketChannelList.get(i).getStatPeriod());
			MarketChannelUser.setChannelHead(MarketChannelList.get(i).getChannelHead());
			MarketChannelUser.setType(MarketChannelList.get(i).getType());
			MarketChannelUser.setChannelName(MarketChannelList.get(i).getChannelName());
			MarketChannelUser.setActualCost(MarketChannelList.get(i).getActualCost());
			MarketChannelUser.setRegCou(MarketChannelList.get(i).getRegCou());
			MarketChannelUser.setFirstinvestCou(MarketChannelList.get(i).getFirstinvestCou());
			MarketChannelUser.setFirstinvestMoney(MarketChannelList.get(i).getFirstinvestMoney());
			MarketChannelUser.setFirstinvestYMoney(MarketChannelList.get(i).getFirstinvestYMoney());
			MarketChannelUser.setInvCou(MarketChannelList.get(i).getInvCou());
			MarketChannelUser.setInvMoney(MarketChannelList.get(i).getInvMoney());
			MarketChannelUser.setInvYMoney(MarketChannelList.get(i).getInvYMoney());
			MarketChannelUser.setDdzMoney(MarketChannelList.get(i).getDdzMoney());
			MarketChannelUser.setRegCost(MarketChannelList.get(i).getRegCost());
			MarketChannelUser.setFirstinvestCost(MarketChannelList.get(i).getFirstinvestCost());
			MarketChannelUser.setAvgFirstinvestMoney(MarketChannelList.get(i).getAvgFirstinvestMoney());
			MarketChannelUser.setRegInvConversion(MarketChannelList.get(i).getRegInvConversion());
			MarketChannelUser.setFirstinvestRot(MarketChannelList.get(i).getFirstinvestRot());
			MarketChannelUser.setCumulativeRot(MarketChannelList.get(i).getCumulativeRot());

			va.add(MarketChannelUser);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("statPeriod","日期");
		headMap.put("channelHead","主负责人");
		headMap.put("type","渠道类型");
		headMap.put("channelName","渠道名称");
		headMap.put("actualCost","实际消费");
		headMap.put("regCou","新增注册人");
		headMap.put("firstinvestCou","新增首投人数");
		headMap.put("firstinvestMoney","首投金额");
		headMap.put("firstinvestYMoney","首投年化金额");
		headMap.put("invCou","投资总人数");
		headMap.put("invMoney","投资总金额");
		headMap.put("invYMoney","年化投资总金额");
		headMap.put("ddzMoney","点点赚购买金额");
		headMap.put("regCost","注册成本");
		headMap.put("firstinvestCost","首投成本");
		headMap.put("avgFirstinvestMoney","人均首投");
		headMap.put("regInvConversion","注册人投资转化率");
		headMap.put("firstinvestRot","首投ROI");
		headMap.put("cumulativeRot","累计ROI");


		String title = "渠道负责人明细";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}

	
}
