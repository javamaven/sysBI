package io.renren.service.yunying.dayreport.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportDepSalesDao;
import io.renren.entity.yunying.dayreport.DmReportDepSalesEntity;
import io.renren.service.yunying.dayreport.DmReportDepSalesService;
import io.renren.util.DateUtil;
import io.renren.util.NumberUtil;



@Service("dmReportDepSalesService")
public class DmReportDepSalesServiceImpl implements DmReportDepSalesService {
	@Autowired
	private DmReportDepSalesDao dmReportDepSalesDao;
	
	SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	@Override
	public DmReportDepSalesEntity queryObject(String statPeriod){
		return dmReportDepSalesDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportDepSalesEntity> queryList(Map<String, Object> map){
		
	
		
		
			List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesDao.queryList(map);
		

		try {
			String reg_begindate= map.get("reg_begindate") + "";
			String reg_enddate= map.get("reg_enddate") + "";
			String week = DateUtil.getWeekOfDate(sdf.parse(reg_enddate));
			
				
			if(week.equals("星期一")){//-3
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 3, "yyyyMMdd");
			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 1, "yyyyMMdd");
			}else{//周六周日-7
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 7, "yyyyMMdd");
			}
			
			map.put("reg_enddate",reg_enddate);
			map.put("reg_begindate",reg_enddate);
			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesDao.queryList(map);
			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
				for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
					DmReportDepSalesEntity vo2 = dmReportDepSalesList2.get(j);
					if(entity.getSalesType().equals(vo2.getSalesType())){
						String today = "";
						if(StringUtils.isEmpty(entity.getZongji())){
							today = "0";
						}else {
							today = entity.getZongji().replace("%", "");
						}
						String yes = "";
						if(StringUtils.isEmpty(vo2.getZongji())){
							yes = "0";
							entity.setWeekTongRate("0.00%");
						}else {
							yes = vo2.getZongji().replace("%", "");
							DecimalFormat df = new DecimalFormat("0.00");
							double rate = (Double.parseDouble(today)-Double.parseDouble(yes))/Double.parseDouble(yes);
							if(!"期限占比总销量".equals(vo2.getSalesType())){
								entity.setWeekTongRate(df.format(rate*100) + "%");
							}
						}

						
					}
				}
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
			
		}
				
   	return dmReportDepSalesList;
	}
