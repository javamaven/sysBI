package io.renren.controller.shichang;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.entity.shichang.DmReportDalilyMarketingEntity;
import io.renren.entity.yunying.dayreport.DmReportVipUserEntity;
import io.renren.service.UserBehaviorService;
import io.renren.service.shichang.DmReportDalilyMarketingService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-31 14:16:57
 */
@Controller
@RequestMapping("dmreportdalilymarketing")
public class DmReportDalilyMarketingController {
	@Autowired
	private DmReportDalilyMarketingService dmReportDalilyMarketingService;
	
	@Autowired
	UserBehaviorService userBehaviorService;
	
	private String reportType = "市场每日数据";
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/dayList")
	public R dayList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
//		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryList(map);
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryDayList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String type = map.get("type") + "";
		List<DmReportDalilyMarketingEntity> dataList = null;
		String title = "";
		Map<String, String> headMap = null;
		if("day".equals(type)){
			dataList = dmReportDalilyMarketingService.queryDayList(map);
			title = "渠道每日数据";
			headMap = getDayFields();
		}else if("month".equals(type)){
			dataList = dmReportDalilyMarketingService.queryMonthList(map);
			title = "渠道每月数据";
			headMap = getMonthFields();
		}else if("total".equals(type)){
			dataList = dmReportDalilyMarketingService.queryTotalList(map);
			title = "渠道全部数据";
			headMap = getTotalFields();
		}
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < dataList.size(); i++) {
			DmReportDalilyMarketingEntity entity = dataList.get(i);
			va.add(entity);
		}

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	
	private Map<String,String> getTotalFields(){
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("regNum", "注册人数");
		headMap.put("authNum", "实名人数");
		
		headMap.put("firstInvNum", "首投人数");
		
		headMap.put("invFirstMoney", "首投金额");
		headMap.put("yInvFirstMoney", "年化首投金额");
		headMap.put("invMoney", "投资金额");
		
		headMap.put("yInvMoney", "年化投资金额");
		headMap.put("reAmount", "充值金额");
		headMap.put("wiAmount", "提现金额");
		
		headMap.put("pureRecharge", "充提差");
		headMap.put("dayReAmount", "当日注册当日充值");
		headMap.put("dayInvMoney", "当日注册当日投资");
		
		headMap.put("cRegNum", "渠道注册人数");
		headMap.put("cAuthNum", "渠道实名人数");
		headMap.put("cFirstInvNum", "渠道首投人数");
		
		headMap.put("cInvFirstMoney", "渠道首投金额");
		headMap.put("yCInvFirstMoney", "年化渠道首投金额");
		headMap.put("cInvMoney", "渠道投资金额");
		
		headMap.put("yCInvMoney", "渠道年化投资金额");
		headMap.put("cCost", "渠道费用");
		headMap.put("cRecharge", "渠道充值");
		
		return headMap;
		
	}
	
	private Map<String,String> getMonthFields(){
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("regNum", "注册人数");
		headMap.put("authNum", "实名人数");
		
		headMap.put("firstInvNum", "首投人数");
		
		headMap.put("invFirstMoney", "首投金额");
		headMap.put("yInvFirstMoney", "年化首投金额");
		headMap.put("invMoney", "投资金额");
		
		headMap.put("yInvMoney", "年化投资金额");
		headMap.put("reAmount", "充值金额");
		headMap.put("wiAmount", "提现金额");
		
		headMap.put("pureRecharge", "充提差");
		headMap.put("dayReAmount", "当日注册当日充值");
		headMap.put("dayInvMoney", "当日注册当日投资");
		
		headMap.put("cRegNum", "渠道注册人数");
		headMap.put("cAuthNum", "渠道实名人数");
		headMap.put("cFirstInvNum", "渠道首投人数");
		
		headMap.put("cInvFirstMoney", "渠道首投金额");
		headMap.put("yCInvFirstMoney", "年化渠道首投金额");
		headMap.put("cInvMoney", "渠道投资金额");
		
		headMap.put("yCInvMoney", "渠道年化投资金额");
		headMap.put("mInvMoney", "当月首投用户当日投资");
		headMap.put("cCost", "渠道费用");
		headMap.put("cRecharge", "渠道充值");
		
		return headMap;
		
	}
	
	private Map<String,String> getDayFields(){
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("regNum", "注册人数");
		headMap.put("authNum", "实名人数");
		
		headMap.put("firstInvNum", "首投人数");
		headMap.put("reNum", "充值人数");
		headMap.put("invNum", "投资人数");
		
		headMap.put("invFirstMoney", "首投金额");
		headMap.put("yInvFirstMoney", "年化首投金额");
		headMap.put("invMoney", "投资金额");
		
		headMap.put("yInvMoney", "年化投资金额");
		headMap.put("reAmount", "充值金额");
		headMap.put("wiAmount", "提现金额");
		
		headMap.put("pureRecharge", "充提差");
		headMap.put("dayReAmount", "当日注册当日充值");
		headMap.put("dayInvMoney", "当日注册当日投资");
		
		headMap.put("cRegNum", "渠道注册人数");
		headMap.put("cAuthNum", "渠道实名人数");
		headMap.put("cFirstInvNum", "渠道首投人数");
		
		headMap.put("cInvFirstMoney", "渠道首投金额");
		headMap.put("yCInvFirstMoney", "年化渠道首投金额");
		headMap.put("cInvMoney", "渠道投资金额");
		
		headMap.put("yCInvMoney", "渠道年化投资金额");
		headMap.put("mInvMoney", "当月首投用户当日投资");
		headMap.put("cCost", "渠道费用");
		headMap.put("cRecharge", "渠道充值");
		
		return headMap;
		
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/monthList")
	public R monthList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryMonthList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/totalList")
	public R totalList(Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		
		//查询列表数据
		List<DmReportDalilyMarketingEntity> dmReportDalilyMarketingList = dmReportDalilyMarketingService.queryTotalList(map);
		int total = dmReportDalilyMarketingService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDalilyMarketingList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{statPeriod}")
	public R info(@PathVariable("statPeriod") String statPeriod){
		DmReportDalilyMarketingEntity dmReportDalilyMarketing = dmReportDalilyMarketingService.queryObject(statPeriod);
		
		return R.ok().put("dmReportDalilyMarketing", dmReportDalilyMarketing);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingService.save(dmReportDalilyMarketing);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody DmReportDalilyMarketingEntity dmReportDalilyMarketing){
		dmReportDalilyMarketingService.update(dmReportDalilyMarketing);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] statPeriods){
		dmReportDalilyMarketingService.deleteBatch(statPeriods);
		
		return R.ok();
	}
	
}
