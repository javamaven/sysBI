package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.ChannelRenewDataEntity;

public interface ChannelRenewDataDao extends BaseDao<ChannelRenewDataEntity> {

	// 30日60日90日，费用
	List<ChannelRenewDataEntity> queryChannelCost(Map<String, Object> map);

	// 上线时间
	List<ChannelRenewDataEntity> queryOnlineTime(Map<String, Object> map);

	// 30日60日90日，年化金额
	List<ChannelRenewDataEntity> queryYearAmount(Map<String, Object> map);

	// 30日60日90日，年化ROI
	List<ChannelRenewDataEntity> queryYearRoi(Map<String, Object> map);

	// 30日60日90日，首投人数，首投金额为，首投年化金额
	// 30日60日90日，复投人数，复投金额为，复投年化金额
	List<ChannelRenewDataEntity> queryFirstInvestUserNum(Map<String, Object> map);

	// 30日60日90日,首投年化ROI 传入不同参数，返回对应信息
	List<ChannelRenewDataEntity> queryFirstInvestYearRoi(Map<String, Object> map);

}
