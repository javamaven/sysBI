package io.renren.controller.crm;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.Constant;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/** 
 * created by HUGUANG 2017年10月16日 下午4:06:44 
 */

@RestController
@RequestMapping("/crm/vip")
public class VIPController {

	private static final Logger logger = LoggerFactory.getLogger(VIPController.class);
	
	@Resource
	private DataSourceFactory dataSourceFactory;
	
	/**
	 * 查询价值分类
	 * @author HUGUANG 2017年10月16日下午4:13:51
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("crm:vip:list")
	public R list(VIPQueryParam param, Integer page, Integer limit){
		logger.info("查询VIP记录,param={},page={},limit={}",JSON.toJSONString(param),page,limit);
		String[] selectSql = new String[] {
				"SELECT vu.user_id, vu.dep_user_id, vu.user_name, vu.real_name, vu.gender, vu.phone,",
				"vub.recover_account_wait AS pre_account_wait,vui.recover_account_wait AS cur_account_wait,vui.account_balance,vui.level,vub.value_type,fa.real_name AS belong_real_name,cr.remark,cr.comm_remark,vui.data_date,",
				"fa.`real_name` as belong_real_name, vub.tags "
		};
		String[] fromSql = new String[]{
				" FROM vip_user_indicator vui ",
				" LEFT JOIN vip_user vu ON vu.user_id = vui.user_id ",
				" LEFT JOIN call_record cr ON cr.user_id = vui.user_id ",
				" LEFT JOIN vip_user_belongs vub ON vu.user_id = vub.user_id ",
				" LEFT JOIN financial_advisor fa ON fa.id = vub.belongs_to "
		};
		//查询列表的语句
		StringBuilder querySql = new StringBuilder();
		querySql.append(StringUtils.join(selectSql)).append(StringUtils.join(fromSql));
		querySql.append(param.toWhereSql());
		querySql.append(" ORDER BY cr.id DESC");
		querySql.append(" LIMIT ").append((page-1)*limit).append(",").append(limit);
		String sql = querySql.toString();
		String countSql = "select count(1) as count " + StringUtils.join(fromSql) + param.toWhereSql();
		logger.info("查询VIP管理列表,SQL={}", sql);
		logger.info("查询VIP列表,count SQL={}", countSql);
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			List<Map<String, Object>> listCall = null;
			for(Map<String, Object> map : list) {
				ju = new JdbcUtil(dataSourceFactory, "crmMysql");
				String ui = String.valueOf((Long) map.get("user_id"));
				if ((Long) map.get("user_id") != null) {
					listCall = ju.query("SELECT count(1) AS count FROM call_record WHERE user_id = " + ui + ';');
					Integer callCount = Integer.valueOf(listCall.get(0).get("count").toString());
					logger.info("callCount by userId={}", callCount);
					map.put("call_count", listCall == null ? 0 : callCount);
				}
			}
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			List<Map<String, Object>> list1 = ju.query(countSql);
			int count = 0;
			if(list != null && !list1.isEmpty()){
				count = Integer.valueOf(list1.get(0).get("count").toString());
			}
			PageUtils pageUtil = new PageUtils(list, count, limit, page);
			return R.ok().put("page", pageUtil);
		} catch (SQLException e) {
			logger.error("查询VIP列表失败", e);
		}
		return R.error("查询VIP列表失败");
	}
	
	/**
	 * 获取理财顾问名下vip信息
	 * @author HUGUANG 2017年10月18日下午5:12:55
	 */
	@ResponseBody
	@RequestMapping("/query_financial_advisor")
	@RequiresPermissions("crm:vip:list")
	public R query_financial_advisor(){
		StringBuilder sql = new StringBuilder("SELECT belongs_to,COUNT(1) AS belongs_count FROM vip_user_belongs GROUP BY belongs_to ");
		logger.info("查询理财顾问名下vip用户信息,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql.toString());
			List<Map<String, Object>> listCall = null;
			for(Map<String, Object> map : list) {
				ju = new JdbcUtil(dataSourceFactory, "crmMysql");
				if ((Long) map.get("user_id") != null) {
					listCall = ju.query("SELECT belongs_to,COUNT(1) AS youxiao_count"+ 
							" FROM vip_user_belongs vub" + 
							" LEFT JOIN vip_user_indicator vui ON vui.user_id = vub.user_id" +
							" WHERE vui.vip_status = 1 AND vub.belongs_to=" + map.get("belongs_to"));
					Integer youxiao_count = Integer.valueOf(listCall.get(0).get("youxiao_count").toString());
					logger.info("youxiao_count={}", youxiao_count);
					map.put("youxiao_count", youxiao_count == null ? 0 : youxiao_count);
				}
			}
			return R.ok().put("list", list);
		} catch (SQLException e) {
			logger.error("查询理财顾问名下vip用户信息失败", e);
			return R.error("查询理财顾问名下vip用户信息失败");
		}
	}
	
