package io.renren.controller;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;
import io.renren.service.UserBehaviorService;
import io.renren.service.shichang.ChannelHeadManagerService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.Constant;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;


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
	@Autowired
	private UserBehaviorService userBehaviorService;
	@Autowired
	private ChannelHeadManagerService channelHeadManagerService;
	
	private  String reportType="渠道负责人表";
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
	@RequiresPermissions("marketChannel:list")
	public Page list(Map<String, Object> params, Integer page, Integer limit, String reg_begindate,String reg_enddate,
					 String channelHead,String channelName,String channelName_a) {
		System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		params.put("page", (page - 1) * limit);
		params.put("limit", limit);
		params.put("reg_begindate", reg_begindate);
		params.put("reg_enddate", reg_enddate);
		params.put("channelHead", channelHead);
		params.put("channelName", JSON.parseArray(channelName + "", String.class));
		params.put("channelName_a", JSON.parseArray(channelName_a + "", String.class));
		if(getUserId() != Constant.SUPER_ADMIN){//不是超级管理员
			boolean isMarketDirector = channelHeadManagerService.isMarketDirector();
			if(!isMarketDirector){
				List<String> headList = channelHeadManagerService.queryAuthByChannelHead();
				System.err.println(headList);
				String headString = "";
				for (int i = 0; i < headList.size(); i++) {
					String head = headList.get(i);
					if(i == headList.size() - 1){
						headString += "'" + head + "'";
					}else{
						headString += "'" + head + "',";
					}
				}
				if(headList.size() > 0){
					params.put("channelHeadList", headString);
				}else{
					params.put("channelHeadList", "'123^abc'");
				}
				System.err.println("+++++++channelHead+++++" + headString);
			}
		}

		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List< MarketChannelEntity> dmReportDailyDataList = marketChannelDataService.queryList(query);
		int total = marketChannelDataService.queryTotal(params);


		Page page1 = new Page(total, dmReportDailyDataList);
		return page1;


	}

	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params,HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		Map<String,Object> map = JSON.parseObject(params, Map.class);
		if(getUserId() != Constant.SUPER_ADMIN){//不是超级管理员
			boolean isMarketDirector = channelHeadManagerService.isMarketDirector();
			if(!isMarketDirector){
				List<String> headList = channelHeadManagerService.queryAuthByChannelHead();
				System.err.println(headList);
				String headString = "";
				for (int i = 0; i < headList.size(); i++) {
					String head = headList.get(i);
					if(i == headList.size() - 1){
						headString += "'" + head + "'";
					}else{
						headString += "'" + head + "',";
					}
				}
				if(headList.size() > 0){
					map.put("channelHeadList", headString);
				}else{
					map.put("channelHeadList", "'123^abc'");
				}
			}
			System.err.println("+++++++channelHead+++++" + map);
		}
		List<MarketChannelEntity> ProjectSumList = marketChannelDataService.queryList(map);
		JSONArray va = new JSONArray();

		for (int i = 0; i < ProjectSumList.size(); i++) {
			MarketChannelEntity entity = ProjectSumList.get(i);
			va.add(entity);
		}

		Map<String, String> headMap = marketChannelDataService.getExcelFields();

		String title = "渠道负责人明细";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);



	}

}
