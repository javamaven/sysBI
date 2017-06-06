package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.entity.LabelTagManagerEntity;
import io.renren.service.LabelTagManagerService;
import io.renren.service.UserBehaviorService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 标签表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-11 19:46:06
 */
@RestController
@RequestMapping("labelTagManager")
public class LabelTagManagerController {
	@Autowired
	private LabelTagManagerService labelTagManagerService;
	@Autowired
	private UserBehaviorService userBehaviorService;
	private  String reportType="标签表";
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("labelTagManager:save")
	public R save(@RequestBody Map<String, Map<String,Object>> map) {
		long struUserId = getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);
		LabelTagManagerEntity labelTagManager = new LabelTagManagerEntity();
		labelTagManager.setSysUser(strUsername);
		labelTagManager.setCreateDate(new Date());

		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			//标签主表
			if (entry.getKey() == "tag") {
				for (Map.Entry<String, Object> mapEntry : entry.getValue().entrySet()) {
					switch (mapEntry.getKey()) {
						case "Type":
							labelTagManager.setType(mapEntry.getValue().toString());
							break;
						case "MainLabel_Name":
							labelTagManager.setMainlabelName(mapEntry.getValue().toString());
							break;
					}
				}
				labelTagManagerService.save(labelTagManager);
			};
		}
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				//标签主表
				if (entry.getKey() != "tag") {
					for (Map.Entry<String, Object> mapEntry : entry.getValue().entrySet()) {
						switch (mapEntry.getKey()) {
							case "labelID":
								labelTagManager.setLabelID(Integer.parseInt(mapEntry.getValue().toString()));
								break;
							case "labelType":
								labelTagManager.setLabelType(mapEntry.getValue().toString());
								break;
							case "labelContene":
								labelTagManager.setLabelContene(mapEntry.getValue().toString());
								break;
							case "labelLogic":
								labelTagManager.setLabelLogic(mapEntry.getValue().toString());
								break;
							case "labelCondition":
								labelTagManager.setLabelCondition(mapEntry.getValue().toString());
								break;
						}
					}
					labelTagManagerService.saveDetail(labelTagManager);
				}
			};

			return R.ok();
		}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("labelTagManager:delete")
	public R delete(@RequestBody Map<String, Object> params){

		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"删除",reportType," ");
		long struUserId = getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);

		params.put("sysUser",strUsername);

		labelTagManagerService.deleteAll(params);
		return R.ok();
	}

	/**
	 * 查询是否存在
	 */
	@RequestMapping("/exists")
	@RequiresPermissions("labelTagManager:save")
	public R exists(@RequestBody Map<String, Object> params){
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");

		long struUserId = getUserId();
		// 获取用户账号
		String strUsername = labelTagManagerService.querySysUser(struUserId);

		params.put("sysUser",strUsername);

		int queryExists = labelTagManagerService.exists(params);
		return R.ok().put("queryExists",queryExists);
	}


	
}
