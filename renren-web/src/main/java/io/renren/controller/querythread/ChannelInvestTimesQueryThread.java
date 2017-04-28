package io.renren.controller.querythread;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.service.ChannelInvestTimesService;

/**
 * 渠道投资次数分析
 * @author liaodehui
 *
 */
public class ChannelInvestTimesQueryThread implements Runnable{
	private ChannelInvestTimesService service;
	private  Map<String, Object> params;
	private Map<String,ChannelInvestTimesEntity> result;
	private int queryTask;//1:注册人数 2：首投人数 ，首投金额 3：投资次数 4：投资人数 5：累积投资金额 6：累积投资年华金额 7：首投使用红包金额 8：累积红包金额 9：点点赚平均投资金额 
	private Map<String,Object> channelListMap;
	
	public ChannelInvestTimesQueryThread(ChannelInvestTimesService service,  Map<String, Object> params, 
			Map<String, Object> channelListMap, Map<String,ChannelInvestTimesEntity> result, int queryTask){
		this.service = service;
		this.params = params;
		this.result = result;
		this.queryTask = queryTask;
		this.channelListMap = channelListMap;
	}

	@Override
	public void run() {
		List<ChannelInvestTimesEntity> list = null;
		switch (queryTask) {
		//1:注册人数	 	2：首投人数 ，首投金额 	3：投资次数 		4：投资人数 		 5：累积投资金额 
		//6：累积投资年华金额 	7：首投使用红包金额 	8：累积红包金额	9：点点赚平均投资金额
		case 1:
			list = service.queryRegisterUserNum(params);
			break;
		case 2:
			list = service.queryFirstInvestUserNum(params);
			break;
		case 3:
			list = service.queryInvestTimes(params);
			break;
		case 4:
			list = service.queryInvestUserNum(params);
			break;
		case 5:
			list = service.queryInvestAmount(params);
			break;
		case 6:
			list = service.queryInvestYearAmount(params);
			break;
		case 7:
			list = service.queryFirstInvestRedMoney(params);
			break;
		case 8:
			list = service.queryAllRedMoney(params);
			break;
		case 9:
			list = service.queryDdzPerInvestAmount(params);
			break;
		default:
			break;
		}
		System.err.println("++++++++++++++type=" + queryTask + " ;size=" + list.size() + " params=" + params);
		for (int i = 0; i < list.size(); i++) {
			ChannelInvestTimesEntity channelInvestTimesEntity = list.get(i);
			if(channelInvestTimesEntity == null){
				continue;
			}
			if(channelInvestTimesEntity.getChannelLabel() == null){
				result.put("weizhi", channelInvestTimesEntity);
				channelListMap.put("weizhi", channelInvestTimesEntity);
			}else{
				result.put(channelInvestTimesEntity.getChannelLabel(), channelInvestTimesEntity);
				channelListMap.put(channelInvestTimesEntity.getChannelLabel(), channelInvestTimesEntity);
			}
		}
	}
}
