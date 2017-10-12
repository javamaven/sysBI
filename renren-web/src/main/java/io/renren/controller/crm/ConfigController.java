package io.renren.controller.crm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.renren.annotation.SysLog;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.utils.R;

/**
 * 参数配置
 * @author shihuaguo
 *
 */
@RestController
@RequestMapping("/crm/config")
public class ConfigController {
	
private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	@Autowired
	private DataSourceFactory dataSourceFactory;
	
	/**
	 * 查询vip配置
	 */
	@ResponseBody
	@RequestMapping("/getVipConfig")
	@RequiresPermissions("crm:config:list")
	public R getVipConfig(){
		logger.info("获取vip配置信息");
		StringBuilder sql = new StringBuilder("SELECT t.`last_account_wait`,t.`loss_rate`,t.`word_skill_url` FROM `vip_config` t");
		logger.info("获取vip配置信息,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql.toString());
			if(list != null && !list.isEmpty()){
				return R.ok().put("vip_config", list.get(0));
			}
			return R.ok();
		} catch (SQLException e) {
			logger.error("获取vip配置信息失败", e);
			return R.error("获取vip配置信息失败");
		}
	}
	
	/**
	 * 更新VIP配置
	 */
	@SysLog("更新VIP配置")
	@ResponseBody
	@RequestMapping("/updateVipConfig")
	@RequiresPermissions("crm:config:update")
	public R updateVipConfig(Integer last_account_wait, Integer loss_rate){
		logger.info("更新VIP配置,last_account_wait={},loss_rate={}", last_account_wait, loss_rate);
		StringBuilder sql = new StringBuilder("update `vip_config` ");
		sql.append("set `last_account_wait`=").append(last_account_wait).append(",loss_rate=").append(loss_rate);
		logger.info("更新VIP配置,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("更新VIP配置失败", e);
			return R.error("更新VIP配置失败");
		}
	}
	
	/**
	 * 查询理财顾问
	 */
	@ResponseBody
	@RequestMapping("/query_financial_advisor")
	@RequiresPermissions("crm:config:list")
	public R query_financial_advisor(){
		logger.info("查询理财顾问");
		StringBuilder sql = new StringBuilder("SELECT t.`id`,t.`role`,t.`real_name`,t.`system_account`,t.`email` FROM `financial_advisor` t");
		logger.info("查询理财顾问,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql.toString());
			return R.ok().put("list", list);
		} catch (SQLException e) {
			logger.error("查询理财顾问失败", e);
			return R.error("查询理财顾问失败");
		}
	}
	
	//convert null to blank string
	private static final String cnbs(String s){
		return s == null ? "" : s;
	}
	
	/**
	 * 添加理财顾问
	 */
	@SysLog("添加理财顾问")
	@ResponseBody
	@RequestMapping("/save_financial_advisor")
	@RequiresPermissions("crm:config:add")
	public R save_financial_advisor(String role,String real_name,String system_account,String email){
		logger.info("添加理财顾问,role={},real_name={},system_account={},email={}",role,real_name,system_account,email);
		String err = checkAdvisor(role, real_name, system_account, email);
		if(err != null){
			return R.error(err);
		}
		StringBuilder sql = new StringBuilder("insert into financial_advisor(`role`,`real_name`,`system_account`,`email`) value('");
		sql.append(cnbs(role)).append("','");
		sql.append(cnbs(real_name)).append("','");
		sql.append(cnbs(system_account)).append("','");
		sql.append(cnbs(email)).append("')");
		logger.info("添加理财顾问,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("添加理财顾问失败", e);
			return R.error("添加理财顾问失败");
		}
	}
	
	private String checkAdvisor(String role,String real_name,String system_account,String email){
		if(StringUtils.isBlank(role)){
			return "角色不能为空";
		}
		if(StringUtils.isBlank(real_name)){
			return "姓名不能为空";
		}
		if(StringUtils.isBlank(system_account)){
			return "经分系统账号不能为空";
		}
		return null;
	}
	
