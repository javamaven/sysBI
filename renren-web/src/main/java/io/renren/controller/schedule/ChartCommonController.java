package io.renren.controller.schedule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import io.renren.entity.ChannelChannelAllEntity;
import io.renren.service.ChannelChannelAllService;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 生成图表 controller，此类主要为phantomjs工具提供图表截图页面
 * 
 * @data 2016/4/9 11:28
 */
@RestController
@RequestMapping(value = "/common/create-chart-image")
public class ChartCommonController {

	@Autowired
	private ChannelChannelAllService channelChannelAllService;

	/**
	 * 通过报表类型，生成不同图片
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chart-report-id")
	public ModelAndView chartByReportId(String type, String params, HttpServletRequest request) throws Exception {
		ModelAndView html = null;
		switch (type) {
		case "channel_all":// 渠道分次投资情况
			html = printChannelAllPicture(request, params);
			break;
		default:
			break;
		}
		return html;
	}

	/**
	 * 渠道分次投资情况-截图
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private ModelAndView printChannelAllPicture(HttpServletRequest request, String params) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/schedule/chart.html");
		mv.addObject("param", params);
		return mv;
	}

	/**
	 * 获取图表数据
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@ResponseBody
	@RequestMapping("/queryChannelAllChart")
	public void mainChart(@RequestBody Map<String, Object> params, HttpServletResponse response) throws IOException {
		// {reg_begindate=20170201, invest_enddate=20170522, limit=20,
		// reg_enddate=20170522, channelName=null, page=1, investTimes=24}
		// Subject subject = ShiroUtils.getSubject();
		// //sha256加密
		// String password = new Sha256Hash("admin").toHex();
		// UsernamePasswordToken token = new UsernamePasswordToken("admin",
		// password);
		// subject.login(token);
		String jsonString = params.get("paramsJson") + "";
		Map<String,Object> paramsJson = JSON.parseObject(jsonString, Map.class);
		params.putAll(paramsJson);
		// 查询列表数据
		Query query = new Query(params);
		// 查询数据

		List<ChannelChannelAllEntity> queryList = channelChannelAllService.queryList(query);
		// 查询数据
		List<ChannelChannelAllEntity> chartMain = channelChannelAllService.queryMainChart(query);
		writeObjectToClient(R.ok().put("mainChart", chartMain).put("channelNameList", paramsJson.get("channelName")), response);
	}

	public static void writeObjectToClient(Object obj, HttpServletResponse response) throws IOException {
		String jsonStr = JSON.toJSONString(obj);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(jsonStr);
	}
}
