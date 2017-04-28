package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelLossEntity;

public interface ChannelLossService {

	List<ChannelLossEntity> queryRegisterUserNum(Map<String, Object> map);// 1:注册人数

	List<ChannelLossEntity> queryFirstInvestUserNum(Map<String, Object> map);// 2：首投人数

	List<ChannelLossEntity> queryInvestUserNum(Map<String, Object> map);// 3：投资人数

	List<ChannelLossEntity> queryFirstInvestAmount(Map<String, Object> map);// 4：首次投资金额

	List<ChannelLossEntity> queryInvestAmount(Map<String, Object> map);// 5：累积投资金额

	List<ChannelLossEntity> queryInvestYearAmount(Map<String, Object> map);// 6：累计投资年化金额

	List<ChannelLossEntity> queryFirstInvestUseRedMoney(Map<String, Object> map);// 7：首投使用红包金额

	List<ChannelLossEntity> queryTotalUseRedMoney(Map<String, Object> map);// 8：累积红包金额

	List<ChannelLossEntity> queryDdzPerInvestAmount(Map<String, Object> map);// 9：点点赚平均投资金额
}
