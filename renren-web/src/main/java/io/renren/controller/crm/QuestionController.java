package io.renren.controller.crm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import io.renren.utils.PageUtils;
import io.renren.utils.R;

/**
 * CRM问题管理
 *
 * @author shihuaguo
 * @date 2017-10-12
 */
@RestController
@RequestMapping("/crm/question")
public class QuestionController {
	
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private DataSourceFactory dataSourceFactory;

	/**
	 * 问题列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("crm:question:list")
	public R list(Integer page, Integer limit){
		String sql1 = "SELECT t.`id`,t.`type`,t.`module`,t.`desc` FROM `question_type` t ORDER BY t.`id`";
		String sql2 = "SELECT t.`id`,t.`question_id`,t.`desc` FROM `question_item` t ORDER BY t.`question_id`,t.id";
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			List<Map<String, Object>> list = ju.query(sql1);
			ju = new JdbcUtil(dataSourceFactory, "crmMysql");
			List<Map<String, Object>> list1 = ju.query(sql2);
			int i = 1;
			for(Map<String, Object> map : list){
				map.put("no", i++);
				List<Map<String, Object>> items = new ArrayList<>();
				for(Map<String, Object> map1 : list1){
					//logger.info(map1.get("question_id") + " " + map.get("id"));
					if(map1.get("question_id").toString().equals(map.get("id").toString())){
						items.add(map1);
					}
				}
				map.put("items", items);
			}
			PageUtils pageUtil = new PageUtils(list, list.size(), limit, page);
			return R.ok().put("page", pageUtil).put("items", list1);
		} catch (SQLException e) {
			logger.error("查询问题记录失败", e);
		}
		return R.error("查询问题记录失败");
	}
	
	/**
	 * 添加问题选项
	 */
	@SysLog("添加问题选项")
	@ResponseBody
	@RequestMapping("/add")
	@RequiresPermissions("crm:question:add")
	public R add(Integer tid, String items){
		logger.info("添加问题选项,tid={},items={}", tid, items);
		StringBuilder sql = new StringBuilder("INSERT INTO `question_item`(question_id,`desc`) VALUES");
		String[] itemArr = items.split("__");
		for(int i = 0; i < itemArr.length; i++){
			sql.append("(").append(tid).append(",'").append(itemArr[i]).append("'),");
		}
		sql.setLength(sql.length() - 1);
		logger.info("添加问题选项,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("添加问题选项失败", e);
			return R.error("添加问题选项失败");
		}
	}
	
	/**
	 * 删除问题选项
	 */
	@SysLog("删除问题选项")
	@ResponseBody
	@RequestMapping("/del")
	@RequiresPermissions("crm:question:del")
	public R del(String iidArr){
		logger.info("删除问题选项,iidArr={}", iidArr);
		StringBuilder sql = new StringBuilder("delete from `question_item` where id in(");
		String[] iids = iidArr.split("__");
		for(int i = 0; i < iids.length; i++){
			sql.append(iids[i]).append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		logger.info("删除问题选项,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("删除问题选项失败", e);
			return R.error("删除问题选项失败");
		}
	}
	
	/**
	 * 更新问题
	 */
	@SysLog("更新问题")
	@ResponseBody
	@RequestMapping("/updateQ")
	@RequiresPermissions("crm:question:update")
	public R updateQuestion(Integer tid, String desc){
		logger.info("更新问题描述,tid={},desc={}", tid, desc);
		StringBuilder sql = new StringBuilder("update `question_type` ");
		sql.append("set `desc`='").append(desc).append("' where id=").append(tid);
		logger.info("更新问题描述,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("更新问题描述失败", e);
			return R.error("更新问题描述失败");
		}
	}
	
	/**
	 * 更新问题选项
	 */
	@SysLog("更新问题")
	@ResponseBody
	@RequestMapping("/updateItem")
	@RequiresPermissions("crm:question:update")
	public R updateQuestionItem(Integer tid, String desc){
		logger.info("更新问题选项,tid={},desc={}", tid, desc);
		StringBuilder sql = new StringBuilder("update `question_item` ");
		sql.append("set `desc`='").append(desc).append("' where id=").append(tid);
		logger.info("更新问题选项,执行SQL={}", sql.toString());
		JdbcUtil ju = new JdbcUtil(dataSourceFactory, "crmMysql");
		try {
			ju.execute(sql.toString());
			return R.ok();
		} catch (SQLException e) {
			logger.error("更新问题选项失败", e);
			return R.error("更新问题选项失败");
		}
	}
	
	public static void main(String[] args){
		System.out.println("aaa__bbb".split("__").length);
	}
}
