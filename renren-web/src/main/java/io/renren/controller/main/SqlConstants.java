package io.renren.controller.main;

public class SqlConstants {
	
	static String city_string = "'上海','东莞','东营','中山','临汾','临沂','丹东','丽水','乌鲁木齐','佛山','保定','兰州','包头','北京','北海','南京','南宁','南昌','南通','厦门','台州','合肥','呼和浩特','咸阳','哈尔滨','唐山','嘉兴','大同','大连','天津','太原','威海','宁波','宝鸡','宿迁','常州','广州','廊坊','延安','张家口','徐州','德州','惠州','成都','扬州','承德','拉萨','无锡','日照','昆明','杭州','枣庄','柳州','株洲','武汉','汕头','江门','沈阳','沧州','河源','泉州','泰安','泰州','济南','济宁','海口','淄博','淮安','深圳','清远','温州','渭南','湖州','湘潭','滨州','潍坊','烟台','玉溪','珠海','盐城','盘锦','石家庄','福州','秦皇岛','绍兴','聊城','肇庆','舟山','苏州','莱芜','菏泽','营口','葫芦岛','衡水','衢州','西宁','西安','贵阳','连云港','邢台','邯郸','郑州','鄂尔多斯','重庆','金华','铜川','银川','镇江','长春','长沙','长治','阳泉','青岛','韶关'";
	
	/**
	 * 投资信息（平台投资情况滚动）
	 */
	public static String invest_info_sql = 
			"SELECT " +
			"	* " +
			"FROM " +
			"	dm_report_internal_display " +
			"where 1=1 " +
			"and type=1 " + 
			"and CAPITAL > 500 " + //投资金额小于500元的过滤掉
			"and addtime >= ? " +
			"and addtime <= ? " +
			"order by addtime asc ";
	
	//普通标交易总额sql
	public static String pt_total_amount = "SELECT SUM( CASE WHEN BT.TENDER_TYPE = 'normal_tender' THEN nvl ( BT.account_money, BT.account_tender ) + nvl (BT.reward_money, 0) WHEN BT.TENDER_TYPE = 'change_tender' THEN nvl ( BT.account_money, BT.account_tender ) ELSE 0 END ) TOTAL_AMOUNT FROM diyou_borrow_tender BT WHERE BT. STATUS IN (0, 1)";
//			"SELECT " +
//			"	SUM (p1. ACCOUNT) AS total_amount " +
//			"FROM " +
//			"	diyou_borrow p1 " +
//			"LEFT JOIN mjkf_borrow_file p2 ON p1.borrow_nid = p2.BORROW_NID " +
//			"WHERE " +
//			"	1 = 1 " +
//			"AND p1.publish = 'yes' " +
//			"AND p1.tender_last_time <= TO_NUMBER( SYSDATE -TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 * 1000 " + 
//			"AND ( " +
//			"	( " +
//			"		p1.status = 1 " +
//			"		AND p1.borrow_end_time < TO_NUMBER( SYSDATE -TO_DATE('1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS')) * 24 * 60 * 60 * 1000 " + 
//			"	) " +
//			"	OR p1.status = 3 " +
//			") " +
//			"AND p1.borrow_account_scale > CASE " +
//			"WHEN p2.borrow_nid IS NULL THEN " +
//			"	99.99 " +
//			"ELSE " +
//			"	0 " +
//			"END " ;
	
	//普通版债转交易总额
	public static String change_total_amount_sql = 
			"SELECT " +
			"	SUM (tc.amount) CHANGE_TOTAL_AMOUNT " +
			"FROM " +
			"	diyou_borrow_change c, " +
			"	( " +
			"		SELECT " +
			"			t1.change_id, " +
			"			MIN (t1.addtime) AS minaddtime, " +
			"			MAX (t1.addtime) AS maxaddtime, " +
			"			SUM (nvl(t1.account_money,t1.ACCOUNT_TENDER)) AS amount " +
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
	
	//存管版交易总额
	static String cg_total_amount_bak = 
			"SELECT " +
			"	IFNULL(SUM(borrow_account), 0) / 100 CG_TOTAL_AMOUNT " +
			"FROM " +
			"	project " +
			"WHERE " +
			"	project_complete_date > '0000-00-00' ";
	//存管版交易总额
	
