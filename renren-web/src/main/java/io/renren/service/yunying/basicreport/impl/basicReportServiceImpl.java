package io.renren.service.yunying.basicreport.impl;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;

import io.renren.dao.yunying.basicreport.BasicReportDao;
import io.renren.service.yunying.basicreport.BasicReportService;
import io.renren.system.common.ConfigProp;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MailUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;

@Service("basicReportService")
public class basicReportServiceImpl implements BasicReportService {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	DruidDataSource dataSource;
	
	@Autowired
	private BasicReportDao basicReportDao;
	
	String query_sql = 
			"SELECT " +
			"	u.user_id 用户ID, " +
			"	u2.user_name 用户名, " +
			"	u2.phone 手机号, " +
			"	u.register_time 注册时间, " +
			"	case when u.channel_id is null or u.channel_id = '' then u.activity_tag else u.channel_id end 用户来源, " +
			"	case when u3.real_name is null then '否' else '是' end 实名认证, " +
			"	u3.real_name 真实姓名, " +
			"	'否' 是否投资, " +
			"  0 投资金额, " +
			"	0 投资次数, " +
			"	'' 最近一次投资时间, " +
			"	'' 最近一次投资期限, " +
			"	ifnull(c.amount,0)/100 账户余额 " +
			"FROM " +
			"	mdtx_user.user_ext_info u " +
			"LEFT JOIN mdtx_user.user_basic_info u2 ON (u.user_id = u2.id) " +
			"LEFT JOIN mdtx_user.user_auth_info u3 ON (u.user_id = u3.user_id) " +
			"left join mdtx_business.account_tender c on (u.user_id=c.user_id) " +
			"WHERE " +
			"	1 = 1 " +
			"AND u.register_time >= ? " +
			"AND u.register_time <= ? " +
			"AND u2.is_borrower = 0  " +
			"and u2.phone is not null and u2.phone <> '' " +
			"and ifnull(c.amount,0)/100 <= 100 ";

	//记录未能识别是否收费，或者未录入dim_channel表的渠道
	List<String> channelRecordList = new ArrayList<>();
	
