package io.renren.controller.crm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.renren.controller.AbstractController;
import io.renren.service.main.MainService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * 通话记录
 * @author shihuaguo
 *
 */
@RestController
@RequestMapping("/crm/callrecord")
public class CallRecordController extends AbstractController{

private static final Logger logger = LoggerFactory.getLogger(CallRecordController.class);
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Autowired
	private MainService mainService;
	
	/**
	 * 通话记录列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("crm:callrecord:list")
	public R list(CallrecordQueryParam param, Integer page, Integer limit){
		logger.info("查询通话记录,param={},page={},limit={}",JSON.toJSONString(param),page,limit);
		String[] selectSql = new String[]{
				"SELECT t.id,t.`call_time`,t.`user_id`,v.`dep_user_id`,v.`user_name`,v.`real_name`,v.`gender`,v.`phone`,",
				"vb.`value_type`,vi.`level`,t.`status`,t.`account_wait`,t.`account_wait_inc_rate`,",
				"fa.`real_name` as belong_real_name,t.`remark` "
		};
		String[] fromSql = new String[]{
				"FROM `call_record` t LEFT JOIN `vip_user` v ON t.`user_id`=v.`user_id` ",
				"LEFT JOIN `vip_user_belongs` vb ON t.`user_id`=vb.`user_id`",
				"LEFT JOIN `vip_user_indicator` vi ON t.`user_id`=vi.`user_id` AND DATE_FORMAT(t.`call_time`,'%Y%m%d')=vi.`data_date`",
				"left join `financial_advisor` fa on vb.`belongs_to`=fa.`id` "
		};
		//查询列表的语句
		StringBuilder querySql = new StringBuilder();
		querySql.append(StringUtils.join(selectSql)).append(StringUtils.join(fromSql));
		querySql.append(param.toWhereSql());
		querySql.append(" order by t.id desc");
		querySql.append(" limit ").append((page-1)*limit).append(",").append(limit);
		String sql = querySql.toString();
		String countSql = "select count(1) as count " + StringUtils.join(fromSql) + param.toWhereSql();
		logger.info("查询通话记录,SQL={}", sql);
		logger.info("查询通话记录,count SQL={}", countSql);
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			List<Map<String, Object>> list1 = ju.query(countSql);
			int count = 0;
			if(list != null && !list1.isEmpty()){
				count = Integer.valueOf(list1.get(0).get("count").toString());
			}
			PageUtils pageUtil = new PageUtils(list, count, limit, page);
			return R.ok().put("page", pageUtil);
		} catch (SQLException e) {
			logger.error("查询通话记录失败", e);
		}
		return R.error("查询通话记录失败");
	}
	
	/**
	 * 添加通话时获取用户信息
	 */
	@ResponseBody
	@RequestMapping("/getVipInfo")
	@RequiresPermissions("crm:callrecord:list")
	public R getVipInfo(Integer user_id, Integer id, Integer p){
		logger.info("添加通话时获取用户上一次通话信息,user_id={},id={}",user_id,id);
		String call_record_sql = "select * from `call_record` t where t.id=" + id;
		if(p == 1){
			call_record_sql = "select * from `call_record` t where t.user_id=" + user_id + " and t.id<" + id + " order by t.id desc limit 1";
		}else if(p == 2){
			call_record_sql = "select * from `call_record` t where t.user_id=" + user_id + " and t.id>" + id + " order by t.id limit 1";
		}
		logger.info("添加通话时获取用户上一次通话信息,sql={}",call_record_sql);
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			R ok = R.ok();
			List<Map<String, Object>> list = ju.query(call_record_sql);
			if(!list.isEmpty()){
				ok.put("call_record", list.get(0));
			}else {
				return R.error(p==1 ? "已到达第一条记录":"已达最后一条");
			}
			String vip_sql = "select * from `vip_user` t where t.user_id=" + user_id;
			logger.info("添加通话时获取用户vip信息,sql={}",vip_sql);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			list = ju.query(vip_sql);
			if(!list.isEmpty()){
				Map<String, Object> vip = list.get(0);
				ok.put("vip", vip);
			}
			//通话次数
			String call_count_sql = "SELECT t.`user_id`,COUNT(*) AS COUNT FROM `call_record` t WHERE t.`user_id`=" + user_id + " GROUP BY t.`user_id`";
			logger.info("添加通话时获取用户通话次数信息,sql={}",call_count_sql);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			list = ju.query(call_count_sql);
			if(!list.isEmpty()){
				Map<String, Object> call_count = list.get(0);
				ok.put("call_count", call_count.get("COUNT"));
			}
			
			//获取上一次通话时待收和通话时间
			String pre_call_sql = "SELECT t.account_wait,t.call_time,t.question_item_json FROM `call_record` t WHERE t.`user_id`=" + user_id + " order by t.id desc limit 1";
			logger.info("添加通话时获取用户上一次通话时待收和通话时间,sql={}",call_count_sql);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			list = ju.query(pre_call_sql);
			if(!list.isEmpty()){
				Map<String, Object> pre_call_info = list.get(0);
				ok.put("pre_call_info", pre_call_info);
			}
			return ok;
		} catch (SQLException e) {
			logger.error("查询通话记录失败", e);
		}
		return R.error("查询通话记录失败");
	}
	
	/**
	 * 获取通话时用户待收
	 */
	@ResponseBody
	@RequestMapping("/getAccountWait")
	public R getAccountWait(Integer oldUserId, Integer cgUserId){
		logger.info("获取通话时用户待收,oldUserId={},cgUserId={}",oldUserId,cgUserId);
		R ok = R.ok();
		Map<String, Object> result = mainService.queryUserWait(oldUserId.toString(), cgUserId.toString());
		ok.put("wait", result.get("WAIT"));
		return ok;
	}
	
	/**
	 * 添加通话记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("crm:callrecord:save")
	public R saveCallrecord(@RequestBody Callrecord callrecord){
		logger.info("添加通话记录,callrecord={}",JSON.toJSONString(callrecord));
		
		//插入通话记录的SQL
		StringBuilder sb = new StringBuilder();
		sb.append("insert into call_record(user_id,call_time,status,account_wait,account_wait_inc_rate,call_type,call_duration,question_item_json,remark,comm_remark,create_by) ");
		sb.append("SELECT t.`user_id`,?,?,?,");
		sb.append("(?-t.`recover_account_wait`)*100/t.`recover_account_wait`,?,?,?,?,?,?");
		sb.append(" FROM `vip_user_belongs` t WHERE t.user_id=?");
		
		Object[] params = new Object[11];
		int i = 0;
		params[i++] = callrecord.getCall_time();
		params[i++] = callrecord.getStatus();
		params[i++] = callrecord.getAccount_wait();
		params[i++] = callrecord.getAccount_wait();
		params[i++] = callrecord.getCall_type();
		params[i++] = callrecord.getCall_duration();
		params[i++] = callrecord.getQuestion_item_json();
		params[i++] = callrecord.getRemark();
		params[i++] = callrecord.getComm_remark();
		params[i++] = getUserId();
		params[i++] = callrecord.getUser_id();
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		String sql = sb.toString();
		logger.info("添加通话记录,SQL={}", sql);
		logger.info("添加通话记录,参数={}", JSON.toJSONString(params));
		try {
			ju.execute(sql, params);
			return R.ok();
		} catch (SQLException e) {
			logger.error("添加通话记录失败", e);
			return R.error("添加通话记录失败");
		}
	}
}
