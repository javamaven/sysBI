package io.renren.controller.querythread;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelStftInfoEntity;
import io.renren.service.ChannelStftInfoService;

/**
 * 渠道首投复投情况
 * 
 * @author liaodehui
 *
 */
public class ChannelStFtInfoQueryThread implements Runnable {
	private ChannelStftInfoService service;
	private Map<String, Object> params;
	private Map<String, ChannelStftInfoEntity> result;
	private int queryTask;

	private Map<String, Object> channelListMap;

	public ChannelStFtInfoQueryThread(ChannelStftInfoService service, Map<String, Object> params,
			Map<String, Object> channelListMap, Map<String, ChannelStftInfoEntity> result, int queryTask) {
		this.service = service;
		this.params = params;
		this.result = result;
		this.queryTask = queryTask;
		this.channelListMap = channelListMap;
	}

	@Override
	public void run() {
		List<ChannelStftInfoEntity> list = null;
		switch (queryTask) {
		// 1.注册人数
		// 2.首投人数，首投金额,首投年化投资金额  人均首投
		// 3.用户年化投资金额
		// 4.项目复投金额
		// 5.首投用户投资金额
		// 6.用户投资金额,  
		// 7。首投平均期限
		// 8.复投人数
		// 9。首投用户项目投资金额
		// 10.项目投资金额,项目投资人数
		// 11.用户投资人数
		case 1:
			list = service.queryRegisterUserNum(params);
			break;
		case 2:
			list = service.queryFirstInvestUserNum(params);
			break;
		case 3:
			list = service.queryInvestYearAmount(params);
			break;
		case 4:
			list = service.queryProMultiInvestAmount(params);
			break;
		case 5:
			list = service.queryFirstInvestUserAmount(params);
			break;
		case 6:
			list = service.queryUserInvestAmount(params);
			break;
		case 7:
			list = service.queryFirstInvestPerTime(params);
			break;
		case 8:
			list = service.queryMultiInvestUserNum(params);
			break;
		case 9:
			list = service.queryFirstInvestUserProInvestAmount(params);
			break;
		case 10:
			list = service.queryProInvestAmount(params);
			break;
		case 11:
			list = service.queryUserInvestNum(params);
			break;
		default:
			break;
		}
		System.err.println("++++++++++++++type=" + queryTask + " ;size=" + list.size() + " params=" + params);
		for (int i = 0; i < list.size(); i++) {
			ChannelStftInfoEntity channelStftInfoEntity = list.get(i);
			if (channelStftInfoEntity == null) {
				continue;
			}
			
			if (channelStftInfoEntity.getChannelLabel() == null) {
				result.put("weizhi", channelStftInfoEntity);
				channelListMap.put("weizhi", channelStftInfoEntity);
			} else {
				channelStftInfoEntity.setChannelLabel(channelStftInfoEntity.getChannelLabel().trim());
				result.put(channelStftInfoEntity.getChannelLabel(), channelStftInfoEntity);
				channelListMap.put(channelStftInfoEntity.getChannelLabel(), channelStftInfoEntity);
			}
		}
	}
}