	static String cg_total_amount = 
			"SELECT " +
			"  SUM(tender_capital) CG_TOTAL_AMOUNT " +
			"FROM " +
			"  ( " +
			"    SELECT " +
			"      IFNULL( " +
			"        CASE a.tender_subject " +
			"        WHEN 1 THEN " +
			"          a.tender_capital / 100 " +
			"        WHEN 2 THEN " +
			"          b.pay_amount / 100 " +
			"        END, " +
			"        0 " +
			"      ) tender_capital " +
			"    FROM " +
			"      project_tender_detail a " +
			"    LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id " +
			"    WHERE " +
			"      a.tender_account_status IN (0, 1) " +
			"    AND a.id NOT IN ( " +
			"      SELECT " +
			"        tender_detail_id " +
			"      FROM " +
			"        financial_plan_order_detail " +
			"    ) " +
			"    UNION ALL " +
			"      SELECT " +
			"        IFNULL(tender_amount / 100, 0) " +
			"      FROM " +
			"        financial_plan_order " +
			"  ) S ";

	
	//点点赚交易总额
	static String ddz_total_amount = 
			"select sum(project_scale_) DDZ_TOTAL_AMOUNT from mjkf_ddz_borrow " +
			"WHERE " +
			"	1 = 1 " +
			"AND issue_date_ <= SYSDATE " +
			"AND cleared_ = 'Y' ";
	
	/**
	 *地图数据查询
	 */
	public static String query_ditu_sql =
			"SELECT " +
			"	CITY, " +
			"	TO_CITY, " +
			"	sum(CAPITAL) TENDER_CAPITAL, " +
			"	case when count(1)=100 then 100 else mod(count(1), 100) END INVEST_TIMES, " +
			"	ADDTIME " +
			"FROM " +
			"	dm_report_internal_display " +
			"WHERE " +
			"	1 = 1 " +
			"AND type=1 " +
			"AND addtime >= ? " +
			"AND addtime <= ? " +
			"and (CITY in ("+city_string+") or CITY is null ) " +
			"and (TO_CITY in ("+city_string+") or CITY is null ) " +
			"GROUP BY " +
			"	city, " +
			"	TO_CITY " +
			"ORDER BY " +
			"	addtime limit 20 ";

	/**
	 * 地图上的闪点数据：注册和充值数据
	 */
	
	public static String query_register_charge_data = 
			"select city from dm_report_internal_display " +
			"where 1=1 " +
			"AND addtime >= ? " +
			"AND addtime <= ? " +
			"and type in (2,3) " +
			"and ( CITY in ("+city_string+")  ) " +
			"order by addtime  limit 200 ";
	
	
	/******交易趋势图***************************************************************************************************/
	//存管版交易金额
	public static String cg_invest_amount_list = "SELECT tender_capital MONEY,time TIME FROM( SELECT IFNULL( CASE a.tender_subject WHEN 1 THEN a.tender_capital / 100 WHEN 2 THEN b.pay_amount / 100 END, 0) tender_capital,a.addtime TIME FROM project_tender_detail a LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id WHERE a.tender_account_status IN (0, 1) AND a.id NOT IN ( SELECT tender_detail_id FROM financial_plan_order_detail ) AND a.addtime >= ? UNION ALL SELECT IFNULL(tender_amount / 100, 0),tender_time TIME FROM financial_plan_order WHERE 1 = 1 AND tender_time >= ? ) S";
	//普通版项目交易金额
	public static String pt_pro_invest_amount_list = "SELECT p1.ACCOUNT MONEY, tender_last_time TIME FROM diyou_borrow p1 LEFT JOIN mjkf_borrow_file p2 ON p1.borrow_nid = p2.BORROW_NID WHERE 1 = 1 AND p1.publish = 'yes' AND p1.tender_last_time <= TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS') ) * 24 * 60 * 60 * 1000 AND p1.tender_last_time >= TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - ? AND ( ( p1.status = 1 AND p1.borrow_end_time < TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 AND p1.borrow_end_time > TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - ? ) OR p1.status = 3 ) AND p1.borrow_account_scale > CASE WHEN p2.borrow_nid IS NULL THEN 99.99 ELSE 0 END";
	//普通版债转交易金额
	public static String pt_change_invest_amount_list = "SELECT tc.amount MONEY, tc.maxaddtime TIME FROM diyou_borrow_change c, ( SELECT t1.change_id, MIN (t1.addtime) AS minaddtime, MAX (t1.addtime) AS maxaddtime, SUM (t1.ACCOUNT_TENDER) AS amount FROM diyou_borrow_tender t1 WHERE t1.change_id IS NOT NULL GROUP BY t1.change_id) tc WHERE tc.change_id = c. ID AND (c.status = 1 OR c.status = 5) AND tc.maxaddtime <= TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 AND tc.maxaddtime >= TO_NUMBER ( SYSDATE - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - ?";

