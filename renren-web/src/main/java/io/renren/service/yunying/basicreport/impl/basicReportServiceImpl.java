package io.renren.service.yunying.basicreport.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.basicreport.BasicReportDao;
import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;

import static io.renren.utils.ShiroUtils.getUserId;

@Service("basicReportService")
public class basicReportServiceImpl implements BasicReportService {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Autowired
	private BasicReportDao basicReportDao;
	
	String query_sql = 
			"SELECT " +
			"	u.user_id 用户ID, " +
			"	u2.user_name 用户名, " +
			"	u2.phone 手机号, " +
			"	u.register_time 注册时间, " +
			"	u.activity_tag 用户来源, " +
			"	case when u3.real_name is null then '否' else '是' end 实名认证, " +
			"	u3.real_name 真实姓名, " +
			"	'否' 是否投资, " +
			"  0 投资金额, " +
			"	0 投资次数, " +
			"	'' 最近一次投资时间, " +
			"	'' 最近一次投资期限, " +
			"	0 账户余额 " +
			"FROM " +
			"	mdtx_user.user_ext_info u " +
			"LEFT JOIN mdtx_user.user_basic_info u2 ON (u.user_id = u2.id) " +
			"LEFT JOIN mdtx_user.user_auth_info u3 ON (u.user_id = u3.user_id) " +
			"left join creditor_purchase_order c on (c.assignee_user_id=u.user_id)  " +
			"left join financial_plan_order_detail l on (l.user_id=u.user_id)  " +
			"left join project_tender_detail p on (p.user_id=u.user_id) " +
			"WHERE " +
			"	1 = 1 " +
			"AND u.register_time >= ? " +
			"AND u.register_time <= ? " +
			"AND u2.is_borrower = 0  " +
			"group by u.user_id " +
			"HAVING IFNULL(sum(c.pay_amount) + 	sum(l.tender_amount) + 	sum(p.tender_capital),0) = 0 ";

