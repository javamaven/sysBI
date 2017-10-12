package io.renren.controller.yunying.dayreport;

import static io.renren.utils.ShiroUtils.getUserId;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import io.renren.service.UserBehaviorService;
import io.renren.system.jdbc.DataSourceFactory;
import io.renren.system.jdbc.JdbcUtil;
import io.renren.util.MapUtil;
import io.renren.util.UserBehaviorUtil;
import io.renren.utils.EasyUiPage;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;

@Controller
@RequestMapping(value = "/yunying/source")
public class UserRegistrationSourceController {


	@Autowired
	private DataSourceFactory dataSourceFactory;
	@Autowired
	private UserBehaviorService userBehaviorService;

	private  String reportType="用户注册来源";

	
	  /**
	   * 用户注册来源
	   */
	  @ResponseBody
	  @RequestMapping("/list")
	  public EasyUiPage daylist(Integer page, Integer limit ,String period ) {
	    UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
	    userBehaviorUtil.insert(getUserId(),new Date(),"查看",reportType," ");
//	    period = "20170925";
	    page = 1;
	    limit = 100;
	    String month="";
	    
	    if (StringUtils.isNotEmpty(period)) {
	      period = period.replace("-", "");
	      month = period.substring(0,6);
	    }
	    
	    long l1 = System.currentTimeMillis();

	    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

	    try {
	      String path = this.getClass().getResource("/").getPath();
	      String detail_sql;
	      detail_sql = FileUtil.readAsString(new File(path + File.separator + "sql/用户注册来源.txt"));
	      detail_sql = detail_sql.replace("${month}", month);
	      detail_sql = detail_sql.replace("${period}", period);
	      List<Map<String, Object>> list = new JdbcUtil(dataSourceFactory, "oracle26").query(detail_sql);
	      resultList.addAll(list);
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    int total = resultList.size();
	    PageUtils pageUtil = new PageUtils(resultList, total, limit, page);
	    long l2 = System.currentTimeMillis();

	    System.err.println("+++++++用户注册来源数据查询耗时：" + (l2 - l1));
	    //最終返回數據格式
	    List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
	    int channelId = 0;
	    for (int i = 0; i < resultList.size(); i++) {
	      Map<String, Object> map = resultList.get(i);
	      map.put("id", i+1);
	      String huizongValue = MapUtil.getValue(map, "HUIZONG");
	      if("渠道".equals(huizongValue)){
	        channelId = i+1;
	        retList.add(map);
	      }else if("汇总".equals(huizongValue) || "邀请".equals(huizongValue) || "自然流量".equals(huizongValue)){
	        retList.add(map);
	      }else{
	        map.put("_parentId", channelId);
	        retList.add(map);
	      }
	    }
	    
//	    return R.ok().put("page", pageUtil);
	    EasyUiPage pageObj = new EasyUiPage();
	    pageObj.setRows(retList);
	    pageObj.setTotal(retList.size());
	    return pageObj;
	  }
	  
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void exportMonthListExcel(String params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBehaviorUtil userBehaviorUtil = new UserBehaviorUtil(userBehaviorService);
		userBehaviorUtil.insert(getUserId(),new Date(),"导出",reportType," ");
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String period = map.get("period") + "";
		EasyUiPage r=daylist(1, 1000000, period);
		List<Map<String,Object>> resultList = (List<Map<String, Object>>) r.getRows();
		// 查询列表数据
		JSONArray va = new JSONArray();
		for (int i = 0; i < resultList.size(); i++) {
			va.add(resultList.get(i));
		}
		Map<String, String> headMap = null;
		String title = "用户注册来源";
		headMap = getDayListExcelFields();

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}

	
	
	
	private Map<String, String> getDayListExcelFields() {

		Map<String, String> headMap = new LinkedHashMap<String, String>();

		headMap.put("HUIZONG", "注册来源");
		headMap.put("ALL_REG", "注册人数");
		headMap.put("M_REG", "当月注册");
		headMap.put("D_REG", "当日注册");
		headMap.put("ALL_FIRST", "首投人数");
		headMap.put("M_FIRST", "当月首投人数");
		headMap.put("D_FIRST", "当日首投人数");
		
		headMap.put("M_FIRST_INV", "当月首投金额");
		headMap.put("D_FIRST_INV", "当日首投金额");
		headMap.put("WEIZHI4", "当月充值");
//		headMap.put("M_WI", "当月提现");
		headMap.put("WEIZHI3", "当月净充值");
		headMap.put("WEIZHI2", "当日充值");
//		headMap.put("D_WI", "当日提现");
		
		headMap.put("WEIZHI1", "当日净充值");
		headMap.put("M_INV", "当月投资");
		headMap.put("D_INV", "当日投资");
		headMap.put("AWIAT", "待收本金");
		

		return headMap;

	}





}