//	@Override
//	public List<DmReportDepSalesEntity> queryList(Map<String, Object> map){
//		return dmReportDepSalesDao.queryList(map);
//	}
//	@Override
//	public List<DmReportDepSalesEntity> queryLists(Map<String, Object> map){
//		return dmReportDepSalesDao.queryLists(map);
//	}
	
	@Override
	public List<DmReportDepSalesEntity> queryLists(Map<String, Object> map){
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesDao.queryLists(map);
		try {
			String reg_begindate= map.get("reg_begindate") + "";
			String reg_enddate= map.get("reg_enddate") + "";
			String week = DateUtil.getWeekOfDate(sdf.parse(reg_enddate));
			System.err.println("+++++++++++++今天++++++++" + week);
			if(week.equals("星期一")){//-3
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 3, "yyyyMMdd");
			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 1, "yyyyMMdd");
			}else{//周六周日-7
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 7, "yyyyMMdd");
			}
			map.put("reg_enddate",reg_enddate);
			map.put("reg_begindate",reg_enddate);
			System.err.println("+++++++++++++查询昨天参数++++++++" + map);
			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesDao.queryLists(map);
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
						newEntity.setShiwutian(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShiwutian())-Double.parseDouble(yesEntity.getShiwutian()))/Double.parseDouble(yesEntity.getShiwutian())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getYiyue()) || StringUtils.isEmpty(yesEntity.getYiyue())){
						newEntity.setYiyue("");
					}else{
						newEntity.setYiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getYiyue())-Double.parseDouble(yesEntity.getYiyue()))/Double.parseDouble(yesEntity.getYiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getEryue()) || StringUtils.isEmpty(yesEntity.getEryue())){
						newEntity.setEryue("");
					}else{
						newEntity.setEryue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getEryue())-Double.parseDouble(yesEntity.getEryue()))/Double.parseDouble(yesEntity.getEryue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanyue()) || StringUtils.isEmpty(yesEntity.getSanyue())){
						newEntity.setSanyue("");
					}else{
						newEntity.setSanyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getSanyue())-Double.parseDouble(yesEntity.getSanyue()))/Double.parseDouble(yesEntity.getSanyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getSiyue()) || StringUtils.isEmpty(yesEntity.getSiyue())){
						newEntity.setSiyue("");
					}else{
						newEntity.setSiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getSiyue())-Double.parseDouble(yesEntity.getSiyue()))/Double.parseDouble(yesEntity.getSiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getWuyue()) || StringUtils.isEmpty(yesEntity.getWuyue())){
						newEntity.setWuyue("");
					}else{
						newEntity.setWuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getWuyue())-Double.parseDouble(yesEntity.getWuyue()))/Double.parseDouble(yesEntity.getWuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getLiuyue()) || StringUtils.isEmpty(yesEntity.getLiuyue())){
						newEntity.setLiuyue("");
					}else{
						newEntity.setLiuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getLiuyue())-Double.parseDouble(yesEntity.getLiuyue()))/Double.parseDouble(yesEntity.getLiuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getBayue()) || StringUtils.isEmpty(yesEntity.getBayue())){
						newEntity.setBayue("");
					}else{
						newEntity.setBayue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getBayue())-Double.parseDouble(yesEntity.getBayue()))/Double.parseDouble(yesEntity.getBayue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getJiuyue()) || StringUtils.isEmpty(yesEntity.getJiuyue())){
						newEntity.setJiuyue("");
					}else{
						newEntity.setJiuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getJiuyue())-Double.parseDouble(yesEntity.getJiuyue()))/Double.parseDouble(yesEntity.getJiuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getShiyue()) || StringUtils.isEmpty(yesEntity.getShiyue())){
						newEntity.setShiyue("");
					}else{
						newEntity.setShiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShiyue())-Double.parseDouble(yesEntity.getShiyue()))/Double.parseDouble(yesEntity.getShiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getShiyiyue()) || StringUtils.isEmpty(yesEntity.getShiyiyue())){
						newEntity.setShiyiyue("");
					}else{
						newEntity.setShiyiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShiyiyue())-Double.parseDouble(yesEntity.getShiyiyue()))/Double.parseDouble(yesEntity.getShiyiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getShieryue()) || StringUtils.isEmpty(yesEntity.getShieryue())){
						newEntity.setShieryue("");
					}else{
						newEntity.setShieryue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShieryue())-Double.parseDouble(yesEntity.getShieryue()))/Double.parseDouble(yesEntity.getShieryue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getShiwuyue()) || StringUtils.isEmpty(yesEntity.getShiwuyue())){
						newEntity.setShiwuyue("");
					}else{
						newEntity.setShiwuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShiwuyue())-Double.parseDouble(yesEntity.getShiwuyue()))/Double.parseDouble(yesEntity.getShiwuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getShibayue()) || StringUtils.isEmpty(yesEntity.getShibayue())){
						newEntity.setShibayue("");
					}else{
						newEntity.setShibayue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getShibayue())-Double.parseDouble(yesEntity.getShibayue()))/Double.parseDouble(yesEntity.getShibayue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getErshisiyue()) || StringUtils.isEmpty(yesEntity.getErshisiyue())){
						newEntity.setErshisiyue("");
					}else{
						newEntity.setErshisiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getErshisiyue())-Double.parseDouble(yesEntity.getErshisiyue()))/Double.parseDouble(yesEntity.getErshisiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanshiwuyue()) || StringUtils.isEmpty(yesEntity.getSanshiwuyue())){
						newEntity.setSanshiwuyue("");
					}else{
						newEntity.setSanshiwuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getSanshiwuyue())-Double.parseDouble(yesEntity.getSanshiwuyue()))/Double.parseDouble(yesEntity.getSanshiwuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getSanshiliuyue()) || StringUtils.isEmpty(yesEntity.getSanshiliuyue())){
						newEntity.setSanshiliuyue("");
					}else{
						newEntity.setSanshiliuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getSanshiliuyue())-Double.parseDouble(yesEntity.getSanshiliuyue()))/Double.parseDouble(yesEntity.getSanshiliuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getSishibayue()) || StringUtils.isEmpty(yesEntity.getSishibayue())){
						newEntity.setSishibayue("");
					}else{
						newEntity.setSishibayue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getSishibayue())-Double.parseDouble(yesEntity.getSishibayue()))/Double.parseDouble(yesEntity.getSishibayue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getLiushiyue()) || StringUtils.isEmpty(yesEntity.getLiushiyue())){
						newEntity.setLiushiyue("");
					}else{
						newEntity.setLiushiyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getLiushiyue())-Double.parseDouble(yesEntity.getLiushiyue()))/Double.parseDouble(yesEntity.getLiushiyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getJiushiliuyue()) || StringUtils.isEmpty(yesEntity.getJiushiliuyue())){
						newEntity.setJiushiliuyue("");
					}else{
						newEntity.setJiushiliuyue(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getJiushiliuyue())-Double.parseDouble(yesEntity.getJiushiliuyue()))/Double.parseDouble(yesEntity.getJiushiliuyue())*100, 2)+"%");
					}
					
					if(StringUtils.isEmpty(todayEntity.getZongji()) || StringUtils.isEmpty(yesEntity.getZongji())){
						newEntity.setZongji("");
					}else{
						newEntity.setZongji(""+NumberUtil.keepPrecision((Double.parseDouble(todayEntity.getZongji())-Double.parseDouble(yesEntity.getZongji()))/Double.parseDouble(yesEntity.getZongji())*100, 2)+"%");
					}
					
					
				}
			}
			dmReportDepSalesList.add(newEntity);

		} catch (ParseException e) {
			e.printStackTrace();
			
		}
		return dmReportDepSalesList;
	}
	public List<DmReportDepSalesEntity> queryListss(Map<String, Object> map){
		List<DmReportDepSalesEntity> dmReportDepSalesList = dmReportDepSalesDao.queryListss(map);
		try {
			String reg_begindate= map.get("reg_begindate") + "";
			String reg_enddate= map.get("reg_enddate") + "";
			String week = DateUtil.getWeekOfDate(sdf.parse(reg_enddate));
			if(week.equals("星期一")){//-3
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 3, "yyyyMMdd");
			}else if(week.equals("星期二") || week.equals("星期三") || week.equals("星期四") || week.equals("星期五")){//-1
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 1, "yyyyMMdd");
			}else{//周六周日-7
				reg_enddate = DateUtil.getCurrDayBefore(reg_enddate, 7, "yyyyMMdd");
			}
			map.put("reg_enddate",reg_enddate);
			map.put("reg_begindate",reg_enddate);
			List<DmReportDepSalesEntity> dmReportDepSalesList2 = dmReportDepSalesDao.queryListss(map);
			for (int i = 0; i < dmReportDepSalesList.size(); i++) {
				DmReportDepSalesEntity entity = dmReportDepSalesList.get(i);
				for (int j = 0; j < dmReportDepSalesList2.size(); j++) {
					DmReportDepSalesEntity vo2 = dmReportDepSalesList2.get(j);
					if(entity.getSalesType().equals(vo2.getSalesType())){
						String today = entity.getZhanbi().replace("%", "");
						String yes = vo2.getZhanbi().replace("%", "");
						 DecimalFormat df = new DecimalFormat("0.00");
						double rate = (Double.parseDouble(today)-Double.parseDouble(yes))/Double.parseDouble(yes);
						
						entity.setWeekTongRate(df.format(rate*100) + "%");
								
						
					}
				}
			}
			

		} catch (ParseException e) {
			e.printStackTrace();
			
		}
		
		return dmReportDepSalesList;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportDepSalesDao.queryTotal(map);
	}
	@Override
	public List<DmReportDepSalesEntity> queryListha(Map<String, Object> map){
		return dmReportDepSalesDao.queryListha(map);
	}
	@Override
	public List<DmReportDepSalesEntity> queryListhe(Map<String, Object> map){
		return dmReportDepSalesDao.queryListhe(map);
	}
	@Override
	public List<DmReportDepSalesEntity> queryListxi(Map<String, Object> map){
		return dmReportDepSalesDao.queryListxi(map);
	}
	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "产品");
		headMap.put("shiwutian", "15天");
		headMap.put("yiyue", "1个月");

		headMap.put("eryue", "2个月");
		headMap.put("sanyue", "3个月");
		headMap.put("liuyue", "6个月");
		headMap.put("bayue", "8个月");

		headMap.put("jiuyue", "9个月");
		headMap.put("shiyue", "10个月");
		headMap.put("shieryue", "12个月");
		
		headMap.put("shiwuyue", "15个月");
		headMap.put("shibayue", "18个月");
		headMap.put("ershisiyue", "24个月");

		headMap.put("sanshiwuyue", "35个月");
		headMap.put("sanshiliuyue", "36个月");
		headMap.put("sishibayue", "48个月");

		headMap.put("liushiyue", "60个月");
		headMap.put("jiushiliuyue", "96个月");
		headMap.put("zongji", "总计");
		headMap.put("zhanbi", "占比");
		headMap.put("weekTongRate", "周同比");
		return headMap;
	}
	@Override
	public Map<String, String> getExcelFieldsha() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "产品");
		headMap.put("shiwutian", "15天");
		headMap.put("yiyue", "1个月");

		headMap.put("eryue", "2个月");
		headMap.put("sanyue", "3个月");
		headMap.put("liuyue", "6个月");
		headMap.put("bayue", "8个月");

		headMap.put("jiuyue", "9个月");
		headMap.put("shiyue", "10个月");
		headMap.put("shieryue", "12个月");
		
		headMap.put("shiwuyue", "15个月");
		headMap.put("shibayue", "18个月");
		headMap.put("ershisiyue", "24个月");

		headMap.put("sanshiwuyue", "35个月");
		headMap.put("sanshiliuyue", "36个月");
		headMap.put("sishibayue", "48个月");

		headMap.put("liushiyue", "60个月");
		headMap.put("jiushiliuyue", "96个月");
		headMap.put("zongji", "总计");
		headMap.put("zhanbi", "占比");
		return headMap;
	}
	
	@Override
	public Map<String, String> getExcelFields1() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("zichan", "期限");
		headMap.put("shiwutian", "15天");
		headMap.put("yiyue", "1个月");

		headMap.put("eryue", "2个月");
		headMap.put("sanyue", "3个月");
		headMap.put("liuyue", "6个月");
		headMap.put("bayue", "8个月");

		headMap.put("jiuyue", "9个月");
		headMap.put("shiyue", "10个月");
		headMap.put("shieryue", "12个月");
		
		headMap.put("shiwuyue", "15个月");
		headMap.put("shibayue", "18个月");
		headMap.put("ershisiyue", "24个月");

		headMap.put("sanshiwuyue", "35个月");
		headMap.put("sanshiliuyue", "36个月");
		headMap.put("sishibayue", "48个月");

		headMap.put("liushiyue", "60个月");
		headMap.put("jiushiliuyue", "96个月");
		headMap.put("zongji", "总计");
		return headMap;
	}

	@Override
	public Map<String, String> getExcelFields2() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "类型");
		headMap.put("zhanbi", "金额");
		headMap.put("weekTongRate", "周同比");
	

		return headMap;
	}
	public Map<String, String> getExcelFieldshe() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "日期");
		headMap.put("salesType", "类型");
		headMap.put("zhanbi", "金额");
//		headMap.put("weekTongRate", "周同比");
	

		return headMap;
	}

	
}