	@Override
	public PageUtils queryList(Integer page, Integer limit, String registerStartTime, String registerEndTime, int start, int end) {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, Object>> resultList = util.query(query_sql, registerStartTime, registerEndTime);

			Map<String, String> channel_map = getChannelMap();
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
				String channel_label = map.get("用户来源") + "";
				if (channel_map.containsKey(channel_label)) {
					map.put("用户来源", channel_map.get(channel_label));
					list.add(map);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if(list.size() > 0){
			if (end > list.size()) {
				retList.addAll(list.subList(start, list.size()));
			} else {
				retList.addAll(list.subList(start, end));
			}
		}
		if (retList.size() > 0) {
			getAmontByUserId(retList);// 统计用户的账户余额
		}

		PageUtils pageUtil = new PageUtils(retList, list.size(), limit, page);
		return pageUtil;
	}


	

	String channel_sql = 
			"SELECT " +
			"	d.CHANNEL_ID, " +
			"	d.CHANNEL_NAME, " +
			"	d.CHANNEL_NAME_BACK, " +
			"	d.CHANNEL_LABEL, " +
			"	d.PAYMENT_WAY " +
			"FROM " +
			"	DIM_CHANNEL d " +
			"WHERE " +
			"	1 = 1 " +
			"AND d.PAYMENT_WAY not LIKE '%CPS%' and d.CHANNEL_NAME not LIKE '%CPS%' " +
			"AND d.CHANNEL_NAME not LIKE '%触宝%' " +
			"AND d.CHANNEL_NAME not LIKE '%北瓜%' " +
			"AND d.CHANNEL_NAME not LIKE '%360摇一摇%' " ;
//			"AND ( d.PAYMENT_WAY LIKE '%免费%' OR d.PAYMENT_WAY LIKE '%测试%' OR d.PAYMENT_WAY LIKE '%置换%' OR d.CHANNEL_NAME LIKE '%免费%' OR d.CHANNEL_NAME LIKE '%测试%' OR d.CHANNEL_NAME LIKE '%置换%' )  ";
	
	private Map<String, String> getChannelMap() throws SQLException {
		List<Map<String, Object>> channel_list = new JdbcUtil(dataSourceFactory, "oracle26").query(channel_sql);
		Map<String,String> channel_map = new HashMap<String,String>();
		for (int i = 0; i < channel_list.size(); i++) {
			Map<String, Object> map = channel_list.get(i);
			String channelName = map.get("CHANNEL_NAME") + "";
			String chanelLabel = map.get("CHANNEL_LABEL") + "";
			channel_map.put(chanelLabel, channelName);
		}
		return channel_map;
	}
	
	/**
	 * 推荐返利
	 */
	String fanli_sql = 
			 "select d.spreads_userid USER_ID,sum(d.account) FANLI from diyou_spreads_log d " +
			 "where 1=1 ${userIdString} " +
			 "group by d.spreads_userid ";
	/**
	 * 普通版充值
	 */
	String pt_recharge_sql = 
			"select user_id USER_ID,sum(money) MONEY from  mjkf_p2p.diyou_account_recharge " +
			"where 1=1 ${userIdString} " +
			"group by user_id ";
	/**
	 * 存管版充值
	 */
	String cg_recharge_sql = 		
			"SELECT user_id USER_ID,sum(amount)/100 MONEY from account_recharge " +
			"where 1=1 ${userIdString} " +
			"and deduct_status=20 group by user_id ";
	
			 
	/**
	 * 统计用户的账户余额
	 * @param retList
	 */
	@Override
	public void getAmontByUserId(List<Map<String, Object>> retList) {
		List<String> inList = new ArrayList<String>();
		if(retList.size() == 0){
			return;
		}
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			inList.add(map.get("用户ID") + "");
		}
		String inString1 = getInString("d.spreads_userid", inList);
		String inString2 = getInString("user_id", inList);
		String inString3 = getInString("user_id", inList);
		//返利
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		//存管版充值
		JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "mysql");
		//普通版充值
		JdbcUtil util3 = new JdbcUtil(dataSourceFactory, "oracle");
		Map<String, Double> dataMap = new HashMap<String, Double>();
		try {
			List<Map<String, Object>> list = util.query(fanli_sql.replace("${userIdString}", inString1));
			List<Map<String, Object>> list2 = util2.query(cg_recharge_sql.replace("${userIdString}", inString2));
			List<Map<String, Object>> list3 = util3.query(pt_recharge_sql.replace("${userIdString}", inString3));
			
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String userId = map.get("USER_ID") + "";
				double fanli = Double.parseDouble(map.get("FANLI") + "");
				if(dataMap.containsKey(userId)){
					dataMap.put(userId, dataMap.get(userId) + fanli);
				}else{
					dataMap.put(userId, fanli);
				}
			}
			for (int i = 0; i < list2.size(); i++) {
				Map<String, Object> map = list2.get(i);
				String userId = map.get("USER_ID") + "";
				double money = Double.parseDouble(map.get("MONEY") + "");
				if(dataMap.containsKey(userId)){
					dataMap.put(userId, dataMap.get(userId) + money);
				}else{
					dataMap.put(userId, money);
				}
			}
			for (int i = 0; i < list3.size(); i++) {
				Map<String, Object> map = list3.get(i);
				String userId = map.get("USER_ID") + "";
				double money = Double.parseDouble(map.get("MONEY") + "");
				if(dataMap.containsKey(userId)){
					dataMap.put(userId, dataMap.get(userId) + money);
				}else{
					dataMap.put(userId, money);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			String userId = map.get("用户ID") + "";
			if(dataMap.containsKey(userId)){
				map.put("账户余额", dataMap.get(userId));
			}
		}
	}
	
	private static String getInString(String id, List<String> list) {
		StringBuffer sb = new StringBuffer();
		String returnString = "";
		if (list.size() == 0 || null == list) {
			returnString = sb.append(id).append("=''").toString();
		}
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				sb.append(" and (" + id);
				sb.append(" in (");
			}
			sb.append("'");
			sb.append(list.get(i).toString());
			sb.append("'");
			if (i >= 900 && i < list.size() - 1) {
				if (i % 900 == 0) {
					sb.append(") or ");
					sb.append(id);
					sb.append(" in (");
				} else {
					sb.append(",");
				}
			} else {
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
			if (i == list.size() - 1) {
				sb.append("))");
			}
		}
		returnString = sb.toString();
		return returnString;
	}
	
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("用户ID", "用户ID");
		headMap.put("用户名", "用户名");
		headMap.put("手机号", "手机号");
		
		headMap.put("注册时间", "注册时间");
		headMap.put("用户来源", "用户来源");
		headMap.put("实名认证", "实名认证");
		
		headMap.put("真实姓名", "真实姓名");
		headMap.put("是否投资", "是否投资");
		headMap.put("投资次数", "投资次数");
		
		headMap.put("最近一次投资时间", "最近一次投资时间");
		headMap.put("最近一次投资期限", "最近一次投资期限");
		headMap.put("账户余额", "账户余额");
		
		return headMap;
	}
	
	@Override
	public Map<String, String> getExcelFirstInvestNotMultiFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("USERNAME", "用户名");
		headMap.put("CG_USER_ID", "存管ID");
		headMap.put("REALNAME", "姓名");
		
		headMap.put("PHONE", "手机");
		headMap.put("REGISTER_TIME", "注册时间");
		headMap.put("DEPOSITORY_FIRSTINVEST_TIME", "首投时间");
		
		headMap.put("DEPOSITORY_FIRSTINVEST_BALANCE", "首投金额");
		headMap.put("BORROW_PERIOD", "首投期限");
		
		return headMap;
	}
	
	/**
	 * 查询注册3天未投资用户数据
	 */
	@Override
	public List<Map<String, Object>> queryRegisterThreeDaysNotInvestList(Map<String, Object> params) {
		List<Map<String, Object>> list = basicReportDao.queryRegisterThreeDaysNotInvestList(params);
		return list;
	}

	
	
	/**
	 * 本月首投3天后未复投用户
	 */
	@Override
	public PageUtils queryFirstInvestNotMultiList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		int page = 1;
		int limit = 0;
		if(map != null && !map.containsKey("page")){
			map.put("page", 1);
		}else{
			page = Integer.parseInt(map.get("page") + "");
		}
		if(map != null && !map.containsKey("limit")){
			map.put("limit", 10000);
			limit = 10000;
		}else{
			limit = Integer.parseInt(map.get("limit") + "");
		}
		
		int start = (page - 1) * limit;
		int end = start + limit;
		
		Query query = new Query(map);
		String statPeriod = map.get("statPeriod") + "";
