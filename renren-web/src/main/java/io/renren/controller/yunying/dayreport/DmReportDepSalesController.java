package io.renren.controller.yunying.dayreport;

import java.io.IOException;
import java.text.DecimalFormat;
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
	public R list(Integer page, Integer limit,String statPeriod){
		
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryList(map);
		
//		try {
//			String week = dateFm.format(sdf.parse(statPeriod));
//			if(week.equals("星期一")){//-3
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 3, "yyyyMMdd");
//			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 1, "yyyyMMdd");
//			}else{//周六周日-7
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 7, "yyyyMMdd");
//			}
//			map.put("statPeriod",statPeriod);
//			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesService.queryList(map);
//			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
//				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
//				for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
//					DmReportDepSalesEntity vo2 = dmReportDepSalesList2.get(j);
//					if(entity.getSalesType().equals(vo2.getSalesType())){
//						String today = "";
//						if(StringUtils.isEmpty(entity.getZongji())){
//							today = "0";
//						}else {
//							today = entity.getZongji().replace("%", "");
//						}
//						String yes = "";
//						if(StringUtils.isEmpty(vo2.getZongji())){
//							yes = "0";
//							entity.setWeekTongRate("0.00%");
//						}else {
//							yes = vo2.getZongji().replace("%", "");
//							DecimalFormat df = new DecimalFormat("0.00");
//							double rate = Double.parseDouble(today)/Double.parseDouble(yes);
//							if(!"期限占比总销量".equals(vo2.getSalesType())){
//								entity.setWeekTongRate(df.format(rate*100) + "%");
//							}
//						}
//
//						
//					}
//				}
//			}
//			
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			
//		}
		
		
		int total = dmReportDepSalesService.queryTotal(map);

		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list2")
	@RequiresPermissions("dmreportdepsales:list")
	public R list2(Integer page, Integer limit,String statPeriod){
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryLists(map);
		
//		try {
//			String week = dateFm.format(sdf.parse(statPeriod));
//			if(week.equals("星期一")){//-3
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 3, "yyyyMMdd");
//			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 1, "yyyyMMdd");
//			}else{//周六周日-7
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 7, "yyyyMMdd");
//			}
//			map.put("statPeriod",statPeriod);
//			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesService.queryLists(map);
//			DmReportDepSalesEntity yesEntity = null;
//			for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
//				DmReportDepSalesEntity en = dmReportDepSalesList2.get(j);
//				if(en.getZichan().equals("资产")){
//					yesEntity = en;
//				}
//			}
////			DmReportDepSalesEntity entity = dmReportDepSalesList2.get(i);
////			entity.setWeekTongRate(dmReportDepSalesList2.get(0)+"");
//			DmReportDepSalesEntity newEntity = new DmReportDepSalesEntity();
//			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
//				DmReportDepSalesEntity todayEntity = dmReportDepSalesList.get(i);
//				if(todayEntity.getZichan().equals("资产")){
//					newEntity.setStatPeriod(todayEntity.getStatPeriod());
//					newEntity.setZichan("周同比");
//					if(StringUtils.isEmpty(todayEntity.getShiwutian()) || StringUtils.isEmpty(yesEntity.getShiwutian())){
//						newEntity.setShiwutian("");
//					}else{
//						newEntity.setShiwutian(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiwutian())/Double.parseDouble(yesEntity.getShiwutian()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getYiyue()) || StringUtils.isEmpty(yesEntity.getYiyue())){
//						newEntity.setYiyue("");
//					}else{
//						newEntity.setYiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getYiyue())/Double.parseDouble(yesEntity.getYiyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getEryue()) || StringUtils.isEmpty(yesEntity.getEryue())){
//						newEntity.setEryue("");
//					}else{
//						newEntity.setEryue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getEryue())/Double.parseDouble(yesEntity.getEryue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getSanyue()) || StringUtils.isEmpty(yesEntity.getSanyue())){
//						newEntity.setSanyue("");
//					}else{
//						newEntity.setSanyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanyue())/Double.parseDouble(yesEntity.getSanyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getLiuyue()) || StringUtils.isEmpty(yesEntity.getLiuyue())){
//						newEntity.setLiuyue("");
//					}else{
//						newEntity.setLiuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getLiuyue())/Double.parseDouble(yesEntity.getLiuyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getBayue()) || StringUtils.isEmpty(yesEntity.getBayue())){
//						newEntity.setBayue("");
//					}else{
//						newEntity.setBayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getBayue())/Double.parseDouble(yesEntity.getBayue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getJiuyue()) || StringUtils.isEmpty(yesEntity.getJiuyue())){
//						newEntity.setJiuyue("");
//					}else{
//						newEntity.setJiuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getJiuyue())/Double.parseDouble(yesEntity.getJiuyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getShiyue()) || StringUtils.isEmpty(yesEntity.getShiyue())){
//						newEntity.setShiyue("");
//					}else{
//						newEntity.setShiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiyue())/Double.parseDouble(yesEntity.getShiyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getShieryue()) || StringUtils.isEmpty(yesEntity.getShieryue())){
//						newEntity.setShieryue("");
//					}else{
//						newEntity.setShieryue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShieryue())/Double.parseDouble(yesEntity.getShieryue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getShiwuyue()) || StringUtils.isEmpty(yesEntity.getShiwuyue())){
//						newEntity.setShiwuyue("");
//					}else{
//						newEntity.setShiwuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiwuyue())/Double.parseDouble(yesEntity.getShiwuyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getShibayue()) || StringUtils.isEmpty(yesEntity.getShibayue())){
//						newEntity.setShibayue("");
//					}else{
//						newEntity.setShibayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShibayue())/Double.parseDouble(yesEntity.getShibayue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getErshisiyue()) || StringUtils.isEmpty(yesEntity.getErshisiyue())){
//						newEntity.setErshisiyue("");
//					}else{
//						newEntity.setErshisiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getErshisiyue())/Double.parseDouble(yesEntity.getErshisiyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getSanshiwuyue()) || StringUtils.isEmpty(yesEntity.getSanshiwuyue())){
//						newEntity.setSanshiwuyue("");
//					}else{
//						newEntity.setSanshiwuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanshiwuyue())/Double.parseDouble(yesEntity.getSanshiwuyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getSanshiliuyue()) || StringUtils.isEmpty(yesEntity.getSanshiliuyue())){
//						newEntity.setSanshiliuyue("");
//					}else{
//						newEntity.setSanshiliuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanshiliuyue())/Double.parseDouble(yesEntity.getSanshiliuyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getSishibayue()) || StringUtils.isEmpty(yesEntity.getSishibayue())){
//						newEntity.setSishibayue("");
//					}else{
//						newEntity.setSishibayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSishibayue())/Double.parseDouble(yesEntity.getSishibayue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getLiushiyue()) || StringUtils.isEmpty(yesEntity.getLiushiyue())){
//						newEntity.setLiushiyue("");
//					}else{
//						newEntity.setLiushiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getLiushiyue())/Double.parseDouble(yesEntity.getLiushiyue()), 2));
//					}
//					
//					if(StringUtils.isEmpty(todayEntity.getJiushiliuyue()) || StringUtils.isEmpty(yesEntity.getJiushiliuyue())){
//						newEntity.setJiushiliuyue("");
//					}else{
//						newEntity.setJiushiliuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getJiushiliuyue())/Double.parseDouble(yesEntity.getJiushiliuyue()), 2));
//					}
//					
//					
//				}
//			}
//			dmReportDepSalesList.add(newEntity);
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			
//		}
		
		
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	@ResponseBody
	@RequestMapping("/list3")
	@RequiresPermissions("dmreportdepsales:list")
	public R list3(Integer page, Integer limit,String statPeriod){
		if (StringUtils.isNotEmpty(statPeriod)) {
			statPeriod = statPeriod.replace("-", "");
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("statPeriod",statPeriod);
		//查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListss(map);
		//周同比，判断星期几
//		try {
//			String week = dateFm.format(sdf.parse(statPeriod));
//			if(week.equals("星期一")){//-3
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 3, "yyyyMMdd");
//			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 1, "yyyyMMdd");
//			}else{//周六周日-7
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 7, "yyyyMMdd");
//			}
//			map.put("statPeriod",statPeriod);
//			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesService.queryListss(map);
//			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
//				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
//				for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
//					DmReportDepSalesEntity vo2 = dmReportDepSalesList2.get(j);
//					if(entity.getSalesType().equals(vo2.getSalesType())){
//						String today = entity.getZhanbi().replace("%", "");
//						String yes = vo2.getZhanbi().replace("%", "");
//						 DecimalFormat df = new DecimalFormat("0.00");
//						double rate = Double.parseDouble(today)/Double.parseDouble(yes);
//						
//						entity.setWeekTongRate(df.format(rate*100) + "%");
//								
//						
//					}
//				}
//			}
//			
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			
//		}
		
		
		
		int total = dmReportDepSalesService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(dmReportDepSalesList, total, limit, page);
		
		return R.ok().put("page", pageUtil);
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel")
	public void partExport(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
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
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel1")
	public void partExport1(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryLists(map);
		try {
			String week = dateFm.format(sdf.parse(statPeriod));
			if(week.equals("星期一")){//-3
				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 3, "yyyyMMdd");
			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 1, "yyyyMMdd");
			}else{//周六周日-7
				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 7, "yyyyMMdd");
			}
			map.put("statPeriod",statPeriod);
			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesService.queryLists(map);
			DmReportDepSalesEntity yesEntity = null;
			for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
				DmReportDepSalesEntity en = dmReportDepSalesList2.get(j);
				if(en.getZichan().equals("资产")){
					yesEntity = en;
				}
			}

			DmReportDepSalesEntity newEntity = new DmReportDepSalesEntity();
			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
				DmReportDepSalesEntity todayEntity = dmReportDepSalesList.get(i);
				if(todayEntity.getZichan().equals("资产")){
					newEntity.setStatPeriod(todayEntity.getStatPeriod());
					newEntity.setZichan("周同比");
					if(StringUtils.isEmpty(todayEntity.getShiwutian()) || StringUtils.isEmpty(yesEntity.getShiwutian())){
						newEntity.setShiwutian("");
					}else{
						newEntity.setShiwutian(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiwutian())/Double.parseDouble(yesEntity.getShiwutian()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getYiyue()) || StringUtils.isEmpty(yesEntity.getYiyue())){
						newEntity.setYiyue("");
					}else{
						newEntity.setYiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getYiyue())/Double.parseDouble(yesEntity.getYiyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getEryue()) || StringUtils.isEmpty(yesEntity.getEryue())){
						newEntity.setEryue("");
					}else{
						newEntity.setEryue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getEryue())/Double.parseDouble(yesEntity.getEryue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanyue()) || StringUtils.isEmpty(yesEntity.getSanyue())){
						newEntity.setSanyue("");
					}else{
						newEntity.setSanyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanyue())/Double.parseDouble(yesEntity.getSanyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getLiuyue()) || StringUtils.isEmpty(yesEntity.getLiuyue())){
						newEntity.setLiuyue("");
					}else{
						newEntity.setLiuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getLiuyue())/Double.parseDouble(yesEntity.getLiuyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getBayue()) || StringUtils.isEmpty(yesEntity.getBayue())){
						newEntity.setBayue("");
					}else{
						newEntity.setBayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getBayue())/Double.parseDouble(yesEntity.getBayue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getJiuyue()) || StringUtils.isEmpty(yesEntity.getJiuyue())){
						newEntity.setJiuyue("");
					}else{
						newEntity.setJiuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getJiuyue())/Double.parseDouble(yesEntity.getJiuyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getShiyue()) || StringUtils.isEmpty(yesEntity.getShiyue())){
						newEntity.setShiyue("");
					}else{
						newEntity.setShiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiyue())/Double.parseDouble(yesEntity.getShiyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getShieryue()) || StringUtils.isEmpty(yesEntity.getShieryue())){
						newEntity.setShieryue("");
					}else{
						newEntity.setShieryue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShieryue())/Double.parseDouble(yesEntity.getShieryue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getShiwuyue()) || StringUtils.isEmpty(yesEntity.getShiwuyue())){
						newEntity.setShiwuyue("");
					}else{
						newEntity.setShiwuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShiwuyue())/Double.parseDouble(yesEntity.getShiwuyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getShibayue()) || StringUtils.isEmpty(yesEntity.getShibayue())){
						newEntity.setShibayue("");
					}else{
						newEntity.setShibayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getShibayue())/Double.parseDouble(yesEntity.getShibayue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getErshisiyue()) || StringUtils.isEmpty(yesEntity.getErshisiyue())){
						newEntity.setErshisiyue("");
					}else{
						newEntity.setErshisiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getErshisiyue())/Double.parseDouble(yesEntity.getErshisiyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanshiwuyue()) || StringUtils.isEmpty(yesEntity.getSanshiwuyue())){
						newEntity.setSanshiwuyue("");
					}else{
						newEntity.setSanshiwuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanshiwuyue())/Double.parseDouble(yesEntity.getSanshiwuyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanshiliuyue()) || StringUtils.isEmpty(yesEntity.getSanshiliuyue())){
						newEntity.setSanshiliuyue("");
					}else{
						newEntity.setSanshiliuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSanshiliuyue())/Double.parseDouble(yesEntity.getSanshiliuyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getSishibayue()) || StringUtils.isEmpty(yesEntity.getSishibayue())){
						newEntity.setSishibayue("");
					}else{
						newEntity.setSishibayue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getSishibayue())/Double.parseDouble(yesEntity.getSishibayue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getLiushiyue()) || StringUtils.isEmpty(yesEntity.getLiushiyue())){
						newEntity.setLiushiyue("");
					}else{
						newEntity.setLiushiyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getLiushiyue())/Double.parseDouble(yesEntity.getLiushiyue()), 2));
					}
					
					if(StringUtils.isEmpty(todayEntity.getJiushiliuyue()) || StringUtils.isEmpty(yesEntity.getJiushiliuyue())){
						newEntity.setJiushiliuyue("");
					}else{
						newEntity.setJiushiliuyue(""+NumberUtil.keepPrecision(Double.parseDouble(todayEntity.getJiushiliuyue())/Double.parseDouble(yesEntity.getJiushiliuyue()), 2));
					}
					
					
				}
			}
			dmReportDepSalesList.add(newEntity);
			System.out.println(dmReportDepSalesList);
		} catch (ParseException e) {
			e.printStackTrace();
			
		}
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
//		va.add(dmReportDepSalesList);
//		for (int i = 0; i < dataList.size(); i++) {
//			DmReportDepSalesEntity entity = dataList.get(i);
//			va.add(entity);
//		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields1();

		String title = "底层资产供应情况";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/exportExcel2")
	public void partExport2(String params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = JSON.parseObject(params, Map.class);
		String statPeriod = map.get("statPeriod") + "";
		if (StringUtils.isNotEmpty(statPeriod)) {
			map.put("statPeriod", statPeriod.replace("-", ""));
		}
		// 查询列表数据
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesService.queryListss(map);
//		try {
//			String week = dateFm.format(sdf.parse(statPeriod));
//			if(week.equals("星期一")){//-3
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 3, "yyyyMMdd");
//			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 1, "yyyyMMdd");
//			}else{//周六周日-7
//				statPeriod = DateUtil.getCurrDayBefore(statPeriod, 7, "yyyyMMdd");
//			}
//			map.put("statPeriod",statPeriod);
//			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesService.queryListss(map);
//			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
//				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
//				for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
//					DmReportDepSalesEntity vo2 = dmReportDepSalesList2.get(j);
//					if(entity.getSalesType().equals(vo2.getSalesType())){
//						String today = entity.getZhanbi().replace("%", "");
//						String yes = vo2.getZhanbi().replace("%", "");
//						 DecimalFormat df = new DecimalFormat("0.00");
//						double rate = Double.parseDouble(today)/Double.parseDouble(yes);
//						
//						entity.setWeekTongRate(df.format(rate*100) + "%");
//								
//						
//					}
//				}
//			}
//			
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//			
//		}
		
		JSONArray va = new JSONArray();
		for (int i = 0; i < dmReportDepSalesList.size(); i++) {
			DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
			va.add(entity);
		}
		Map<String, String> headMap = dmReportDepSalesService.getExcelFields2();

		String title = "理财计划留存情况（单位：万）";

		ExcelUtil.downloadExcelFile(title, headMap, va, response);
	}
}