	@Override
	public PageUtils queryList(Integer page, Integer limit, String registerStartTime, String registerEndTime, int start, int end) {
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, Object>> resultList = util.query(query_sql, registerStartTime, registerEndTime);
			//免费渠道
			Map<String, String> channel_map = getChannelMap();
			//所有渠道
			Map<String, String> all_channel_map = getAllchannelMap();
			List<Map<String, Object>> insertList = new ArrayList<>();
			List<String> unknowNeedMoneyList = new ArrayList<>();//未知是否收费渠道
			List<String> unknowChannelList = new ArrayList<>();//渠道未录入dim_channel表
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
				String channel_label = map.get("用户来源") + "";
				//每小时推送免费的渠道
				//用户来源为null
				//
				if (channel_map.containsKey(channel_label) || StringUtils.isEmpty(channel_label)) {
					map.put("用户来源", channel_map.get(channel_label));
					list.add(map);
					continue;
				}
				//如果用户来源不为null，并且渠道还没更新到dim_channel,入库保存，并通知市场部人员更新
				if(!StringUtils.isEmpty(channel_label)){
					if(all_channel_map.containsKey(channel_label)){
						String channelType = all_channel_map.get(channel_label);
						if(StringUtils.isEmpty(channelType) || "null".equals(channelType)){//渠道是否收费未能确定，入库保存
							insertList.add(map);
							if(!channelRecordList.contains(channel_label)){
								unknowNeedMoneyList.add(channel_label);
								channelRecordList.add(channel_label);
							}
						}
					}else{//渠道还没录入，入库保存
						insertList.add(map);
						if(!channelRecordList.contains(channel_label)){
							unknowChannelList.add(channel_label);
							channelRecordList.add(channel_label);
						}
					}
				}
			}
			//插入记录表,3天未复投推送报表，带上这部分数据
			insertPhoneSaleData(insertList);
			if(unknowNeedMoneyList.size() > 0 || unknowChannelList.size() > 0){
				MailUtil mailUtil = new MailUtil();
				String content = "";
				if(unknowNeedMoneyList.size() > 0){
					content += "	未能识别是否是收费渠道：";
					for (int i = 0; i < unknowNeedMoneyList.size(); i++) {
						if(i == unknowNeedMoneyList.size() - 1){
							content += unknowNeedMoneyList.get(i);
						}else{
							content += unknowNeedMoneyList.get(i) + " , ";
						}
					}
				}
				if(unknowChannelList.size() > 0){
					content += "	<br/>未知渠道：";
					for (int i = 0; i < unknowChannelList.size(); i++) {
						if(i == unknowChannelList.size() - 1){
							content += unknowChannelList.get(i);
						}else{
							content += unknowChannelList.get(i) + " , ";
						}
					}
				}
				content += "	<br/><br/>以上渠道麻烦市场部同事及时录入渠道信息，并登录经分系统标记是否收费渠道,谢谢！";
				String phones = ConfigProp.getPhoneSaleChannelConfirmPhone();
				List<String> receiver = Arrays.asList(phones.split(","));
				List<String> chaosong = null;
				mailUtil.send("注册一小时未投资用户,未能识别是否收费渠道", content , receiver, chaosong);
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


	/**
	 * 将用户来源不为null的，并且渠道是否收费未能确定的用户数据插入数据库
	 * @param insertList
	 */
	private void insertPhoneSaleData(List<Map<String, Object>> insertList) {
		// TODO Auto-generated method stub
		List<List<Object>> dataList = new ArrayList<>();
		for (int i = 0; i < insertList.size(); i++) {
			List<Object> list = new ArrayList<>();
			Map<String, Object> map = insertList.get(i);
			list.add(JSON.toJSON(map));
			list.add(map.get("用户来源"));
			list.add(map.get("用户ID"));
			list.add("");
			dataList.add(list);
		}
		String insert_sql = "insert into phone_sale_hour_not_send(insert_time, data, channel_label, cg_user_id, channel_type) values(now(),?,?,?,?)";
		try {
			new JdbcHelper(dataSource).batchInsert(insert_sql, dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 所有渠道
	 * @return
	 */
	private Map<String, String> getAllchannelMap() {
		// TODO Auto-generated method stub
		String sql = "SELECT d.CHANNEL_LABEL,d.CHANNEL_NAME, t.CHANNEL_TYPE FROM dim_channel d LEFT JOIN dim_channel_type t ON ( d.channel_label = t.channel_label ) GROUP BY d.CHANNEL_LABEL";
		Map<String,String> channel_map = new HashMap<String,String>();
		List<Map<String, Object>> channel_list;
		try {
			channel_list = new JdbcHelper(dataSource).query(sql);
			for (int i = 0; i < channel_list.size(); i++) {
				Map<String, Object> map = channel_list.get(i);
				String channelType = map.get("CHANNEL_TYPE") + "";
				String chanelLabel = map.get("CHANNEL_LABEL") + "";
				channel_map.put(chanelLabel, channelType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return channel_map ;
	}




	String channel_sql = 
			"SELECT " +
			"	d.CHANNEL_ID, " +
			"	d.CHANNEL_NAME, " +
			"	d.CHANNEL_NAME_BACK, " +
			"	d.CHANNEL_LABEL, " +
			"	d.PAYMENT_WAY " +
			"FROM " +
			"	DIM_CHANNEL d LEFT JOIN DIM_CHANNEL_TYPE C ON (D.CHANNEL_LABEL = C.CHANNEL_LABEL)" +
			"WHERE " +
			"	1 = 1 " +
			"AND C.CHANNEL_TYPE LIKE '%免费%' " +
			"AND d.CHANNEL_NAME not LIKE '%触宝%' " +
			"AND d.CHANNEL_NAME not LIKE '%北瓜%' " +
			"AND d.CHANNEL_NAME not LIKE '%360摇一摇%' " ;			
	
	private Map<String, String> getChannelMap() throws SQLException {
		//		List<Map<String, Object>> channel_list = new JdbcUtil(dataSourceFactory, "oracle26").query(channel_sql);
		List<Map<String, Object>> channel_list = new JdbcHelper(dataSource).query(channel_sql);
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

	@Override
	public void batchInsertPhoneSaleCgUser(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		basicReportDao.batchInsertPhoneSaleCgUser(list);
	}

	@Override
	public List<Map<String, Object>> queryPhoneSaleCgUserList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		//type=1
		List<Map<String, Object>> list = null;
		String type = map.get("type") + "";
		map.put("cgUserId", "1");
		map.put("isExport", 1);//1已导出 2未导出  and p.is_export != #{isExport}
		if("1".equals(type)){//本月首投后三天未复投
			map.put("statPeriod", map.get("date"));
			map.put("limit", 100000);
			PageUtils page = queryFirstInvestNotMultiList(map);
			
			list = transformListData(page, map);
			
		}else{
			list = basicReportDao.queryPhoneSaleCgUserList(map);
		}
		return list;
	}

	//本月首投三天未复投功能与筛选存管版用户功能合并，返回结果KEY值不一样，需转换
	private List<Map<String, Object>> transformListData(PageUtils page, Map<String, Object> params) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) page.getList();
		List<Map<String, Object>> retList = new ArrayList<>();
		params.put("isExport", 2);
		String isKaitongCg = params.get("isKaitongCg") + "";//0 没选，1开通存管，2没开通存管
		params.put("isKaitongCg", 3);
		params.put("date", "");
		params.put("cgUserId", "");
		List<Map<String, Object>> exportList = basicReportDao.queryPhoneSaleCgUserList(params);
		
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Map<String, Object> newMap = new HashMap<>();
			boolean isExport = false;
			for (int j = 0; j < exportList.size(); j++) {
				Map<String, Object> map2 = exportList.get(j);
				String userId1 = map.get("USER_ID") + "";
				String userId2 = map2.get("user_id") + "";
				if(userId1.equals(userId2)){
					isExport = true;
					break;
				}
			}
			if(isExport){//已经导出过，不再查询
				continue;
			}
			
			newMap.put("user_id", map.get("USER_ID"));
			newMap.put("phone", map.get("PHONE"));
			newMap.put("user_name", map.get("USERNAME"));
			newMap.put("real_name", map.get("REALNAME"));
			newMap.put("call_date", map.get("CALL_DATE"));
			newMap.put("give_date", map.get("GIVE_DATE"));
			newMap.put("cg_user_id", map.get("CG_USER_ID"));
			Object cgOpenTime = map.get("DEPOSITORY_FIRSTINVEST_TIME");
			if("1".equals(isKaitongCg)){//开通存管
				if(cgOpenTime == null || StringUtils.isEmpty(cgOpenTime.toString())){
					continue;
				}
			}else if("2".equals(isKaitongCg)){//没开通存管
				if(cgOpenTime != null && !StringUtils.isEmpty(cgOpenTime.toString())){
					continue;
				}
			}
			newMap.put("depository_open_time", cgOpenTime);
			retList.add(newMap);
		}
		
		return retList ;
	}

	@Override
	public int queryPhoneSaleCgUserTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return basicReportDao.queryPhoneSaleCgUserTotal(map);
	}

	@Override
	public void updatePhoneSaleCgUserList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		basicReportDao.updatePhoneSaleCgUserList(map);
	}

	
}