//		query.put("startTime", statPeriod + " 00:00:00");
//		query.put("endTime",  statPeriod + " 23:59:59");
//		query.put("month",  statPeriod.substring(0 ,7));
//		List<String> userNameList = getPhoneCallNameList();
//		query.put("userNameList",  userNameList);
//		List<Map<String,Object>> dataList = basicReportDao.queryFirstInvestNotMultiList(query);
		List<Map<String, Object>> dataList = null;
		try {
			String path = this.getClass().getResource("/").getPath();
			String sql = FileUtil.readAsString(new File(path + File.separator + "sql/本月首投3天未复投用户.txt"));
			sql = sql.replace("${start}", "'" + statPeriod + " 00:00:00'");
			sql = sql.replace("${end}", "'" + statPeriod + " 23:59:59'");
			sql = sql.replace("${month}", "'" + statPeriod.substring(0 ,7) + "'");
			dataList = new JdbcUtil(dataSourceFactory, "oracle26").query(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		int totalCount = basicReportDao.queryFirstInvestNotMultiTotal(query);
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		if (dataList.size() > 0) {
			if (end > dataList.size()) {
				retList.addAll(dataList.subList(start, dataList.size()));
			} else {
				retList.addAll(dataList.subList(start, end));
			}
		}
		PageUtils pageList = new PageUtils(retList, dataList.size(), query.getLimit(), query.getPage());
		return pageList;
	}
	
	public List<String> getPhoneCallNameList(){
		List<String> list = new ArrayList<>();
		String sql = "select * from phone_sale_excel_data";
		List<Map<String, Object>> channel_list = null;
		try {
			channel_list = new JdbcUtil(dataSourceFactory, "oracle26").query(sql);
			for (int i = 0; i < channel_list.size(); i++) {
				Map<String, Object> map = channel_list.get(i);
				list.add(map.get("USER_NAME") + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 电销外呼申请历史数据（注册未投资用户）
	 */
	@Override
	public void batchInsertPhoneSaleData(List<Map<String, String>> dataList) {
//		basicReportDao.batchInsertPhoneSaleData(dataList);
		JdbcUtil jdbcUtil = new JdbcUtil(dataSourceFactory, "oracle26");
		
		String sql = "  insert into dm_report_phone_history_export (BATCH_ID, user_id, phone,INSERT_DATE, EXPORT_USER_ID ) values (?,?,?,sysdate,?)";
		
//		  BATCH_ID VARCHAR2(200),
//	         USER_ID VARCHAR2(200), 
//	         PHONE VARCHAR2(200),
//        INSERT_DATE DATE,
//        EXPORT_USER_ID VARCHAR2(200)
		List<List<Object>> insertList = new ArrayList<List<Object>>();
		
		try {
			long userId = getUserId();
			UUID randomUUID = UUID.randomUUID();
			List<Object> list = null;
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> map = dataList.get(i);
				list = new ArrayList<Object>();
				list.add(randomUUID);
				list.add(map.get("用户ID"));
				list.add(map.get("电话"));
				list.add(userId);
				insertList.add(list);
			}
			
			jdbcUtil.batchInsert(sql , insertList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