	//存管版交易金额
	public static String cg_30minutes_invest_amount = "SELECT SUM(tender_capital) CG_TOTAL_AMOUNT FROM( SELECT IFNULL( CASE a.tender_subject WHEN 1 THEN a.tender_capital / 100 WHEN 2 THEN b.pay_amount / 100 END, 0) tender_capital FROM project_tender_detail a LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id WHERE a.tender_account_status IN (0, 1) AND a.id NOT IN ( SELECT tender_detail_id FROM financial_plan_order_detail ) AND a.addtime >= ? AND a.addtime < ? UNION ALL SELECT IFNULL(tender_amount / 100, 0) FROM financial_plan_order WHERE 1 = 1 AND tender_time >= ? AND tender_time < ? ) S";
	//普通版项目交易金额
	public static String pt_pro_last_30minutes_invest_amount = "SELECT SUM(p1. ACCOUNT) AS MONEY FROM diyou_borrow p1 LEFT JOIN mjkf_borrow_file p2 ON p1.borrow_nid = p2.BORROW_NID WHERE 1 = 1 AND p1.publish = 'yes' AND p1.tender_last_time <= TO_NUMBER ( TO_DATE ( ?, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 AND p1.tender_last_time >= TO_NUMBER ( TO_DATE ( ?, 'yyyy-mm-dd hh24:mi:ss' ) - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - 0.5 * 60 * 60 * 1000 AND ( ( p1.status = 1 AND p1.borrow_end_time < TO_NUMBER ( TO_DATE ( ?, 'yyyy-mm-dd hh24:mi:ss' ) - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 AND p1.borrow_end_time > TO_NUMBER ( TO_DATE ( ?, 'yyyy-mm-dd hh24:mi:ss' ) - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - 0.5 * 60 * 60 * 1000 ) OR p1.status = 3 ) AND p1.borrow_account_scale > CASE WHEN p2.borrow_nid IS NULL THEN 99.99 ELSE 0 END";
	//普通版债转交易金额
	public static String pt_change_30minutes_invest_amount = "SELECT SUM(tc.amount) CHANGE_TOTAL_AMOUNT FROM diyou_borrow_change c, ( SELECT t1.change_id, MIN (t1.addtime) AS minaddtime, MAX (t1.addtime) AS maxaddtime, SUM (t1.ACCOUNT_TENDER) AS amount FROM diyou_borrow_tender t1 WHERE t1.change_id IS NOT NULL GROUP BY t1.change_id) tc WHERE tc.change_id = c. ID AND (c.status = 1 OR c.status = 5) AND tc.maxaddtime <= TO_NUMBER ( TO_DATE (?, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 AND tc.maxaddtime >= TO_NUMBER ( TO_DATE (?, 'yyyy-mm-dd hh24:mi:ss') - TO_DATE ( '1970-01-01 8:0:0', 'YYYY-MM-DD HH24:MI:SS' ) ) * 24 * 60 * 60 * 1000 - 0.5 * 60 * 60 * 1000";

	/******个人累计投资top10***************************************************************************************************/
	public static String top10_invest_sql = "SELECT * FROM ( SELECT P .USER_id, SUM (P .TENDER_CAPITAL) money FROM dm_trading_project_detail P LEFT JOIN dm_user_basic U ON (P .user_id = U .user_id) WHERE 1 = 1 AND U .IS_BORROWER = 0 AND U .USER_TYPE <> 2 GROUP BY P .user_id ORDER BY SUM (P .TENDER_CAPITAL) DESC ) s WHERE 1 = 1 AND ROWNUM < 11";

	/******当月投资总额，当天投资总额***************************************************************************************************/
	//普通版债转投资（普通版只算债转）
	public static String curr_invest_sql = 
					"select sum(p.account) money " +
					"  from mjkf_p2p.diyou_borrow_change p " +
					"where 1 = 1 " +
					"   and p.addtime >= " +
					"       (to_date(?, 'yyyy-mm-dd hh24:mi:ss') - " +
					"       to_date('1970-01-01', 'yyyy-mm-dd')) * 24 * 60 * 60 * 1000 " +
					"   and p.status = 1 ";
	//存管版投资 调用 sqlcg_invest_amount_list
	public static String curr_cg_invest_sql = "SELECT round(sum(tender_capital), 0) MONEY FROM ( SELECT IFNULL( CASE a.tender_subject WHEN 1 THEN a.tender_capital / 100 WHEN 2 THEN b.pay_amount / 100 END, 0 ) tender_capital, a.addtime TIME FROM project_tender_detail a LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id WHERE a.tender_account_status IN (0, 1) AND a.id NOT IN ( SELECT tender_detail_id FROM financial_plan_order_detail ) AND a.addtime >= ? UNION ALL SELECT IFNULL(tender_amount / 100, 0), tender_time TIME FROM financial_plan_order WHERE 1 = 1 AND tender_time >= ? ) S";
		
