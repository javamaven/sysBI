package io.renren.service.yunying.basicreport.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.PageUtils;

@Service("basicReportService")
public class basicReportServiceImpl implements BasicReportService {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	
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
		System.err.println("+++++++++start=" + start + " ;end=" + end);
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
			"AND d.PAYMENT_WAY not LIKE '%CPS%' " +
			"AND d.CHANNEL_NAME not LIKE '%触宝%' " +
			"AND d.CHANNEL_NAME not LIKE '%北瓜%' " +
			"AND d.CHANNEL_NAME not LIKE '%360摇一摇%' ";
	
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
			 "where d.spreads_userid in ${userIdString} " +
			 "group by d.spreads_userid ";
	/**
	 * 普通版充值
	 */
	String pt_recharge_sql = 
			"select user_id USER_ID,sum(money) MONEY from  mjkf_p2p.diyou_account_recharge " +
			"where user_id in ${userIdString} " +
			"group by user_id ";
	/**
	 * 存管版充值
	 */
	String cg_recharge_sql = 		
			"SELECT user_id USER_ID,sum(amount)/100 MONEY from account_recharge " +
			"where user_id in ${userIdString} " +
			"and deduct_status=20 group by user_id ";
	
			 
	/**
	 * 统计用户的账户余额
	 * @param retList
	 */
	private void getAmontByUserId(List<Map<String, Object>> retList) {
		String userIdString = "(";
		for (int i = 0; i < retList.size(); i++) {
			Map<String, Object> map = retList.get(i);
			if(i == retList.size() - 1){
				userIdString += map.get("用户ID");
			}else{
				userIdString += map.get("用户ID") + ",";
			}
		}
		userIdString += ")";
		//返利
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		//存管版充值
		JdbcUtil util2 = new JdbcUtil(dataSourceFactory, "mysql");
		//普通版充值
		JdbcUtil util3 = new JdbcUtil(dataSourceFactory, "oracle");
		Map<String, Double> dataMap = new HashMap<String, Double>();
		try {
			List<Map<String, Object>> list = util.query(fanli_sql.replace("${userIdString}", userIdString));
			List<Map<String, Object>> list2 = util2.query(cg_recharge_sql.replace("${userIdString}", userIdString));
			List<Map<String, Object>> list3 = util3.query(pt_recharge_sql.replace("${userIdString}", userIdString));
			System.err.println("+++++++list=" + list);
			System.err.println("+++++++list2=" + list2);
			System.err.println("+++++++list3=" + list3);
			System.err.println("++++++++++++++++++++++++");
			
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

}