	/**
	 * SELECT t.`id`,t.`role`,t.`real_name`,t.`system_account`,t.`email` FROM `financial_advisor` t
	 * @author HUGUANG 2017年10月18日下午6:03:17
	 */
	private List<Map<String, Object>> formatList(List<Map<String, Object>> list) {
		return null;
	}

	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
/*		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String end_time = map.get("end_time") + "";
		String stat_time = map.get("stat_time") + "";
		String channelName = map.get("channelName") + "";
		String channelHead = map.get("channelHead") + "";
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
					channelHead += " and channelHead in ("+headString+") ";
				}else{
					channelHead += " and channelHead in ('123^abc') ";//没有权限的完全不能看到
				}
			}
			System.err.println("+++++++channelHead+++++" + channelHead);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {
			String path = this.getClass().getResource("/").getPath();
			String detail_sql;
			detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/channelAssess.txt"));
			detail_sql = detail_sql.replace("${channelName}", channelName);
			detail_sql = detail_sql.replace("${channelHead}", channelHead);
			detail_sql = detail_sql.replace("${end_time}", end_time);
			detail_sql = detail_sql.replace("${stat_time}", stat_time);
			List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
			resultList.addAll(list);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "渠道质量评估";
		headMap = getDayListExcelFields();
		ExcelUtil.downloadExcelFile(title, headMap, va, response);*/
	}
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("CHANNELHEAD", "负责人");
		headMap.put("CHANNELNAME", "渠道名称");
		headMap.put("CHANNELLABEL", "渠道标签");
		headMap.put("COST", "渠道费用");
		
		headMap.put("REGISTERED", "注册人数");
		headMap.put("AVGCOST", "人均注册成本");
		
		headMap.put("CGNUM", "存管实名人数");
		headMap.put("CZNUM", "充值人数");
		headMap.put("CZMONEY", "充值金额万");
		headMap.put("TXMONEY", "提现金额万");
		headMap.put("CTMONEY", "充提差万");
		
		headMap.put("SHOUTOU", "首投人数");
		headMap.put("FIRSTCOST", "人均首投成本");
		headMap.put("FIRSTMONEY", "首投金额");
		headMap.put("ROI", "首投金额ROI");
		
		
		headMap.put("INVESTNUM", "投资人数");
		headMap.put("INVESTMONEY", "投资金额");
		headMap.put("PTZNUM", "平台注册人数");
		
		headMap.put("PTINVESTNUM", "平台投资人数");
		headMap.put("PTINVESTMONEY", "平台投资金额");
		headMap.put("ZHMONEY", "账户余额万");
		headMap.put("DSMONEY", "待收金额万");
		headMap.put("DSLSNUM", "待收流失人数");
		headMap.put("INVESTLS", "投资用户流失率");
		headMap.put("ZICHAN", "资产留存率");
		headMap.put("CHONGZHI", "充值金额留存率");
		return headMap;

	}
	
}