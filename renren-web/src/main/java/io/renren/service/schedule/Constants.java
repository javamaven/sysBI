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
	}
}
