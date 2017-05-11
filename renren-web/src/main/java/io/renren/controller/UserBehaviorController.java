package io.renren.controller;

import io.renren.entity.UserBehaviorEntity;
import io.renren.service.UserBehaviorService;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 市场部每日渠道数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 15:30:24
 */
@RestController
@RequestMapping("/black/userBehavior")
public class UserBehaviorController {
	@Autowired
	private UserBehaviorService userBehaviorService;


	/**
	 * 列表
	 */
	@RequestMapping("/first")
	 @RequiresPermissions("Behavior:list")
	public R list(@RequestBody Map<String, Object> params) {



		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<UserBehaviorEntity> UserBehaviorDataList = userBehaviorService.queryList(query);

		return R.ok().put("page", UserBehaviorDataList);

	}

}
