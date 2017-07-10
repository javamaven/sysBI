package io.renren.service.shichang;

import io.renren.utils.PageUtils;

public interface ChannelStatService {

	PageUtils queryTotalList(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelName);

	PageUtils queryDetailList(Integer page, Integer limit, String registerStartTime, String registerEndTime,
			String firstInvestStartTime, String firstInvestEndTime, String channelLabel);


}