	/**
	 * 修改理财顾问
	 */
	@SysLog("修改理财顾问")
	@ResponseBody
	@RequestMapping("/update_financial_advisor")
	@RequiresPermissions("crm:config:update")
	public R update_financial_advisor(Integer id, String role,String real_name,String system_account,String email){
		logger.info("修改理财顾问,id={},role={},real_name={},system_account={},email={}",id,role,real_name,system_account,email);
		String err = checkAdvisor(role, real_name, system_account, email);
		if(err != null){
			return R.error(err);
		}
		StringBuilder sql = new StringBuilder("update financial_advisor set role=");
		sql.append(cnbs(role)).append(",real_name='");
		sql.append(cnbs(real_name)).append("',system_account='");
		sql.append(cnbs(system_account)).append("',email='");
		sql.append(cnbs(email)).append("' where id=").append(id);
		logger.info("修改理财顾问,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("修改理财顾问失败", e);
			return R.error("修改理财顾问失败");
		}
	}
	
	/**
	 * 删除理财顾问
	 */
	@SysLog("删除理财顾问")
	@ResponseBody
	@RequestMapping("/del_financial_advisor")
	@RequiresPermissions("crm:config:del")
	public R del_financial_advisor(Integer id){
		logger.info("删除理财顾问,id={}",id);
		StringBuilder sql = new StringBuilder("delete from financial_advisor where id=").append(id);
		logger.info("删除理财顾问,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("删除理财顾问失败", e);
			return R.error("删除理财顾问失败");
		}
	}
	
	/**
	 * 查询价值分类、标签或通话状态
	 */
	@ResponseBody
	@RequestMapping("/value_class_list")
	@RequiresPermissions("crm:config:list")
	public R value_class_list(String type){
		logger.info("查询价值分类、标签或通话状态,type={}", type);
		StringBuilder sql = new StringBuilder("select t.`id`,t.type,t.sub_type,t.`name`,t.`desc` from `value_class` t");
		sql.append(" where t.type='").append(type).append("'");
		logger.info("查询价值分类、标签或通话状态,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql.toString());
			return R.ok().put("list", list);
		} catch (SQLException e) {
			logger.error("查询价值分类、标签或通话状态失败", e);
			return R.error("查询价值分类、标签或通话状态失败");
		}
	}
	
	/**
	 * 添加价值分类、标签或通话状态
	 */
	@SysLog("添加价值分类、标签或通话状态")
	@ResponseBody
	@RequestMapping("/save_value_class")
	@RequiresPermissions("crm:config:add")
	public R save_value_class(String type,String name,String desc){
		String msg = "1".equals(type) ? "价值分类" :("2".equals(type) ? "标签" : "通话状态");
		logger.info("添加价值分类、标签或通话状态,type={},name={},desc={}",type,name,desc);
		if(StringUtils.isBlank(name)){
			return R.error(msg + "不能为空");
		}
		StringBuilder sql = new StringBuilder("insert into value_class(`type`,`name`,`desc`) value('");
		sql.append(type).append("','");
		sql.append(name).append("','");
		sql.append(cnbs(desc)).append("')");
		logger.info("添加价值分类、标签或通话状态,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("添加" + msg + "失败", e);
			return R.error("添加" + msg + "失败");
		}
	}
	
	/**
	 * 修改价值分类、标签或通话状态
	 */
	@SysLog("修改价值分类、标签或通话状态")
	@ResponseBody
	@RequestMapping("/update_value_class")
	@RequiresPermissions("crm:config:update")
	public R update_value_class(Integer id, String type,String name,String desc){
		String msg = "1".equals(type) ? "价值分类" :("2".equals(type) ? "标签" : "通话状态");
		logger.info("修改价值分类、标签或通话状态,id={},type={},name={},desc={}",id,type,name,desc);
		if(StringUtils.isBlank(name)){
			return R.error(msg + "不能为空");
		}
		StringBuilder sql = new StringBuilder("update value_class set name='");
		sql.append(name).append("',`desc`='");
		sql.append(cnbs(desc)).append("' where id=").append(id);
		logger.info("修改价值分类、标签或通话状态,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("修改" + msg + "失败", e);
			return R.error("修改" + msg + "失败");
		}
	}
	
	/**
	 * 删除价值分类、标签或通话状态
	 */
	@SysLog("删除价值分类、标签或通话状态")
	@ResponseBody
	@RequestMapping("/del_value_class")
	@RequiresPermissions("crm:config:del")
	public R del_value_class(Integer id){
		logger.info("删除价值分类、标签或通话状态,id={}",id);
		StringBuilder sql = new StringBuilder("delete from value_class where id=").append(id);
		logger.info("删除价值分类、标签或通话状态,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("删除价值分类、标签或通话状态失败", e);
			return R.error("删除价值分类、标签或通话状态失败");
		}
	}

}
