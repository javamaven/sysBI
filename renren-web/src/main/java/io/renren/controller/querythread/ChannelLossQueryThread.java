package io.renren.controller.querythread;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelLossEntity;
import io.renren.service.ChannelLossService;

/**
 * 渠道流失分析
 * 
 * @author liaodehui
 *
 */
public class ChannelLossQueryThread implements Runnable {
	private ChannelLossService service;
	private Map<String, Object> params;
	private Map<String, ChannelLossEntity> result;
	private int queryTask;// 1:注册人数 2：首投人数 3：投资人数 4：首次投资金额 5：累积投资金额 6：累计投资年化金额
							// 7：首投使用红包金额 8：累积红包金额 9：点点赚平均投资金额
	private Map<String, Object> channelListMap;

	public ChannelLossQueryThread(ChannelLossService service, Map<String, Object> params,
			Map<String, Object> channelListMap, Map<String, ChannelLossEntity> result, int queryTask) {
		this.service = service;
		this.params = params;
		this.result = result;
		this.queryTask = queryTask;
		this.channelListMap = channelListMap;
	}

	@Override
	public void run() {
		List<ChannelLossEntity> list = null;
		switch (queryTask) {
		// 1:注册人数 2：首投人数 3：投资人数 4：首次投资金额 5：累积投资金额 ,累计投资年化金额 
		// 6：累计投资年化金额 7：首投使用红包金额 8：累积红包金额 9：点点赚投资天数，点点赚平均投资金额
		case 1:
			list = service.queryRegisterUserNum(params);
			break;
		case 2:
			list = service.queryFirstInvestUserNum(params);
			break;
		case 3:
			list = service.queryInvestUserNum(params);
			break;
		case 4:
			list = service.queryFirstInvestAmount(params);
			break;
		case 5:
			list = service.queryInvestAmount(params);
			break;
		case 6:
			list = service.queryInvestYearAmount(params);
			break;
		case 7:
			list = service.queryFirstInvestUseRedMoney(params);
			break;
		case 8:
			list = service.queryTotalUseRedMoney(params);
			break;
		case 9:
			list = service.queryDdzPerInvestAmount(params);
			break;
		default:
			break;
		}
		System.err.println("++++++++++++++type=" + queryTask + " ;size=" + list.size() + " params=" + params);
		for (int i = 0; i < list.size(); i++) {
			ChannelLossEntity ChannelLossEntity = list.get(i);
			if (ChannelLossEntity == null) {
				continue;
			}
			if (ChannelLossEntity.getChannelLabel() == null) {
				result.put("weizhi", ChannelLossEntity);
				channelListMap.put("weizhi", ChannelLossEntity);
			} else {
				result.put(ChannelLossEntity.getChannelLabel(), ChannelLossEntity);
				channelListMap.put(ChannelLossEntity.getChannelLabel(), ChannelLossEntity);
			}
		}
	}
}
