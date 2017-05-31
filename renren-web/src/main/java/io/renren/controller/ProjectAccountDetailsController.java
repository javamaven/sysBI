package io.renren.controller;

import io.renren.entity.ProjectAccountDetailsEntity;
import io.renren.service.ProjectAccountDetailsService;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


/**
 * 项目台帐明细
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-31 10:07:16
 */
@Controller
@RequestMapping("dmreportfinrepaymentdetail")
public class ProjectAccountDetailsController {
	@Autowired
	private ProjectAccountDetailsService projectAccountDetailsService;


	@RequestMapping("/black")
	@RequiresPermissions("curly:list")
	public R list(@RequestBody Map<String, Object> params) {






		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<ProjectAccountDetailsEntity> projectAccountDetailsList = projectAccountDetailsService.queryList(query);

		return R.ok().put("page", projectAccountDetailsList);
	}

}
