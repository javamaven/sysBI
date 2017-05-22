package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelStftInfoEntity;
import io.renren.utils.PageUtils;

public interface ChannelStftInfoService {

	// 1.注册人数
	List<ChannelStftInfoEntity> queryRegisterUserNum(Map<String, Object> map);

	// 2.首投人数，首投金额,首投年化投资金额
	List<ChannelStftInfoEntity> queryFirstInvestUserNum(Map<String, Object> map);

	// 3用户年化投资金额
	List<ChannelStftInfoEntity> queryInvestYearAmount(Map<String, Object> map);

	// 4.项目复投金额
	List<ChannelStftInfoEntity> queryProMultiInvestAmount(Map<String, Object> map);

	// 5.首投用户投资金额
	List<ChannelStftInfoEntity> queryFirstInvestUserAmount(Map<String, Object> map);

	// 6.用户投资金额
	List<ChannelStftInfoEntity> queryUserInvestAmount(Map<String, Object> map);

	// 11.用户投资人数
	List<ChannelStftInfoEntity> queryUserInvestNum(Map<String, Object> map);

	// 7。首投平均期限
	List<ChannelStftInfoEntity> queryFirstInvestPerTime(Map<String, Object> map);

	// 8.复投人数
	List<ChannelStftInfoEntity> queryMultiInvestUserNum(Map<String, Object> map);

	// 9首投用户项目投资金额
	List<ChannelStftInfoEntity> queryFirstInvestUserProInvestAmount(Map<String, Object> map);

	// 10 项目投资金额,项目投资人数
	List<ChannelStftInfoEntity> queryProInvestAmount(Map<String, Object> map);

	PageUtils query(Map<String, Object> params);

	Map<String, String> getExcelFields();

}
