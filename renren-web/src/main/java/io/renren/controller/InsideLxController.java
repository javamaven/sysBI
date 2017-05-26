package io.renren.controller;

import com.alibaba.fastjson.JSONArray;
import io.renren.entity.DailyEntity;
import io.renren.entity.InsideLxEntity;
import io.renren.service.InsideLxService;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.renren.utils.ShiroUtils.getUserId;


/**
 * 员工拉新统计表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-05-26 09:27:51
 */
@Controller
@RequestMapping("dmreportinsidelx")
public class InsideLxController {
	@Autowired
	private InsideLxService insideLxService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportinsidelx:list")
	public R list(@RequestBody Map<String, Object> params) {






		//查询列表数据
		Query query = new Query(params);
		//查询列表数据
		List<InsideLxEntity> dmReportDailyDataList = insideLxService.queryList(query);

		return R.ok().put("page", dmReportDailyDataList);
	}

	@ResponseBody
	@RequestMapping("/partExport")
	public void partExport(HttpServletResponse response, HttpServletRequest request) throws IOException {


		List<InsideLxEntity> InsideList = insideLxService.queryExport();
		JSONArray va = new JSONArray();

		for(int i = 0 ; i < InsideList.size() ; i++) {
			InsideLxEntity InsideLxUser = new InsideLxEntity();
			InsideLxUser.setStatPeriod(InsideList.get(i).getStatPeriod());
			InsideLxUser.setLxName(InsideList.get(i).getLxName());
			InsideLxUser.setLaxDep(InsideList.get(i).getLaxDep());

			InsideLxUser.setLxUserCou(InsideList.get(i).getLxUserCou());
			InsideLxUser.setLxUserTg(InsideList.get(i).getLxUserTg());

			InsideLxUser.setReach(InsideList.get(i).getReach());
			InsideLxUser.setLxDs(InsideList.get(i).getLxDs());
			InsideLxUser.setLxUserPw(InsideList.get(i).getLxUserPw());
			InsideLxUser.setJf(InsideList.get(i).getJf());
			InsideLxUser.setJfpw(InsideList.get(i).getJfpw());
			va.add(InsideLxUser);
		}
		Map<String,String> headMap = new LinkedHashMap<String,String>();
		headMap.put("statPeriod","日期");
		headMap.put("lxName","员工姓名");
		headMap.put("laxDep","部门");
		headMap.put("lxUserCou","当季度有效拉新人数");
		headMap.put("lxUserTg","当季度拉新目标人数");
		headMap.put("reach","当季度有效拉新人数指标达成率");
		headMap.put("lxDs","满足日均待收金额的人数");
		headMap.put("lxUserPw","当季度累计有效拉新人数排名");
		headMap.put("jf","季度度新增积分值");
		headMap.put("jfpw","季度度新增积分值排名");

		String title = "员工拉新绩效统计";

		ExcelUtil.downloadExcelFile(title,headMap,va,response);
	}



}
