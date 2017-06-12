package io.renren.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.entity.MarketChannelEntity;
import io.renren.service.MarketChannelService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
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
@RequestMapping("market")
public class MarketChannelController {
	@Autowired
	private MarketChannelService marketChannelDataService;
	@Autowired
	private UserBehaviorService userBehaviorService;
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
		public Page list(Map<String, Object> params, Integer page, Integer limit, String statPeriod,String sourcecaseno,
						 String customername,String giveoutmoneytime,String willgetmoneydate) {
			System.err.println("+++++++++++++++++++++++params+++++++++++++++++++++++++++++++++++" + params);
			UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
			userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
			params.put("page", (page - 1) * limit);
			params.put("limit", limit);
			params.put("statPeriod", statPeriod);
			params.put("sourcecaseno", sourcecaseno);
			params.put("customername", customername);
			params.put("giveoutmoneytime", giveoutmoneytime);
			params.put("willgetmoneydate", willgetmoneydate);

			//查询列表数据
			Query query = new Query(params);
			int total = marketChannelDataService.queryTotal(params);
			//查询列表数据
			List< MarketChannelEntity> dmReportDailyDataList = marketChannelDataService.queryList(query);

			Page page1 = new Page(total, dmReportDailyDataList);
			return page1;


		}

	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(String params,HttpServletResponse response, HttpServletRequest request) throws IOException {

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");

		Map<String,Object> map = JSON.parseObject(params, Map.class);
//		String statPeriod = map.get("statPeriod") + "";
//		if (StringUtils.isNotEmpty(statPeriod)) {
//			map.put("statPeriod", statPeriod);
//		}
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
