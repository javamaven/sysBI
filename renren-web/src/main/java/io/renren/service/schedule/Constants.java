package io.renren.service.schedule;

/**
 * 常量
 *
 */
public class Constants {

	/**
	 * 报表推送任务类型
	 *
	 */
	public static class TaskType {


		/**
		 * 渠道首投复投
		 */
		public static final String CHANNEL_STFT = "1";

		/**
		 * 渠道流失分析
		 */
		public static final String CHANNEL_LOSS = "2";
		/**
		 * 渠道投资次数分析
		 */
		public static final String CHANNEL_INVEST_TIMES = "3";
		/**
		 * 渠道续费
		 */
		public static final String CHANNEL_RENEW = "4";
		/**
		 * 用户激活
		 */
		public static final String USER_ACTIVE_INFO = "5";
		/**
		 * 用户投资
		 */
		public static final String USER_INVEST_INFO = "6";
		
		/**
		 * 渠道负责人
		 */
		public static final String MARKET_CHANNEL = "7";
		
		/**
		 * 渠道分次投资情况
		 */
		public static final String CHANNEL_ALL = "8";
		
		/**
		 * 用户行为日志
		 */
		public static final String USER_BEHAVIOR = "9";
		
		/**
		 * 每日理财计划基本数据
		 */
		public static final String LICAI_PLAN = "10";

		/**
		 * 每日基本数据
		 */
		public static final String EVERY_DAY_BASIC_DATA_PLAN = "11";

		/**
		 * 每日资金迁移数据报告
		 */
		public static final String EVERY_DAY_ACC_TRANSFER = "12";

		/**
		 * 每日待收数据报告
		 */
		public static final String EVERY_DAY_AWAIT_DATA = "13";

		/**
		 * 每日提现用户数据报告
		 */
		public static final String EVERY_DAY_GET_CASH = "14";

		/**
		 * 每日回款用户数据报告
		 */
		public static final String EVERY_DAY_RECOVER_DATA = "15";
		
		/**
		 * 项目账台明细
		 */

		public static final String PROJECT_PARAMETER = "16";

		/**
		 * 项目总台帐
		 */
		public static final String PROJECT_PARAMETER_SUM = "17";

		/**
		 * 存管报备总表
		 */
		public static final String DEPOSITORY_TOTAL = "18";
		/**
		 * 历史绩效发放记录
		 */
		public static final String PERFORMANCE_HIS = "19";
		/**
		 * 绩效台帐-分配表
		 */
		public static final String PERFORMANCE_PARAMETER = "20";
		
		/**
		 * 持有点点赚用户数据
		 */
		public static final String DDZ_USER = "21";
		
		/**
		 * 活动渠道成本数据报告
		 */
		public static final String CHANNEL_COST = "22";
		
		/**
		 * 每日VIP用户数据报告
		 */
		public static final String VIP_USER = "23";
		
		/**
		 * 注册1未投资用户
		 */
		public static final String REGISTER_ONE_HOUR_NOT_INVEST = "24";
		
		/**
		 * 注册3天未投资用户
		 */
		public static final String REGISTER_THREE_DAY_NOT_INVEST = "25";
		
		/**
		 * 本月注册且首投后三天没有进行复投用户数据
		 */
		public static final String FIRST_INVEST_THREE_DAY_NOT_INVEST = "26";
		
		/**
		 * 普通版有待收但是未开通存的账户数据
		 */
		public static final String OLD_DATA = "27";
		/**
		 * 广州P2P月报数据
		 */
		public static final String MONTHLY_REPORT = "28";
		
	}
}
