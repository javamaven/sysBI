package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qiniu.streaming.model.StreamListing;

import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;
import io.renren.service.yunying.dayreport.DmReportDepSalesService;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;
import io.renren.utils.ExcelUtil;
import io.renren.utils.PageUtils;
import io.renren.utils.R;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-29 16:30:26	
 */
@Controller
@RequestMapping("dmreportdepsales")
public class DmReportDepSalesController {
	@Autowired
	private DmReportDepSalesService dmReportDepSalesService;
	
	SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("dmreportdepsales:list")
	public R list(Integer page, Integer limit,String reg_begindate,String reg_enddate){
		
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("reg_begindate",reg_begindate);
		map.put("reg_enddate",reg_enddate);
		//查询列表数据
		if (reg_begindate.equals(reg_enddate)) {
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryList(map);		
		
		
		
		int total = dmReportDepSalesService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
		}
		else {
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListha(map);
			int total = dmReportDepSalesService.queryTotal(map);

			PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
			
			return R.ok().put("page", pageUtil);
		}
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list2")
	@RequiresPermissions("dmreportdepsales:list")
	public R list2(Integer page, Integer limit,String reg_begindate,String reg_enddate){
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("reg_enddate",reg_enddate);
		map.put("reg_begindate",reg_begindate);
		//查询列表数据
		
		if (reg_begindate.equals(reg_enddate)) {
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryLists(map);
		
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
		}else {
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListxi(map);
			int total = dmReportDepSalesService.queryTotal(map);
			
			PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
			
			return R.ok().put("page", pageUtil);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/list3")
	@RequiresPermissions("dmreportdepsales:list")
	public R list3(Integer page, Integer limit,String reg_begindate,String reg_enddate){
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("reg_begindate",reg_begindate);
		map.put("reg_enddate",reg_enddate);
		
		if (reg_begindate.equals(reg_enddate)) {
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListss(map);
		
		
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
		}else {
			//查询列表数据
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListhe(map);
			int total = dmReportDepSalesService.queryTotal(map);
			
			PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
			
			return R.ok().put("page", pageUtil);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String reg_begindate = map.get("reg_begindate") + "";
		String reg_enddate = map.get("reg_enddate") + "";
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		if (reg_begindate.equals(reg_enddate)) {

		/**底层资产供应情况*/
		String title = "各类产品的实际销售情况";
		// 查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryList(map);
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields();
		/*************************************************************************/
//		/**底层资产供应情况*/
//		String title2 = "底层资产供应情况";
//		Map<String, String> headMap2 = dmReportDepSalesService.getExcelFields1();
//		// 查询列表数据
//		List<DmReportDepSalesEntity> dataList2 = dmReportDepSalesService.queryLists(map);
//		JSONArray va2 = new JSONArray();
//		for (int i = 0; i < dataList2.size(); i++) {
//			DmReportDepSalesEntity entity = dataList2.get(i);
//			va2.add(entity);
//		}
//		/*************************************************************************/
//		/**理财计划留存情况（单位：万）*/
//		String title3 = "理财计划留存情况（单位：万）";
//		List<DmReportDepSalesEntity> dataList3 = dmReportDepSalesService.queryListss(map);
//		JSONArray va3 = new JSONArray();
//		for (int i = 0; i < dataList3.size(); i++) {
//			DmReportDepSalesEntity entity = dataList3.get(i);
//			va3.add(entity);
//		}
//		Map<String, String> headMap3 = dmReportDepSalesService.getExcelFields2();
		/*************************************************************************/
//		List<String> titleList = new ArrayList<>();
//		titleList.add(title);
//		titleList.add(title2);
//		titleList.add(title3);
//		List<Map<String, String>> headMapList = new ArrayList<Map<String,String>>();
//		headMapList.add(headMap);
//		headMapList.add(headMap2);
//		headMapList.add(headMap3);
//		List<JSONArray> ja = new ArrayList<JSONArray>();
//		ja.add(va);
//		ja.add(va2);
//		ja.add(va3);
//		ExcelUtil.downloadExcelFile(titleList , headMapList, ja , response);
		ExcelUtil.downloadExcelFile(title, headMap, va, response);
		}else {
			/**底层资产供应情况*/
			String title = "各类产品的实际销售情况";
			// 查询列表数据
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListha(map);
			
			JSONArray va = new JSONArray();
			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
				va.add(entity);
			}
			Map<String, String> headMap = dmReportDepSalesService.getExcelFieldsha();
			ExcelUtil.downloadExcelFile(title, headMap, va, response);
		}
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel1")
	public void partExport1(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String reg_begindate = map.get("reg_begindate") + "";
		String reg_enddate = map.get("reg_enddate") + "";
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		if (reg_begindate.equals(reg_enddate)) {
		// 查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryLists(map);
		
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields1();

		String title = "底层资产供应情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
		}else {
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListxi(map);
			
			
			JSONArray va = new JSONArray();
			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
				va.add(entity);
			}
			Map<String, String> headMap = dmReportDepSalesService.getExcelFields1();

			String title = "底层资产供应情况";

			ExcelUtil.downloadExcelFile(title, headMap, va, response);
		}
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void partExport2(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String reg_begindate = map.get("reg_begindate") + "";
		String reg_enddate = map.get("reg_enddate") + "";
		if (StringUtils.isNotEmpty(reg_begindate)) {
			reg_begindate = reg_begindate.replace("-", "");
		}
		
		if (StringUtils.isNotEmpty(reg_enddate)) {
			reg_enddate = reg_enddate.replace("-", "");
		}
		if (reg_begindate.equals(reg_enddate)) {
		// 查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListss(map);
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields2();

		String title = "理财计划留存情况（单位：万）";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}else {
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListhe(map);
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFieldshe();

		String title = "理财计划留存情况（单位：万）";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	}
}