	/******当月投资笔数，当天投资笔数***************************************************************************************************/
	//普通版债转投资（普通版）
	public static String curr_invest_times_sql = 
					"select sum(s.times) invest_times from ( " +
					"	select count(1) times from diyou_borrow_tender p  " +
					"	where 1=1 and p.addtime >  " +
					"	(to_date(?, 'yyyy-mm-dd hh24:mi:ss')-to_date('1970-01-01 08:00:00','yyyy-mm-dd hh24:mi:ss'))* 24*60*60*1000 " +
					"	union all " +
					"	select count(1) times from  mjkf_ddz.mjkf_ddz_investor_account_log l " + 
					"	where 1=1 and l.type_ in ('B','RP')  " +
					"	and l.created_time_ > to_date(?, 'yyyy-mm-dd hh24:mi:ss') " +
					" ) s ";
	//存管版投资 调用 sqlcg_invest_amount_list
	public static String curr_cg_invest_times_sql = "SELECT  count(1) invest_times  FROM ( SELECT IFNULL( CASE a.tender_subject WHEN 1 THEN a.tender_capital / 100 WHEN 2 THEN b.pay_amount / 100 END, 0 ) tender_capital, a.addtime TIME FROM project_tender_detail a LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id WHERE a.tender_account_status IN (0, 1) AND a.id NOT IN ( SELECT tender_detail_id FROM financial_plan_order_detail ) AND a.addtime >= ? UNION ALL SELECT IFNULL(tender_amount / 100, 0), tender_time TIME FROM financial_plan_order WHERE 1 = 1 AND tender_time >= ? ) S";

	
	//普通版累计投资笔数（普通版）
	public static String curr_invest_total_times_sql = 
						"select sum(s.times) invest_times from ( " +
						"	select count(1) times from mjkf_p2p.diyou_borrow_tender p where 1=1 and p.status in (0,1) " +
						"	union all " +
						"	select count(1) times from  mjkf_ddz.mjkf_ddz_investor_account_log l where l.type_ in ('B','RP') " + 
						" ) s ";
		//存管版投资笔数 调用 sqlcg_invest_amount_list
	public static String curr_cg_invest_total_times_sql = "SELECT count(1) invest_times FROM ( SELECT IFNULL( CASE a.tender_subject WHEN 1 THEN a.tender_capital / 100 WHEN 2 THEN b.pay_amount / 100 END, 0 ) tender_capital, a.addtime TIME FROM project_tender_detail a LEFT JOIN creditor_purchase_order b ON a.id = b.relate_tender_detail_id WHERE a.tender_account_status IN (0, 1) AND a.id NOT IN ( SELECT tender_detail_id FROM financial_plan_order_detail ) UNION ALL SELECT IFNULL(tender_amount / 100, 0), tender_time TIME FROM financial_plan_order WHERE 1 = 1 ) S ";

	
	/******各省份交易额***************************************************************************************************/
	public static String province_invest_sql = "SELECT s.province, sum(s.money) money FROM ( SELECT p.user_id, sum( CASE WHEN p.tender_subject = 2 THEN p.tend_cash ELSE p.tender_capital END ) money,max(nvl(h.area_pro, c.f_area)) province FROM edw_trading_project_detail p LEFT JOIN edw_user_basic u ON (p.user_id = u.user_id) LEFT JOIN ( SELECT area_city, max(area_pro) area_pro FROM dim_phone_area GROUP BY area_city ) h ON (h.area_city = u.city) LEFT JOIN ( SELECT area, max(f_area) f_area FROM dim_card_area GROUP BY area ) c ON (u.city = c.area) WHERE 1 = 1 AND p.addtime >= to_date ( ?, 'yyyy-mm-dd hh24:mi:ss' ) GROUP BY p.user_id ) s WHERE 1 = 1 GROUP BY s.province ORDER BY money desc ";

	
}
