package io.renren.controller.crm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
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
				"vub.recover_account_wait,vui.recover_account_wait,vui.account_balance,vui.level,vub.value_type,fa.real_name AS belong_real_name,cr.remark,cr.comm_remark,vui.data_date,",
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
			/*List<Map<String, Object>> listTags = null;
			for(Map<String, Object> map : list) {
				ju = new JdbcUtil(dataSourceFactory, "crmMysql");
				String ui = String.valueOf((Long) map.get("user_id"));
				if ((Long) map.get("user_id") != null) {
					listTags = ju.query("SELECT tags FROM vip_user_belongs WHERE user_id = " + ui + ';');
					logger.info("listTags={}", listTags);
				}
			}*/
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
}