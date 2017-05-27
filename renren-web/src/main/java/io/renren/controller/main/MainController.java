package io.renren.controller.main;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.pool.DruidDataSource;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcHelper;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.DateUtil;
import io.renren.utils.R;

@RestController
@RequestMapping(value = "/main")
public class MainController {

	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	@Autowired
	private DruidDataSource dataSource;
	
	String invest_info_sql = 
			"SELECT " +
			"	* " +
			"FROM " +
			"	dm_report_internal_display " +
			"where 1=1 " +
			"and addtime >= ? " +
			"and addtime <= ? " +
			"order by addtime asc ";
	
	/**
	 * 平台投资情况滚动
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryInvestInfo")
	public R queryInvestInfo()  {
		List<Map<String,Object>> dataList = null;
		List<List<Map<String,Object>>> dituData = new ArrayList<List<Map<String,Object>>>();
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		long l1 = System.currentTimeMillis();
		System.err.println("++++++++time+++++++++++" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		try {
			JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
			String startTime = getStartTime();
			String endTime = getEndTime();
			dataList = jdbcHelper.query(invest_info_sql, startTime, endTime);
//			for (int i = 0; i < dataList.size(); i++) {
//				Map<String, Object> map = dataList.get(i);
//				map.put("TENDER_CAPITAL", i);
//				newList.add(map);
//				if(i == 10){
//					break;
//				}
//			}
			List<Map<String,Object>> list;
			for (int i = 0; i < dataList.size(); i++) {
				list = new ArrayList<Map<String,Object>>();
				Map<String,Object> city = new HashMap<String,Object>();
				Map<String,Object> to_city = new HashMap<String,Object>();
				Map<String, Object> map = dataList.get(i);
				String CITY = map.get("CITY") + "";
				String TO_CITY = map.get("TO_CITY") + "";
				String TENDER_CAPITAL = map.get("TENDER_CAPITAL") + "";
				city.put("name", CITY);
				to_city.put("name", TO_CITY);
				to_city.put("value", 5);
//				to_city.put("money", TENDER_CAPITAL);
				list.add(city);
				list.add(to_city);
				dituData.add(list);
				data.add(to_city);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++++平台投资情况滚动,耗时：" + (l2 - l1));
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("data", dataList);
		ret.put("dituData", dituData);
		ret.put("maopao_data", data);
		return R.ok().put("data", dataList).put("dituData", dituData).put("maopao_data", data);
//		return ret;
	}
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
	private String getEndTime() {
		String currDate = sdf.format(new Date());
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, 2, "yyyy-MM-dd HH");
		return currDayBefore + ":59:59";
	}

	private String getStartTime() {
		String currDate = sdf.format(new Date());
		String currDayBefore = DateUtil.getCurrDayBefore(currDate, 2, "yyyy-MM-dd HH");
		return currDayBefore + ":00:00";
	}

	/**
	 * 平台注册人数
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryRegisterUserNum")
	public R queryRegisterUserNum()  {
		int register_user = 0;
		try {
			long l1 = System.currentTimeMillis();
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query("select  COUNT(1) as  register_user  from  diyou_users");
			Map<String, Object> map = list.get(0);
			register_user = Integer.parseInt(map.get("REGISTER_USER") + "");
			long l2 = System.currentTimeMillis();
			System.err.println("+++++++++queryRegisterUserNum,耗时：" + (l2 - l1));
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		}
		return R.ok().put("register_user", register_user);
	}
	
	//普通标交易总额sql
	String pt_total_amount = 
			"SELECT " +
			"	SUM (p1. ACCOUNT) AS total_amount " +
			"FROM " +
			"	diyou_borrow p1 " +
			"LEFT JOIN mjkf_borrow_file p2 ON p1.borrow_nid = p2.BORROW_NID " +
			"WHERE " +
			"	1 = 1 " +
			"AND p1.publish = 'yes' " +
			"AND p1.tender_last_time <= TO_NUMBER( SYSDATE -TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 * 1000 " + 
			"AND ( " +
			"	( " +
			"		p1.status = 1 " +
			"		AND p1.borrow_end_time < TO_NUMBER( SYSDATE -TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 * 1000 " + 
			"	) " +
			"	OR p1.status = 3 " +
			") " +
			"AND p1.borrow_account_scale > CASE " +
			"WHEN p2.borrow_nid IS NULL THEN " +
			"	99.99 " +
			"ELSE " +
			"	0 " +
			"END " ;

	/**
	 * 普通标交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryPutongTotalInvestAmount(){
		double total_amount = 0;
		try {
			long l1 = System.currentTimeMillis();
			JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
			List<Map<String, Object>> list = util.query(pt_total_amount);
			Map<String, Object> map = list.get(0);
			total_amount = Double.parseDouble(map.get("TOTAL_AMOUNT") + "");
			long l2 = System.currentTimeMillis();
			System.err.println("+++++++++普通标交易总额："+total_amount+",耗时：" + (l2 - l1));
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		} 
		return total_amount;
	}
	
	String change_total_amount_sql = 
			"SELECT " +
			"	SUM (tc.amount) CHANGE_TOTAL_AMOUNT " +
			"FROM " +
			"	diyou_borrow_change c, " +
			"	( " +
			"		SELECT " +
			"			t1.change_id, " +
			"			MIN (t1.addtime) AS minaddtime, " +
			"			MAX (t1.addtime) AS maxaddtime, " +
			"			SUM (t1.ACCOUNT_TENDER) AS amount " +
			"		FROM " +
			"			diyou_borrow_tender t1 " +
			"		WHERE " +
			"			t1.change_id IS NOT NULL " +
			"		GROUP BY " +
			"			t1.change_id " +
			"	) tc " +
			"WHERE " +
			"	tc.change_id = c. ID " +
			"AND (c.status = 1 OR c.status = 5) " +
			"AND tc.maxaddtime <= TO_NUMBER( SYSDATE -TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 * 1000 "; 
	/**
	 * 债转标交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryChangeTotalInvestAmount() throws SQLException{
		long l1 = System.currentTimeMillis();
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(change_total_amount_sql);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CHANGE_TOTAL_AMOUNT") + "");
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++++债转标交易总额："+total_amount+",耗时：" + (l2 - l1));
		return total_amount;
	}
	
	String ddz_total_amount = 
				"select sum(project_scale_) DDZ_TOTAL_AMOUNT from mjkf_ddz_borrow " +
				"WHERE " +
				"	1 = 1 " +
				"AND issue_date_ <= SYSDATE " +
				"AND cleared_ = 'Y' ";
	/**
	 * 点点赚交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryDdzTotalInvestAmount() throws SQLException{
		long l1 = System.currentTimeMillis();
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "oracle");
		List<Map<String, Object>> list = util.query(ddz_total_amount);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("DDZ_TOTAL_AMOUNT") + "");
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++++点点赚交易总额："+total_amount+",耗时：" + (l2 - l1));
		return total_amount;
	}
	
	String cg_total_amount = 
			"SELECT " +
			"	IFNULL(SUM(borrow_account), 0) / 100 CG_TOTAL_AMOUNT " +
			"FROM " +
			"	project " +
			"WHERE " +
			"	project_complete_date > '0000-00-00' ";
	/**
	 * 存管版交易总额
	 * @return
	 * @throws SQLException
	 */
	private double queryCgTotalInvestAmount() throws SQLException{
		long l1 = System.currentTimeMillis();
		JdbcUtil util = new JdbcUtil(dataSourceFactory, "mysql");
		List<Map<String, Object>> list = util.query(cg_total_amount);
		Map<String, Object> map = list.get(0);
		double total_amount = Double.parseDouble(map.get("CG_TOTAL_AMOUNT") + "");
		long l2 = System.currentTimeMillis();
		System.err.println("+++++++++点点赚交易总额："+total_amount+",耗时：" + (l2 - l1));
		return total_amount;
	}
	
	java.text.NumberFormat numberFormat = java.text.NumberFormat.getInstance();   
	/**
	 * 平台投资总额
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/queryTotalInvestAmount")
	public R queryTotalInvestAmount() {
		double total_amount = 0;
		long l1 = System.currentTimeMillis();
		try {
			total_amount += queryPutongTotalInvestAmount();
			total_amount += queryChangeTotalInvestAmount();
			total_amount += queryDdzTotalInvestAmount();
			total_amount += queryCgTotalInvestAmount();
		} catch (Exception e) {
			dataSourceFactory.reInitConnectionPoll();
			e.printStackTrace();
		} 
		long l2 = System.currentTimeMillis();
		numberFormat.setGroupingUsed(false);  
		System.err.println("+++++++++平台投资总额:"+numberFormat.format(total_amount)+",耗时：" + (l2 - l1));
		
//		System.err.println("++++++++++mysql连接池情况++++++++++++++");
//		dataSourceFactory.printMysqlConnectionPoll();
//		System.err.println("++++++++++oracle连接池情况++++++++++++++");
//		dataSourceFactory.printOracleConnectionPoll();
		return R.ok().put("total_amount", numberFormat.format(total_amount));
	}
}
