package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelInvestTimesEntity;
import io.renren.utils.PageUtils;

public interface ChannelInvestTimesService {

	List<ChannelInvestTimesEntity> queryRegisterUserNum(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryFirstInvestUserNum(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryInvestTimes(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryInvestUserNum(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryInvestAmount(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryInvestYearAmount(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryFirstInvestRedMoney(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryAllRedMoney(Map<String, Object> map);

	List<ChannelInvestTimesEntity> queryDdzPerInvestAmount(Map<String, Object> map);

	PageUtils query(Map<String, Object> params);

	Map<String, String> getExcelFields();

}
