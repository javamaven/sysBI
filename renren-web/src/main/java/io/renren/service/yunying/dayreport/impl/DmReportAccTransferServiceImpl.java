package io.renren.service.yunying.dayreport.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.yunying.dayreport.DmReportAccTransferDao;
import io.renren.entity.yunying.dayreport.DmReportAccTransferEntity;
import io.renren.service.yunying.dayreport.DmReportAccTransferService;



@Service("dmReportAccTransferService")
public class DmReportAccTransferServiceImpl implements DmReportAccTransferService {
	@Autowired
	private DmReportAccTransferDao dmReportAccTransferDao;
	
	@Override
	public DmReportAccTransferEntity queryObject(String statPeriod){
		return dmReportAccTransferDao.queryObject(statPeriod);
	}
	
	@Override
	public List<DmReportAccTransferEntity> queryList(Map<String, Object> map){
		List<DmReportAccTransferEntity> list = dmReportAccTransferDao.queryList(map);
		for (int i = 0; i < list.size(); i++) {
			DmReportAccTransferEntity dmReportAccTransferEntity = list.get(i);
			String statPeriod = dmReportAccTransferEntity.getStatPeriod();
			if(statPeriod.length() == 8){
				String year = statPeriod.substring(0, 4);
				String month = statPeriod.substring(4, 6);
				String day = statPeriod.substring(6, 8);
				dmReportAccTransferEntity.setStatPeriod(year + "-" + month + "-" + day);
			}
		}
		return list;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dmReportAccTransferDao.queryTotal(map);
	}
	
	@Override
	public void save(DmReportAccTransferEntity dmReportAccTransfer){
		dmReportAccTransferDao.save(dmReportAccTransfer);
	}
	
	@Override
	public void update(DmReportAccTransferEntity dmReportAccTransfer){
		dmReportAccTransferDao.update(dmReportAccTransfer);
	}
	
	@Override
	public void delete(String statPeriod){
		dmReportAccTransferDao.delete(statPeriod);
	}
	
	@Override
	public void deleteBatch(String[] statPeriods){
		dmReportAccTransferDao.deleteBatch(statPeriods);
	}

	@Override
	public Map<String, String> getExcelFields() {
		Map<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("statPeriod", "发起日期");

		headMap.put("awaitAuditM", "待审核金额");
		headMap.put("processingM", "处理中金额");
		headMap.put("successfulM", "成功金额");
		
		headMap.put("failureM", "失败金额");
		headMap.put("nthroughM", "审核不通过金额");
		headMap.put("pCou", "发起人数");
		
		headMap.put("recoverM", "回款金额");
		headMap.put("recoverCou", "回款人数");
		
		return headMap;
	}
	
}
