package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelLossEntity;

public interface ChannelLossDao extends BaseDao<ChannelLossEntity> {

	List<ChannelLossEntity> queryRegisterUserNum(Map<String, Object> map);

	List<ChannelLossEntity> queryFirstInvestUserNum(Map<String, Object> map);

	List<ChannelLossEntity> queryInvestUserNum(Map<String, Object> map);

	List<ChannelLossEntity> queryFirstInvestAmount(Map<String, Object> map);

	List<ChannelLossEntity> queryInvestAmount(Map<String, Object> map);

	List<ChannelLossEntity> queryInvestYearAmount(Map<String, Object> map);

	List<ChannelLossEntity> queryFirstInvestUseRedMoney(Map<String, Object> map);

	List<ChannelLossEntity> queryTotalUseRedMoney(Map<String, Object> map);

	List<ChannelLossEntity> queryDdzPerInvestAmount(Map<String, Object> map);

}
