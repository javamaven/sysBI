package io.renren.controller.crm;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.renren.controller.AbstractController;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 统计报表
 * @author shihuaguo
 * @date 20171016
 *
 */
@RestController
@RequestMapping("/crm/report")
public class ReportController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	/**
	 * 查询当前资产信息
	 */
	@ResponseBody
	@RequestMapping("/getVipReport")
	@RequiresPermissions("crm:report:list")
	public R getVipReport(){
		logger.info("获取当前资产信息");
		StringBuilder sql = new StringBuilder("SELECT * FROM `vip_report` t ORDER BY t.`data_date` DESC limit 1");
		logger.info("获取当前资产信息,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			R r = R.ok();
			List<Map<String, Object>> list = ju.query(sql.toString());
			if(list != null && !list.isEmpty()){
				r.put("vip_report", list.get(0));
			}
			//获取昨日vip人数
			Date date = new Date();
			date = DateUtils.addDays(date, -1);
			String get_pre_day = "SELECT * FROM `vip_report` t where t.data_date='" + DateFormatUtils.format(date, "yyyyMMdd") + "' limit 1";
			logger.info("获取前一日数据,执行SQL={}", get_pre_day);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			list = ju.query(get_pre_day);
			if(list != null && !list.isEmpty()){
				r.put("vip_report_pre", list.get(0));
			}
			return r;
		} catch (SQLException e) {
			logger.error("获取当前资产信息失败", e);
			return R.error("获取当前资产信息失败");
		}
	}
	
	/**
	 * 查询资产趋势
	 */
	@ResponseBody
	@RequestMapping("/getVipReportTender")
	@RequiresPermissions("crm:report:list")
	public R getVipReportTender(DateParam param){
		logger.info("获取资产趋势,param={}", JSON.toJSONString(param));
		StringBuilder sql = new StringBuilder("SELECT t.`platform_account_wait`,t.`avg_account_wait`,t.`platform_tender_count`,");
		sql.append("t.`vip_account_wait`,t.`avg_vip_account_wait`,t.`vip_count`,t.`data_date` ");
		sql.append("FROM `vip_report` t ");
		sql.append(param.toWhereSql());
		sql.append(" ORDER BY t.`data_date`");
		logger.info("获取资产趋势,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			R r = R.ok();
			List<Map<String, Object>> list = ju.query(sql.toString());
			if(list != null && !list.isEmpty()){
				r.put("tender", list);
			}
			return r;
		} catch (SQLException e) {
			logger.error("获取资产趋势失败", e);
			return R.error("获取资产趋势失败");
		}
	}
	
	/**
	 * 外呼统计
	 */
	@ResponseBody
	@RequestMapping("/call_record_statistics")
	@RequiresPermissions("crm:report:list")
	public R call_record_statistics(String belongs_to, DateParam param, Integer page, Integer limit){
		logger.info("获取外呼统计,param={},belongs_to={}", JSON.toJSONString(param), belongs_to);
//		StringBuilder sql = new StringBuilder("SELECT DATE_FORMAT(t.`call_time`,'%Y%m%d') AS date_,fa.`real_name`,fa.id as fid");
//		sql.append("COUNT(t.`id`) AS call_count,SUM(IF(t.`status` IN (115,116),0,1)) AS call_yx_count,");
//		sql.append("SUM(IF(v.`user_id` IS NULL,0,1)) drwhfgl,SUM(t.`call_duration`) drts,COUNT(DISTINCT v.`user_id`) vip_count ");
//		sql.append("FROM `call_record` t ");
//		sql.append("LEFT JOIN `vip_user` v ON t.`user_id`=v.`user_id` ");
//		sql.append("LEFT JOIN `vip_user_belongs` vb ON t.`user_id`=vb.`user_id` ");
//		sql.append("LEFT JOIN `financial_advisor` fa ON vb.`belongs_to`=fa.`id` ");
		
		StringBuilder sql = new StringBuilder("SELECT DATE_FORMAT(cr.`call_time`,'%Y%m%d') AS date_,fa.`real_name`,fa.`id` AS fid,");
		sql.append("COUNT(cr.`id`) AS call_count,");
		sql.append("SUM(IF(cr.`status` NOT IN (115,116),1,0)) AS call_yx_count,");
		sql.append("COUNT(DISTINCT vb.`user_id`) drwhfgl,");
		sql.append("SUM(IFNULL(cr.`call_duration`,0)) drts,");
		sql.append("COUNT(DISTINCT vb.`user_id`) vip_count,");
		sql.append("SUM(IFNULL(vi.`recover_account_wait`,0)) vip_wait,");
		sql.append("SUM(IFNULL(vb.`recover_account_wait`,0)) vip_ori_wait ");
		sql.append("FROM `financial_advisor` fa ");
		sql.append("LEFT JOIN `vip_user_belongs` vb ON fa.`id`=vb.`belongs_to` ");
		sql.append("LEFT JOIN `call_record` cr ON vb.`user_id`=cr.`user_id` ");
		sql.append("LEFT JOIN `vip_user_indicator` vi ON vb.`user_id`=vi.`user_id` AND DATE_FORMAT(cr.`call_time`,'%Y%m%d')=SUBSTR(vi.`data_date`,1,8) ");
		sql.append(param.toWhereSql1("call_time"));
		if(StringUtils.isNotBlank(belongs_to)){
			sql.append(" and vb.belongs_to=").append(belongs_to);
		}
		sql.append(" GROUP BY fa.`id`,fa.`real_name`,DATE_FORMAT(cr.`call_time`,'%Y%m%d') ");
		logger.info("获取外呼统计,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			R r = R.ok();
			List<Map<String, Object>> list = ju.query(sql.toString());
			//查询每个理财顾问的vip人数、vip总待收、待收提升率
			/*List<Integer> fids = new ArrayList<>();
			for(Map<String, Object> map : list){
				Integer fid = Integer.parseInt(map.get("fid").toString());
				if(!fids.contains(fid)){
					fids.add(fid);
				}
			}
			if(!fids.isEmpty()){
				
			}*/
			PageUtils pageUtil = new PageUtils(list, list.size(), limit, page);
			return r.put("page", pageUtil);
		} catch (SQLException e) {
			logger.error("获取外呼统计失败", e);
			return R.error("获外呼统计失败");
		}
	}
}
