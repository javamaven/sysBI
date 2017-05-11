package io.renren.controller.querythread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelRenewDataEntity;
import io.renren.service.ChannelRenewDataService;

/**
 * 渠道续费数据汇总
 * 
 * @author liaodehui
 *
 */
public class ChannelRenewDataQueryThread implements Runnable {

	private ChannelRenewDataService service;
	private Map<String, Object> params;
	private Map<String, ChannelRenewDataEntity> result;
	private int queryTask;//
	private Map<String, Object> channelListMap;

	public ChannelRenewDataQueryThread(ChannelRenewDataService service, Map<String, Object> params,
			Map<String, Object> channelListMap, Map<String, ChannelRenewDataEntity> result, int queryTask) {
		this.service = service;
		this.params = params;
		this.result = result;
		this.queryTask = queryTask;
		this.channelListMap = channelListMap;
	}

	@Override
	public void run() {
		List<ChannelRenewDataEntity> list = null;
		switch (queryTask) {
		case 1:
			list = service.queryChannelCost(params);// 30日60日90日，费用
			break;
		case 2:
			list = service.queryOnlineTime(params);// 上线时间
			break;
		case 3:
			list = service.queryYearAmount(params);// 30日60日90日，年化金额 ,传入不同参数
			break;
		case 4:
			list = service.queryFirstInvestUserNumDay30(params);//30日首(复)投人数，首(复)投金额为，首投年化金额
			break;
		case 5:
//			list = service.queryFirstInvestUserNum(params);// 30日60日90日，首投人数，首投金额为，首投年化金额
															// 30日60日90日，复投人数，复投金额为，复投年化金额
			list = service.queryFirstInvestUserNumDay60(params);//60日首(复)投人数，首(复)投金额为，首投年化金额
			break;
		case 6:
			list = service.queryFirstInvestUserNumDay90(params);//90日首(复)投人数，首(复)投金额为，首投年化金额
			// 传入不同参数，返回对应信息
			break;
//		case 7:
//			list = service.queryDay60FirstInvestYearRoi(params);// 60日,首投年化ROI
//			// 传入不同参数，返回对应信息
//			break;
//		case 8:
//			list = service.queryDay90FirstInvestYearRoi(params);// 90日,首投年化ROI
//			// 传入不同参数，返回对应信息
//			break;
		default:
			break;
		}
		System.err.println("++++++++++++++type=" + queryTask + " ;size=" + list.size() + " params=" + params);
		for (int i = 0; i < list.size(); i++) {
			ChannelRenewDataEntity entity = list.get(i);
			if (entity == null) {
				continue;
			}
			if (entity.getChannelLabel() == null) {
				result.put("weizhi", entity);
				if (channelListMap != null) {
					channelListMap.put("weizhi", entity);
				}
			} else {
				result.put(entity.getChannelLabel(), entity);
				if (channelListMap != null) {
					channelListMap.put(entity.getChannelLabel(), entity);
				}
			}
		}
	}
}
